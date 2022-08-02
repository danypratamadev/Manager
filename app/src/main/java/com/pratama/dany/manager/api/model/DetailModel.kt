package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DetailModel(@SerializedName("menu")
                     @Expose val menu: String,
                       @SerializedName("jumlah")
                     @Expose val jumlah: Int,
                       @SerializedName("subtotal")
                     @Expose val subtotal: Int) {
}