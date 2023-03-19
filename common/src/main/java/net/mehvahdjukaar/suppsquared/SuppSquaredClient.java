package net.mehvahdjukaar.suppsquared;

import net.mehvahdjukaar.moonlight.api.misc.EventCalled;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.mehvahdjukaar.supplementaries.client.renderers.color.MimicBlockColor;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.minecraft.client.renderer.RenderType;

public class SuppSquaredClient {


    public static void init() {
        ClientPlatformHelper.addBlockColorsRegistration(SuppSquaredClient::registerBlockColors);

    }


    public static void setup() {
        ClientPlatformHelper.registerRenderType(SuppSquared.IRON_BRACE.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(SuppSquared.IRON_FRAME.get(), RenderType.cutout());
        ClientPlatformHelper.registerRenderType(SuppSquared.IRON_CROSS_BRACE.get(), RenderType.cutout());

    }


    @EventCalled
    private static void registerBlockColors(ClientPlatformHelper.BlockColorEvent event) {

        event.register(new MimicBlockColor(), SuppSquared.IRON_BRACE.get(), SuppSquared.IRON_CROSS_BRACE.get(),
                SuppSquared.IRON_FRAME.get());

    }

}
