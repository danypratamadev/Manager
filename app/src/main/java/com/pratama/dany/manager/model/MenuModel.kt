package com.pratama.dany.manager.model

data class MenuModel(var idMenu: Int = 0, var ktgMenu: Int = 0,
                     var namaMenu: String = "", var statusMenu: Int = 0, var gbrMenu: String = "",
                     var hargaMenu: Int = 0, var ps: Int = 0, var pp: Int = 0, var pp_plus: Int = 0,
                     var pp_min: Int = 0, var hargaMenuOn: Int = 0, var psOn: Int = 0, var ppOn: Int = 0,
                     var pp_plusOn: Int = 0, var pp_minOn: Int = 0) {
}