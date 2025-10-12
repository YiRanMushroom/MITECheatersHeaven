package com.yiranmushroom.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.yiranmushroom.MITECheatersHeaven;
import net.minecraft.EntityAITasks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.EntityAITaskEntry;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityAITasks.class)
public abstract class EntityAITasksMixin {
/*    @WrapOperation(method = "onUpdateTasks", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean fixCME1(List instance, Object o, Operation<Boolean> original, @Share("removeList") LocalRef<List> removeList) {
        if (removeList.get() == null) {
            removeList.set(new ArrayList<>());
        }
        removeList.get().add(o);
        return true;
    }

    @WrapOperation(method = "onUpdateTasks", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean fixCME2(List instance, Object o, Operation<Boolean> original, @Share("addList") LocalRef<List> addList) {
        if (addList.get() == null) {
            addList.set(new ArrayList<>());
        }
        addList.get().add(o);
        return true;
    }

    @Inject(method = "onUpdateTasks", at = @At(value = "INVOKE", target = "Lnet/minecraft/Profiler;startSection(Ljava/lang/String;)V", ordinal = 0))
    private void applyChanges(CallbackInfo ci, @Share("removeList") LocalRef<List> removeList, @Share("addList") LocalRef<List> addList) {
        List entries = ((EntityAITasks) (Object) this).getTaskEntries();
        if (removeList.get() != null) {
            MITECheatersHeaven.getLOGGER().info("Removed {} tasks to prevent CME", removeList.get().size());
            entries.removeAll(removeList.get());
        }
        if (addList.get() != null) {
            MITECheatersHeaven.getLOGGER().info("Added {} tasks to prevent CME", addList.get().size());
            entries.addAll(addList.get());
        }
        removeList.set(null);
        addList.set(null);
    }*/

    @Shadow
    private List taskEntries;

    @Shadow
    private int tickCount;

    @Shadow
    private int tickRate;

    @Shadow
    private List executingTaskEntries;

    @Shadow
    protected abstract boolean canUse(EntityAITaskEntry par1EntityAITaskEntry);

    @Shadow
    protected abstract boolean canContinue(EntityAITaskEntry par1EntityAITaskEntry);

    @Shadow
    private net.minecraft.Profiler theProfiler;

    /**
     * @author YiranMushroom
     * @reason Fix ConcurrentModificationException
     */
    @Overwrite
    public void onUpdateTasks() {
        ArrayList<EntityAITaskEntry> var1 = new ArrayList<>();
        if (this.tickCount++ % this.tickRate == 0) {
            for (int i = 0; i < this.taskEntries.size(); ++i) {
                EntityAITaskEntry var3 = (EntityAITaskEntry) this.taskEntries.get(i);
                boolean var4 = this.executingTaskEntries.contains(var3);
                if (var4) {
                    if (this.canUse(var3) && this.canContinue(var3)) {
                        continue;
                    }
                    var3.action.resetTask();
                    this.executingTaskEntries.remove(var3);
                }
                if (this.canUse(var3) && var3.action.shouldExecute()) {
                    var1.add(var3);
                    this.executingTaskEntries.add(var3);
                }
            }
        } else {
            for (int i = 0; i < this.executingTaskEntries.size(); ++i) {
                EntityAITaskEntry var3 = (EntityAITaskEntry) this.executingTaskEntries.get(i);
                if (!var3.action.continueExecuting()) {
                    var3.action.resetTask();
                    this.executingTaskEntries.remove(i);
                    i--;
                }
            }
        }

        this.theProfiler.startSection("goalStart");
        for (EntityAITaskEntry var3 : var1) {
            this.theProfiler.startSection(var3.action.getClass().getSimpleName());
            var3.action.startExecuting();
            this.theProfiler.endSection();
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("goalTick");
        for (int i = 0; i < this.executingTaskEntries.size(); ++i) {
            EntityAITaskEntry var3 = (EntityAITaskEntry) this.executingTaskEntries.get(i);
            var3.action.updateTask();
        }
        this.theProfiler.endSection();
    }

}
