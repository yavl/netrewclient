package com.netrew.ui.widgets

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.netrew.Globals
import com.netrew.game.components.Mappers
import com.rafaskoberg.gdx.typinglabel.TypingConfig
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.actors.alpha
import ktx.scene2d.*

class PopupMenu() : Group() {
    private val drawableBg: TextureRegionDrawable
    private val table: KTableWidget

    init {
        TypingConfig.DEFAULT_SPEED_PER_CHAR = 0f
        alpha = 0f

        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888);
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

        val labelStyle = Label.LabelStyle(Globals.defaultFont, Color.WHITE)
        val nameLabel = Label("", labelStyle)
        nameLabel.name = "nameLabel"

        val ageLabel = Label("", labelStyle)
        ageLabel.name = "ageLabel"

        val healthss = Globals.bundle.format("popup.health.wounded")
        val healthString = Globals.bundle.format("popup.health", healthss, "{COLOR=RED}{SHAKE}")
        val woundedLabel = TypingLabel("$healthString", labelStyle)
        woundedLabel.name = "woundedLabel"
        val verticalGroup = verticalGroup {
            addActor(nameLabel)
            space(10f)
            addActor(ageLabel)
            space(10f)
            addActor(woundedLabel)
        }
        table.add(verticalGroup)
    }

    fun show() {
        if (hasActions())
            clearActions()
        addAction(Actions.fadeIn(0.1f))
    }

    fun hide() {
        val actions = SequenceAction()
        actions.addAction(Actions.fadeOut(0.2f))
        actions.addAction(Actions.visible(false))
        addAction(actions)
    }

    fun update(entity: Entity) {
        val characterName = Mappers.character.get(entity).name
        val characterAge = 0

        val nameLabel = findActor<Label>("nameLabel")
        nameLabel.setText(Globals.bundle.format("popup.name", characterName))

        val ageLabel = findActor<Label>("ageLabel")
        ageLabel.setText(Globals.bundle.format("popup.age", characterAge))
    }
}