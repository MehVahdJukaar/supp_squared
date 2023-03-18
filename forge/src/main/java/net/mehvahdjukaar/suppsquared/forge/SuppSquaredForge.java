package net.mehvahdjukaar.suppsquared.forge;

import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.SuppSquaredClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Author: MehVahdJukaar
 */
@Mod(SuppSquared.MOD_ID)
public class SuppSquaredForge {

    public SuppSquaredForge() {
        SuppSquared.commonInit();
        if (PlatformHelper.getEnv().isClient()) {
            SuppSquaredClient.init();
            SuppSquaredForgeClient.init();
        }

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SuppSquaredForge::setup);
    }

    public static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(SuppSquared::commonSetup);
    }


}

