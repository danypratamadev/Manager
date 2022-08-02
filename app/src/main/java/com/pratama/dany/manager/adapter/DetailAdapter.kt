package com.pratama.dany.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pratama.dany.manager.R
import com.pratama.dany.manager.model.DetailModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailAdapter(val context: Context, val listDetail: ArrayList<DetailModel>)
    : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private val locale = Locale("in", "ID")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)
    private var nomorUrut = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.detail_layout, parent, false)
        return DetailViewHolder(view)

    }

    override fun getItemCount(): Int {

        return listDetail.size

    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {

        val currentItem = listDetail[position]

        nomorUrut++

        if(nomorUrut % 2 == 0){

            holder.detailCard.setBackgroundColor(context.resources.getColor(R.color.Gray50))

        }

        holder.jumlahPesan.text = currentItem.jumlah.toString() + "x"
        holder.namaMenu.text = currentItem.namaMenu
        holder.totalPesan.text = numberFormat.format(currentItem.subtotal)



    }


    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val detailCard = itemView.findViewById<LinearLayout>(R.id.cardDetail)
        val namaMenu = itemView.findViewById<TextView>(R.id.namaMenu)
        val jumlahPesan = itemView.findViewById<TextView>(R.id.jmlPesan)
        val totalPesan = itemView.findViewById<TextView>(R.id.totalPesan)

    }
}