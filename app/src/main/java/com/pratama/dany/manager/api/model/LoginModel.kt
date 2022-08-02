package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginModel(@SerializedName("iduser")
                      @Expose val iduser: String,
                      @SerializedName("nama")
                      @Expose val nama: String,
                      @SerializedName("email")
                      @Expose val email: String,
                      @SerializedName("role")
                      @Expose val role: Int,
                      @SerializedName("cabang")
                      @Expose val cabang: Int,
                      @SerializedName("namacabang")
                      @Expose val namacabang: String,
                      @SerializedName("alamatcabang")
                      @Expose val alamatcabang: String) {
}