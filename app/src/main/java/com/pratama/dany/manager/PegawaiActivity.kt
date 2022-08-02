package com.pratama.dany.manager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.pratama.dany.manager.adapter.PegawaiAdapter
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.PegawaiResult
import com.pratama.dany.manager.model.PegawaiModel
import kotlinx.android.synthetic.main.activity_pegawai.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PegawaiActivity : AppCompatActivity(), View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private val listPegawai = ArrayList<PegawaiModel>()
    private var idCabang = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pegawai)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Daftar Pegawai"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        idCabang = sharedPreferences.getInt("cabang", 0)

        rvPegawai.layoutManager = LinearLayoutManager(this)
        rvPegawai.setHasFixedSize(true)
        rvPegawai.visibility = View.INVISIBLE

        swipeRefresh.setColorScheme(R.color.Blue600, R.color.Green600, R.color.Orange600, R.color.Red600)
        swipeRefresh.setOnRefreshListener(this)

        Handler().postDelayed({
            getDataFromDb()
        }, 300)

        tambahPgw.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when(p0?.id){



        }

    }

    override fun onRefresh() {
        getDataFromDb()
    }

    private fun getDataFromDb(){

        val data = ApiEndPoint.REGISTER_API.getPegawai("sfYZONlALBQsBMU6dGzlJVWG17M6qx27", idCabang)
        data.enqueue(object: Callback<PegawaiResult> {

            override fun onResponse(call: Call<PegawaiResult>, response: Response<PegawaiResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val result = response.body()?.result

                    if(status!!){

                        listPegawai.clear()
                        val listTampung = ArrayList<PegawaiModel>()

                        for (i in result!!.indices){

                            val model = PegawaiModel(result[i].idPgw, result[i].nama, result[i].email,
                                result[i].alamat, result[i].telepon, result[i].akses, result[i].status)
                            listTampung.add(model)

                        }

                        listPegawai.addAll(listTampung)
                        val adapter = PegawaiAdapter(this@PegawaiActivity, listPegawai)
                        rvPegawai.adapter = adapter
                        swipeRefresh.isRefreshing = false

                        Handler().postDelayed({

                            shimmerPegawai.stopShimmer()
                            shimmerPegawai.visibility = View.GONE
                            rvPegawai.visibility = View.VISIBLE

                        }, 500)

                    } else {

                        Toast.makeText(this@PegawaiActivity, call.toString(), Toast.LENGTH_SHORT).show()
                        swipeRefresh.isRefreshing = false

                    }

                } else {

                    Toast.makeText(this@PegawaiActivity, "Gagal!!", Toast.LENGTH_SHORT).show()
                    swipeRefresh.isRefreshing = false

                }

            }

            override fun onFailure(call: Call<PegawaiResult>, t: Throwable) {

                Toast.makeText(this@PegawaiActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                swipeRefresh.isRefreshing = false
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
