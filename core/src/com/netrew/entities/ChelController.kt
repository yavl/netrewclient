package com.netrew.entities

class ChelController {
    val model: Chel
    val view: ChelView

    constructor(model: Chel, view: ChelView) {
        this.model = model
        this.view = view
    }
}