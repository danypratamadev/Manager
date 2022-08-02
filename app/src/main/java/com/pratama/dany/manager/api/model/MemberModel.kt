package com.pratama.dany.manager.api.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MemberModel(@SerializedName("member")
                       @Expose val member: String) {
}