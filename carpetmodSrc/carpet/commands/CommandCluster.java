package carpet.commands;

import carpet.helpers.ClusterHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;

import carpet.helpers.ClusterHelper.OptimalClusteringReport;
import carpet.helpers.ClusterHelper.ClusterChunksReport;

import java.util.List;
import java.util.stream.Collectors;

public class CommandCluster extends CommandCarpetBase {
    private final ClusterHelper clusterHelper = new ClusterHelper();

    @Override
    public String getName() {
        return "cluster";
    }

    /*
     * Usage:
     * /cluster set hashSize/clusterSize/clusterWidth/targetStart/targetEnd/clusterSearchStart/desiredHashStart <data>
     * /cluster compute optimalClustering/clusterChunks
     * /cluster construct loadCluster/makeClusterLoader
     */
    @Override
    public String getUsage(ICommandSender sender) {
        return "/cluster";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) return;
        switch (args[0]) {
            case "set": {
                switch (args[1]) {
                    case "hashSize": {
                        clusterHelper.setHashSize(Integer.parseInt(args[2]));
                        break;
                    }
                    case "clusterSize": {
                        clusterHelper.setClusterSize(Integer.parseInt(args[2]));
                        break;
                    }
                    case "clusterWidth": {
                        clusterHelper.setClusterWidth(Integer.parseInt(args[2]));
                        break;
                    }
                    case "targetStart": {
                        ChunkPos pos = new ChunkPos(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        clusterHelper.setTargetStart(pos);
                        break;
                    }
                    case "targetEnd": {
                        ChunkPos pos = new ChunkPos(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        clusterHelper.setTargetEnd(pos);
                        break;
                    }
                    case "clusterSearchStart": {
                        ChunkPos pos = new ChunkPos(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        clusterHelper.setClusterSearchStart(pos);
                        break;
                    }
                    case "desiredHashStart": {
                        clusterHelper.setDesiredHashStart(Integer.parseInt(args[2]));
                        break;
                    }
                }
                break;
            }
            case "compute": {
                switch (args[1]) {
                    case "optimalClustering": {
                        OptimalClusteringReport optimalClusteringReport = clusterHelper.computeOptimalClustering();
                        if (optimalClusteringReport == null) {
                            notifyCommandListener(sender, this, "Not completely initialized! ");
                            return;
                        }
                        int targetX = optimalClusteringReport.target.x;
                        int targetZ = optimalClusteringReport.target.z;
                        notifyCommandListener(sender, this,
                                String.format("Optimal target chunk (%d, %d) with total clustering %d and hash start %d",
                                        targetX, targetZ,
                                        optimalClusteringReport.totalClustering, optimalClusteringReport.hashStart));
                        notifyCommandListener(sender, this,
                                "Preloaded chunks: " + optimalClusteringReport.preloadedChunks.stream().map(pos -> "(" + (targetX + pos.x) + ", " + (targetZ + pos.z) + ")").collect(Collectors.joining(", ")));
                        notifyCommandListener(sender, this, "Detailed clustering of the 21 clustered chunks: ");
                        for (ChunkPos pos: optimalClusteringReport.detailedClustering.keySet()) {
                            int clustering = optimalClusteringReport.detailedClustering.get(pos);
                            notifyCommandListener(sender, this, String.format("Chunk (%d, %d) has clustering %d", pos.x, pos.z, clustering));
                        }
                        break;
                    }
                    case "clusterChunks": {
                        int suboptimalRange = 5;
                        if (args.length >= 3) suboptimalRange = Integer.parseInt(args[2]);
                        OptimalClusteringReport optimalClusteringReport = clusterHelper.getLastOptimalClusteringReport();
                        ClusterChunksReport clusterChunksReport = clusterHelper.getClusterChunks(optimalClusteringReport, suboptimalRange);
                        if (clusterChunksReport == null) {
                            notifyCommandListener(sender, this, "Not completely initialized! ");
                            return;
                        }
                        notifyCommandListener(sender, this, "Cluster chunks has been generated");
                        ChunkPos target = optimalClusteringReport.target;
                        for (int hashStart: clusterChunksReport.hashStartToClusterChunks.keySet()) {
                            int suboptimalClustering = ClusterHelper.getTotalClustering(target, optimalClusteringReport.preloadedChunks,
                                    hashStart, clusterHelper.getClusterSize(), clusterHelper.getHashSize() - 1);
                            notifyCommandListener(sender, this, String.format("Hash start %d with total clustering %d and cluster grid length %d",
                                    hashStart, suboptimalClustering, clusterChunksReport.hashStartToMaxZ.get(hashStart) - clusterHelper.getClusterSearchStart().z + 1));
                        }
                        notifyCommandListener(sender, this, "Please choose one via /cluster set desiredHashStart <value>. ");
                        notifyCommandListener(sender, this, "You can observe the cluster in action via /cluster construct loadCluster then running /chunk dump loadedChunks");
                        break;
                    }
                }
                break;
            }
            case "construct": {
                List<ChunkPos> clusterChunks = clusterHelper.getLastClusterChunksReport().hashStartToClusterChunks.get(clusterHelper.getDesiredHashStart());
                if (clusterChunks == null) {
                    notifyCommandListener(sender, this, "Not properly initialized! ");
                    return;
                }
                switch (args[1]) {
                    case "loadCluster": {
                        for (ChunkPos pos: clusterChunks) {
                            sender.getEntityWorld().getChunk(pos.x, pos.z);
                        }
                        break;
                    }
                    case "makeClusterLoader": {
                        notifyCommandListener(sender, this, "Not implemented yet, sorry. ");
                        break;
                    }
                }
                break;
            }
        }
    }
}
