package com.yiranmushroom.mixin;

import com.yiranmushroom.api.IIsChaining;
import net.minecraft.Packet85SimpleSignal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInput;
import java.io.IOException;

@Mixin(Packet85SimpleSignal.class)
public class Packet85SimpleSignalAddChainingMixin implements IIsChaining {
    @Unique
    private boolean ith$chaining = false;


    @Override
    public boolean isChaining() {
        return ith$chaining;
    }

    @Override
    public void setChaining(boolean chaining) {
        this.ith$chaining = chaining;
    }

    @Inject(method = "readPacketData", at = @At("RETURN"))
    private void ith$readChainingData(DataInput data_input, CallbackInfo ci) throws IOException {
        this.ith$chaining = data_input.readBoolean();
    }

    @Inject(method = "writePacketData", at = @At("RETURN"))
    private void ith$writeChainingData(java.io.DataOutput data_output, CallbackInfo ci) throws IOException {
        data_output.writeBoolean(this.ith$chaining);
    }
}
