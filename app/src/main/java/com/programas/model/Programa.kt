package com.programas.model

import android.os.Parcelable
import android.text.format.Time
import kotlinx.parcelize.Parcelize

@Parcelize

data class Programa(

    var id:String,

    val nombre: String,

    val cadena: String?,

    val canal: Int?,

    val horaTransmision: Double?

    ) : Parcelable{


constructor () :
this("", "", "", 0, 0.0)
}