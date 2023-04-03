package carpet.logging.logHelpers;

import com.google.common.collect.Maps;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.HashMap;
import java.util.Map;

public abstract class PopulationConstants {
    private static class FeatureToNameMap extends HashMap<Class<? extends WorldGenerator>, String> {
        public FeatureToNameMap() {
            put(WorldGenBigMushroom.class, "big mushroom");
            put(WorldGenBigTree.class, "big oak tree");
            put(WorldGenBirchTree.class, "birch tree");
            put(WorldGenBlockBlob.class, "taiga mossy stone");
            put(WorldGenBush.class, "singular mushroom");
            put(WorldGenCactus.class, "cactus");
            put(WorldGenCanopyTree.class, "dark oak tree");
            put(WorldGenClay.class, "clay blobs");
            put(WorldGenDeadBush.class, "dead bush");
            put(WorldGenDesertWells.class, "desert wells");
            put(WorldGenDoublePlant.class, "tall plant");
            put(WorldGenDungeons.class, "spawner");
            put(WorldGenEndGateway.class, "end gateway");
            put(WorldGenEndIsland.class, "small end islands");
            put(WorldGenEndPodium.class, "end fountain");
            put(WorldGeneratorBonusChest.class, "bonus chest");
            put(WorldGenFire.class, "nether fire");
            put(WorldGenFlowers.class, "dandelion");
            put(WorldGenFossils.class, "fossil");
            put(WorldGenGlowStone1.class, "glowstone 1");
            put(WorldGenGlowStone2.class, "glowstone 2");
            put(WorldGenHellLava.class, "nether spring");
            put(WorldGenIcePath.class, "ice path");
            put(WorldGenIceSpike.class, "ice spike");
            put(WorldGenLakes.class, "lakes");
            put(WorldGenLiquids.class, "spring");
            put(WorldGenMegaJungle.class, "large jungle");
            put(WorldGenMegaPineTree.class, "large spruce");
            put(WorldGenMelon.class, "melon");
            put(WorldGenMinable.class, "generic overworld ore");
            put(WorldGenPumpkin.class, "pumpkin");
            put(WorldGenReed.class, "sugar canes");
            put(WorldGenSand.class, "underwater sand");
            put(WorldGenSavannaTree.class, "acacia tree");
            put(WorldGenShrub.class, "small jungle tree");
            put(WorldGenSpikes.class, "end pillar");
            put(WorldGenSwamp.class, "swamp tree");
            put(WorldGenTaiga1.class, "spruce tree 1");
            put(WorldGenTaiga2.class, "spruce tree 2");
            put(WorldGenTallGrass.class, "tall grass");
            put(WorldGenTrees.class, "oak or jungle tree");
            put(WorldGenVines.class, "jungle vines");
            put(WorldGenWaterlily.class, "lily pad");
        }
    }

    public static final Map<Class<? extends WorldGenerator>, String> featureToName = new FeatureToNameMap();

    /**
     * @param optionString a string encoded to contain all the info for population logger
     *                     the string would look like category(key1,key2,...)+category(key1,key2,...)+...
     *                     the keys could be a name, or -name to exclude a name, or all to add all
     *                     for example, world(overworld)+feature(spring)+flag(all)+suppression()
     *                     will log placement of liquid pockets and population suppression in the overworld
     */
    public static PopulationHelper.PopulationLoggerOptions resolveOptionsByString(String optionString) {
        return null; // todo
    }
}
