package carpet.logging.logHelpers;

import carpet.logging.LoggerRegistry;
import carpet.utils.Messenger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class LiquidPocketHelper {
    private static final LiquidPocketHelper INSTANCE = new LiquidPocketHelper();

    private LiquidPocketHelper() {
    }

    public static LiquidPocketHelper getInstance() {
        return INSTANCE;
    }

    public void onLiquidPocketPlacement(BlockPos pos) {
        LoggerRegistry.getLogger("liquidPocket").log((playerOption -> new ITextComponent[]
                {Messenger.m(null, "w Placement of a liquid pocket at position " + pos)}));
    }
}
