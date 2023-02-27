package com.capstone.yojo

import java.io.Serializable

data class PlaceData(
    var name : String?,
    var address : String?,
    var latitude :Double?,
    var longitude :Double?
) : Serializable