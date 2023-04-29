package net.mehvahdjukaar.suppsquared.forge;

import net.mehvahdjukaar.moonlight.api.platform.ClientPlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.mehvahdjukaar.suppsquared.SuppSquaredClient;
import net.minecraft.world.phys.Vec3;
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
            ClientPlatformHelper.addClientSetup(SuppSquaredClient::setup);
        }

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(SuppSquaredForge::setup);

        /*
        v(t) = sqrt(f * r / m);
        f = ma;
        a = DV;
        m = 1;
        (Vrot(t) ^ 2) / Rmin = DV;
        t = ticks;
        v(t) = v0 + DV * t;
        r(t) = r0 + DR * t;

        float radInc = 0.2f;
        float targetRad = 1.2f;
        Vec3 radius = Vec3.ZERO;//current radius r(t)

        Vec3 gravityMovement = radius.normalize().scale(radInc);
        Vec3 sidewayMovement = radius.cross(new Vec3(0, 0, 1)).scale(Math.sqrt(targetRad * radInc * radInc / radius.length()));
        Vec3 vecToApply = gravityMovement.add(sidewayMovement);*/
    }

    public static void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(SuppSquared::commonSetup);
    }


}

