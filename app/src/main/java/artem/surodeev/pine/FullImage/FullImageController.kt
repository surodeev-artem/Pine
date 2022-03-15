package artem.surodeev.pine.FullImage

import android.graphics.drawable.Drawable

interface FullImageController {
    fun setImage(drawable: Drawable)

    fun setError(errorCode: Int)
}