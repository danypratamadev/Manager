package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PegawaiModel(@SerializedName("idpegawai")
                         @Expose val idPgw: String,
                        @SerializedName("nama")
                         @Expose val nama: String,
                        @SerializedName("email")
                         @Expose val email: String,
                        @SerializedName("alamat")
                         @Expose val alamat: String,
                        @SerializedName("telepon")
                        @Expose val telepon: String,
                        @SerializedName("hakakses")
                        @Expose val akses: Int,
                        @SerializedName("status")
                        @Expose val status: Int) {
}