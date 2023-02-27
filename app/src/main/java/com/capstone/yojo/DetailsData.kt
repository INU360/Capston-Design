package com.capstone.yojo

import android.os.Parcel
import android.os.Parcelable

data class DetailsData (
        var floor : Long? ,
        var price :Long? ,
        var size : Double?,
        var type : String?,
        var date : Long?,
        var deposit : Long?,
        var monthly : Long?
        ): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readValue(Long::class.java.classLoader) as Long?,
                parcel.readValue(Long::class.java.classLoader) as Long?,
                parcel.readValue(Double::class.java.classLoader) as Double?,
                parcel.readString(),
                parcel.readValue(Long::class.java.classLoader) as Long?,
                parcel.readValue(Long::class.java.classLoader) as Long?,
                parcel.readValue(Long::class.java.classLoader) as Long?

        )
        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(floor)
                parcel.writeValue(price)
                parcel.writeValue(size)
                parcel.writeString(type)
                parcel.writeValue(date)
                parcel.writeValue(deposit)
                parcel.writeValue(monthly)

        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<DetailsData> {
                override fun createFromParcel(parcel: Parcel): DetailsData {
                        return DetailsData(parcel)
                }

                override fun newArray(size: Int): Array<DetailsData?> {
                        return arrayOfNulls(size)
                }
        }
}