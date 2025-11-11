package com.yiranmushroom.mixin.ite_mixin;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiCrafting.class)
@Restriction(require = @Condition(value = "mite_ite"))
public abstract class GuiCraftingMixin extends InventoryEffectRenderer {

    public GuiCraftingMixin(Container par1Container) {
        super(par1Container);
    }

    @Inject(method = "drawGuiContainerForegroundLayer", at = @At(value = "HEAD"), cancellable = true)
    private void mod$drawGuiContainerForegroundLayer(int par1, int par2, CallbackInfo ci) {
        ContainerWorkbench container_workbench = (ContainerWorkbench) this.inventorySlots;
        String var3 = Translator.get("tile.toolbench." + BlockWorkbench.getToolMaterial(container_workbench.getBlockMetadata()) + ".name");
        this.fontRenderer.drawString(var3, 29, 6, 4210752);
        this.fontRenderer.drawString(I18n.getString("container.inventory"), 7, this.ySize - 96 + 3, 4210752);
        ci.cancel();

    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At(value = "HEAD"), cancellable = true)
    private void mod$drawGuiContainerBackgroundLayer(float par1, int par2, int par3, CallbackInfo ci) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiCrafting.craftingTableGuiTextures);
        int var4 = this.guiLeft;
        int var5 = this.guiTop;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        EntityClientPlayerMP player = this.mc.thePlayer;
        if (player.crafting_ticks > 0) {
            this.drawTexturedModalRect(var4 + 90, var5 + 34, 176, 0, player.crafting_ticks * 23 / player.crafting_period, 16);
        }

        SlotCrafting slot_crafting = (SlotCrafting) this.inventorySlots.getSlot(0);
        if (slot_crafting.getNumCraftingResults(player) > 1) {
            this.mc.getTextureManager().bindTexture(GuiIngame.MITE_icons);
            float grey = 0.54509807F;
            GL11.glColor4f(grey, grey, grey, 1.0F);
            this.drawTexturedModalRect(var4 + 147, var5 + 31, 16, 0, 3, 3);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        ci.cancel();

    }
}
