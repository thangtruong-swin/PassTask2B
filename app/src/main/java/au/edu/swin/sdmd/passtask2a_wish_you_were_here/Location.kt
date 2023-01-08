package au.edu.swin.sdmd.passtask2a_wish_you_were_here

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Location(
    var title: String, val city: String,
    val date: String, var rating: Double,
    var visited: Boolean = false
    ): Parcelable