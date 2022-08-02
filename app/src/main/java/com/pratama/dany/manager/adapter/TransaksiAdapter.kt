package com.pratama.dany.manager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pratama.dany.manager.DetailsActivity
import com.pratama.dany.manager.R
import com.pratama.dany.manager.model.TransaksiModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TransaksiAdapter(val context: Context, val listTransaksi: ArrayList<TransaksiModel>)
    : RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    private var nomorUrut = 0
    private val locale = Locale("in", "ID")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.transaksi_layout, parent, false)
        return TransaksiViewHolder(view)

    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {

        val currentItem = listTransaksi[position]
        nomorUrut++

        if(nomorUrut % 2 == 0){

            holder.cardTransaksi.setBackgroundColor(context.resources.getColor(R.color.Gray50))

        }

        holder.noTrans.text = currentItem.idTrans
        holder.plgTrans.text = currentItem.namaPlg
        holder.jamTrans.text = currentItem.jamTrans
        holder.totalTrans.text = numberFormat.format(currentItem.totBayar)

        holder.itemView.setOnClickListener{

            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("idTrans", currentItem.idTrans)
            intent.putExtra("namaPlg", currentItem.namaPlg)
            intent.putExtra("meja", currentItem.meja)
            intent.putExtra("jam", currentItem.jamTrans)
            intent.putExtra("total", currentItem.totBayar)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return listTransaksi.size
    }

    class TransaksiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cardTransaksi = itemView.findViewById<LinearLayout>(R.id.cardTransaksi)
        val noTrans = itemView.findViewById<TextView>(R.id.nomorTrans)
        val plgTrans = itemView.findViewById<TextView>(R.id.plgTrans)
        val jamTrans = itemView.findViewById<TextView>(R.id.jamTrans)
        val totalTrans = itemView.findViewById<TextView>(R.id.totalTrans)

    }
}