package com.capstone.yojo

import android.os.Parcel
import android.os.Parcelable

data class OpiMarkerData (
    var name : String? ,
    var address :String?,
    var keyValue : String?,
    var dong : String?,
    var build : Long?,
    var latitude : Double?,
    var longitude : Double?
        ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Long::class.java.classLoader) as Long?,
        parcel.readValue(Double::class.java.classLoader) as Double?,
        parcel.readValue(Double::class.java.classLoader) as Double?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(keyValue)
        parcel.writeString(dong)
        parcel.writeValue(build)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OpiMarkerData> {
        override fun createFromParcel(parcel: Parcel): OpiMarkerData {
            return OpiMarkerData(parcel)
        }

        override fun newArray(size: Int): Array<OpiMarkerData?> {
            return arrayOfNulls(size)
        }
    }
}