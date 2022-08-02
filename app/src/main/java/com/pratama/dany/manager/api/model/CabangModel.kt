package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CabangModel(@SerializedName("nama")
                  @Expose val namaCabang: String,
                  @SerializedName("alamat")
                  @Expose val alamatCabang: String,
                  @SerializedName("telepon")
                  @Expose val teleponCabang: String,
                  @SerializedName("p_gojek")
                  @Expose val potonganGojek: Int,
                  @SerializedName("p_grab")
                  @Expose val potonganGrab: Int) {
}