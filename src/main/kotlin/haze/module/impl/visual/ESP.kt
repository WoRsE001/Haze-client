package haze.module.impl.visual

import haze.event.Event
import haze.event.impl.RenderEvent
import haze.module.Category
import haze.module.Module
import haze.utility.render.Render3D
import haze.utility.level

// Blood! It's everywhere. SCWxD killed you on 27.02.2026 at 11:32.
object ESP : Module("ESP", Category.VISUAL) {
    override fun onEvent(event: Event) {
        if (event is RenderEvent.World) {
            for (entity in level.entitiesForRendering()) {
                Render3D.drawBox(event.poseStack, entity.boundingBox, 1f, 1f, 1f, 1f, 10f)
            }
        }
    }
}