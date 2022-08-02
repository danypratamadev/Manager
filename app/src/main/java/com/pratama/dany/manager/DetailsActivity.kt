package com.pratama.dany.manager

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.pratama.dany.manager.adapter.DetailAdapter
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.DetailResult
import com.pratama.dany.manager.model.DetailModel
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailsActivity : AppCompatActivity() {

    private var idPgw = ""
    private var namaCabang = ""
    private var namaPgw = ""
    private var idTrans = ""
    private var namaPlg = ""
    private var noMeja = 0
    private var jamTrans = ""
    private var total = 0

    private val listDetai = ArrayList<DetailModel>()
    private val locale = Locale("in", "ID")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Detail Pemesanan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        idPgw = sharedPreferences.getString("iduser", "").toString()
        namaCabang = sharedPreferences.getString("namacabang", "").toString()
        namaPgw = sharedPreferences.getString("nama", "").toString()

        idTrans = intent.getStringExtra("idTrans")
        namaPlg = intent.getStringExtra("namaPlg")
        noMeja = intent.getIntExtra("meja", 0)
        jamTrans = intent.getStringExtra("jam")
        total = intent.getIntExtra("total", 0)

        noTransaksi.text = idTrans
        namaPelanggan.text = namaPlg
        noMejaPelanggan.text = noMeja.toString()
        waktuTransaksi.text = jamTrans
        totalTransaksi.text = numberFormat.format(total)

        rvDetail?.layoutManager = LinearLayoutManager(this)
        rvDetail?.setHasFixedSize(true)
        rvDetail?.visibility = View.INVISIBLE

        Handler().postDelayed({

            getDataDetailTransaksi()

        }, 300)

    }

    private fun getDataDetailTransaksi(){

        val data = ApiEndPoint.REGISTER_API.getDetailTransaksi(
            "sfYZONlALBQsBMU6dGzlJVWG17M6qx27", idTrans
        )

        data.enqueue(object: Callback<DetailResult> {

            override fun onResponse(call: Call<DetailResult>, response: Response<DetailResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message
                    val result = response.body()?.result

                    if(status!!){

                        listDetai.clear()
                        val listTampung = ArrayList<DetailModel>()

                        for (i in result!!.indices){

                            val model = DetailModel(result[i].menu, result[i].jumlah, result[i].subtotal)
                            listTampung.add(model)

                        }

                        listDetai.addAll(listTampung)
                        val adapter = DetailAdapter(this@DetailsActivity, listDetai)
                        rvDetail?.adapter = adapter
                        Handler().postDelayed({

                            shimmerDetail.stopShimmer()
                            shimmerDetail.visibility = View.GONE
                            rvDetail.visibility = View.VISIBLE

                        }, 1500)

                    } else {

                        Toast.makeText(this@DetailsActivity, message, Toast.LENGTH_SHORT).show()

                    }

                } else {

                    Toast.makeText(this@DetailsActivity, "Gagal!!", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<DetailResult>, t: Throwable) {
                Toast.makeText(this@DetailsActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
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
