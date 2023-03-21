package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.resources.recipe.IRecipeTemplate;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.Logger;

public class ServerPackProvider extends DynServerResourcesProvider {

    public static final ServerPackProvider INSTANCE = new ServerPackProvider();

    public ServerPackProvider() {
        super(new DynamicDataPack(SuppSquared.res("generated_pack"), Pack.Position.BOTTOM, true, true));
        this.dynamicPack.generateDebugResources = true;
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
        SimpleTagBuilder builder = SimpleTagBuilder.of(Supplementaries.res("item_shelves"));

        SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
            builder.addEntry(sign);
            String id = Utils.getID(sign).toString();
            if(wood != WoodTypeRegistry.OAK_TYPE) {
                dynamicPack.addSimpleBlockLootTable(sign);
            }
        });

        dynamicPack.addTag(builder, Registry.BLOCK_REGISTRY);
        dynamicPack.addTag(builder, Registry.ITEM_REGISTRY);

        addItemShelfRecipes(manager);
    }

    private void addItemShelfRecipes(ResourceManager manager) {
        IRecipeTemplate<?> template = RPUtils.readRecipeAsTemplate(manager,
                ResType.RECIPES.getPath(Supplementaries.res("item_shelf")));

        SuppSquared.ITEM_SHELVES.forEach((w, b) -> {
            if (w != WoodTypeRegistry.OAK_TYPE) {
                Item i = b.asItem();
                //check for disabled ones. Will actually crash if its null since vanilla recipe builder expects a non-null one
                if (i.getItemCategory() != null) {
                    FinishedRecipe newR = template.createSimilar(WoodTypeRegistry.OAK_TYPE, w, w.mainChild().asItem());
                    if (newR == null) return;
                    newR = ForgeHelper.addRecipeConditions(newR, template.getConditions());
                    dynamicPack.addJson(SuppSquared.res(Utils.getID(b).getPath()), newR.serializeRecipe(), ResType.RECIPES);
                    ResourceLocation advancementId = newR.getAdvancementId();
                    if (advancementId != null) {
                        dynamicPack.addJson(newR.getAdvancementId(), newR.serializeAdvancement(), ResType.ADVANCEMENTS);
                    }
                }
            }
        });
    }

    @Override
    public void generateStaticAssetsOnStartup(ResourceManager manager) {

    }


}
