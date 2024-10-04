package net.mehvahdjukaar.suppsquared.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.LOD;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.supplementaries.client.TextUtils;
import net.mehvahdjukaar.supplementaries.common.block.TextHolder;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlock;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlockTile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.state.BlockState;

public class PlaqueTileRenderer implements BlockEntityRenderer<PlaqueBlockTile> {
    private final Font font;
    private final Camera camera;

    public PlaqueTileRenderer(BlockEntityRendererProvider.Context context) {
        font = context.getFont();
        camera = Minecraft.getInstance().gameRenderer.getMainCamera();
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
        poseStack.mulPose(RotHlpr.rot(state.getValue(PlaqueBlock.FACING).getOpposite()));

        // render item
        TextHolder textHolder = tile.getTextHolder();

        // render text
        poseStack.translate(0, 0, (-0.5 + 0.0625 + 0.005));

        float f = 0.015625F * 0.9f;

        poseStack.scale(f, -f, f);

        var textProperties = TextUtil.renderProperties(textHolder.getColor(), textHolder.hasGlowingText(),
                combinedLightIn,
                textHolder.supplementaries$isAntique() ? Style.EMPTY : Style.EMPTY.applyFormat(ChatFormatting.BOLD),
                state.getValue(PlaqueBlock.FACING).step(),
                new LOD(camera, tile.getBlockPos())::isVeryNear);

        poseStack.translate(0, -16, 0);

        TextUtils.renderTextHolderLines(textHolder, PlaqueBlockTile.LINE_SEPARATION, this.font, poseStack, bufferIn, textProperties);

        poseStack.popPose();
    }


}
