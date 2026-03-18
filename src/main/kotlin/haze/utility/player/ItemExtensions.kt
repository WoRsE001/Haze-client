package haze.utility.player

import com.mojang.brigadier.StringReader
import haze.utility.mc
import haze.utility.player
import net.minecraft.commands.arguments.item.ItemInput
import net.minecraft.commands.arguments.item.ItemParser
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ArmorStandItem
import net.minecraft.world.item.ArrowItem
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.BoatItem
import net.minecraft.world.item.BottleItem
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.BrushItem
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.CrossbowItem
import net.minecraft.world.item.EggItem
import net.minecraft.world.item.EmptyMapItem
import net.minecraft.world.item.EnderEyeItem
import net.minecraft.world.item.EnderpearlItem
import net.minecraft.world.item.ExperienceBottleItem
import net.minecraft.world.item.FireChargeItem
import net.minecraft.world.item.FireworkRocketItem
import net.minecraft.world.item.FishingRodItem
import net.minecraft.world.item.FlintAndSteelItem
import net.minecraft.world.item.HangingEntityItem
import net.minecraft.world.item.InstrumentItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.KnowledgeBookItem
import net.minecraft.world.item.PlaceOnWaterBlockItem
import net.minecraft.world.item.PotionItem
import net.minecraft.world.item.SnowballItem
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.SpyglassItem
import net.minecraft.world.item.TridentItem
import net.minecraft.world.item.WindChargeItem
import net.minecraft.world.item.WritableBookItem
import net.minecraft.world.item.WrittenBookItem
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.component.UseEffects
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import org.apache.commons.lang3.function.Consumers
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

// Blood! It's everywhere. SCWxD killed you on 17.03.2026 at 5:41.
fun createItem(stack: String, amount: Int = 1): ItemStack =
    ItemParser(mc.level!!.registryAccess()).parse(StringReader(stack)).let {
        ItemInput(it.item, it.components).createItemStack(amount, false)
    }

fun ItemStack.isMergeable(other: ItemStack): Boolean = ItemStack.isSameItemSameComponents(this, other)

fun ItemStack.canMerge(other: ItemStack): Boolean {
    return this.isMergeable(other) && this.count + other.count <= this.maxStackSize
}

val ItemStack.attackDamage: Double
    get() {
        val entityBaseDamage = player.getAttributeValue(Attributes.ATTACK_DAMAGE)
        val baseDamage = getAttributeValue(Attributes.ATTACK_DAMAGE, EquipmentSlot.MAINHAND)
        return entityBaseDamage + baseDamage + getSharpnessDamage()
    }

@JvmOverloads
fun ItemStack.getSharpnessDamage(level: Int = getEnchantment(Enchantments.SHARPNESS)): Double = level * 1.25

val ItemStack.attackSpeed: Double
    get() = getAttributeValue(Attributes.ATTACK_SPEED, EquipmentSlot.MAINHAND)

val ItemStack.durability
    get() = this.maxDamage - this.damageValue

@JvmOverloads
fun DataComponentGetter.getAttributeValue(attribute: Holder<Attribute>, slot: EquipmentSlot? = null): Double {
    val attributeModifiers = this[DataComponents.ATTRIBUTE_MODIFIERS] ?: return 0.0

    val attribInstance = AttributeInstance(attribute, Consumers.nop())

    for (entry in attributeModifiers.modifiers) {
        if ((slot?.let(entry.slot::test) ?: true) && entry.attribute == attribute) {
            attribInstance.addTransientModifier(entry.modifier)
        }
    }

    return attribInstance.value
}

fun <E : Any> ResourceKey<Registry<E>>.getOrNull(): Registry<E>? =
    mc.level?.registryAccess()?.lookup(this)?.getOrNull()

fun ResourceKey<Enchantment>.toRegistryEntryOrNull(): Holder<Enchantment>? =
    Registries.ENCHANTMENT.getOrNull()?.get(this)?.getOrNull()

fun ItemStack.getBlock(): Block? {
    val item = this.item
    if (item !is BlockItem) {
        return null
    }

    return item.block
}

fun ItemStack.isFullBlock(): Boolean {
    val block = this.getBlock() ?: return false
    return block.defaultBlockState().isCollisionShapeFullBlock(mc.level!!, BlockPos.ZERO)
}