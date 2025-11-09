package com.yiranmushroom.enchantments

import net.minecraft.*

class ChainingEnchantment(id: Int, difficulty: Int) : Enchantment(id, EnumRarity.rare, difficulty) {
    override fun getNameSuffix(): String {
        return "chaining"
    }

    override fun canEnchantItem(item: Item): Boolean {
        return isItemSupported(item)
    }

    override fun isOnCreativeTab(creativeTabs: CreativeTabs): Boolean {
        return creativeTabs == CreativeTabs.tabTools
    }

    override fun getNumLevels(): Int {
        return 1
    }

    companion object {
        private val supportedEnchantmentsChecker = mutableListOf<(Item) -> Boolean>(
            {
                it is ItemPickaxe || it is ItemAxe
            }
        )

        private val supportedBlockMap = mutableMapOf<Int, (Block) -> Boolean>(
            Block.gravel.blockID to { true },
            Block.blockClay.blockID to { true },
            Block.tallGrass.blockID to { true },
            Block.obsidian.blockID to { true },
        )

        @JvmStatic
        public fun addSupportedBlock(
            blockID: Int,
            checker: (Block) -> Boolean = { true }
        ) {
            supportedBlockMap[blockID] = checker
        }

        private val supportedBlocksChecker = mutableListOf<(Block) -> Boolean>(
            {
                it is BlockOre || it is BlockLog || it is BlockLeaves || it is BlockRedstoneOre ||
                        supportedBlockMap[it.blockID]?.invoke(it) == true
            }
        )

        private val areTwoBlocksSimilarChecker = mutableListOf<(Block, Block) -> Boolean>(
            { block1, block2 ->
                block1 === block2 || block1.blockID == block2.blockID
            },
            { block1, block2 ->
                block1 is BlockRedstoneOre && block2 is BlockRedstoneOre
            }
        )

        @JvmStatic
        fun areTwoBlockSimilar(id1: Block, id2: Block): Boolean {
            return areTwoBlocksSimilarChecker.any { it(id1, id2) }
        }

        @JvmStatic
        fun addSupportedItemChecker(checker: (Item) -> Boolean) {
            supportedEnchantmentsChecker.add(checker)
        }

        @JvmStatic
        fun addSupportedBlockChecker(checker: (Block) -> Boolean) {
            supportedBlocksChecker.add(checker)
        }

        @JvmStatic
        fun isBlockSupported(block: Block): Boolean {
            return supportedBlocksChecker.any { it(block) }
        }

        @JvmStatic
        fun isItemSupported(item: Item): Boolean {
            return supportedEnchantmentsChecker.any { it(item) }
        }

        @JvmField
        public var maxChainingCount = 256
    }
}

/*
interface IDoChaining {
    fun doChaining(): Boolean
    fun requestChaining(on: Boolean)
}*/
