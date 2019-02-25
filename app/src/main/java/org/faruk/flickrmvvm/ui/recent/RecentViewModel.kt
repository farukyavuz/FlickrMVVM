package org.faruk.flickrmvvm.ui.recent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.faruk.flickrmvvm.common.TYPE_CARD
import org.faruk.flickrmvvm.model.Photos
import org.faruk.flickrmvvm.model.RecentResponse
import org.faruk.flickrmvvm.network.AppException
import org.faruk.flickrmvvm.network.FlickrApi
import org.faruk.flickrmvvm.network.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RecentViewModel @Inject constructor() : ViewModel() {

    val photos: MutableLiveData<Resource<Photos?>> = MutableLiveData()
    //val api = retrofit().create(FlickrApi::class.java)

    @Inject
    lateinit var flickrApi: FlickrApi

    fun getRecentPhotos(
        currentPage: Int = 1,
        perPage: Int = 30
    ) {

//        if (!Util.isNetworkAvailable()) {
//            launches.value = Resource.error(AppException(Throwable("Check Your Internet Connection!", null)))
//        }

        val mCall = flickrApi.getRecent(currentPage, perPage)

        photos.value = Resource.loading()
        mCall.enqueue(object : Callback<RecentResponse> {
            override fun onResponse(call: Call<RecentResponse>?, response: Response<RecentResponse>?) {
                try {
                    if (response?.body() != null) {
                        response.body()?.photos?.photo?.forEach { it.mViewType = TYPE_CARD }
                        photos.value = Resource.success(response.body()?.photos)
                    }
                } catch (e: Exception) {
                    val exception = AppException(e)
                    photos.value = Resource.error(exception)
                    call?.cancel()
                }
            }

            override fun onFailure(call: Call<RecentResponse>?, t: Throwable?) {
                val exception = AppException(t)
                photos.value = Resource.error(exception)
            }
        })
    }


}