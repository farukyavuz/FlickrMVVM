package org.faruk.flickrmvvm.ui.recent

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import org.faruk.flickrmvvm.R
import org.faruk.flickrmvvm.common.DETAIL_DATA
import org.faruk.flickrmvvm.common.TYPE_CARD
import org.faruk.flickrmvvm.common.TYPE_FOOTER
import org.faruk.flickrmvvm.model.FlickParams
import org.faruk.flickrmvvm.model.Photo
import org.faruk.flickrmvvm.ui.fullscreen.FullScreenImageActivity
import org.faruk.flickrmvvm.util.loadFromFlickr

class RecentAdapter(var itemList: MutableList<Photo>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int {
        return itemList!!.count()
    }

    override fun getItemViewType(position: Int): Int {
        return itemList!![position].mViewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolderCard -> {
                holder.bindView(itemList?.get(position))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val mInflater = LayoutInflater.from(parent.context)
        val mViewHolder: RecyclerView.ViewHolder
        when (viewType) {
            TYPE_CARD -> {
                val v = mInflater.inflate(R.layout.item_card, parent, false)
                mViewHolder = ViewHolderCard(v)
            }
            TYPE_FOOTER -> {
                val v = mInflater.inflate(R.layout.item_progress, parent, false)
                mViewHolder = ViewHolderFooter(v)
            }
            else -> {
                val v = mInflater.inflate(R.layout.item_card, parent, false)
                mViewHolder = ViewHolderCard(v)
            }
        }

        return mViewHolder
    }

    class ViewHolderFooter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var progressBar: ProgressBar = itemView.findViewById(R.id.itemProgressBar) as ProgressBar
    }

    class ViewHolderCard(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.itemCardImage) as ImageView
        var userImage: ImageView = itemView.findViewById(R.id.itemCardUserImage) as ImageView
        var username: TextView = itemView.findViewById(R.id.itemCardUsername) as TextView
        var title: TextView = itemView.findViewById(R.id.itemCardTitle) as TextView
        var date: TextView = itemView.findViewById(R.id.itemCardDate) as TextView

        fun bindView(photo: Photo?) {
            photo?.let {
                if (it.urlS.isNotEmpty()) {
                    this.image.loadFromFlickr(it.urlS)
                } else {
                    this.image.loadFromFlickr(
                        FlickParams(
                            it.farm,
                            it.server,
                            it.id,
                            it.secret
                        ).toString()
                    )
                }

                userImage.loadFromFlickr(it.getUserPhotoUrl(), true)

                username.text = it.ownerName
                title.text = if (it.title.length > 30) "${it.title.substring(0, 30)}..." else it.title
                date.text = it.getDateFormated()


                itemView.setOnClickListener {
                    val intentFullScreen = Intent(itemView.context, FullScreenImageActivity::class.java)
                    intentFullScreen.putExtra(DETAIL_DATA, photo)

                    val pairImg: Pair<View, String> =
                        Pair(this.image, itemView.context.getString(R.string.transition_image))

                    val activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        pairImg
                    )

                    ActivityCompat.startActivity(
                        itemView.context as Activity,
                        intentFullScreen,
                        activityOptionsCompat.toBundle()
                    )
                }
            }


        }

    }
}