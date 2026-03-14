package haze.utility.player

import net.minecraft.core.component.DataComponents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUseAnimation
import net.minecraft.world.item.Items

// испорченно SCWGxD в 30.12.2025:20:01
val ItemStack.isConsumable: Boolean
    get() = this.isFood || this.item == Items.POTION || this.item == Items.MILK_BUCKET

val ItemStack.isFood: Boolean
    get() = foodComponent != null && this.useAnimation == ItemUseAnimation.EAT

val ItemStack.foodComponent: FoodProperties?
    get() = this.get(DataComponents.FOOD)