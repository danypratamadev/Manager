package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PublicResult(@SerializedName("status")
                        @Expose val status: Boolean,
                        @SerializedName("message")
                        @Expose val message: String) {
}