package net.mehvahdjukaar.suppsquared.client;

import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesProvider;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter;
import net.mehvahdjukaar.moonlight.api.resources.textures.SpriteUtils;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ClientPackProvider extends DynClientResourcesProvider {

    public static final ClientPackProvider INSTANCE = new ClientPackProvider();

    public ClientPackProvider() {
        super(new DynamicTexturePack(SuppSquared.res("generated_pack"), Pack.Position.BOTTOM, true, true));
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
        StaticResource isItemModel = StaticResource.getOrLog(manager,
                ResType.ITEM_MODELS.getPath(Supplementaries.res("item_shelf")));
        StaticResource isBlockState = StaticResource.getOrLog(manager,
                ResType.BLOCKSTATES.getPath(Supplementaries.res("item_shelf")));
        StaticResource isModel = StaticResource.getOrLog(manager,
                ResType.BLOCK_MODELS.getPath(SuppSquared.res("item_shelves/birch")));

        SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
            String id = Utils.getID(sign).getPath();

            try {
                addSimilarJsonResource(manager, isItemModel,
                        s -> s.replace("item_shelf", id)
                                .replace("supplementaries:items/item_shelf_", "suppsquared:item/item_shelves/")
                );
                addSimilarJsonResource(manager, isBlockState,
                        s -> s.replace("item_shelf", id)
                                .replace("supplementaries:block/item_shelf_", "suppsquared:block/item_shelves/")
                );
                addSimilarJsonResource(manager, isModel,
                        s -> s.replace("birch", id.replace("item_shelf_", ""))
                );
            } catch (Exception ex) {
                getLogger().error("Failed to generate models for {} : {}", sign, ex);
            }
        });


        //item textures
        try (TextureImage template = TextureImage.open(manager, Supplementaries.res("items/item_shelf"))) {

            Respriter respriter = Respriter.of(template);

            SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {

                ResourceLocation textureRes = SuppSquared.res("item/item_shelves/" + Utils.getID(sign).getPath()
                        .replace("item_shelf_",""));

                if (alreadyHasTextureAtLocation(manager, textureRes)) return;

                TextureImage newImage = null;
                Item signItem = wood.getItemOfThis("sign");
                if (signItem != null) {
                    try (TextureImage vanillaSign = TextureImage.open(manager,
                            RPUtils.findFirstItemTextureLocation(manager, signItem));
                         TextureImage signMask = TextureImage.open(manager,
                                 Supplementaries.res("items/hanging_signs/sign_board_mask"))) {

                        List<Palette> targetPalette = Palette.fromAnimatedImage(vanillaSign, signMask);
                        newImage = respriter.recolor(targetPalette);


                    } catch (Exception ignored) {
                    }
                }
                //if it failed use plank one
                if (newImage == null) {
                    try (TextureImage plankPalette = TextureImage.open(manager,
                            RPUtils.findFirstBlockTextureLocation(manager, wood.planks))) {
                        Palette targetPalette = SpriteUtils.extrapolateWoodItemPalette(plankPalette);
                        newImage = respriter.recolor(targetPalette);

                    } catch (Exception ex) {
                        getLogger().error("Failed to generate Sign Post item texture for for {} : {}", sign, ex);
                    }
                }
                if (newImage != null) {
                    dynamicPack.addAndCloseTexture(textureRes, newImage);
                }
            });
        } catch (Exception ex) {
            getLogger().error("Could not generate any Item Shelves item texture : ", ex);
        }

        //block textures
        try (TextureImage template = TextureImage.open(manager,
                Supplementaries.res("blocks/item_shelf"))) {

            Respriter respriter = Respriter.of(template);

            SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
                var textureRes = SuppSquared.res("block/item_shelves/" + Utils.getID(sign).getPath()
                        .replace("item_shelf_",""));
                if (alreadyHasTextureAtLocation(manager, textureRes)) return;

                try (TextureImage plankTexture = TextureImage.open(manager,
                        RPUtils.findFirstBlockTextureLocation(manager, wood.planks))) {
                    Palette palette = Palette.fromImage(plankTexture);
                    TextureImage newImage = respriter.recolor(palette);

                    dynamicPack.addAndCloseTexture(textureRes, newImage);
                } catch (Exception ex) {
                    getLogger().error("Failed to generate Item Shelf block texture for for {} : {}", sign, ex);
                }
            });
        } catch (Exception ex) {
            getLogger().error("Could not generate any Item Shelf block texture : ", ex);
        }
    }

    @Override
    public void generateStaticAssetsOnStartup(ResourceManager manager) {

    }
    @Override
    public void addDynamicTranslations(AfterLanguageLoadEvent lang) {
        SuppSquared.ITEM_SHELVES.forEach((type, block) ->
                LangBuilder.addDynamicEntry(lang, "block.suppsquared.item_shelf", type, block));
    }

}
