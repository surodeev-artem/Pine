package artem.surodeev.pine

import android.graphics.drawable.Drawable

object ImageCasher {
    private var currentDrawable: Drawable? = null

    fun setDrawable(drawable: Drawable?) {
        currentDrawable = drawable
    }

    fun getDrawable() = currentDrawable
}