package net.mehvahdjukaar.suppsquared.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.SuppSquaredClient;

public class SuppSquaredFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        //be sure supplementaries stuff loaded first. we touch to ensure class loading.
        //should be taken care by load order but apparently can fail sometimes...
        try {
            // Check if the class has been loaded
            Class.forName("net.mehvahdjukaar.supplementaries.reg.ModRegistry");
        } catch (ClassNotFoundException e) {
            SuppSquared.LOGGER.warn("Supplementaries has not been loaded before this mod. How?");
        }
        Object r = ModRegistry.DAUB;
        Object c = CommonConfigs.SPEC;

        SuppSquared.commonInit();
        if (PlatformHelper.getEnv().isClient()) {
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::init);
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::setup);
        }

    }

}
