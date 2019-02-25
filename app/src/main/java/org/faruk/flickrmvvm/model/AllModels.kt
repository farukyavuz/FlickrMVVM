package org.faruk.flickrmvvm.model

import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.faruk.flickrmvvm.util.parseToFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
class FlickParams(
    @SerializedName("farm_id") val farmId: Int,
    @SerializedName("server_id") val serverId: String,
    val id: String,
    val secret: String
) : Parcelable {
    override fun toString(): String {
        return "http://farm${farmId}.static.flickr.com/${serverId}/${id}_${secret}.jpg"
    }
}

@Parcelize
data class Photo(
    val id: String = "",
    val owner: String = "",
    val secret: String = "",
    val title: String = "",
    val farm: Int = 0,
    @SerializedName("dateupload") val dateUpload: String = "1000000000",
    @SerializedName("ownername") val ownerName: String = "",
    @SerializedName("url_o") val urlO: String = "",
    val description: Description? = null,
    val server: String = "",
    @SerializedName("url_m") val urlM: String = "",
    @SerializedName("url_s") val urlS: String = "",
    var mViewType: Int = 1,
    @SerializedName("iconserver") val iconServer: String = "",
    @SerializedName("iconfarm") val iconFarm: Int = 0,
    val views: Int = 0
) : Parcelable {

    fun getDateFormated(): String {
        try {
            Log.e("dateupload : ", dateUpload)
            val mDate: Date? = Date(this.dateUpload.toInt() * 1000L)
            val mInputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
            mInputFormat.timeZone = TimeZone.getTimeZone("UTC-3")
            val mDateOut = mInputFormat.parse(mInputFormat.format(mDate))
            return "${mDateOut.parseToFormat("d MMM")}, ${mDateOut.parseToFormat("yyyy")}"
        } catch (e: ParseException) {
            e.printStackTrace()
            return "NaN"
        }
    }

    fun getUserPhotoUrl(): String {
        return "http://farm${iconFarm}.staticflickr.com/${iconServer}/buddyicons/${owner}.jpg"
    }
}

@Parcelize
data class Description(val _content: String) : Parcelable

@Parcelize
data class Photos(
    val page: Int,
    val pages: Int,
    @SerializedName("perpage") val perPage: Int,
    val total: Int,
    val photo: List<Photo>
) : Parcelable

@Parcelize
data class RecentResponse(val photos: Photos) : Parcelable