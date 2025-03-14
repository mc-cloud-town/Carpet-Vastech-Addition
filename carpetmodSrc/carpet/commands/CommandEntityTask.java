package carpet.commands;

import carpet.commands.CommandCarpetBase;
import carpet.logging.LoggerRegistry;
import carpet.utils.JavaVersionUtil;
import carpet.utils.Messenger;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class CommandEntityTask extends CommandCarpetBase {
    private static final JavaVersionUtil.FieldAccessor<AtomicLong> SEED_ACCESSOR =
        JavaVersionUtil.objectFieldAccessor(Random.class, "seed", AtomicLong.class);

    private static long getSeed(Random random) {
        return SEED_ACCESSOR.get(random).get();
    }

    private static void setSeed(Random random, long seed) {
        SEED_ACCESSOR.get(random).set(seed);
    }

    @Override
    public String getName() {
        return "entityTask";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/entityTask <uuid> or no UUID if you have subscribed to /log entityTasks";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!command_enabled("commandEntityTask", sender)) return;
        String uuidString = null;
        if (args.length == 0 && sender instanceof EntityPlayer) {
            uuidString = LoggerRegistry.getLogger("entityTask").getSubscribedPlayers().get(sender.getName());
        } else uuidString = args[0];
        UUID uuid = null;
        try {
            uuid = UUID.fromString(uuidString);
        } catch (Throwable throwable) {
            throw new CommandException("Invalid UUID string");
        }
        List<Entity> entities;
        UUID finalUuid = uuid;
        if ((entities = sender.getEntityWorld().getEntities(Entity.class, __ -> true)
                .stream().filter(entity -> entity.getUniqueID().equals(finalUuid)).collect(Collectors.toList())).isEmpty()) {
            throw new CommandException("No entity with such UUID exist");
        } else {
            Entity entity = entities.get(0);
            if (entity instanceof EntityLiving) {
                Messenger.s(sender, "Currently executing tasks for target entity " + uuidString + ": ");
                EntityAITasks tasks = ((EntityLiving) entity).tasks;
                for (EntityAITasks.EntityAITaskEntry taskEntry : tasks.executingTaskEntries) {
                    EntityAIBase task = taskEntry.action;
                    Messenger.s(sender, task.getTask());
                }
            }
            Messenger.s(sender, "Current RNG seed for this entity is " + getSeed(entity.accessRandom()));
            if (entity instanceof EntityVillager) {
                EntityVillager villager = (EntityVillager) entity;
                Messenger.s(sender, "levelUpTime of the villager is " + villager.getTimeUntilReset());
            }
            if (args.length >= 1) {
                try {
                    long seed = Long.parseLong(args[1]);
                    setSeed(entity.accessRandom(), seed);
                    Messenger.s(sender, "RNG seed for this entity has been updated to " + getSeed(entity.accessRandom()));
                } catch (Throwable ignore) {}
            }
            if (args.length >= 2 && "setLevelUpTime".equals(args[1])) {
                try {
                    int levelUpTime = Integer.parseInt(args[2]);
                    EntityVillager villager = (EntityVillager) entity;
                    villager.setTimeUntilReset(levelUpTime);
                    villager.setNeedsInitilization(levelUpTime > 0);
                    Messenger.s(sender, "levelUpTime of this villager has been updated to " + levelUpTime + "ticks");
                } catch (Throwable ignore) {}
            }
        }
    }
}
