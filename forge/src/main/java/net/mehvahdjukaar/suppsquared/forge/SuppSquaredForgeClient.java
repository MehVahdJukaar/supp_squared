package net.mehvahdjukaar.suppsquared.forge;

import net.minecraftforge.common.MinecraftForge;

public class SuppSquaredForgeClient {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(SuppSquaredForgeClient.class);
    }

}
