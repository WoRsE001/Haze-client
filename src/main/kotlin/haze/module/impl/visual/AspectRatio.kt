package haze.module.impl.visual

import haze.module.Category
import haze.module.Module

// испорченно SCWGxD в 27.12.2025:20:49
object AspectRatio : Module(
    "AspectRatio",
    Category.VISUAL
) {
    val factor by number("Factor", 1.3, 0.0..2.0, 0.01)
}