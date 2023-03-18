package net.mehvahdjukaar.suppsquared;

import com.google.gson.JsonParser;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class ServerPackProvider extends DynServerResourcesProvider {

    public static final ServerPackProvider INSTANCE = new ServerPackProvider();

    public ServerPackProvider() {
        super(new DynamicDataPack(SuppSquared.res("generated_pack"), Pack.Position.BOTTOM, true, true));
        this.dynamicPack.generateDebugResources = true;
        this.dynamicPack.addNamespaces("minecraft");
    }

    @Override
    public Logger getLogger() {
        return SuppSquared.LOGGER;
    }

    @Override
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {

        //------item shelves-----
        StaticResource isLoot = StaticResource.getOrLog(manager,
                ResType.LOOT_TABLES.getPath(Supplementaries.res("item_shelf")));

        SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
            String id = Utils.getID(sign).toString();

            try {
                addSimilarJsonResource(manager, isLoot,
                        s -> s.replace("supplementaries:item_shelf", id));
            } catch (Exception ex) {
                getLogger().error("Failed to generate loot tables for {} : {}", sign, ex);
            }
        });

        StaticResource sackLoot = StaticResource.getOrLog(manager,
                ResType.LOOT_TABLES.getPath(Supplementaries.res("sack")));

        for(DyeColor c : DyeColor.values()){
            try {
                addSimilarJsonResource(manager, sackLoot,
                        s -> s.replace("supplementaries:sack", "suppsquared:sack_"+c.getName()));
            } catch (Exception ex) {
            }
        }

    }

    @Override
    public void generateStaticAssetsOnStartup(ResourceManager manager) {

    }


}
