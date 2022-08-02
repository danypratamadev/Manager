package com.pratama.dany.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import com.pratama.dany.manager.adapter.PageFragmentAdapter
import com.pratama.dany.manager.model.KategoriModel
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private var listKategori = ArrayList<KategoriModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        setSupportActionBar(findViewById(R.id.theToolBar))
        supportActionBar?.title = "Kelola Menu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        db = DatabaseHelper(this)
        listKategori = db.selectDataKategori()
        menuViewPager?.visibility = View.INVISIBLE

        val pageFragmentAdapter = PageFragmentAdapter(supportFragmentManager, listKategori, shimmerMenu, menuViewPager)
        val limit = if (pageFragmentAdapter.count > 1) pageFragmentAdapter.count - 1 else 1

        Handler().postDelayed({

            menuViewPager?.offscreenPageLimit = limit
            menuViewPager?.adapter = pageFragmentAdapter
            tabKelolaMenu?.setupWithViewPager(menuViewPager)

        }, 300)

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
