package carpet.commands;

import carpet.logging.logHelpers.PopulationHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandPopulationLoggingOptions extends CommandCarpetBase {

    @Override
    public String getName() {
        return "populationLoggingOptions";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/populationLoggingOptions <optionStrings>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            PopulationHelper.optionsByPlayer.put(player.getUniqueID(), args[0]);
        }
    }
}
