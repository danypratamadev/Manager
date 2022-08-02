package com.pratama.dany.manager

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.pratama.dany.manager.adapter.TransaksiAdapter
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.DashResult
import com.pratama.dany.manager.model.TransaksiModel
import com.pratama.dany.manager.api.model.TransaksiResult
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    View.OnClickListener {

    private val fbd : FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var sharedPreferences: SharedPreferences
    private var versiApp = ""
    private var idCabang = 0
    private var namaCabang = ""
    private var alamatCabang = ""

    private val locale = Locale("in", "ID")
    private val numberFormat = NumberFormat.getCurrencyInstance(locale)

    private val listTransaksi = ArrayList<TransaksiModel>()
    private val calendar = Calendar.getInstance()
    private val formatTglFull = SimpleDateFormat("dd MMMM yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Manager Kuah Panas"

        sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        idCabang = sharedPreferences.getInt("cabang", 0)
        namaCabang = sharedPreferences.getString("namacabang", "").toString()
        alamatCabang = sharedPreferences.getString("alamatcabang", "").toString()

        namaCabangDis.text = namaCabang
        alamatCabangDis.text = alamatCabang

        val tglFull = formatTglFull.format(calendar.time)
        tglSekarang?.text = "Hari ini, $tglFull"
        rvTransaksi?.layoutManager = LinearLayoutManager(this)
        rvTransaksi?.setHasFixedSize(true)
        emptyTrans?.visibility = View.INVISIBLE
        rvTransaksi?.visibility = View.INVISIBLE
        swipeRefresh.setColorScheme(R.color.Blue600, R.color.Green600, R.color.Orange600, R.color.Red600)
        swipeRefresh.setOnRefreshListener(this)

        fbd.collection(namaCabang).document("transaksi").addSnapshotListener { snapshot, exception ->

            if(exception != null){
                Log.w("Dash", "listen:error", exception)
                return@addSnapshotListener
            }

            getDataDash()
            getDataTransaksi()

        }

        try{
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            versiApp = packageInfo.versionName
            versiAppDis.text = "Manager Versi $versiApp"
        } catch (e: PackageManager.NameNotFoundException){
            Log.i("VERSI", e.toString())
        }

        kelolaMenu.setOnClickListener(this)
        kelolaPegawai.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {

        when(p0?.id){

            R.id.kelolaMenu -> {

                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)

            }

            R.id.kelolaPegawai -> {

                val intent = Intent(this, PegawaiActivity::class.java)
                startActivity(intent)

            }

        }

    }

    override fun onRefresh() {

        getDataDash()
        getDataTransaksi()

    }

    private fun getDataDash(){

        val dash = ApiEndPoint.REGISTER_API.getDashToday("sfYZONlALBQsBMU6dGzlJVWG17M6qx27", idCabang)
        dash.enqueue(object: Callback<DashResult> {

            override fun onResponse(call: Call<DashResult>, response: Response<DashResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message
                    val transakasi = response.body()?.result?.transaksi
                    val item = response.body()?.result?.item
                    val total = response.body()?.result?.total

                    if(status!!){

                        animateUpdate(transakasi!!, jmlTrans, 0)
                        animateUpdate(item!!, jmlItem, 0)
                        animateUpdate(total!!, jmlPendapatan, 1)

                    } else {

                        Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()

                    }

                } else {

                    Toast.makeText(this@HomeActivity, "Gagal!!", Toast.LENGTH_SHORT).show()

                }

            }

            override fun onFailure(call: Call<DashResult>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                Log.e("Error Rest Api:", t.toString())
            }

        })

    }

    private fun getDataTransaksi(){

        val data = ApiEndPoint.REGISTER_API.getTransaksi(
            "sfYZONlALBQsBMU6dGzlJVWG17M6qx27", idCabang
        )

        data.enqueue(object: Callback<TransaksiResult> {

            override fun onResponse(call: Call<TransaksiResult>, response: Response<TransaksiResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val message = response.body()?.message
                    val result = response.body()?.result

                    if(status!!){

                        listTransaksi.clear()
                        val listTampung = ArrayList<TransaksiModel>()

                        for (i in result!!.indices){

                            val model = TransaksiModel(result[i].id, result[i].member, result[i].meja,
                                result[i].jam, result[i].total)
                            listTampung.add(model)

                        }

                        listTransaksi.addAll(listTampung)
                        val adapter = TransaksiAdapter(this@HomeActivity, listTransaksi)
                        rvTransaksi?.adapter = adapter
                        swipeRefresh.isRefreshing = false
                        Handler().postDelayed({

                            shimmerTransaksi.stopShimmer()
                            shimmerTransaksi.visibility = View.INVISIBLE
                            emptyTrans.visibility = View.INVISIBLE
                            rvTransaksi.visibility = View.VISIBLE

                        }, 1500)


                    } else {

                        Toast.makeText(this@HomeActivity, message, Toast.LENGTH_SHORT).show()
                        swipeRefresh.isRefreshing = false
                        Handler().postDelayed({

                            shimmerTransaksi.stopShimmer()
                            shimmerTransaksi.visibility = View.INVISIBLE
                            emptyTrans.visibility = View.VISIBLE

                        }, 300)

                    }

                } else {

                    Toast.makeText(this@HomeActivity, "Gagal!!", Toast.LENGTH_SHORT).show()
                    swipeRefresh.isRefreshing = false
                    Handler().postDelayed({

                        shimmerTransaksi.stopShimmer()
                        shimmerTransaksi.visibility = View.INVISIBLE
                        emptyTrans.visibility = View.VISIBLE

                    }, 300)

                }

            }

            override fun onFailure(call: Call<TransaksiResult>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                swipeRefresh.isRefreshing = false
                Handler().postDelayed({

                    shimmerTransaksi.stopShimmer()
                    shimmerTransaksi.visibility = View.INVISIBLE
                    emptyTrans.visibility = View.VISIBLE

                }, 300)
                Log.e("Error Rest Api:", t.toString())
            }


        })

    }

    private fun animateUpdate(value: Int, view: TextView, op: Int){

        val animator = ValueAnimator.ofInt(value)
        animator.setDuration(1000)
        animator.addUpdateListener(object: ValueAnimator.AnimatorUpdateListener {

            override fun onAnimationUpdate(p0: ValueAnimator?) {

                if(op == 0){

                    view.text = p0?.animatedValue.toString()

                } else {

                    view.text = numberFormat.format(p0?.animatedValue)

                }

            }

        })
        animator.start();

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.option_manager, menu)

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

            R.id.settings -> {

                val intent = Intent(this, PengaturanActivity::class.java)
                startActivity(intent)
                return true

            }

            R.id.signout -> {

                val sharedPreferences = getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("LOG", false)
                editor.apply()

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
                return true

            }

            else -> {

                return super.onOptionsItemSelected(item)

            }

        }

    }

}
