package net.mehvahdjukaar.suppsquared.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.supplementaries.client.TextUtils;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.supplementaries.reg.ModTextures;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlock;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlockTile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.state.BlockState;

public class PlaqueTileRenderer implements BlockEntityRenderer<PlaqueBlockTile> {
    private final Font font;

    public PlaqueTileRenderer(BlockEntityRendererProvider.Context context) {
        font = context.getFont();
    }

    @Override
    public int getViewDistance() {
        return 48;
    }

    @Override
    public void render(PlaqueBlockTile tile, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {

        poseStack.pushPose();
        BlockState state = tile.getBlockState();
        //rotate towards direction

        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(RotHlpr.rot(state.getValue(PlaqueBlock.FACING)));
        poseStack.mulPose(RotHlpr.XN90);

        // render item
        TextHolder textHolder = tile.getTextHolder();

        // render text
        poseStack.scale(0.010416667F, -0.010416667F, 0.010416667F);

        var textProperties = getRenderTextProperties(textHolder, combinedLightIn);

        poseStack.translate(0, -16, (-0.5 + 0.0625 + 0.005) / 0.010416667F);

        TextUtils.renderTextHolderLines(textHolder, PlaqueBlockTile.LINE_SEPARATION, this.font, poseStack, bufferIn, textProperties);

        poseStack.popPose();
    }

    public TextUtil.RenderTextProperties getRenderTextProperties(TextHolder textHolder, int combinedLight) {
        return new TextUtil.RenderTextProperties(textHolder.getColor(), textHolder.hasGlowingText(), combinedLight,
                textHolder.hasAntiqueInk() ? Style.EMPTY: Style.EMPTY.applyFormat(ChatFormatting.BOLD), () -> true);
    }

}
