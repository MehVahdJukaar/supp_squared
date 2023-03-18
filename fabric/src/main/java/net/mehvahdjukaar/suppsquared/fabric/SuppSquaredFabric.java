package net.mehvahdjukaar.suppsquared.fabric;

import net.fabricmc.api.ModInitializer;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.platform.fabric.RegHelperImpl;
import net.mehvahdjukaar.suppsquared.SuppSquared;

public class SuppSquaredFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        SuppSquared.commonInit();


    }

}
