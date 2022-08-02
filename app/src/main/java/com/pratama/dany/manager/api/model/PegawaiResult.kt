package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PegawaiResult(@SerializedName("status")
                         @Expose val status: Boolean,
                         @SerializedName("message")
                         @Expose val message: String,
                         @SerializedName("result")
                         @Expose val result: ArrayList<PegawaiModel>) {
}