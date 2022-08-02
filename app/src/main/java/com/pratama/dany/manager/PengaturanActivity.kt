package com.pratama.dany.manager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.CabangResult
import kotlinx.android.synthetic.main.activity_pengaturan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaturanActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPreferences: SharedPreferences
    private val fbd : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var idCabang = 0
    private var namaCabang = ""
    private var alamatCabang = ""
    private var teleponCabang = ""
    private var potonganGojek = 0
    private var potonganGrab = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Pengaturan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        idCabang = sharedPreferences.getInt("cabang", 0)
        namaCabang = sharedPreferences.getString("namacabang", "").toString()
        alamatCabang = sharedPreferences.getString("alamatcabang", "").toString()

        namaCabangDis.text = namaCabang
        alamatCabangDis.text = alamatCabang

        fbd.collection(namaCabang).document("profil").addSnapshotListener { snapshot, exception ->

            if(exception != null){
                Log.w("Select Door", "listen:error", exception)
                return@addSnapshotListener
            }

            if(!snapshot?.getString("update").equals("")){
                getDataCabang()
            }

        }

        profilCabang.setOnClickListener(this)
        cardMitra.setOnClickListener(this)
        editProfileCabang.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when(p0?.id){

            R.id.profilCabang -> {

                val intent = Intent(this, EditCabangActivity::class.java)
                intent.putExtra("id", idCabang)
                intent.putExtra("nama", namaCabang)
                intent.putExtra("alamat", alamatCabang)
                intent.putExtra("telepon", teleponCabang)
                intent.putExtra("gojek", potonganGojek)
                intent.putExtra("grab", potonganGrab)
                startActivity(intent)

            }

            R.id.cardMitra -> {

                if(formMitra.isExpanded){
                    formMitra.collapse()
                } else {
                    formMitra.expand()
                }

            }

            R.id.editProfileCabang -> {

                val intent = Intent(this, EditCabangActivity::class.java)
                intent.putExtra("id", idCabang)
                intent.putExtra("nama", namaCabang)
                intent.putExtra("alamat", alamatCabang)
                intent.putExtra("telepon", teleponCabang)
                intent.putExtra("gojek", potonganGojek)
                intent.putExtra("grab", potonganGrab)
                startActivity(intent)

            }

        }

    }

    private fun getDataCabang(){

        val cabang = ApiEndPoint.REGISTER_API.getDataCabang(ApiEndPoint.KEY_ACCESS, idCabang)
        cabang.enqueue(object: Callback<CabangResult> {

            override fun onResponse(call: Call<CabangResult>, response: Response<CabangResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message
                    val result = response.body()?.result

                    if(status!!){

                        namaCabang = result!!.namaCabang
                        alamatCabang = result.alamatCabang
                        teleponCabang = result.teleponCabang
                        potonganGojek = result.potonganGojek
                        potonganGrab = result.potonganGrab

                        namaCabangDis.text = namaCabang
                        alamatCabangDis.text = alamatCabang

                        if(teleponCabang.equals("")){
                            teleponCabangDis.text = "Tambahkan nomor telepon"
                        } else {
                            teleponCabangDis.text = teleponCabang
                        }

                        potongGojek.setText(potonganGojek.toString())
                        potongGrab.setText(potonganGrab.toString())

                    } else {

                        Toast.makeText(this@PengaturanActivity, message, Toast.LENGTH_SHORT).show()

                    }

                } else {

                    Toast.makeText(this@PengaturanActivity, "Gagal!!", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<CabangResult>, t: Throwable) {
                Toast.makeText(this@PengaturanActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                Log.e("Error Rest Api:", t.toString())
            }

        })

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
