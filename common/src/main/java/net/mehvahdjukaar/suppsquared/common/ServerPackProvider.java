package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ServerPackProvider extends DynServerResourcesGenerator {

    public static final ServerPackProvider INSTANCE = new ServerPackProvider();

    public ServerPackProvider() {
        super(new DynamicDataPack(SuppSquared.res("generated_pack"), Pack.Position.BOTTOM, true, true));
    }

    @Override
    public Collection<String> additionalNamespaces() {
        return List.of("minecraft", "supplementaries");
    }

    @Override
    public Logger getLogger() {
        return SuppSquared.LOGGER;
    }

    @Override
    public void regenerateDynamicAssets(Consumer<ResourceGenTask> executor) {
        super.regenerateDynamicAssets(executor);
        executor.accept((manager, sink) -> {

            //------item shelves-----
            SimpleTagBuilder builder = SimpleTagBuilder.of(SuppSquared.res("item_shelves"));

            SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
                builder.addEntry(sign);
                if (wood != WoodTypeRegistry.OAK_TYPE) {
                    sink.addSimpleBlockLootTable(sign);
                }
            });

            sink.addTag(builder, Registries.BLOCK);
            sink.addTag(builder, Registries.ITEM);

            addItemShelfRecipes(manager, sink);
        });
    }


    private void addItemShelfRecipes(ResourceManager manager, ResourceSink sink) {
        Recipe<?> recipe = RPUtils.readRecipe(manager, Supplementaries.res("item_shelf"));

        SuppSquared.ITEM_SHELVES.forEach((w, b) -> {
            if (w != WoodTypeRegistry.OAK_TYPE) {
                try {
                    var newR = RPUtils.makeSimilarRecipe(recipe, WoodTypeRegistry.OAK_TYPE, w, Supplementaries.res("item_shelf"));
                    //newR = ForgeHelper.addRecipeConditions(newR, recipe);
                    sink.addRecipe(newR);
                } catch (Exception e) {
                    Supplementaries.LOGGER.error("Failed to generate recipe for item shelf {}:", w, e);
                }
            }
        });
    }
}

