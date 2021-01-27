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

open class PopupWindow : Table(), Disposable {
    /** Vertical space between window elements */
    protected val verticalSpace = 10f
    protected val labelStyle = Label.LabelStyle(Globals.Fonts.defaultFont, Color.WHITE)
    private val drawableBgTexture: Texture

    init {
        TypingConfig.DEFAULT_SPEED_PER_CHAR = 0f
        alpha = 0f

        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
        pixmap.setColor(Color(0f, 0f, 0f, 0.4f))
        pixmap.fill()
        drawableBgTexture = Texture(pixmap)
        val drawableBg = TextureRegionDrawable(TextureRegion(drawableBgTexture))
        pixmap.dispose()

        background = drawableBg
        pad(4f)
        setSize(300f, 200f)
        y -= height
        align(Align.center)
    }

    open fun show() {
        if (hasActions())
            clearActions()
        addAction(Actions.fadeIn(0.1f))
    }

    open fun hide() {
        val actions = SequenceAction()
        actions.addAction(Actions.fadeOut(0.2f))
        actions.addAction(Actions.visible(false))
        addAction(actions)
    }

    /** update label texts */
    open fun update(entity: Entity) {
        if (Mappers.character.get(entity) == null) // todo fix why is characterComponent null on nameLabel hover
            return
        val characterName = Mappers.character.get(entity).name
        val characterAge = 0

        val nameLabel = findActor<Label>("nameLabel")
        nameLabel.setText(Globals.bundle.format("popup.name", characterName))

        val ageLabel = findActor<Label>("ageLabel")
        ageLabel.setText(Globals.bundle.format("popup.age", characterAge))
    }

    override fun dispose() {
        drawableBgTexture.dispose()
    }
}