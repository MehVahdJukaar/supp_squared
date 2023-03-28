package net.mehvahdjukaar.suppsquared;

import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.PlatformHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBraceBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.ItemShelfBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SackBlock;
import net.mehvahdjukaar.supplementaries.common.items.OptionalTagBlockItem;
import net.mehvahdjukaar.supplementaries.common.items.SackItem;
import net.mehvahdjukaar.supplementaries.common.items.TimberFrameItem;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.RegUtils;
import net.mehvahdjukaar.suppsquared.client.ClientPackProvider;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlock;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlockTile;
import net.mehvahdjukaar.suppsquared.common.SackDyeRecipe;
import net.mehvahdjukaar.suppsquared.common.ServerPackProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.mehvahdjukaar.supplementaries.reg.ModConstants.SACK_NAME;
import static net.mehvahdjukaar.supplementaries.reg.ModConstants.TIMBER_FRAME_NAME;
import static net.mehvahdjukaar.supplementaries.reg.RegUtils.getTab;

/**
 * Author: MehVahdJukaar
 */
public class SuppSquared {

    public static final String MOD_ID = "suppsquared";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }


    public static void commonInit() {
        if (PlatformHelper.getEnv().isClient()) {
            ClientPackProvider.INSTANCE.register();
        }
        ServerPackProvider.INSTANCE.register();
        BlockSetAPI.addDynamicBlockRegistration(SuppSquared::registerItemShelves, WoodType.class);
        BlockSetAPI.addDynamicItemRegistration(SuppSquared::registerItemShelfItems, WoodType.class);
    }

    public static void commonSetup() {
    }

    private static void registerItemShelves(Registrator<Block> event, Collection<WoodType> types) {
        for (WoodType wood : types) {
            Block instance;
            if (wood == WoodTypeRegistry.OAK_TYPE) {
                //instance = ITEM_SHELF.get();
            } else {
                String name = wood.getVariantId("item_shelf");
                ItemShelfBlock block = new ItemShelfBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.WOOD)
                        .sound(SoundType.WOOD)
                        .strength(0.75F, 0.1F)
                        .noOcclusion()
                        .noCollission());
                instance = block;
                event.register(SuppSquared.res(name), block);
                ITEM_SHELVES.put(wood, instance);
                wood.addChild("supplementaries:item_shelf", (Object) instance);
            }
        }
    }

    public static void registerItemShelfItems(Registrator<Item> event, Collection<WoodType> woodTypes) {
        for (var entry : ITEM_SHELVES.entrySet()) {
            WoodType wood = entry.getKey();
            if (wood == WoodTypeRegistry.OAK_TYPE) continue;
            Block block = entry.getValue();
            Item item = new WoodBasedBlockItem(
                    block, new Item.Properties().stacksTo(16).tab(getTab(CreativeModeTab.TAB_DECORATIONS, "hanging_sign")), wood, 200
            );
            event.register(Utils.getID(block), item);
        }
        ITEM_SHELVES.put(WoodTypeRegistry.OAK_TYPE, ModRegistry.ITEM_SHELF.get());
        WoodTypeRegistry.OAK_TYPE.addChild("supplementaries:item_shelf", (Object) ModRegistry.ITEM_SHELF.get());

    }

    private static final RegHelper.VariantType[] TYPES = List.of(RegHelper.VariantType.SLAB, RegHelper.VariantType.STAIRS, RegHelper.VariantType.VERTICAL_SLAB).toArray(RegHelper.VariantType[]::new);


    public static final Supplier<RecipeSerializer<SackDyeRecipe>> SACK_DYE_RECIPE =
            RegHelper.registerRecipeSerializer(new ResourceLocation("suppsquared:sack_dye"), () -> new SimpleRecipeSerializer<>(SackDyeRecipe::new));

    public static final Map<RegHelper.VariantType, Supplier<Block>> DAUBS = RegHelper.registerBlockSet(TYPES, ModRegistry.DAUB, SuppSquared.MOD_ID, false);

    public static final Map<RegHelper.VariantType, Supplier<Block>> FRAMES = RegHelper.registerBlockSet(TYPES, ModRegistry.DAUB_FRAME, SuppSquared.MOD_ID, false);


    public static final Map<WoodType, Block> ITEM_SHELVES = new LinkedHashMap<>();

    public static final Map<DyeColor, Supplier<Block>> SACKS = Arrays.stream(DyeColor.values())
            .collect(Collectors.toUnmodifiableMap(d -> d, d ->
                    regBlock(SACK_NAME + "_" + d.getName(), () -> new SackBlock(
                            BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.WOOD).strength(0.8F).sound(ModSounds.SACK)
                                    .color(d.getMaterialColor())
                    ))));

    public static final Map<DyeColor, Supplier<Item>> SACK_ITEMS = Arrays.stream(DyeColor.values())
            .collect(Collectors.toUnmodifiableMap(d -> d, d ->
                    regItem(SACK_NAME + "_" + d.getName(), () -> new SackItem(
                            SACKS.get(d).get(),
                            new Item.Properties().tab(getTab(CreativeModeTab.TAB_DECORATIONS, SACK_NAME)).stacksTo(1)
                    ))));

    public static final Map<DyeColor, Supplier<Block>> GOLDEN_CANDLE_HOLDERS = RegUtils.
            registerCandleHolders(res("gold_candle_holder"));


    //iron frames

    //iron frame
    public static final RegSupplier<FrameBlock> IRON_FRAME = regBlock("metal_frame", () ->
            new FrameBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
                    .strength(5.0F, 6.0F)
                    .dynamicShape().sound(SoundType.METAL)));
    public static final Supplier<Item> IRON_FRAME_ITEM = regItem("metal_frame", () -> new TimberFrameItem(IRON_FRAME.get(),
            new Item.Properties().tab(getTab(CreativeModeTab.TAB_BUILDING_BLOCKS, TIMBER_FRAME_NAME))));

    //iron brace
    public static final Supplier<FrameBraceBlock> IRON_BRACE = regBlock("metal_brace", () ->
            new FrameBraceBlock(BlockBehaviour.Properties.copy(IRON_FRAME.get())));
    public static final Supplier<Item> TIMBER_BRACE_ITEM = regItem("metal_brace", () -> new TimberFrameItem(IRON_BRACE.get(),
            new Item.Properties().tab(getTab(CreativeModeTab.TAB_BUILDING_BLOCKS, TIMBER_FRAME_NAME))));

    //iron cross brace
    public static final Supplier<FrameBlock> IRON_CROSS_BRACE = regBlock("metal_cross_brace", () ->
            new FrameBlock(BlockBehaviour.Properties.copy(IRON_FRAME.get())));
    public static final Supplier<Item> TIMBER_CROSS_BRACE_ITEM = regItem("metal_cross_brace", () -> new TimberFrameItem(IRON_CROSS_BRACE.get(),
            new Item.Properties().tab(getTab(CreativeModeTab.TAB_BUILDING_BLOCKS, TIMBER_FRAME_NAME))));


    //plaque
    public static final Supplier<PlaqueBlock> GOLD_PLAQUE = regWithItem("gold_plaque", () ->
            new PlaqueBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.NONE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops().noCollission()
                    .strength(1, 1)), CreativeModeTab.TAB_DECORATIONS);

    public static final Supplier<PlaqueBlock> IRON_PLAQUE = regWithItem("iron_plaque", () ->
                    new PlaqueBlock(BlockBehaviour.Properties.copy(GOLD_PLAQUE.get())),
            CreativeModeTab.TAB_DECORATIONS);

    public static final Supplier<PlaqueBlock> COPPER_PLAQUE = regWithItem("copper_plaque", () ->
                    new PlaqueBlock(BlockBehaviour.Properties.copy(GOLD_PLAQUE.get())),
            CreativeModeTab.TAB_DECORATIONS);

    public static final Supplier<BlockEntityType<PlaqueBlockTile>> PLAQUE_TILE =
            RegHelper.registerBlockEntityType(res("plaque"),
                    () -> PlatformHelper.newBlockEntityType(
                            PlaqueBlockTile::new,
                            IRON_PLAQUE.get(), COPPER_PLAQUE.get(), GOLD_PLAQUE.get()));


    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> sup) {
        return RegHelper.registerItem(SuppSquared.res(name), sup);
    }

    public static <T extends BlockEntityType<E>, E extends BlockEntity> Supplier<T> regTile(String name, Supplier<T> sup) {
        return RegHelper.registerBlockEntityType(SuppSquared.res(name), sup);
    }

    public static <T extends Block> RegSupplier<T> regBlock(String name, Supplier<T> sup) {
        return RegHelper.registerBlock(SuppSquared.res(name), sup);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory, CreativeModeTab tab) {
        return regWithItem(name, blockFactory, new Item.Properties().tab(getTab(tab, name)), 0);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory, CreativeModeTab tab, int burnTime) {
        return regWithItem(name, blockFactory, new Item.Properties().tab(getTab(tab, name)), burnTime);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory, Item.Properties properties, int burnTime) {
        RegSupplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, properties, burnTime);
        return block;
    }

    public static <T extends Block> Supplier<T> regWithItem(String name, Supplier<T> block, CreativeModeTab tab, String requiredMod) {
        CreativeModeTab t = PlatformHelper.isModLoaded(requiredMod) ? tab : null;
        return regWithItem(name, block, t);
    }

    public static RegSupplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, CreativeModeTab group, String tagKey) {
        return RegHelper.registerItem(SuppSquared.res(name), () -> new OptionalTagBlockItem(blockSup.get(), new Item.Properties().tab(group), tagKey));
    }

    public static RegSupplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties, int burnTime) {
        return RegHelper.registerItem(SuppSquared.res(name), () -> burnTime == 0 ? new BlockItem(blockSup.get(), properties) :
                new WoodBasedBlockItem(blockSup.get(), properties, burnTime));
    }

    public static RegSupplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties) {
        return regBlockItem(name, blockSup, properties, 0);
    }

}
