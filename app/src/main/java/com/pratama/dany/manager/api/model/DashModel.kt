package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DashModel(@SerializedName("transaksi")
                     @Expose val transaksi: Int,
                     @SerializedName("item")
                     @Expose val item: Int,
                     @SerializedName("total")
                     @Expose val total: Int) {
}