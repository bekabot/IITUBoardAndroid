package kz.iitu.iituboardandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.iitu.iituboardandroid.api.response.Record
import kz.iitu.iituboardandroid.ui.RecordVH

class RecordsAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var recordsData = ArrayList<Record>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.record_vh, parent, false)
        return RecordVH(view)
    }

    override fun getItemCount() = recordsData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val recordVH = holder as RecordVH
        recordVH.set(recordsData[position])
    }

    fun set(records: List<Record>) {
        this.recordsData = ArrayList(records)
        notifyDataSetChanged()
    }
}