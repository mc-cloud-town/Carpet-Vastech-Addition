package carpet.logging.logHelpers;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import javafx.util.Pair;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class PopulationConstants {
    private static class FeatureToNameMap extends HashMap<Class<? extends WorldGenerator>, String> {
        public FeatureToNameMap() {
            put(WorldGenBigMushroom.class, "big_mushroom");
            put(WorldGenBigTree.class, "big_oak");
            put(WorldGenBirchTree.class, "birch");
            put(WorldGenBlockBlob.class, "taiga_mossy_stone");
            put(WorldGenBush.class, "mushroom");
            put(WorldGenCactus.class, "cactus");
            put(WorldGenCanopyTree.class, "dark_oak");
            put(WorldGenClay.class, "clay");
            put(WorldGenDeadBush.class, "dead_bush");
            put(WorldGenDesertWells.class, "desert_wells");
            put(WorldGenDoublePlant.class, "tall_plant");
            put(WorldGenDungeons.class, "spawner");
            put(WorldGenEndGateway.class, "end_gateway");
            put(WorldGenEndIsland.class, "end_island");
            put(WorldGenEndPodium.class, "end_fountain");
            put(WorldGeneratorBonusChest.class, "bonus_chest");
            put(WorldGenFire.class, "fire");
            put(WorldGenFlowers.class, "dandelion");
            put(WorldGenFossils.class, "fossil");
            put(WorldGenGlowStone1.class, "glowstone1");
            put(WorldGenGlowStone2.class, "glowstone2");
            put(WorldGenHellLava.class, "nether_spring");
            put(WorldGenIcePath.class, "ice_path");
            put(WorldGenIceSpike.class, "ice_spike");
            put(WorldGenLakes.class, "lakes");
            put(WorldGenLiquids.class, "spring");
            put(WorldGenMegaJungle.class, "large_jungle");
            put(WorldGenMegaPineTree.class, "large_spruce");
            put(WorldGenMelon.class, "melon");
            put(WorldGenMinable.class, "overworld_ore");
            put(WorldGenPumpkin.class, "pumpkin");
            put(WorldGenReed.class, "sugarcanes");
            put(WorldGenSand.class, "sand");
            put(WorldGenSavannaTree.class, "acacia");
            put(WorldGenShrub.class, "jungle");
            put(WorldGenSpikes.class, "end_pillar");
            put(WorldGenSwamp.class, "swamp_tree");
            put(WorldGenTaiga1.class, "spruce");
            put(WorldGenTaiga2.class, "pine");
            put(WorldGenTallGrass.class, "tall_grass");
            put(WorldGenTrees.class, "oak_or_jungle");
            put(WorldGenVines.class, "vines");
            put(WorldGenWaterlily.class, "lilypad");
        }
    }

    public static final Map<Class<? extends WorldGenerator>, String> featureToName = new FeatureToNameMap();
    public static final Map<String, Class<? extends WorldGenerator>> nameToFeature = new HashMap<>();

    static {
        for (Class<? extends WorldGenerator> featureClass: featureToName.keySet()) {
            nameToFeature.put(featureToName.get(featureClass), featureClass);
        }
    }

    public static final int CACHED_OPTIONS_COUNT = 16;
    public static final Deque<Pair<String, PopulationHelper.PopulationLoggerOptions>> cachedOptions = new ArrayDeque<>();
    public static final PopulationHelper.PopulationLoggerOptions EMPTY_OPTIONS = new PopulationHelper.PopulationLoggerOptions();

    /**
     * @param optionString a string encoded to contain all the info for population logger
     *                     the string would look like category(key1,key2,...)+category(key1,key2,...)+...
     *                     the keys could be a name, or -name to exclude a name, or all to add all
     *                     for example, world(overworld)+feature(spring)+flag(all)+suppression()
     *                     will log placement of liquid pockets and population suppression in the overworld
     */
    public static PopulationHelper.PopulationLoggerOptions resolveOptionsByString(String optionString) {
        for (Pair<String, PopulationHelper.PopulationLoggerOptions> pair: cachedOptions) {
            if (pair.getKey().equals(optionString)) return pair.getValue();
        }
        PopulationHelper.PopulationLoggerOptions options = resolveOptionsByString0(optionString);
        while (cachedOptions.size() >= CACHED_OPTIONS_COUNT) {
            cachedOptions.pollFirst();
        }
        cachedOptions.addLast(new Pair<>(optionString, options));
        return options;
    }

    private static final Pattern CATEGORY_PATTERN = Pattern.compile("(?<category>.+)\\((?<args>(.+))\\)");
    private static PopulationHelper.PopulationLoggerOptions resolveOptionsByString0(String optionString) {
        System.out.println("Trying to resolve option " + optionString);
        PopulationHelper.PopulationLoggerOptions result = new PopulationHelper.PopulationLoggerOptions();
        String[] categoryOptions = optionString.split("\\+");
        for (String categoryOption: categoryOptions) {
            Matcher matcher = CATEGORY_PATTERN.matcher(categoryOption);
            if (!matcher.find()) return result;
            String category = matcher.group("category");
            List<String> args = Arrays.stream(matcher.group("args").split(" "))
                    .map(s -> s.replace(" ", ""))
                    .filter(s -> s.length() > 0)
                    .collect(Collectors.toList());
            switch (category) {
                case "world": {
                    result.worldsToLog.addAll(args);
                    break;
                }
                case "feature": {
                    Class<? extends WorldGenerator> featureClass = null;
                    for (String arg: args) {
                        if ((featureClass = nameToFeature.get(arg)) != null) result.featuresToLog.add(featureClass);
                        else {
                            // special case
                            if ("all".equals(arg)) {
                                result.featuresToLog.addAll(featureToName.keySet());
                            }
                        }
                    }
                    break;
                }
                case "flag": {
                    for (String arg: args) {
                        for (PopulationHelper.PopulationFlag flag: PopulationHelper.PopulationFlag.values()) {
                            if (flag.getAcronym().equals(arg)) result.flagsToLog.add(flag);
                        }
                    }
                    break;
                }
                case "suppression": {
                    result.shouldLogPopulationSuppression = true;
                    break;
                }
                case "async_population": {
                    result.shouldLogAsyncPopulation = true;
                    break;
                }
            }
        }
        return result;
    }


}
