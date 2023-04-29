package net.mehvahdjukaar.suppsquared.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.util.VillagerAIManager;
import net.mehvahdjukaar.supplementaries.common.entities.trades.ModVillagerTrades;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.SuppSquaredClient;

public class SuppSquaredFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        //be sure supp configs are loaded. might cause issues with quilt if i dont for some reason
        CommonConfigs.init();
        if (!CommonConfigs.SPEC.isLoaded()) {
            throw new AssertionError();
        }

        SuppSquared.commonInit();
        if (PlatformHelper.getEnv().isClient()) {
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::init);
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::setup);
        }

    }

}
