package haze.module.impl.player

import haze.event.Event
import haze.event.impl.TickEvent
import haze.module.Category
import haze.module.Module
import haze.utility.gameMode
import haze.utility.math.random
import haze.utility.mc
import haze.utility.player
import haze.utility.player.inventory.armorValue
import haze.utility.player.inventory.attackDamage
import haze.utility.player.inventory.foodComponent
import haze.utility.player.inventory.getEnchantment
import haze.utility.player.inventory.isChestArmor
import haze.utility.player.inventory.isFood
import haze.utility.player.inventory.isFootArmor
import haze.utility.player.inventory.isFull
import haze.utility.player.inventory.isFullBlock
import haze.utility.player.inventory.isHeadArmor
import haze.utility.player.inventory.isLegArmor
import haze.utility.player.inventory.isPlayerArmor
import haze.utility.player.inventory.isSword
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.FishingRodItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.ProjectileItem
import net.minecraft.world.item.enchantment.Enchantments

// Blood! It's everywhere. SCWxD killed you on 17.03.2026 at 4:34.
object InventoryCleaner : Module("InventoryCleaner", Category.PLAYER) {
    private val sort = toggleAbleGroup("Sort", true)

        private val sortDelay by sort.numberRange("Delay", 0.0..0.0, 0.0..10.0, 1.0)

    private val equipArmor = toggleAbleGroup("Equip armor", true)

        private val equipArmorDelay by equipArmor.numberRange("Delay", 0.0..0.0, 0.0..10.0, 1.0)

    private val throwTrash = toggleAbleGroup("Throw trash", true)

        private val throwTrashDelay by throwTrash.numberRange("Delay", 0.0..0.0, 0.0..10.0, 1.0)

    private var delay = 0

    override fun onEvent(event: Event) {
        if (event !is TickEvent.Pre) return
        if (mc.screen !is InventoryScreen) return
        if (sort.toggled) sort(player.inventoryMenu)
        if (equipArmor.toggled) equipArmor(player.inventoryMenu)
        if (throwTrash.toggled) throwTrash(player.inventoryMenu)
        if (delay > 0) delay--
    }

    fun sort(inventoryMenu: InventoryMenu) {
        for (itemForSort in ItemsForSort.entries) {
            val slotForSort = inventoryMenu.slots[ItemsForSort.entries.indexOf(itemForSort) + 36]
            var bestSlot: Slot? = if (itemForSort.matching(slotForSort.item)) slotForSort else null

            for (slot in inventoryMenu.slots) {
                if (!itemForSort.matching(slot.item)) continue
                if (bestSlot == null || itemForSort.comparator(slot.item, bestSlot.item)) {
                    bestSlot = slot
                }
            }

            if (bestSlot != null && bestSlot != slotForSort) {
                if (delay > 0) return

                gameMode.handleInventoryMouseClick(
                    player.containerMenu.containerId,
                    bestSlot.index,
                    ItemsForSort.entries.indexOf(itemForSort),
                    ClickType.SWAP,
                    player
                )

                delay = sortDelay.random().toInt()
            }
        }
    }

    fun equipArmor(inventoryMenu: InventoryMenu) {
        for (i in 5..8) {
            var bestSlot: Slot? = if (inventoryMenu.slots[i].item.isPlayerArmor) inventoryMenu.slots[i] else null

            for (slot in inventoryMenu.slots) {
                if (!slot.item.isPlayerArmor || i != armorNumberSlot(slot.item)) continue

                if (bestSlot == null || slot.item.armorValue > bestSlot.item.armorValue)
                    bestSlot = slot
            }

            if (bestSlot != null && i != bestSlot.index) {
                if (inventoryMenu.slots[i].item.isEmpty) {
                    if (delay > 0) return

                    gameMode.handleInventoryMouseClick(
                        player.containerMenu.containerId,
                        bestSlot.index,
                        0,
                        ClickType.QUICK_MOVE,
                        player
                    )

                    delay = equipArmorDelay.random().toInt()
                } else if (!inventoryMenu.isFull()) {
                    if (delay > 0) return

                    gameMode.handleInventoryMouseClick(
                        player.containerMenu.containerId,
                        i,
                        0,
                        ClickType.QUICK_MOVE,
                        player
                    )

                    delay = equipArmorDelay.random().toInt()
                }
            }
        }
    }

    fun throwTrash(inventoryMenu: InventoryMenu) {
        for (slot in inventoryMenu.slots) {
            for (itemForSort in ItemsForSort.entries) {
                if (slot.index != ItemsForSort.entries.indexOf(itemForSort) + 36 && itemForSort.matching(slot.item) && isItemToDrop(itemForSort)) {
                    if (delay > 0) return

                    gameMode.handleInventoryMouseClick(
                        player.containerMenu.containerId,
                        slot.index,
                        0,
                        ClickType.THROW,
                        player
                    )

                    delay = throwTrashDelay.random().toInt()
                }
            }
        }
    }

    fun isItemToDrop(itemForSort: ItemsForSort) =
                itemForSort == ItemsForSort.SWORD ||
                itemForSort == ItemsForSort.FISHING_ROD ||
                itemForSort == ItemsForSort.BOW

    fun armorNumberSlot(item: ItemStack): Int {
        if (item.isHeadArmor) return 5
        if (item.isChestArmor) return 6
        if (item.isLegArmor) return 7
        return if (item.isFootArmor) 8
        else -1
    }

    enum class ItemsForSort(val matching: (ItemStack) -> Boolean, val comparator: (ItemStack, ItemStack) -> Boolean) {
        SWORD({ it.isSword }, { item1, item2 -> item1.attackDamage > item2.attackDamage }),
        FISHING_ROD({ it.item is FishingRodItem }, { item1, item2 -> if (item1.isEnchanted != item2.isEnchanted) item1.isEnchanted else item1.getEnchantment(Enchantments.KNOCKBACK) > item2.getEnchantment(Enchantments.KNOCKBACK) }),
        BOW({ it.item is BowItem }, { item1, item2 -> if (item1.isEnchanted != item2.isEnchanted) item1.isEnchanted else item1.getEnchantment(Enchantments.KNOCKBACK) > item2.getEnchantment(Enchantments.KNOCKBACK) }),
        BUCKET_WATTER({ it.item == Items.WATER_BUCKET }, { _, _ -> false }),
        GOLDEN_APPLES({ it.item == Items.GOLDEN_APPLE }, { _, _ -> false }),
        FOOD({ it.isFood && it.item != Items.GOLDEN_APPLE }, { item1, item2 -> (item1.foodComponent?.nutrition ?: 1) > (item2.foodComponent?.nutrition ?: 1) }),
        ENDER_PEARLS({ it.item == Items.ENDER_PEARL }, { item1, item2 -> item1.count > item2.count }),
        PROJECTILES({ it.item is ProjectileItem }, { item1, item2 -> item1.count > item2.count }),
        BLOCKS({ it.item is BlockItem && it.isFullBlock() }, { item1, item2 -> item1.count > item2.count })
    }
}