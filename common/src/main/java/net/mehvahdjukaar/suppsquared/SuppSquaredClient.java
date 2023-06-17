package net.mehvahdjukaar.suppsquared;

import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.supplementaries.client.renderers.color.MimicBlockColor;
import net.mehvahdjukaar.suppsquared.client.PlaqueTileRenderer;
import net.minecraft.client.renderer.RenderType;

public class SuppSquaredClient {


    public static void init() {
        ClientHelper.addBlockColorsRegistration(SuppSquaredClient::registerBlockColors);
        ClientHelper.addBlockEntityRenderersRegistration(SuppSquaredClient::registerTileRenderers);
    }

    public static void setup() {
        ClientHelper.registerRenderType(SuppSquared.IRON_BRACE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(SuppSquared.IRON_FRAME.get(), RenderType.cutout());
        ClientHelper.registerRenderType(SuppSquared.IRON_CROSS_BRACE.get(), RenderType.cutout());
        ClientHelper.registerRenderType(SuppSquared.BRASS_LANTERN.get(), RenderType.cutout());
        ClientHelper.registerRenderType(SuppSquared.COPPER_LANTERN.get(), RenderType.cutout());
        ClientHelper.registerRenderType(SuppSquared.CRIMSON_LANTERN.get(), RenderType.cutout());
        SuppSquared.GOLDEN_CANDLE_HOLDERS.values().forEach(v ->
                ClientHelper.registerRenderType(v.get(), RenderType.cutout()));
    }

    private static void registerBlockColors(ClientHelper.BlockColorEvent event) {
        event.register(new MimicBlockColor(), SuppSquared.IRON_BRACE.get(), SuppSquared.IRON_CROSS_BRACE.get(),
                SuppSquared.IRON_FRAME.get());

    }

    private static void registerTileRenderers(ClientHelper.BlockEntityRendererEvent event) {
        event.register(SuppSquared.PLAQUE_TILE.get(), PlaqueTileRenderer::new);
    }

}
