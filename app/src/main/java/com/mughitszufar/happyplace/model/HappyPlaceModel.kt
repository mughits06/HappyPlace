package com.mughitszufar.happyplace.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HappyPlaceModel (
    val id: Long,
    val title: String?,
    val image: String?,
    val description: String?,
    val date: String?,
    val location: String?,
    val latitude: Double,
    val longitude: Double
): Parcelable