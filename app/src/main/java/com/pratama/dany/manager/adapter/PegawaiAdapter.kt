package com.pratama.dany.manager.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pratama.dany.manager.R
import com.pratama.dany.manager.model.PegawaiModel

class PegawaiAdapter(val context: Context, val listPegawai: ArrayList<PegawaiModel>)
    : RecyclerView.Adapter<PegawaiAdapter.PegawaiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PegawaiViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.pegawai_layout, parent, false)
        return PegawaiViewHolder(view)

    }

    override fun onBindViewHolder(holder: PegawaiViewHolder, position: Int) {

        val currentPosition = listPegawai.get(position)

        holder.namaPgw.text = currentPosition.namaPgw

        if(currentPosition.aksesPgw == 3){
            holder.profPgw.text = "Kasir"
        } else if(currentPosition.aksesPgw == 4){
            holder.profPgw.text = "Koki"
        } else {
            holder.profPgw.text = "Pelayan"
        }

        holder.itemView.setOnClickListener{

            /*val intent = Intent(context, EditPegawaiActivity::class.java)
            intent.putExtra("idPgw", currentPosition.idPgw)
            intent.putExtra("namaPgw", currentPosition.namaPgw)
            intent.putExtra("profPgw", currentPosition.provPgw)
            intent.putExtra("emailPgw", currentPosition.emailPgw)
            intent.putExtra("passPgw", currentPosition.passPgw)
            intent.putExtra("alamatPgw", currentPosition.alamatPgw)
            intent.putExtra("nohpPgw", currentPosition.nohpPgw)
            intent.putExtra("aksesPgw", currentPosition.aksesPgw)
            context.startActivity(intent)*/

        }

    }

    override fun getItemCount(): Int {
        return listPegawai.size
    }

    class PegawaiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val namaPgw = itemView.findViewById(R.id.namaPgw) as TextView
        val profPgw = itemView.findViewById(R.id.profPgw) as TextView

    }
}