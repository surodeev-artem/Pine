package artem.surodeev.pine

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import artem.surodeev.pine.Main.MainModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class ImagesAdapter: RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private lateinit var context: Context

    private val imagesList = ArrayList<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.image_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = imagesList[position]
        holder.imageTitle.text = image.description
        val listener = object: RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                MainModel.error()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                holder.imageParent.setOnClickListener {
                    ImageCasher.setDrawable(resource)
                    MainModel.openFullscreenImage(image)
                }
                return false
            }

        }
        Glide.with(context).load(image.urls.small).centerCrop().listener(listener).into(holder.image)

    }

    fun addImages(images: ArrayList<Image>){
        imagesList.addAll(images)
    }

    fun clearList(){
        imagesList.clear()
        notifyDataSetChanged()
    }

    fun getImagesList() = imagesList

    override fun getItemCount() = imagesList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageTitle: TextView = itemView.findViewById(R.id.image_title)
        val image: ImageView = itemView.findViewById(R.id.image)
        val imageParent: View = itemView.findViewById(R.id.image_parent)
    }
}