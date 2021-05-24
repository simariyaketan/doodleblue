package com.doodleblue.test.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class ContactDetail : BaseObservable() {
    @get:Bindable
    var name: String = ""

    @get:Bindable
    var phoneNumber: String = ""

    @get:Bindable
    var photoUri: String? = null

    @get:Bindable
    var isSelected: Boolean = false
}