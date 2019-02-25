package org.faruk.flickrmvvm.ui.recent

import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.faruk.flickrmvvm.R
import org.faruk.flickrmvvm.base.BaseActivity
import org.faruk.flickrmvvm.base.FlickrApp
import org.faruk.flickrmvvm.common.TYPE_FOOTER
import org.faruk.flickrmvvm.di.ViewModelFactory
import org.faruk.flickrmvvm.model.Photo
import org.faruk.flickrmvvm.network.Resource
import org.faruk.flickrmvvm.util.EndlessRecyclerViewScrollListener
import org.faruk.flickrmvvm.util.init
import javax.inject.Inject

class RecentActivity : BaseActivity() {

    override fun getContentView() = R.layout.activity_main

    var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    var recentAdapter: RecentAdapter? = null
    var photoList: MutableList<Photo>? = mutableListOf()
    var currentPage = 1
    var recentViewModel: RecentViewModel? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FlickrApp.appComponent.inject(this)

        recentAdapter = RecentAdapter(photoList)
        recentRecyclerView.init(RecyclerView.VERTICAL)
        recentRecyclerView.adapter = recentAdapter
        addRecyclerListener()

        recentViewModel = ViewModelProviders.of(this, viewModelFactory).get(RecentViewModel::class.java)

        recentViewModel?.getRecentPhotos()

        recentViewModel?.photos?.observe(this, Observer { resource ->

            when (resource.status) {
                Resource.Status.SUCCESS -> {

                    if (resource.data!!.total > 0) {
                        if (resource.data.page != 1) {
                            val mSize = recentAdapter!!.itemList!!.size
                            recentAdapter?.itemList!!.removeAt(mSize - 1)
                            recentAdapter?.itemList!!.addAll(resource.data.photo as MutableList<Photo>)
                            photoList = recentAdapter?.itemList
                            recentRecyclerView.adapter?.notifyItemRangeChanged(
                                mSize - 1,
                                recentAdapter?.itemList?.size!! - mSize
                            )
                        } else {
                            endlessRecyclerViewScrollListener?.resetState()
                            recentAdapter?.itemList = resource.data.photo as MutableList<Photo>
                            photoList = recentAdapter?.itemList
                            recentAdapter?.notifyDataSetChanged()
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    //TODO
                }
                Resource.Status.LOADING -> {
                    //TODO
                }
            }

        })
    }

    private fun addRecyclerListener() {
        endlessRecyclerViewScrollListener = object : EndlessRecyclerViewScrollListener(
            recentRecyclerView.layoutManager as LinearLayoutManager
        ) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (!hasFooter()) {
                    val mHandler = Handler()
                    mHandler.post {
                        recentAdapter?.itemList?.add(Photo(mViewType = TYPE_FOOTER))
                        recentRecyclerView.adapter?.notifyItemInserted(recentAdapter?.itemList?.size!! - 1)
                    }
                    currentPage++
                    recentViewModel?.getRecentPhotos(currentPage)
                }
            }
        }
        recentRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener as EndlessRecyclerViewScrollListener)
    }

    private fun hasFooter(): Boolean {
        return recentAdapter?.itemList?.get(recentAdapter!!.itemList!!.size - 1)!!.mViewType == TYPE_FOOTER
    }
}
