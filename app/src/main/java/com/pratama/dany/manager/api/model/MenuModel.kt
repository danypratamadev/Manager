package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MenuModel(@SerializedName("id")
                     @Expose val id: Int,
                     @SerializedName("menu")
                     @Expose val menu: String,
                     @SerializedName("kategori")
                     @Expose val kategori: Int,
                     @SerializedName("harga")
                     @Expose val harga: Int,
                     @SerializedName("gambar")
                     @Expose val gambar: String,
                     @SerializedName("status")
                     @Expose val status: Int,
                     @SerializedName("ps")
                     @Expose val ps: Int,
                     @SerializedName("pp")
                     @Expose val pp: Int,
                     @SerializedName("pp_min")
                     @Expose val pp_min: Int,
                     @SerializedName("pp_plus")
                     @Expose val pp_plus: Int,
                     @SerializedName("on_harga")
                     @Expose val hargaOn: Int,
                     @SerializedName("on_ps")
                     @Expose val psOn: Int,
                     @SerializedName("on_pp")
                     @Expose val ppOn: Int,
                     @SerializedName("on_pp_min")
                     @Expose val pp_minOn: Int,
                     @SerializedName("on_pp_plus")
                     @Expose val pp_plusOn: Int) {
}