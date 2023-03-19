package net.mehvahdjukaar.suppsquared.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.supplementaries.reg.ClientRegistry;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.SuppSquaredClient;

public class SuppSquaredFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        SuppSquared.commonInit();
        if (PlatformHelper.getEnv().isClient()) {
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::init);
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::setup);
        }

    }

}
