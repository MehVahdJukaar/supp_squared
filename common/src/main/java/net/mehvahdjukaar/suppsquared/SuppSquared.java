package net.mehvahdjukaar.suppsquared;

import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.misc.Registrator;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockSetAPI;
import net.mehvahdjukaar.moonlight.api.set.BlocksColorAPI;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.*;
import net.mehvahdjukaar.supplementaries.common.items.SackItem;
import net.mehvahdjukaar.supplementaries.common.items.TimberFrameItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModCreativeTabs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.mehvahdjukaar.supplementaries.reg.RegUtils;
import net.mehvahdjukaar.suppsquared.client.ClientPackProvider;
import net.mehvahdjukaar.suppsquared.common.*;
import net.mehvahdjukaar.suppsquared.common.CopperLanternBlock;
import net.mehvahdjukaar.suppsquared.common.CrimsonLanternBlock;
import net.mehvahdjukaar.suppsquared.common.LightableLanternBlock;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.Shapes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.mehvahdjukaar.supplementaries.reg.ModConstants.KEY_NAME;
import static net.mehvahdjukaar.supplementaries.reg.ModConstants.SACK_NAME;

/**
 * Author: MehVahdJukaar
 */
public class SuppSquared {

    public static final String MOD_ID = "suppsquared";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static ResourceLocation res(String name) {
        return new ResourceLocation(MOD_ID, name);
    }
    //TODO: figure out why keys areant obfuscared


    public static void commonInit() {
        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientPackProvider.INSTANCE.register();
        }
        ServerPackProvider.INSTANCE.register();
        BlockSetAPI.addDynamicBlockRegistration(SuppSquared::registerItemShelves, WoodType.class);
        BlockSetAPI.addDynamicItemRegistration(SuppSquared::registerItemShelfItems, WoodType.class);

