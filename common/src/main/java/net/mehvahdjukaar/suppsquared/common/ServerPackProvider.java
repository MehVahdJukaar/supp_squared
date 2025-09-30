package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicServerResourceProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.PackGenerationStrategy;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.Recipe;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ServerPackProvider extends DynamicServerResourceProvider {


    public ServerPackProvider() {
        super(SuppSquared.res("dynamic_assets"), PackGenerationStrategy.CACHED);
    }

    @Override
    protected Collection<String> gatherSupportedNamespaces() {
        return List.of("supplementaries", "minecraft");
    }

    @Override
    public void regenerateDynamicAssets(Consumer<ResourceGenTask> executor) {
        executor.accept((manager, sink) -> {

            //------item shelves-----
            SimpleTagBuilder builder = SimpleTagBuilder.of(SuppSquared.res("item_shelves"));

            SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
                builder.addEntry(sign);
                if (wood != VanillaWoodTypes.OAK) {
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
            if (w != VanillaWoodTypes.OAK) {
                try {
                    var newR = RPUtils.makeSimilarRecipe(recipe, VanillaWoodTypes.OAK, w, Supplementaries.res("item_shelf"));
                    //newR = ForgeHelper.addRecipeConditions(newR, recipe);
                    sink.addRecipe(newR);
                } catch (Exception e) {
                    Supplementaries.LOGGER.error("Failed to generate recipe for item shelf {}:", w, e);
                }
            }
        });
    }
}

