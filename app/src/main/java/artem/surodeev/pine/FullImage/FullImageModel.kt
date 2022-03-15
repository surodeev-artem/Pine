package artem.surodeev.pine.FullImage

import android.content.Context
import android.graphics.drawable.Drawable
import artem.surodeev.pine.ImageCasher
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object FullImageModel {
    private lateinit var controller: FullImageController

    fun setController(controller: FullImageController) {
        FullImageModel.controller = controller
    }

    fun getRawImage(rawUrl: String?, context: Context) {
        Glide.with(context).load(rawUrl).centerCrop().placeholder(ImageCasher.getDrawable())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    controller.setImage(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }
}