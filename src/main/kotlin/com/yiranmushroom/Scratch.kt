//import com.yiranmushroom.mixin_helper.BiomeDecoratorScripting
//import net.minecraft.BiomeDecorator
//import net.minecraft.Minecraft
//import net.minecraft.World
//import net.minecraft.WorldGenMinable
//import java.lang.reflect.Field
//
//
//val currentWorldField: Field =
//    BiomeDecoratorScripting::class.java.getDeclaredField("currentWorld").apply { isAccessible = true }
//
//val generateMinableMethod =
//    BiomeDecoratorScripting::class.java.getDeclaredMethod(
//        "genMinable",
//        Int::class.javaPrimitiveType,
//        WorldGenMinable::class.java,
//        Boolean::class.javaPrimitiveType
//    ).apply { isAccessible = true }
//
//private fun BiomeDecorator.getCurrentWorld(): World {
//    return currentWorldField.get(this) as World
//}
//
//private fun BiomeDecorator.generateMinable(count: Int, gen: WorldGenMinable, flag: Boolean = false) {
//    generateMinableMethod.invoke(this, count, gen, flag)
//}
//
//val worldgenMinables = BiomeDecorator::class.java.declaredFields
//    .filter { it.type == WorldGenMinable::class.java }
//    .mapNotNull {
//        try {
//            it.isAccessible = true
//            Pair(it.name, it.get(BiomeDecoratorScripting) as WorldGenMinable)
//        } catch (e: Exception) {
//            null
//        }
//    }.toMap()
//
//Events.onScriptsLoaded {
//    BiomeDecoratorScripting.customDecorationCallback = {
//        if (this.getCurrentWorld().isOverworld) {
////                this.generateMinable(200, this.dirtGen)
//            this.generateMinable(200, worldgenMinables["dirtGen"]!!)
////                this.generateMinable(200, this.gravelGen)
//            this.generateMinable(200, worldgenMinables["gravelGen"]!!)
////                this.generateMinable(50, this.coalGen)
//            this.generateMinable(200, worldgenMinables["coalGen"]!!)
////                this.generateMinable(40, this.copperGen, true)
//            this.generateMinable(100, worldgenMinables["copperGen"]!!, true)
////                this.generateMinable(10, this.silverGen, true)
//            this.generateMinable(50, worldgenMinables["silverGen"]!!, true)
////                this.generateMinable(20, this.goldGen, true)
//            this.generateMinable(40, worldgenMinables["goldGen"]!!, true)
////                this.generateMinable(60, this.ironGen, true)
//            this.generateMinable(80, worldgenMinables["ironGen"]!!, true)
////                this.generateMinable(10, this.mithrilGen, true)
//            this.generateMinable(20, worldgenMinables["mithrilGen"]!!, true)
////                this.generateMinable(5, this.silverfishGen, true)
//            this.generateMinable(5, worldgenMinables["silverfishGen"]!!, true)
////                this.generateMinable(10, this.redstoneGen)
//            this.generateMinable(10, worldgenMinables["redstoneGen"]!!)
////                this.generateMinable(5, this.diamondGen)
//            this.generateMinable(10, worldgenMinables["diamondGen"]!!)
////                this.generateMinable(5, this.lapisGen)
//            this.generateMinable(10, worldgenMinables["lapisGen"]!!)
//
//        } else if (this.getCurrentWorld().isUnderworld) {
////                this.genMinable(300, this.gravelGen)
//            this.generateMinable(300, worldgenMinables["gravelGen"]!!)
//
//            this.generateMinable(50, worldgenMinables["coalGen"]!!)
////                this.genMinable(40, this.copperGen, true)
//            this.generateMinable(400, worldgenMinables["copperGen"]!!, true)
////                this.genMinable(10, this.silverGen, true)
//            this.generateMinable(100, worldgenMinables["silverGen"]!!, true)
////                this.genMinable(20, this.goldGen, true)
//            this.generateMinable(200, worldgenMinables["goldGen"]!!, true)
////                this.genMinable(60, this.ironGen, true)
//            this.generateMinable(600, worldgenMinables["ironGen"]!!, true)
////                this.genMinable(10, this.mithrilGen, true)
//            this.generateMinable(100, worldgenMinables["mithrilGen"]!!, true)
////                this.genMinable(5, this.adamantiteGen, true)
//            this.generateMinable(50, worldgenMinables["adamantiteGen"]!!, true)
////                this.genMinable(10, this.redstoneGen)
//            this.generateMinable(100, worldgenMinables["redstoneGen"]!!)
////                this.genMinable(5, this.diamondGen)
//            this.generateMinable(50, worldgenMinables["diamondGen"]!!)
////                this.genMinable(5, this.lapisGen)
//            this.generateMinable(50, worldgenMinables["lapisGen"]!!)
////                if (this.currentWorld.underworld_y_offset != 0) {
//            if (this.getCurrentWorld().underworld_y_offset != 0) {
//
//
////                    this.genMinable(50, this.silverfishGen)
//                this.generateMinable(50, worldgenMinables["silverfishGen"]!!)
////                }
//            }
//        } else if (!this.getCurrentWorld().isTheEnd) {
//            Minecraft.setErrorMessage("generateOres: don't know how to handle world " + this.getCurrentWorld())
//        }
//    }
//}
