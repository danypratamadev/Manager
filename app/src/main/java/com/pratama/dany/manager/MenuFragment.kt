package com.pratama.dany.manager

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.pratama.dany.manager.adapter.MenuAdapter
import com.pratama.dany.manager.api.ApiEndPoint
import com.pratama.dany.manager.api.model.MenuResult
import com.pratama.dany.manager.model.MenuModel
import kotlinx.android.synthetic.main.fragment_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment(val idKtg: Int, val index: Int, val size: Int, val shimmer: ShimmerFrameLayout,
                   val vPager: ViewPager) : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    private val listMenu = ArrayList<MenuModel>()
    private val fbd : FirebaseFirestore = FirebaseFirestore.getInstance()

    private var idCabang: Int = 0
    private var namaCabang: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context!!.getSharedPreferences("SIGN_IN", Context.MODE_PRIVATE)
        idCabang = sharedPreferences.getInt("cabang", 0)
        namaCabang = sharedPreferences.getString("namacabang", "").toString()

        val config = resources.configuration as Configuration
        if(config.smallestScreenWidthDp >= 600){
            rvMenu.layoutManager = GridLayoutManager(context, 3)
            rvMenu.setHasFixedSize(true)
        } else {
            rvMenu.layoutManager = LinearLayoutManager(context)
            rvMenu.setHasFixedSize(true)
        }

        fbd.collection(namaCabang).document("menu")
            .collection("list").document(idKtg.toString()).addSnapshotListener { snapshot, exception ->

                if(exception != null){
                    Log.w("Select Door", "listen:error", exception)
                    return@addSnapshotListener
                }

                if(!snapshot?.getString("update").equals("")){
                    getDataFromDb()
                }

            }

        swipeRefresh.setColorScheme(R.color.Blue600, R.color.Green600, R.color.Orange600, R.color.Red600)
        swipeRefresh.setOnRefreshListener(this)

    }

    override fun onRefresh() {
        getDataFromDb()
    }

    private fun getDataFromDb(){

        val data = ApiEndPoint.REGISTER_API.getMenuCabang("sfYZONlALBQsBMU6dGzlJVWG17M6qx27", idCabang, idKtg)
        data.enqueue(object: Callback<MenuResult> {

            override fun onResponse(call: Call<MenuResult>, response: Response<MenuResult>) {

                if(response.isSuccessful){

                    val status = response.body()?.status
                    val result = response.body()?.result

                    if(status!!){

                        listMenu.clear()
                        val listTampung = ArrayList<MenuModel>()

                        for (i in result!!.indices){

                            val model = MenuModel(result[i].id, result[i].kategori, result[i].menu, result[i].status,
                                result[i].gambar, result[i].harga, result[i].ps, result[i].pp, result[i].pp_plus, result[i].pp_min,
                                result[i].hargaOn, result[i].psOn, result[i].ppOn, result[i].pp_plusOn, result[i].pp_minOn)
                            listTampung.add(model)

                        }

                        listMenu.addAll(listTampung)
                        val adapter = MenuAdapter(context!!, listMenu)
                        rvMenu.adapter = adapter
                        swipeRefresh.isRefreshing = false

                        if(index == (size - 1)){
                            shimmer.stopShimmer()
                            shimmer.visibility = View.GONE
                            vPager.visibility = View.VISIBLE
                        }

                    } else {

                        Toast.makeText(context, call.toString(), Toast.LENGTH_SHORT).show()
                        swipeRefresh.isRefreshing = false

                    }

                } else {

                    Toast.makeText(context, "Gagal!!", Toast.LENGTH_SHORT).show()
                    swipeRefresh.isRefreshing = false

                }

            }

            override fun onFailure(call: Call<MenuResult>, t: Throwable) {

                Toast.makeText(context, "Periksa kembali jaringan internet!", Toast.LENGTH_SHORT).show()
                swipeRefresh.isRefreshing = false
                Log.e("Error Rest Api:", t.toString())

            }

        })

    }

}
