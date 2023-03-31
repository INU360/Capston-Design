package com.capstone.yojo

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CustomClusterRenderer (
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<MapItem>
) : DefaultClusterRenderer<MapItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(item: MapItem, markerOptions: MarkerOptions) {
        val icon = when (item.category) {
            "병원" -> R.drawable.hospital_marker

            else -> R.drawable.hospital_marker
        }
        markerOptions.icon(BitmapDescriptorFactory.fromResource(icon))
        super.onBeforeClusterItemRendered(item, markerOptions)
    }
}