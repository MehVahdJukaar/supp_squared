package net.mehvahdjukaar.suppsquared.client;


import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.TextUtil;
import net.mehvahdjukaar.supplementaries.client.screens.TextHolderEditScreen;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlock;
import net.mehvahdjukaar.suppsquared.common.PlaqueBlockTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class PlaqueEditScreen extends TextHolderEditScreen<PlaqueBlockTile> {

    private PlaqueEditScreen(PlaqueBlockTile tile) {
        super(tile, Component.translatable("sign.edit"));
    }

    public static void open(PlaqueBlockTile teSign) {
        Minecraft.getInstance().setScreen(new PlaqueEditScreen(teSign));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {

        Lighting.setupForFlatItems();

        super.render(graphics, mouseX, mouseY, partialTicks);

        graphics.drawCenteredString(this.font, this.title, this.width / 2, 40, 16777215);

        MultiBufferSource.BufferSource bufferSource = this.minecraft.renderBuffers().bufferSource();

        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();

        poseStack.translate((this.width / 2d), 0.0D, 50.0D);
        poseStack.scale(93.75F, -93.75F, 93.75F);
        poseStack.translate(0.0D, -1.3125D, 0.0D);
        // renders sign
        poseStack.pushPose();

        poseStack.mulPose(RotHlpr.Y90);
        poseStack.translate(0, -0.5, -0.5);
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

        blockRenderer.renderSingleBlock(tile.getBlockState().setValue(PlaqueBlock.FACING, Direction.EAST),
                poseStack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();

        //renders text
        boolean blink = this.updateCounter / 6 % 2 == 0;

        poseStack.translate(0, 0, 0.0625 + 0.005);
        float f = 0.015625F * 0.9f;

        poseStack.scale(f, -f, f);
        poseStack.translate(0, -1, 0);
        TextUtil.renderGuiText(this.tile.getTextHolder().getGUIRenderTextProperties(),
                this.messages[0], this.font, graphics, bufferSource, this.textInputUtil.getCursorPos(),
                this.textInputUtil.getSelectionPos(), this.lineIndex, blink, PlaqueBlockTile.LINE_SEPARATION);

        poseStack.popPose();
        Lighting.setupFor3DItems();

    }

}

