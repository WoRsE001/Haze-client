package haze.module

import haze.event.Event
import haze.event.EventListener
import haze.key.KeyListener
import haze.key.Keybind
import haze.setting.ToggleAbleConfigureAble
import haze.utility.math.Rect
import haze.utility.mc
import haze.utility.nullCheck
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.input.MouseButtonEvent

// created by dicves_recode on 29.11.2025
abstract class Module(
    name: String,
    val category: Category,
    override var keybind: Keybind = Keybind.NONE.copy(),
    defaultToggled: Boolean = false,
) : ToggleAbleConfigureAble(name, defaultToggled), EventListener, KeyListener {
    override var rect = Rect(0f, 0f, 300f, 50f)

    init {
        registerToEvents()
        registerToKeybinds()
        Modules.register(this)
        category.registerToCategory(this)
    }

    override var json: JsonObject
        get() = buildJsonObject {
            put("Info", buildJsonObject {
                put("Keybind", keybind.key)
                put("Hold", keybind.hold)
            })

            super.json
        }
        set(value) {
            super.json = value
        }

    override fun render(guiGraphics: GuiGraphics, mouseX: Float, mouseY: Float) {
        guiGraphics.fill((rect.lt.x + 5).toInt(), (rect.lt.y + 5).toInt(), (rect.rb.x - 5).toInt(), (rect.lt.y + getHeight() - 5).toInt(), 0xff767676.toInt())
        guiGraphics.drawString(mc.font, name, (rect.lc.x + 10).toInt(),(rect.lc.y - mc.font.lineHeight / 2).toInt(), if (toggled) 0x80ff8000.toInt() else 0xff000000.toInt(), false)

        if (isShowSettings) {
            var offsetY = rect.size.y
            for (item in value) {
                if (!item.visible.invoke()) continue
                item.rect.lt.x = rect.lt.x + 10
                item.rect.lt.y = rect.lt.y + offsetY
                item.render(guiGraphics, mouseX, mouseY)
                offsetY += item.rect.size.y + 5
            }
        }
    }

    override fun mouseClicked(mouseButtonEvent: MouseButtonEvent, bl: Boolean): Boolean {
        val mouseX = mouseButtonEvent.x.toFloat()
        val mouseY = mouseButtonEvent.y.toFloat()
        val button = mouseButtonEvent.button()

        if (rect.isCollided(mouseX, mouseY)) {
            if (button == 0) {
                toggled = !toggled
            } else if (button == 1 && value.isNotEmpty()) {
                isShowSettings = !isShowSettings
            }
            return true
        }

        if (isShowSettings) {
            for (item in value) {
                if (!item.visible.invoke()) continue
                if (item.mouseClicked(mouseButtonEvent, bl))
                    return true
            }
        }

        return false
    }

    override fun mouseReleased(mouseButtonEvent: MouseButtonEvent): Boolean {
        if (isShowSettings) {
            for (item in value) {
                if (!item.visible.invoke()) continue
                if (item.mouseReleased(mouseButtonEvent))
                    return true
            }
        }

        return false
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, hScroll: Double, vScroll: Double): Boolean {
        if (isShowSettings) {
            for (item in value) {
                if (!item.visible.invoke()) continue
                if (item.mouseScrolled(mouseX, mouseY, hScroll, vScroll))
                    return true
            }
        }

        return false
    }

    override fun onEvent(event: Event) {}
    override fun shouldListenEvents() = toggled && nullCheck()

    override fun onKey(action: Int) {
        if (action > 1)
            return

        val pressed = action == 1

        if (keybind.hold)
            toggled = pressed
        else if (!pressed)
            toggle()
    }

    override fun shouldListenKeys() = nullCheck()

    override fun toString() = name

    fun getHeight(): Float {
        var totalHeight = rect.size.y
        if (isShowSettings) {
            for (item in value) {
                if (!item.visible.invoke()) continue
                totalHeight += item.rect.size.y + 5
            }
            totalHeight += 5
        }
        return totalHeight
    }
}
