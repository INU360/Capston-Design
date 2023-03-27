package com.capstone.yojo

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import java.io.Serializable

data class PlaceData(
    var name : String?,
    var address : String?,
    var latitude :Double?,
    var longitude :Double?
) : Serializable, ClusterItem {

    override fun getPosition(): LatLng {
        return LatLng(latitude ?: 0.0, longitude ?: 0.0)
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getSnippet(): String? {
        return address
    }
}