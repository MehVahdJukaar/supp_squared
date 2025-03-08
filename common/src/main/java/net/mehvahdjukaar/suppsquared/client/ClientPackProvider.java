package net.mehvahdjukaar.suppsquared.client;

import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.mehvahdjukaar.moonlight.api.resources.textures.Palette;
import net.mehvahdjukaar.moonlight.api.resources.textures.Respriter;
import net.mehvahdjukaar.moonlight.api.resources.textures.SpriteUtils;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
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
    public boolean dependsOnLoadedPacks() {
        return true;
    }

    @Override
    public void regenerateDynamicAssets(ResourceManager manager) {


        //------item shelves-----
        StaticResource isItemModel = StaticResource.getOrLog(manager,
                ResType.ITEM_MODELS.getPath(SuppSquared.res("item_shelf_suppsquared_shelf")));
        StaticResource isBlockState = StaticResource.getOrLog(manager,
                ResType.BLOCKSTATES.getPath(SuppSquared.res("item_shelf_suppsquared_shelf")));
        StaticResource isModel = StaticResource.getOrLog(manager,
                ResType.BLOCK_MODELS.getPath(SuppSquared.res("item_shelves/suppsquared_shelf")));

        SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
            String id = Utils.getID(sign).getPath();
            if (wood == WoodTypeRegistry.OAK_TYPE) return;
            try {
                addSimilarJsonResource(manager, isBlockState, s ->
                        s.replace("item_shelf_suppsquared_shelf", id)
                                .replace("suppsquared_shelf", id.replace("item_shelf_", "")));
                addSimilarJsonResource(manager, isModel, s ->
                        s.replace("item_shelf_suppsquared_shelf", id)
                                .replace("suppsquared_shelf", id.replace("item_shelf_", "")));
                addSimilarJsonResource(manager, isItemModel, s ->
                        s.replace("item_shelf_suppsquared_shelf", id)
                                .replace("suppsquared_shelf", id.replace("item_shelf_", "")));

            } catch (Exception ex) {
                getLogger().error("Failed to generate models for {} : {}", sign, ex);
            }
        });


        //item textures
        try (TextureImage template = TextureImage.open(manager, Supplementaries.res("item/item_shelf"))) {

            Respriter respriter = Respriter.of(template);

            SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {

                ResourceLocation textureRes = SuppSquared.res("item/item_shelves/" + Utils.getID(sign).getPath()
                        .replace("item_shelf_", ""));

                if (alreadyHasTextureAtLocation(manager, textureRes)) return;

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
                    dynamicPack.addAndCloseTexture(textureRes, newImage);
                }
            });
        } catch (Exception ex) {
            getLogger().error("Could not generate any Item Shelves item texture : ", ex);
        }

        //block textures
        try (TextureImage template = TextureImage.open(manager,
                Supplementaries.res("block/item_shelf"))) {

            Respriter respriter = Respriter.of(template);

            SuppSquared.ITEM_SHELVES.forEach((wood, sign) -> {
                var textureRes = SuppSquared.res("block/item_shelves/" + Utils.getID(sign).getPath()
                        .replace("item_shelf_", ""));
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
    }

    @Override
    public void addDynamicTranslations(AfterLanguageLoadEvent lang) {
        SuppSquared.ITEM_SHELVES.forEach((type, block) ->
                LangBuilder.addDynamicEntry(lang, "block.suppsquared.item_shelf", type, block));
    }


    public void generateSacks(ResourceManager manager) {


        try (TextureImage front_mask = TextureImage.open(manager, SuppSquared.res("block/front_mask"));
             TextureImage open_mask = TextureImage.open(manager, SuppSquared.res("block/open_mask"));
             TextureImage bottom = TextureImage.open(manager, Supplementaries.res("block/sack_bottom"));
             TextureImage closed = TextureImage.open(manager, Supplementaries.res("block/sack_closed"));
             TextureImage open = TextureImage.open(manager, Supplementaries.res("block/sack_open"));
             TextureImage top = TextureImage.open(manager, Supplementaries.res("block/sack_top"));
             TextureImage front = TextureImage.open(manager, Supplementaries.res("block/sack_front"))
        ) {
            Respriter bottom_res = Respriter.of(bottom);
            Respriter closed_res = Respriter.of(closed);
            Respriter open_res = Respriter.masked(open, open_mask);
            Respriter top_res = Respriter.of(top);
            Respriter front_res = Respriter.masked(front, front_mask);

            for (var d : DyeColor.values()) {

                try (TextureImage bottom_c = TextureImage.open(manager, SuppSquared.res("block/sack_" + d.getName() + "_bottom"));
                     TextureImage closed_c = TextureImage.open(manager, SuppSquared.res("block/sack_" + d.getName() + "_closed"));
                     TextureImage open_c = TextureImage.open(manager, SuppSquared.res("block/sack_" + d.getName() + "_open"));
                     TextureImage top_c = TextureImage.open(manager, SuppSquared.res("block/sack_" + d.getName() + "_top"));
                     TextureImage front_c = TextureImage.open(manager, SuppSquared.res("block/sack_" + d.getName() + "_front"))
                ) {
                    this.dynamicPack.addAndCloseTexture(SuppSquared.res("sack_" + d.getName() + "_bottom"),
                            bottom_res.recolor(Palette.fromImage(bottom_c)));

                    this.dynamicPack.addAndCloseTexture(SuppSquared.res("sack_" + d.getName() + "_closed"),
                            closed_res.recolor(Palette.fromImage(closed_c)));

                    var i = open_res.recolor(Palette.fromImage(open_c, open_mask));
                    open_c.crop(open_mask.makeCopy(), false);
                    i.applyOverlay(open_c);
                    this.dynamicPack.addAndCloseTexture(SuppSquared.res("sack_" + d.getName() + "_open"), i);

                    var f = front_res.recolor(Palette.fromImage(front_c, front_mask));
                    front_c.crop(front_mask.makeCopy(), false);
                    f.applyOverlay(front_c);
                    this.dynamicPack.addAndCloseTexture(SuppSquared.res("sack_" + d.getName() + "_front"), f);

                    this.dynamicPack.addAndCloseTexture(SuppSquared.res("sack_" + d.getName() + "_top"),
                            top_res.recolor(Palette.fromImage(top_c)));

                } catch (Exception ignored) {
                }
            }
        } catch (Exception ex) {
            int aa = 1;
        }
    }

}
