package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.logging.log4j.Logger;

public class ServerPackProvider extends DynServerResourcesGenerator {

    public static final ServerPackProvider INSTANCE = new ServerPackProvider();

    public ServerPackProvider() {
        super(new DynamicDataPack(SuppSquared.res("generated_pack"), Pack.Position.BOTTOM, true, true));
        this.dynamicPack.addNamespaces("minecraft");
        this.dynamicPack.addNamespaces("supplementaries");
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
        SimpleTagBuilder builder = SimpleTagBuilder.of(SuppSquared.res("item_shelves"));

        SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
            builder.addEntry(sign);
            if (wood != WoodTypeRegistry.OAK_TYPE) {
                dynamicPack.addSimpleBlockLootTable(sign);
            }
        });

        dynamicPack.addTag(builder, Registries.BLOCK);
        dynamicPack.addTag(builder, Registries.ITEM);

        addItemShelfRecipes(manager);
    }

    private void addItemShelfRecipes(ResourceManager manager) {
        Recipe<?> recipe = RPUtils.readRecipe(manager, ResType.RECIPES.getPath(
                Supplementaries.res("item_shelf")));

        SuppSquared.ITEM_SHELVES.forEach((w, b) -> {
            if (w != WoodTypeRegistry.OAK_TYPE) {
                try {
                    var newR = RPUtils.makeSimilarRecipe(recipe, WoodTypeRegistry.OAK_TYPE, w,
                            "item_shelf");
                    //newR = ForgeHelper.addRecipeConditions(newR, recipe);
                    this.dynamicPack.addRecipe(newR);
                } catch (Exception e) {
                    Supplementaries.LOGGER.error("Failed to generate recipe for item shelf {}:", w, e);
                }
            }
        });
    }
}

