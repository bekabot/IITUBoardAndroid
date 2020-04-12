package kz.iitu.iituboardandroid.ui.record

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.iitu.iituboardandroid.R

class CarouselItemAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.record_carousel_item_vh, parent, false
            )
        return CarouselItemVH(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val vh = holder as CarouselItemVH
        vh.set(items[position])
    }

    fun set(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }
}