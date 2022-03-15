package artem.surodeev.pine

import androidx.recyclerview.widget.DiffUtil

class ImageDiffUtilCallback(private val oldList: ArrayList<Image>,
                            private val newList: ArrayList<Image>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldImage = oldList[oldItemPosition]
        val newImage = newList[newItemPosition]
        return oldImage.id == newImage.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldImage = oldList[oldItemPosition]
        val newImage = newList[newItemPosition]
        return oldImage.description == newImage.description &&
                oldImage.urls == newImage.urls
    }

}