        //we let supp call this so it always happens after
        ModCreativeTabs.SYNCED_ADD_TO_TABS.add(SuppSquared::addItemsToTabs);
    }

    private static void addItemsToTabs(RegHelper.ItemToTabEvent event) {
        if (CommonConfigs.Building.TIMBER_FRAME_ENABLED.get()) {
            event.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> i.is(ModRegistry.TIMBER_CROSS_BRACE_ITEM.get()),
                    IRON_FRAME.get(), IRON_BRACE.get(), IRON_CROSS_BRACE.get());
        }
        if (CommonConfigs.Building.DAUB_ENABLED.get()) {
            event.addAfter(CreativeModeTabs.BUILDING_BLOCKS, i -> i.is(ModRegistry.DAUB.get().asItem()),
                    DAUBS.get(RegHelper.VariantType.STAIRS).get(), DAUBS.get(RegHelper.VariantType.SLAB).get());
        }
        if (CommonConfigs.Building.WATTLE_AND_DAUB_ENABLED.get()) {
            event.addAfter(CreativeModeTabs.BUILDING_BLOCKS, i -> i.is(ModRegistry.DAUB_CROSS_BRACE.get().asItem()),
                    FRAMES.get(RegHelper.VariantType.STAIRS).get(), FRAMES.get(RegHelper.VariantType.SLAB).get());
        }
        if (CommonConfigs.Building.CANDLE_HOLDER_ENABLED.get()) {
            event.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> i.is(CANDLE_HOLDERS),
                    GOLDEN_CANDLE_HOLDERS.values().stream().map(Supplier::get).toArray(Block[]::new));
            event.addAfter(CreativeModeTabs.COLORED_BLOCKS, i -> i.is(CANDLE_HOLDERS),
                    GOLDEN_CANDLE_HOLDERS.values().stream().map(Supplier::get).toArray(Block[]::new));
        }
        if (CommonConfigs.isEnabled(KEY_NAME)) {
            event.addAfter(CreativeModeTabs.TOOLS_AND_UTILITIES, i -> i.is(ModRegistry.KEY_ITEM.get().asItem()),
                    SKELETON_KEY.get());
        }
        if (CommonConfigs.Functional.SACK_ENABLED.get()) {
            event.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> i.is(ModRegistry.SACK_ITEM.get()),
                    SACK_ITEMS.values().stream().map(Supplier::get).toArray(Item[]::new));
            event.addBefore(CreativeModeTabs.COLORED_BLOCKS, i -> i.is(Items.SHULKER_BOX), ModRegistry.SACK_ITEM.get());
            event.addAfter(CreativeModeTabs.COLORED_BLOCKS, i -> i.is(ModRegistry.SACK_ITEM.get()),
                    SACK_ITEMS.values().stream().map(Supplier::get).toArray(Item[]::new));
        }
        if (CommonConfigs.Building.ITEM_SHELF_ENABLED.get()) {
            Block[] array = ITEM_SHELVES.values().stream().filter(i->i!= ModRegistry.ITEM_SHELF.get()).toArray(Block[]::new);
            event.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> i.is(ModRegistry.ITEM_SHELF.get().asItem()), array);
        }
        if (isTagOn("c:brass_ingots") || isTagOn("forge:ingots/brass")) {
            event.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> i.is(Items.SOUL_LANTERN),
                    BRASS_LANTERN.get());
        }
        event.addAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, i -> i.is(Items.SOUL_LANTERN),
                COPPER_LANTERN.get(), CRIMSON_LANTERN.get());

        event.addBefore(CreativeModeTabs.FUNCTIONAL_BLOCKS, i ->
                        i.is(ModRegistry.DOORMAT.get().asItem()) ||
                                i.is(ModRegistry.ITEM_SHELF.get().asItem()) ||
                                i.is(Items.CHEST),
                IRON_PLAQUE.get(), COPPER_PLAQUE.get(), GOLD_PLAQUE.get());
    }

    private static boolean isTagOn(String tag) {
        return BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, new ResourceLocation(tag))).isPresent();
    }

    private static final TagKey<Item> CANDLE_HOLDERS = TagKey.create(Registries.ITEM, new ResourceLocation("supplementaries:candle_holders"));

    public static void commonSetup() {
    }

    private static void registerItemShelves(Registrator<Block> event, Collection<WoodType> types) {
        for (WoodType wood : types) {
            Block instance;
            if (wood == WoodTypeRegistry.OAK_TYPE) {
                //instance = ITEM_SHELF.get();
            } else {
                String name = wood.getVariantId("item_shelf");
                ItemShelfBlock block = new ItemShelfBlock(wood.copyProperties()
                        .sound(SoundType.WOOD)
                        .strength(0.75F, 0.1F)
                        .noOcclusion()
                        .noCollission());
                instance = block;
                event.register(SuppSquared.res(name), block);
                ITEM_SHELVES.put(wood, instance);
                wood.addChild("supplementaries:item_shelf", instance);
            }
        }
    }

    public static void registerItemShelfItems(Registrator<Item> event, Collection<WoodType> woodTypes) {
        for (var entry : ITEM_SHELVES.entrySet()) {
            WoodType wood = entry.getKey();
            if (wood == WoodTypeRegistry.OAK_TYPE) continue;
            Block block = entry.getValue();
            Item item = new WoodBasedBlockItem(
                    block, new Item.Properties().stacksTo(16), wood, 200
            );
            event.register(Utils.getID(block), item);
        }
        ITEM_SHELVES.put(WoodTypeRegistry.OAK_TYPE, ModRegistry.ITEM_SHELF.get());
        WoodTypeRegistry.OAK_TYPE.addChild("supplementaries:item_shelf",  ModRegistry.ITEM_SHELF.get());

    }

    private static final RegHelper.VariantType[] TYPES = List.of(RegHelper.VariantType.SLAB, RegHelper.VariantType.STAIRS).toArray(RegHelper.VariantType[]::new);


    public static final Supplier<RecipeSerializer<SackDyeRecipe>> SACK_DYE_RECIPE =
            RegHelper.registerSpecialRecipe(res("sack_dye"), SackDyeRecipe::new);

    public static final Map<RegHelper.VariantType, Supplier<Block>> DAUBS = RegHelper.registerBlockSet(TYPES, ModRegistry.DAUB, SuppSquared.MOD_ID);

    public static final Map<RegHelper.VariantType, Supplier<Block>> FRAMES = RegHelper.registerBlockSet(TYPES, ModRegistry.DAUB_FRAME, SuppSquared.MOD_ID);


    public static final Map<WoodType, Block> ITEM_SHELVES = new LinkedHashMap<>();

    public static final Map<DyeColor, Supplier<ColoredSackBlock>> SACKS = Util.make(() -> {
        var map = new LinkedHashMap<DyeColor, Supplier<ColoredSackBlock>>();
        for (var c : BlocksColorAPI.SORTED_COLORS) {
            map.put(c, regBlock(SACK_NAME + "_" + c.getName(), () -> new ColoredSackBlock(
                    BlockBehaviour.Properties.copy(Blocks.WHITE_WOOL)
                            .mapColor(c)
                            .pushReaction(PushReaction.DESTROY)
                            .strength(0.8f)
                            .sound(ModSounds.SACK),
                    c
            )));
        }
        return map;
    });

    public static final Map<DyeColor, Supplier<Item>> SACK_ITEMS = Util.make(() -> {
        var map = new LinkedHashMap<DyeColor, Supplier<Item>>();
        for (var c : BlocksColorAPI.SORTED_COLORS) {
            map.put(c, regItem(SACK_NAME + "_" + c.getName(), () -> new SackItem(
                    SACKS.get(c).get(),
                    new Item.Properties().stacksTo(1)
            )));
        }
        return map;
    });

    public static final Map<DyeColor, Supplier<Block>> GOLDEN_CANDLE_HOLDERS = RegUtils.
            registerCandleHolders(res("gold_candle_holder"));


    //iron frames

    //iron frame
    public static final RegSupplier<FrameBlock> IRON_FRAME = regBlock("metal_frame", () ->
            new FrameBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)
                    .strength(5.0F, 6.0F)
                    .mapColor(DyeColor.GRAY)
                    .dynamicShape()
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)));
    public static final Supplier<Item> IRON_FRAME_ITEM = regItem("metal_frame", () -> new TimberFrameItem(IRON_FRAME.get(),
            new Item.Properties()));

    //iron brace
    public static final Supplier<FrameBraceBlock> IRON_BRACE = regBlock("metal_brace", () ->
            new FrameBraceBlock(BlockBehaviour.Properties.copy(IRON_FRAME.get())));
    public static final Supplier<Item> TIMBER_BRACE_ITEM = regItem("metal_brace", () -> new TimberFrameItem(IRON_BRACE.get(),
            new Item.Properties()));

    //iron cross brace
    public static final Supplier<FrameBlock> IRON_CROSS_BRACE = regBlock("metal_cross_brace", () ->
            new FrameBlock(BlockBehaviour.Properties.copy(IRON_FRAME.get())));
    public static final Supplier<Item> TIMBER_CROSS_BRACE_ITEM = regItem("metal_cross_brace", () -> new TimberFrameItem(IRON_CROSS_BRACE.get(),
            new Item.Properties()));


    //plaque
    public static final Supplier<PlaqueBlock> GOLD_PLAQUE = regWithItem("gold_plaque", () ->
            new PlaqueBlock(BlockBehaviour.Properties.copy(Blocks.GOLD_BLOCK)
                    .mapColor(MapColor.NONE)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .noCollission()
                    .strength(1, 1)));

    public static final Supplier<PlaqueBlock> IRON_PLAQUE = regWithItem("iron_plaque", () ->
            new PlaqueBlock(BlockBehaviour.Properties.copy(GOLD_PLAQUE.get())));

    public static final Supplier<PlaqueBlock> COPPER_PLAQUE = regWithItem("copper_plaque", () ->
            new PlaqueBlock(BlockBehaviour.Properties.copy(GOLD_PLAQUE.get())));

    public static final Supplier<BlockEntityType<PlaqueBlockTile>> PLAQUE_TILE =
            RegHelper.registerBlockEntityType(res("plaque"),
                    () -> PlatHelper.newBlockEntityType(
                            PlaqueBlockTile::new,
                            IRON_PLAQUE.get(), COPPER_PLAQUE.get(), GOLD_PLAQUE.get()));

    public static final Supplier<HeavyKeyItem> SKELETON_KEY =
            RegHelper.registerItem(res("heavy_key"), () -> new HeavyKeyItem(
                    new Item.Properties()
                            .fireResistant()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
            ));


    //copper lantern
    public static final Supplier<Block> COPPER_LANTERN = regWithItem("copper_lantern", () -> new CopperLanternBlock(
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_ORANGE)
                    .strength(3.5f)
                    .requiresCorrectToolForDrops()
                    .lightLevel((state) -> state.getValue(LightableLanternBlock.LIT) ? 15 : 0)
                    //TODO: add custom sound mixed
                    .sound(SoundType.COPPER)
    ));

    //brass lantern
    public static final Supplier<Block> BRASS_LANTERN = regWithItem("brass_lantern", () -> new LightableLanternBlock(
            BlockBehaviour.Properties.copy(COPPER_LANTERN.get()),
            Shapes.or(Block.box(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D),
                    Block.box(6.0D, 8.0D, 6.0D, 10.0D, 9.0D, 10.0D),
                    Block.box(4.0D, 7.0D, 4.0D, 12.0D, 8.0D, 12.0D))));

    //crimson lantern
    public static final Supplier<Block> CRIMSON_LANTERN = regWithItem("crimson_lantern", () -> new CrimsonLanternBlock(
            BlockBehaviour.Properties.of()
                    .sound(SoundType.WOOL)
                    .mapColor(DyeColor.RED)
                    .ignitedByLava()
                    .strength(1.5f)
                    .lightLevel((state) -> 15)
                    .noOcclusion())
    );


    public static <T extends Item> Supplier<T> regItem(String name, Supplier<T> sup) {
        return RegHelper.registerItem(SuppSquared.res(name), sup);
    }

    public static <T extends Block> RegSupplier<T> regBlock(String name, Supplier<T> sup) {
        return RegHelper.registerBlock(SuppSquared.res(name), sup);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory) {
        return regWithItem(name, blockFactory, new Item.Properties(), 0);
    }

    public static <T extends Block> RegSupplier<T> regWithItem(String name, Supplier<T> blockFactory, Item.Properties properties, int burnTime) {
        RegSupplier<T> block = regBlock(name, blockFactory);
        regBlockItem(name, block, properties, burnTime);
        return block;
    }

    public static RegSupplier<BlockItem> regBlockItem(String name, Supplier<? extends Block> blockSup, Item.Properties properties, int burnTime) {
        return RegHelper.registerItem(SuppSquared.res(name), () -> burnTime == 0 ? new BlockItem(blockSup.get(), properties) :
                new WoodBasedBlockItem(blockSup.get(), properties, burnTime));
    }

}
