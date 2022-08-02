package com.pratama.dany.manager

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.PublicResult
import kotlinx.android.synthetic.main.activity_edit_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditMenuActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var db : DatabaseHelper
    private lateinit var mdialog: Dialog
    private var gbrMenu: String = ""
    private var idMenu: Int = 0
    private var ktgMenu: Int = 0
    private var namaMenu: String = ""
    private var hargaMenu: Int = 0
    private var statusMenu: Int = 0
    private var pp: Int = 0
    private var ps: Int = 0
    private var pt: Int = 0
    private var pt_min: Int = 0
    private var hargaMenuOn: Int = 0
    private var ppOn: Int = 0
    private var psOn: Int = 0
    private var ptOn: Int = 0
    private var pt_minOn: Int = 0
    private var statusMenuChange: Int = 0

    private var namaKtg: String = ""

    private val permission = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private val PERMISSION = 1000
    private val REQ = 1100
    private var URI: Uri? = null

    private var upNama = 0
    private var upKtg = 0
    private var upHrg = 0
    private var upHrgOn = 0
    private var upSts = 0
    private var upPromoPS = 0
    private var upPromoPP = 0
    private var upPromoPT = 0
    private var upPromoPSOn = 0
    private var upPromoPPOn = 0
    private var upPromoPTOn = 0
    private var alert = 0
    private var idCabang = 0
    private var namaCabang = ""
    private var promoSyarat = 0
    private var promoPersen = 0
    private var promoTambah = 0
    private var promoSyaratOn = 0
    private var promoPersenOn = 0
    private var promoTambahOn = 0
    private var promoPersenChange = 0
    private var promoSyaratChange = 0
    private var promoTambahChange = 0
    private var promoPersenChangeOn = 0
    private var promoSyaratChangeOn = 0
    private var promoTambahChangeOn = 0

    private val fbd : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_menu)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Edit Menu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        db = DatabaseHelper(this)
        val sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        idCabang = sharedPreferences.getInt("cabang", 0)
        namaCabang = sharedPreferences.getString("namacabang", "").toString()

        gbrMenu = intent.getStringExtra("imgMenu")!!
        idMenu = intent.getIntExtra("idMenu", 0)
        ktgMenu = intent.getIntExtra("ktgMenu", 0)
        namaMenu = intent.getStringExtra("namaMenu")!!
        hargaMenu = intent.getIntExtra("hargaMenu", 0)
        statusMenu = intent.getIntExtra("statusMenu", 0)
        pp = intent.getIntExtra("pp", 0)
        ps = intent.getIntExtra("ps", 0)
        pt = intent.getIntExtra("pp_plus", 0)
        pt_min = intent.getIntExtra("pp_min", 0)
        hargaMenuOn = intent.getIntExtra("hargaMenuOn", 0)
        ppOn = intent.getIntExtra("ppOn", 0)
        psOn = intent.getIntExtra("psOn", 0)
        ptOn = intent.getIntExtra("pp_plusOn", 0)
        pt_minOn = intent.getIntExtra("pp_minOn", 0)
        statusMenuChange = intent.getIntExtra("statusMenu", 0)
        namaKtg = db.selectNamaKtg(ktgMenu)

        if(ps != 0){
            promoSyarat = 1
            promoSyaratChange = 1
            switchPromoSyarat.isChecked = true
        }

        if(pp != 0){
            promoPersen = 1
            promoPersenChange = 1
            switchDiskon.isChecked = true
            formDiskon.expand()
            besarDiskon.setText(pp.toString())
        }

        if(pt != 0){
            promoTambah = 1
            promoTambahChange = 1
            switchTambahDiskon.isChecked = true
            formTambahDiskon.expand()
            besarTambahDiskon.setText(pt.toString())
            besarMinimalBeli.setText(pt_min.toString())
        }

        if(psOn != 0){
            promoSyaratOn = 1
            promoSyaratChangeOn = 1
            switchPromoSyaratOn.isChecked = true
        }

        if(ppOn != 0){
            promoPersenOn = 1
            promoPersenChangeOn = 1
            switchDiskonOn.isChecked = true
            formDiskonOn.expand()
            besarDiskonOn.setText(ppOn.toString())
        }

        if(ptOn != 0){
            promoTambahOn = 1
            promoTambahChangeOn = 1
            switchTambahDiskonOn.isChecked = true
            formTambahDiskonOn.expand()
            besarTambahDiskonOn.setText(ptOn.toString())
            besarMinimalBeliOn.setText(pt_minOn.toString())
        }

        Glide.with(this)
            .load(gbrMenu).apply(
                RequestOptions()
                    .override(500, 500)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgMenu)

        edNamaMenu.setText(namaMenu)
        edKtgMenu.setText(namaKtg)
        edKtgMenu.isEnabled = false
        edHargaMenu.setText(hargaMenu.toString())
        edHargaMenuOn.setText(hargaMenuOn.toString())

        if(statusMenu == 1){

            stsYes.setBackgroundResource(R.drawable.back_button_green_stroke_fill)
            stsYes.setTextColor(resources.getColor(R.color.GreenA700))

        } else {

            stsNo.setBackgroundResource(R.drawable.back_button_red_stroke_fill)
            stsNo.setTextColor(resources.getColor(R.color.RedA700))

        }

        simpanMenu.isEnabled = false
        simpanMenu.alpha = 0.5f

        edNamaMenu.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(namaMenu)){

                    upNama = 1
                    enableBtnSimpan()

                } else {

                    upNama = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        edHargaMenu.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(hargaMenu.toString())){

                    upHrg = 1
                    enableBtnSimpan()

                } else {

                    upHrg = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        edHargaMenuOn.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(hargaMenuOn.toString())){

                    upHrgOn = 1
                    enableBtnSimpan()

                } else {

                    upHrgOn = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        switchPromoSyarat.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                promoSyaratChange = 1
                if(promoSyaratChange != promoSyarat){
                    upPromoPS = 1
                } else {
                    upPromoPS = 0
                }
                enableBtnSimpan()
                Toast.makeText(this, "Promo Crazy Price Active", Toast.LENGTH_SHORT).show()
            } else {
                promoSyaratChange = 0
                if(promoSyaratChange != promoSyarat){
                    upPromoPS = 1
                } else {
                    upPromoPS = 0
                }
                enableBtnSimpan()
                Toast.makeText(this, "Promo Crazy Price Disable", Toast.LENGTH_SHORT).show()
            }

        }

        switchPromoSyaratOn.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                promoSyaratChangeOn = 1
                if(promoSyaratChangeOn != promoSyaratOn){
                    upPromoPSOn = 1
                } else {
                    upPromoPSOn = 0
                }
                enableBtnSimpan()
                Toast.makeText(this, "Promo Crazy Price Active", Toast.LENGTH_SHORT).show()
            } else {
                promoSyaratChangeOn = 0
                if(promoSyaratChangeOn != promoSyaratOn){
                    upPromoPSOn = 1
                } else {
                    upPromoPSOn = 0
                }
                enableBtnSimpan()
                Toast.makeText(this, "Promo Crazy Price Disable", Toast.LENGTH_SHORT).show()
            }

        }

        switchDiskon.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                promoPersenChange = 1
                if(promoPersenChange != promoPersen){
                    upPromoPP = 1
                } else {
                    upPromoPP = 0
                }
                enableBtnSimpan()
                formDiskon.expand()
                Toast.makeText(this, "Discount Menu Active", Toast.LENGTH_SHORT).show()
            } else {
                promoPersenChange = 0
                if(promoPersenChange != promoPersen){
                    upPromoPP = 1
                } else {
                    upPromoPP = 0
                }
                enableBtnSimpan()
                formDiskon.collapse()
                Toast.makeText(this, "Discount Menu Disable", Toast.LENGTH_SHORT).show()
            }

        }

        switchDiskonOn.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                promoPersenChangeOn = 1
                if(promoPersenChangeOn != promoPersenOn){
                    upPromoPPOn = 1
                } else {
                    upPromoPPOn = 0
                }
                enableBtnSimpan()
                formDiskonOn.expand()
                Toast.makeText(this, "Discount Menu Active", Toast.LENGTH_SHORT).show()
            } else {
                promoPersenChangeOn = 0
                if(promoPersenChangeOn != promoPersenOn){
                    upPromoPPOn = 1
                } else {
                    upPromoPPOn = 0
                }
                enableBtnSimpan()
                formDiskonOn.collapse()
                Toast.makeText(this, "Discount Menu Disable", Toast.LENGTH_SHORT).show()
            }

        }

        besarDiskon.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(pp.toString())){

                    upPromoPP = 1
                    enableBtnSimpan()

                } else {

                    upPromoPP = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        besarDiskonOn.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(ppOn.toString())){

                    upPromoPPOn = 1
                    enableBtnSimpan()

                } else {

                    upPromoPPOn = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        switchTambahDiskon.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                promoTambahChange = 1
                if(promoTambahChange != promoTambah){
                    upPromoPT = 1
                } else {
                    upPromoPT = 0
                }
                enableBtnSimpan()
                formTambahDiskon.expand()
                Toast.makeText(this, "Adding Discount Menu Active", Toast.LENGTH_SHORT).show()
            } else {
                promoTambahChange = 0
                if(promoTambahChange != promoTambah){
                    upPromoPT = 1
                } else {
                    upPromoPT = 0
                }
                enableBtnSimpan()
                formTambahDiskon.collapse()
                Toast.makeText(this, "Adding Discount Menu Disable", Toast.LENGTH_SHORT).show()
            }

        }

        switchTambahDiskonOn.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                promoTambahChangeOn = 1
                if(promoTambahChangeOn != promoTambahOn){
                    upPromoPTOn = 1
                } else {
                    upPromoPTOn = 0
                }
                enableBtnSimpan()
                formTambahDiskonOn.expand()
                Toast.makeText(this, "Adding Discount Menu Active", Toast.LENGTH_SHORT).show()
            } else {
                promoTambahChangeOn = 0
                if(promoTambahChangeOn != promoTambahOn){
                    upPromoPTOn = 1
                } else {
                    upPromoPTOn = 0
                }
                enableBtnSimpan()
                formTambahDiskonOn.collapse()
                Toast.makeText(this, "Adding Discount Menu Disable", Toast.LENGTH_SHORT).show()
            }

        }

        besarTambahDiskon.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(pt.toString())){

                    upPromoPT = 1
                    enableBtnSimpan()

                } else {

                    upPromoPT = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        besarTambahDiskonOn.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(ptOn.toString())){

                    upPromoPTOn = 1
                    enableBtnSimpan()

                } else {

                    upPromoPTOn = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        besarMinimalBeli.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(pt_min.toString())){

                    upPromoPT = 1
                    enableBtnSimpan()

                } else {

                    upPromoPT = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        besarMinimalBeliOn.addTextChangedListener(object: TextWatcher {

            override fun afterTextChanged(p0: Editable?) {

                if(!p0.toString().equals(pt_minOn.toString())){

                    upPromoPTOn = 1
                    enableBtnSimpan()

                } else {

                    upPromoPTOn = 0
                    enableBtnSimpan()

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }


        })

        chooseImg.setOnClickListener(this)
        stsYes.setOnClickListener(this)
        stsNo.setOnClickListener(this)
        cardPromoSyarat.setOnClickListener(this)
        cardDiskon.setOnClickListener(this)
        cardTambahDiskon.setOnClickListener(this)
        cardPromoSyaratOn.setOnClickListener(this)
        cardDiskonOn.setOnClickListener(this)
        cardTambahDiskonOn.setOnClickListener(this)
        simpanMenu.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when(p0?.id){

            R.id.chooseImg -> {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(
                            this.applicationContext, permission[0])
                        == PackageManager.PERMISSION_GRANTED){

                        chooseImage()

                    } else {

                        ActivityCompat.requestPermissions(this,
                            permission, PERMISSION)

                    }

                } else {

                    chooseImage()

                }

            }

            R.id.stsYes -> {

                stsYes.setBackgroundResource(R.drawable.back_button_green_stroke_fill)
                stsYes.setTextColor(resources.getColor(R.color.GreenA700))
                stsNo.setBackgroundResource(R.drawable.back_button_gray_stroke)
                stsNo.setTextColor(resources.getColor(R.color.Gray700))

                statusMenuChange = 1
                if(statusMenuChange != statusMenu){
                    upSts = 1
                } else {
                    upSts = 0
                }
                enableBtnSimpan()

            }

            R.id.stsNo -> {

                stsNo.setBackgroundResource(R.drawable.back_button_red_stroke_fill)
                stsNo.setTextColor(resources.getColor(R.color.RedA700))
                stsYes.setBackgroundResource(R.drawable.back_button_gray_stroke)
                stsYes.setTextColor(resources.getColor(R.color.Gray700))

                statusMenuChange = 0
                if(statusMenuChange != statusMenu){
                    upSts = 1
                } else {
                    upSts = 0
                }
                enableBtnSimpan()

            }

            R.id.cardPromoSyarat -> {

                if(promoSyaratChange == 1){
                    switchPromoSyarat.isChecked = false
                    promoSyaratChange = 0
                    if(promoSyaratChange != promoSyarat){
                        upPromoPS = 1
                    } else {
                        upPromoPS = 0
                    }
                    enableBtnSimpan()
                    Toast.makeText(this, "Promo Crazy Price Disable", Toast.LENGTH_SHORT).show()
                } else {
                    switchPromoSyarat.isChecked = true
                    promoSyaratChange= 1
                    if(promoSyaratChange != promoSyarat){
                        upPromoPS = 1
                    } else {
                        upPromoPS = 0
                    }
                    enableBtnSimpan()
                    Toast.makeText(this, "Promo Crazy Price Active", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.cardDiskon -> {

                if(promoPersenChange == 1){
                    switchDiskon.isChecked = false
                    promoPersenChange = 0
                    if(promoPersenChange != promoPersen){
                        upPromoPP = 1
                    } else {
                        upPromoPP = 0
                    }
                    enableBtnSimpan()
                    formDiskon.collapse()
                    Toast.makeText(this, "Discount Menu Disable", Toast.LENGTH_SHORT).show()
                } else {
                    switchDiskon.isChecked = true
                    promoPersenChange = 1
                    if(promoPersenChange != promoPersen){
                        upPromoPP = 1
                    } else {
                        upPromoPP = 0
                    }
                    enableBtnSimpan()
                    formDiskon.expand()
                    Toast.makeText(this, "Discount Menu Active", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.cardTambahDiskon -> {

                if(promoTambahChange == 1){
                    switchTambahDiskon.isChecked = false
                    promoTambahChange = 0
                    if(promoTambahChange != promoTambah){
                        upPromoPT = 1
                    } else {
                        upPromoPT = 0
                    }
                    enableBtnSimpan()
                    formTambahDiskon.collapse()
                    Toast.makeText(this, "Adding Discount Menu Disable", Toast.LENGTH_SHORT).show()
                } else {
                    switchTambahDiskon.isChecked = true
                    promoTambahChange = 1
                    if(promoTambahChange != promoTambah){
                        upPromoPT = 1
                    } else {
                        upPromoPT = 0
                    }
                    enableBtnSimpan()
                    formTambahDiskon.expand()
                    Toast.makeText(this, "Adding Discount Menu Active", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.cardPromoSyaratOn -> {

                if(promoSyaratChangeOn == 1){
                    switchPromoSyaratOn.isChecked = false
                    promoSyaratChangeOn = 0
                    if(promoSyaratChangeOn != promoSyaratOn){
                        upPromoPSOn = 1
                    } else {
                        upPromoPSOn = 0
                    }
                    enableBtnSimpan()
                    Toast.makeText(this, "Promo Crazy Price Disable", Toast.LENGTH_SHORT).show()
                } else {
                    switchPromoSyaratOn.isChecked = true
                    promoSyaratChangeOn = 1
                    if(promoSyaratChangeOn != promoSyaratOn){
                        upPromoPSOn = 1
                    } else {
                        upPromoPSOn = 0
                    }
                    enableBtnSimpan()
                    Toast.makeText(this, "Promo Crazy Price Active", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.cardDiskonOn -> {

                if(promoPersenChangeOn == 1){
                    switchDiskonOn.isChecked = false
                    promoPersenChangeOn = 0
                    if(promoPersenChangeOn != promoPersenOn){
                        upPromoPPOn = 1
                    } else {
                        upPromoPPOn = 0
                    }
                    enableBtnSimpan()
                    formDiskonOn.collapse()
                    Toast.makeText(this, "Discount Menu Disable", Toast.LENGTH_SHORT).show()
                } else {
                    switchDiskonOn.isChecked = true
                    promoPersenChangeOn = 1
                    if(promoPersenChangeOn != promoPersenOn){
                        upPromoPPOn = 1
                    } else {
                        upPromoPPOn = 0
                    }
                    enableBtnSimpan()
                    formDiskonOn.expand()
                    Toast.makeText(this, "Discount Menu Active", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.cardTambahDiskonOn -> {

                if(promoTambahChangeOn == 1){
                    switchTambahDiskonOn.isChecked = false
                    promoTambahChangeOn = 0
                    if(promoTambahChangeOn != promoTambahOn){
                        upPromoPTOn = 1
                    } else {
                        upPromoPTOn = 0
                    }
                    enableBtnSimpan()
                    formTambahDiskonOn.collapse()
                    Toast.makeText(this, "Adding Discount Menu Disable", Toast.LENGTH_SHORT).show()
                } else {
                    switchTambahDiskonOn.isChecked = true
                    promoTambahChangeOn = 1
                    if(promoTambahChangeOn != promoTambahOn){
                        upPromoPTOn = 1
                    } else {
                        upPromoPTOn = 0
                    }
                    enableBtnSimpan()
                    formTambahDiskonOn.expand()
                    Toast.makeText(this, "Adding Discount Menu Active", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.simpanMenu -> {

                if(promoPersenChange == 0 && promoTambahChange == 0 && promoPersenChangeOn == 0 && promoTambahChangeOn == 0){
                    showProgressDialog()
                    updateDataMenu()
                } else {
                    var diskon = promoPersenChange
                    var tambah = promoTambahChange
                    var minTambah = promoTambahChange
                    var diskonOn = promoPersenChangeOn
                    var tambahOn = promoTambahChangeOn
                    var minTambahOn = promoTambahChangeOn
                    if(promoPersenChange == 1){
                        if(!besarDiskon.text.toString().equals("") && besarDiskon.text.toString().toInt() > 0){
                            diskon = 0
                        } else {
                            tiBesarDiskon.isErrorEnabled = true
                            tiBesarDiskon.error = "Besaran diskon tidak boleh kosong!"
                        }
                    }

                    if(promoTambahChange == 1){
                        if(!besarTambahDiskon.text.toString().equals("") && besarTambahDiskon.text.toString().toInt() > 0){
                            tambah = 0
                        } else {
                            tiBesarTambahDiskon.isErrorEnabled = true
                            tiBesarTambahDiskon.error = "Besaran tambahan diskon tidak boleh kosong!"
                        }
                    }

                    if(promoTambahChange == 1){
                        if(!besarMinimalBeli.text.toString().equals("") && besarMinimalBeli.text.toString().toInt() > 1){
                            minTambah = 0
                        } else {
                            tiMinimalBeli.isErrorEnabled = true
                            tiMinimalBeli.error = "Minimal 2 porsi!"
                        }
                    }

                    if(promoPersenChangeOn == 1){
                        if(!besarDiskonOn.text.toString().equals("") && besarDiskonOn.text.toString().toInt() > 0){
                            diskonOn = 0
                        } else {
                            tiBesarDiskonOn.isErrorEnabled = true
                            tiBesarDiskonOn.error = "Besaran diskon tidak boleh kosong!"
                        }
                    }

                    if(promoTambahChangeOn == 1){
                        if(!besarTambahDiskonOn.text.toString().equals("") && besarTambahDiskonOn.text.toString().toInt() > 0){
                            tambahOn = 0
                        } else {
                            tiBesarTambahDiskonOn.isErrorEnabled = true
                            tiBesarTambahDiskonOn.error = "Besaran tambahan diskon tidak boleh kosong!"
                        }
                    }

                    if(promoTambahChangeOn == 1){
                        if(!besarMinimalBeliOn.text.toString().equals("") && besarMinimalBeliOn.text.toString().toInt() > 1){
                            minTambahOn = 0
                        } else {
                            tiMinimalBeliOn.isErrorEnabled = true
                            tiMinimalBeliOn.error = "Minimal 2 porsi!"
                        }
                    }

                    if(diskon == 0 && tambah == 0 && minTambah == 0 && diskonOn == 0 && tambahOn == 0 && minTambahOn == 0){
                        showProgressDialog()
                        updateDataMenu()
                    }

                }

            }

        }

    }

    private fun chooseImage(){

        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQ)

    }

    private fun updateDataMenu(){

        if(!edNamaMenu.text.toString().equals("") && !edHargaMenu.text.toString().equals("")){

            namaMenu = edNamaMenu.text.toString().trim()
            hargaMenu = edHargaMenu.text.toString().toInt()
            statusMenu = statusMenuChange
            promoSyarat = promoSyaratChange
            promoPersen = promoPersenChange
            promoTambah = promoTambahChange
            var persen = 0
            var tambah = 0
            var min = 0
            if(promoPersen == 1){
                persen = besarDiskon.text.toString().toInt()
            }
            if(promoTambah == 1){
                tambah = besarTambahDiskon.text.toString().toInt()
                min = besarMinimalBeli.text.toString().toInt()
            }

            hargaMenuOn = edHargaMenuOn.text.toString().toInt()
            promoSyaratOn = promoSyaratChangeOn
            promoPersenOn = promoPersenChangeOn
            promoTambahOn = promoTambahChangeOn
            var persenOn = 0
            var tambahOn = 0
            var minOn = 0
            if(promoPersenOn == 1){
                persenOn = besarDiskonOn.text.toString().toInt()
            }
            if(promoTambahOn == 1){
                tambahOn = besarTambahDiskonOn.text.toString().toInt()
                minOn = besarMinimalBeliOn.text.toString().toInt()
            }

            val data = ApiEndPoint.REGISTER_API.sendUpdateMenu(
                "sfYZONlALBQsBMU6dGzlJVWG17M6qx27", namaMenu, hargaMenu, statusMenu, idMenu, promoSyarat, persen, tambah, min,
                hargaMenuOn, promoSyaratOn, persenOn, tambahOn, minOn)
            data.enqueue(object: Callback<PublicResult> {

                override fun onResponse(call: Call<PublicResult>, response: Response<PublicResult>) {

                    if(response.isSuccessful){

                        val status = response.body()?.status
                        val message = response.body()?.message

                        if(status!!){

                            val data = hashMapOf(

                                "update" to randomString()

                            )

                            fbd.collection(namaCabang).document("menu")
                                .collection("list").document(ktgMenu.toString()).set(data)

                            Handler().postDelayed({
                                alert = 0
                                mdialog.dismiss()
                                onBackPressed()
                            }, 500)

                        } else {

                            Toast.makeText(this@EditMenuActivity, message, Toast.LENGTH_SHORT).show()
                            mdialog.dismiss()

                        }

                    } else {

                        Toast.makeText(this@EditMenuActivity, "Gagal!!", Toast.LENGTH_SHORT).show()
                        mdialog.dismiss()

                    }

                }

                override fun onFailure(call: Call<PublicResult>, t: Throwable) {
                    Toast.makeText(this@EditMenuActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                    mdialog.dismiss()
                    Log.e("Error Rest Api:", t.toString())
                }


            })

        }

    }

    private fun showProgressDialog(){

        mdialog = Dialog(this)
        mdialog.setCancelable(false)
        mdialog.setContentView(R.layout.progress_dialog)

        val caption = mdialog.findViewById<TextView>(R.id.caption)
        caption.text = "Memperbaharui..."

        mdialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mdialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        mdialog.show()

    }

    private fun randomString() : String {

        val char = "ABCDEF012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray()
        val string = StringBuilder()
        val random = Random()
        for (i in 0..3) {
            val hasil = char[random.nextInt(char.size)]
            string.append(hasil)
        }

        return string.toString()

    }

    private fun enableBtnSimpan(){

        if(upNama == 0 && upKtg == 0 && upHrg == 0 && upHrgOn == 0 && upSts == 0 && upPromoPS == 0 && upPromoPP == 0 &&
            upPromoPT == 0 && upPromoPSOn == 0 && upPromoPPOn == 0 && upPromoPTOn == 0){

            simpanMenu.alpha = 0.5f
            simpanMenu.isEnabled = false
            alert = 0

        } else {

            simpanMenu.alpha = 1f
            simpanMenu.isEnabled = true
            alert = 1

        }

    }

    private fun startVibrate(){

        val vib = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vib.vibrate(500)
        }

    }

    private fun showPopupWarning() {

        mdialog = Dialog(this)
        mdialog.setCancelable(true)
        mdialog.setContentView(R.layout.alert_edit_menu)

        val no : Button = mdialog.findViewById(R.id.no)
        val yes : Button = mdialog.findViewById(R.id.yes)

        no.setOnClickListener {

            super.onBackPressed()

        }

        yes.setOnClickListener {

            updateDataMenu()
            mdialog.dismiss()

        }

        mdialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mdialog.getWindow()!!.getAttributes().windowAnimations = R.style.DialogAnimation
        mdialog.show()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                    chooseImage()
                }
                else{
                    Toast.makeText(this, "Permission denied!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){

            if(requestCode == REQ){

                URI = data?.getData() as Uri
                imgMenu.setImageURI(URI)

                //val path = getPathFromURI(URI!!)
                //Toast.makeText(this, path, Toast.LENGTH_SHORT).show()

            }

        }

    }

    private fun getPathFromURI(contentUri: Uri): String? {

        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            android.R.id.home -> {

                onBackPressed()

            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(alert == 1){

            startVibrate()
            showPopupWarning()

        } else {

            super.onBackPressed()

        }
    }

}
