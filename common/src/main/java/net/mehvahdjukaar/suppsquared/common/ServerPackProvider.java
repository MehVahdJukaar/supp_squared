package net.mehvahdjukaar.suppsquared.common;

import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynServerResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicDataPack;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.resources.recipe.IRecipeTemplate;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

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
    public void regenerateDynamicAssets(Consumer<ResourceGenTask> executor) {
        super.regenerateDynamicAssets(executor);

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
        IRecipeTemplate<?> template = RPUtils.readRecipeAsTemplate(manager,
                ResType.RECIPES.getPath(SuppSquared.res("item_shelf_oak")));

        SuppSquared.ITEM_SHELVES.forEach((w, b) -> {
            if (w != VanillaWoodTypes.OAK) {
                FinishedRecipe newR = template.createSimilar(VanillaWoodTypes.OAK, w, w.mainChild().asItem());
                if (newR == null) return;
                newR = ForgeHelper.addRecipeConditions(newR, template.getConditions());
                sink.addJson(SuppSquared.res(Utils.getID(b).getPath()), newR.serializeRecipe(), ResType.RECIPES);
                ResourceLocation advancementId = newR.getAdvancementId();
                if (advancementId != null) {
                    sink.addJson(newR.getAdvancementId(), newR.serializeAdvancement(), ResType.ADVANCEMENTS);
                }
            }
        });
    }


}
