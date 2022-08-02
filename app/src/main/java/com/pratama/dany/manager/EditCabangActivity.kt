package com.pratama.dany.manager

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.PublicResult
import kotlinx.android.synthetic.main.activity_edit_cabang.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditCabangActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mdialog: Dialog
    private val fbd : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var idCabang = 0
    private var namaCabang = ""
    private var alamatCabang = ""
    private var teleponCabang = ""
    private var potonganGojek = 0
    private var potonganGrab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cabang)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Edit Cabang"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mdialog = Dialog(this)

        idCabang = intent.getIntExtra("id", 0)
        namaCabang = intent.getStringExtra("nama")
        alamatCabang = intent.getStringExtra("alamat")
        teleponCabang = intent.getStringExtra("telepon")
        potonganGojek = intent.getIntExtra("gojek", 0)
        potonganGrab = intent.getIntExtra("grab", 0)

        namaCabangDis.setText(namaCabang)
        alamatCabangDis.setText(alamatCabang)
        teleponCabangDis.setText(teleponCabang)
        potongGojek.setText(potonganGojek.toString())
        potongGrab.setText(potonganGrab.toString())

        simpanProfilCabang.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when(p0?.id){

            R.id.simpanProfilCabang -> {

                if(!namaCabangDis.text.toString().equals("") && !alamatCabangDis.text.toString().equals("") &&
                    !teleponCabangDis.text.toString().equals("") && !potongGojek.text.toString().equals("") &&
                    !potongGrab.text.toString().equals("")){

                    namaCabang = namaCabangDis.text.toString()
                    teleponCabang = teleponCabangDis.text.toString()
                    alamatCabang = alamatCabangDis.text.toString()
                    potonganGojek = potongGojek.text.toString().toInt()
                    potonganGrab = potongGrab.text.toString().toInt()

                    showProgressDialog()
                    saveToDatabase()

                } else {

                    if(namaCabangDis.text.toString().equals("")){
                        tiNamaCabang.error = "Masukkan nama cabang!"
                    }

                    if(alamatCabangDis.text.toString().equals("")){
                        tiAlamatCabang.error = "Masukkan alamat cabang!"
                    }

                    if(teleponCabangDis.text.toString().equals("")){
                        tiTeleponCabang.error = "Masukkan telepon cabang!"
                    }

                    if(namaCabangDis.text.toString().equals("")){
                        tiPotongGojek.error = "Masukkan potongan mitra gojek!"
                    }

                    if(namaCabangDis.text.toString().equals("")){
                        tiPotongGrab.error = "Masukkan potongan mitra grab!"
                    }

                }

            }

        }

    }

    private fun saveToDatabase(){

        val update = ApiEndPoint.REGISTER_API.updateDataCabang(ApiEndPoint.KEY_ACCESS, idCabang,
            namaCabang, alamatCabang, teleponCabang, potonganGojek, potonganGrab)
        update.enqueue(object: Callback<PublicResult> {

            override fun onResponse(call: Call<PublicResult>, response: Response<PublicResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message

                    if(status!!){

                        val data = hashMapOf(

                            "update" to randomString()

                        )

                        fbd.collection(namaCabang).document("profil").set(data)

                        Handler().postDelayed({
                            mdialog.dismiss()
                            onBackPressed()
                        }, 500)

                    } else {

                        Toast.makeText(this@EditCabangActivity, message, Toast.LENGTH_SHORT).show()
                        mdialog.dismiss()

                    }

                } else {

                    Toast.makeText(this@EditCabangActivity, "Gagal!!", Toast.LENGTH_SHORT).show()
                    mdialog.dismiss()

                }

            }

            override fun onFailure(call: Call<PublicResult>, t: Throwable) {
                Toast.makeText(this@EditCabangActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                mdialog.dismiss()
                Log.e("Error Rest Api:", t.toString())
            }


        })

    }

    private fun showProgressDialog(){

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            android.R.id.home -> {

                onBackPressed()

            }

        }

        return super.onOptionsItemSelected(item)
    }

}
