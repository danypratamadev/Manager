package com.pratama.dany.manager.adapter

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.shimmer.ShimmerFrameLayout
import com.pratama.dany.manager.DatabaseHelper
import com.pratama.dany.manager.EditMenuActivity
import com.pratama.dany.manager.R
import com.pratama.dany.manager.model.MenuModel
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MenuAdapter(val context: Context, private val listMenu: ArrayList<MenuModel>)
    : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private lateinit var db: DatabaseHelper
    private val locale = Locale("in", "ID")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.menu_layout, parent, false)
        return MenuViewHolder(view)

    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

        val currentPosition = listMenu[position]

        db = DatabaseHelper(context)

        val config = context.resources.configuration as Configuration
        if(config.smallestScreenWidthDp >= 600){
            Glide.with(holder.itemView.context)
                .load(currentPosition.gbrMenu).apply(
                    RequestOptions()
                        .override(200, 200)
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgMenu)
        } else {
            Glide.with(holder.itemView.context)
                .load(currentPosition.gbrMenu).apply(
                    RequestOptions()
                        .override(100, 100)
                )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgMenu)
        }

        holder.namaMenu.text = currentPosition.namaMenu
        if(currentPosition.statusMenu == 1){
            holder.statusMenu.text = "Tersedia"
        } else {
            holder.statusMenu.text = "Habis"
            holder.statusMenu.setTextColor(context.resources.getColor(R.color.RedPrimary))
        }
        holder.hargaMenu.text = numberFormat.format(currentPosition.hargaMenu)

        if(currentPosition.pp != 0) {

            holder.shimmerPromo.visibility = View.VISIBLE
            holder.shimmerPromo.startShimmer()
            holder.promoMenu.text = "${currentPosition.pp}% OFF"

            if (currentPosition.pp_plus != 0) {
                holder.promoMenu.text = "${currentPosition.pp}% + ${currentPosition.pp_plus}%"
            }
        }

        holder.itemView.setOnClickListener {

            val intent = Intent(context, EditMenuActivity::class.java)
            intent.putExtra("imgMenu", currentPosition.gbrMenu)
            intent.putExtra("idMenu", currentPosition.idMenu)
            intent.putExtra("ktgMenu", currentPosition.ktgMenu)
            intent.putExtra("namaMenu", currentPosition.namaMenu)
            intent.putExtra("hargaMenu", currentPosition.hargaMenu)
            intent.putExtra("statusMenu", currentPosition.statusMenu)
            intent.putExtra("pp", currentPosition.pp)
            intent.putExtra("ps", currentPosition.ps)
            intent.putExtra("pp_plus", currentPosition.pp_plus)
            intent.putExtra("pp_min", currentPosition.pp_min)
            intent.putExtra("hargaMenuOn", currentPosition.hargaMenuOn)
            intent.putExtra("ppOn", currentPosition.ppOn)
            intent.putExtra("psOn", currentPosition.psOn)
            intent.putExtra("pp_plusOn", currentPosition.pp_plusOn)
            intent.putExtra("pp_minOn", currentPosition.pp_minOn)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return listMenu.size
    }

    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgMenu = itemView.findViewById(R.id.imgMenu) as ImageView
        val namaMenu = itemView.findViewById(R.id.namaMenu) as TextView
        val statusMenu = itemView.findViewById(R.id.statusMenu) as TextView
        val hargaMenu = itemView.findViewById(R.id.hargaMenu) as TextView
        val shimmerPromo = itemView.findViewById(R.id.shimmerPromo) as ShimmerFrameLayout
        val promoMenu = itemView.findViewById(R.id.promoMenu) as TextView

    }

}