package com.pratama.dany.manager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.KategoriResult
import com.pratama.dany.manager.model.KategoriModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : AppCompatActivity() {

    private var idCabang = 0
    private var namaCabang = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var db: DatabaseHelper
    private val listKategori = ArrayList<KategoriModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        db = DatabaseHelper(this)
        getkategori()

        sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        val LOG = sharedPreferences.getBoolean("LOG", false)
        idCabang = sharedPreferences.getInt("cabang", 0)
        namaCabang = sharedPreferences.getString("namacabang", "").toString()

        if(LOG){

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()

        } else {

            Handler().postDelayed({

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()

            }, 1500)

        }

    }

    private fun getkategori(){

        val ktg = ApiEndPoint.REGISTER_API.getKategori("sfYZONlALBQsBMU6dGzlJVWG17M6qx27")
        ktg.enqueue(object: Callback<KategoriResult> {

            override fun onResponse(call: Call<KategoriResult>, response: Response<KategoriResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message
                    val result = response.body()?.result

                    if(status!!){

                        for (i in result!!.indices){
                            val insert = db.insertDataKategori(result[i].id, result[i].kategori)
                            if(!insert){
                                db.updateDataKategori(result[i].id, result[i].kategori)
                            }
                        }

                    } else {

                        Toast.makeText(this@SplashActivity, message, Toast.LENGTH_SHORT).show()

                    }

                } else {

                    Toast.makeText(this@SplashActivity, "Gagal!!", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<KategoriResult>, t: Throwable) {
                Toast.makeText(this@SplashActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                Log.e("Error Rest Api:", t.toString())
            }

        })

    }

}
