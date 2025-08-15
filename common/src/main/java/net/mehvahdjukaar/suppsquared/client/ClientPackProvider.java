package net.mehvahdjukaar.suppsquared.client;

import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter;
import net.mehvahdjukaar.moonlight.api.resources.textures.SpriteUtils;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.suppsquared.SuppSquared;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Consumer;

public class ClientPackProvider extends DynClientResourcesGenerator {

    public static final ClientPackProvider INSTANCE = new ClientPackProvider();

    public ClientPackProvider() {
        super(new DynamicTexturePack(SuppSquared.res("generated_pack"), Pack.Position.BOTTOM, true, true));
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
            StaticResource isItemModel = StaticResource.getOrLog(manager,
                    ResType.ITEM_MODELS.getPath(SuppSquared.res("item_shelf_suppsquared_shelf")));
            StaticResource isBlockState = StaticResource.getOrLog(manager,
                    ResType.BLOCKSTATES.getPath(SuppSquared.res("item_shelf_suppsquared_shelf")));
            StaticResource isModel = StaticResource.getOrLog(manager,
                    ResType.BLOCK_MODELS.getPath(SuppSquared.res("item_shelves/suppsquared_shelf")));

            SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
                String id = Utils.getID(sign).getPath();
                if (wood == VanillaWoodTypes.OAK) return;
                try {
                    sink.addSimilarJsonResource(manager, isBlockState, s ->
                            s.replace("item_shelf_suppsquared_shelf", id)
                                    .replace("suppsquared_shelf", id.replace("item_shelf_", "")));
                    sink.addSimilarJsonResource(manager, isModel, s ->
                            s.replace("item_shelf_suppsquared_shelf", id)
                                    .replace("suppsquared_shelf", id.replace("item_shelf_", "")));
                    sink.addSimilarJsonResource(manager, isItemModel, s ->
                            s.replace("item_shelf_suppsquared_shelf", id)
                                    .replace("suppsquared_shelf", id.replace("item_shelf_", "")));

                } catch (Exception ex) {
                    getLogger().error("Failed to generate models for {} : {}", sign, ex);
                }
            });
        });

        executor.accept((manager, sink) -> {

            //item textures
            try (TextureImage template = TextureImage.open(manager, Supplementaries.res("item/item_shelf"))) {

                Respriter respriter = Respriter.of(template);

                SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {

                    ResourceLocation textureRes = SuppSquared.res("item/item_shelves/" + Utils.getID(sign).getPath()
                            .replace("item_shelf_", ""));

                    if (sink.alreadyHasTextureAtLocation(manager, textureRes)) return;
//TODO: copy supp code here
                    TextureImage newImage = null;
                    Item signItem = wood.getItemOfThis("sign");
                    if (signItem != null) {
                        try (TextureImage vanillaSign = TextureImage.open(manager,
                                RPUtils.findFirstItemTextureLocation(manager, signItem));
                             TextureImage signMask = TextureImage.open(manager,
                                     Supplementaries.res("item/hanging_signs/sign_board_mask"))) {

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
                        sink.addAndCloseTexture(textureRes, newImage);
                    }
                });
            } catch (Exception ex) {
                getLogger().error("Could not generate any Item Shelves item texture : ", ex);
            }

            });

        executor.accept((manager, sink) -> {

            //block textures
            try (TextureImage template = TextureImage.open(manager,
                    Supplementaries.res("block/item_shelf"))) {

                Respriter respriter = Respriter.of(template);

                SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
                    var textureRes = SuppSquared.res("block/item_shelves/" + Utils.getID(sign).getPath()
                            .replace("item_shelf_", ""));
                    if (sink.alreadyHasTextureAtLocation(manager, textureRes)) return;

                    try (TextureImage plankTexture = TextureImage.open(manager,
                            RPUtils.findFirstBlockTextureLocation(manager, wood.planks))) {
                        Palette palette = Palette.fromImage(plankTexture);
                        try (TextureImage newImage = respriter.recolor(palette)) {
                            sink.addTexture(textureRes, newImage);
                        }
                    } catch (Exception ex) {
                        getLogger().error("Failed to generate Item Shelf block texture for for {} : {}", sign, ex);
                    }
                });
            } catch (Exception ex) {
                getLogger().error("Could not generate any Item Shelf block texture : ", ex);
            }
/*
        try (TextureImage c = TextureImage.open(manager, new ResourceLocation("block/copper_block"));
             TextureImage s = TextureImage.open(manager, SuppSquared.res("block/copper_plaque"))) {

            Respriter front_res = Respriter.of(s);
             Palette targetPalette = Palette.fromImage(c);
            targetPalette.remove(targetPalette.getDarkest());
            targetPalette.remove(targetPalette.getDarkest());
            this.dynamicPack.addAndCloseTexture(SuppSquared.res("block/copper_plaque"), front_res.recolor(targetPalette));
        } catch (Exception e) {
        }*/

            // aa(manager);
        });
    }


    @Override
    public void addDynamicTranslations(AfterLanguageLoadEvent lang) {
        SuppSquared.ITEM_SHELVES.forEach((type, block) ->
                LangBuilder.addDynamicEntry(lang, "block.suppsquared.item_shelf", type, block));
    }

}
