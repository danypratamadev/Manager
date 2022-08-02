package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KategoriModel(@SerializedName("id")
                         @Expose var id: Int = 0,
                         @SerializedName("kategori")
                         @Expose var kategori: String = "") {
}