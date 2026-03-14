package haze.module.impl.visual

import haze.event.Event
import haze.event.impl.RenderEvent
import haze.module.Category
import haze.module.Module
import haze.utility.math.Rect
import haze.utility.render.Render2D

object Tracers : Module("Tracers", Category.VISUAL) {
    override fun onEvent(event: Event) {
        if (event is RenderEvent.Gui) {
            Render2D.guiGraphics = event.guiGraphics
            Render2D.drawRect(Rect(0f, 0f, 100f, 100f), -1)
        }
    }
}
