package com.netrew.ui.windows

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Disposable
import com.netrew.Globals
import com.netrew.game.components.Mappers
import com.rafaskoberg.gdx.typinglabel.TypingConfig
import com.rafaskoberg.gdx.typinglabel.TypingLabel
import ktx.actors.alpha
import ktx.scene2d.scene2d
import ktx.scene2d.verticalGroup

class TreePopupWindow : PopupWindow() {
    init {
        val nameLabel = Label("", labelStyle)
        nameLabel.name = "nameLabel"

        val ageLabel = Label("", labelStyle)
        ageLabel.name = "ageLabel"

        val healthState = Globals.bundle.format("popup.health.wounded")
        val healthString = Globals.bundle.format("popup.health", healthState, "{COLOR=RED}{SHAKE}")
        val woundedLabel = TypingLabel(healthString, labelStyle)
        woundedLabel.name = "woundedLabel"
        val vGroup = scene2d.verticalGroup {
            space(verticalSpace)
            addActor(nameLabel)
            addActor(ageLabel)
            addActor(woundedLabel)
        }
        add(vGroup)
    }

    /** update label texts */
    override fun update(entity: Entity) {
        if (Mappers.tree.get(entity) == null) // todo fix why is characterComponent null on nameLabel hover
            return
        val treeName = Mappers.tree.get(entity).name
        val treeAge = 0

        val nameLabel = findActor<Label>("nameLabel")
        nameLabel.setText(Globals.bundle.format("popup.name", treeName))

        val ageLabel = findActor<Label>("ageLabel")
        ageLabel.setText(Globals.bundle.format("popup.age", treeAge))
    }
}