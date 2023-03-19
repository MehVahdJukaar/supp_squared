package net.mehvahdjukaar.suppsquared;

import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.mehvahdjukaar.supplementaries.client.renderers.color.MimicBlockColor;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.suppsquared.client.PlaqueTileRenderer;
import net.minecraft.client.renderer.RenderType;

public class SuppSquaredClient {


    public static void init() {
        ClientPlatformHelper.addBlockColorsRegistration(SuppSquaredClient::registerBlockColors);
        ClientPlatformHelper.addBlockEntityRenderersRegistration(SuppSquaredClient::registerTileRenderers);
    }

    public static void setup() {
        ClientPlatformHelper.registerRenderType(SuppSquared.IRON_BRACE.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(SuppSquared.IRON_FRAME.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(SuppSquared.IRON_CROSS_BRACE.get(), RenderType.cutout());
        SuppSquared.GOLDEN_CANDLE_HOLDERS.values().forEach(v->
                ClientPlatformHelper.registerRenderType(v.get(), RenderType.cutout()));
    }

    private static void registerBlockColors(ClientPlatformHelper.BlockColorEvent event) {
        event.register(new MimicBlockColor(), SuppSquared.IRON_BRACE.get(), SuppSquared.IRON_CROSS_BRACE.get(),
                SuppSquared.IRON_FRAME.get());

    }

    private static void registerTileRenderers(ClientPlatformHelper.BlockEntityRendererEvent event) {
        event.register(SuppSquared.PLAQUE_TILE.get(), PlaqueTileRenderer::new);
    }

}
