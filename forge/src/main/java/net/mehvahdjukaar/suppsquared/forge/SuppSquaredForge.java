package net.mehvahdjukaar.suppsquared.forge;

import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.forge.RegHelperImpl;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.forge.SupplementariesForge;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.SuppSquaredClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import vazkii.quark.addons.oddities.block.TinyPotatoBlock;

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
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::setup);
        }

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SuppSquaredForge::setup);
    }

    public static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(SuppSquared::commonSetup);
    }


}

