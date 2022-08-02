package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderModel(@SerializedName("id")
                          @Expose val id: String,
                      @SerializedName("idmember")
                      @Expose val idmember: String,
                      @SerializedName("member")
                          @Expose val member: String,
                      @SerializedName("koin")
                          @Expose val koin: Int,
                      @SerializedName("jam")
                          @Expose val jam: String,
                      @SerializedName("discmember")
                      @Expose val dismember: Int,
                      @SerializedName("discpromo")
                      @Expose val dispromo: Int,
                      @SerializedName("total")
                          @Expose val total: Int) {
}