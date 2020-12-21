package com.netrew.ui.widgets

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.netrew.game.Mappers
import ktx.scene2d.*

class PopupMenu() : Group() {
    var selectedEntity: Entity? = null
    private val drawableBg: TextureRegionDrawable
    private val table: KTableWidget

    init {
        val pixmap = Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color(0f, 0f, 0f, 0.4f));
        pixmap.fill();
        drawableBg = TextureRegionDrawable(TextureRegion(Texture(pixmap)));

        table = scene2d.table {
            setBackground(drawableBg)
            pad(4f)
            setSize(300f, 200f)
            setY(getY() - height)
            align(Align.center)
        }
        addActor(table)
    }

    fun build() {
        val characterName = Mappers.name.get(selectedEntity).name

        val verticalGroup = verticalGroup {
            label("Имя: ${characterName}")
            space(10f)
            label("Возраст: 0")
        }
        table.add(verticalGroup)
    }

    fun hide() {
        table.clear()
    }
}