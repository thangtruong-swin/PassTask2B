package au.edu.swin.sdmd.passtask2a_wish_you_were_here

import kotlinx.android.parcel.Parcelize
import android.os.Parcelable

@Parcelize
data class Location(
    var cardViewID: String,
    var title: String, var city: String,
    var date: String, var rating: Double,
    var visited: Boolean = false
    ): Parcelable