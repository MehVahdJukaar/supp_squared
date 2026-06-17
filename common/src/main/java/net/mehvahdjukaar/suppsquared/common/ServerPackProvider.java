package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicServerResourceProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.PackGenerationStrategy;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

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
        // Use our own oak recipe (which uses oak_slab specifically) as the template.
        // Supplementaries' item_shelf recipe uses the generic wooden_slabs tag, which the block
        // type swap can't remap, and it's disabled when this mod is present anyway.
        ResourceLocation oakRecipe = SuppSquared.res("item_shelf_oak");

        SuppSquared.ITEM_SHELVES.forEach((w, b) -> {
            if (w != VanillaWoodTypes.OAK) {
                try {
                    sink.addBlockTypeSwapRecipe(manager, oakRecipe, VanillaWoodTypes.OAK, w, Utils.getID(b));
                } catch (Exception e) {
                    Supplementaries.LOGGER.error("Failed to generate recipe for item shelf {}:", w, e);
                }
            }
        });
    }
}

