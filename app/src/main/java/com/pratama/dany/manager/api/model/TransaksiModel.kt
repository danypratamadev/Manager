package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransaksiModel(@SerializedName("id")
                          @Expose val id: String,
                          @SerializedName("member")
                          @Expose val member: String,
                          @SerializedName("meja")
                          @Expose val meja: Int,
                          @SerializedName("jam")
                          @Expose val jam: String,
                          @SerializedName("total")
                          @Expose val total: Int) {
}