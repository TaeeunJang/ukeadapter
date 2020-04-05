package com.cooper.center.ukeadapter.sample

import com.cooper.center.ukeadapter.UkeViewModel

class Card2ViewModel : UkeViewModel<Card2>() {

    fun getText() : String {
        return model().text!!
    }

    fun getDescription() : String {
        return model().description!!
    }

    fun getTextColor() : Int {
        return model().textColor!!
    }

    fun getDescriptionColor() : Int {
        return model().descriptionColor!!
    }

}
