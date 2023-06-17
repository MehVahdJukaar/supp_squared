package net.mehvahdjukaar.suppsquared.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.fabric.SupplementariesFabric;
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
        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientHelper.addClientSetup(SuppSquaredClient::init);
            ClientHelper.addClientSetup(SuppSquaredClient::setup);
        }

    }

}
