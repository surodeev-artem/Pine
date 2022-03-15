package artem.surodeev.pine.Main

import artem.surodeev.pine.Image

interface MainController {

    fun openFullScreenImage(image: Image)

    fun updateImagesList(images: ArrayList<Image>)

    fun showError()
}