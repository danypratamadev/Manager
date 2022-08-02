package com.pratama.dany.manager.adapter

import android.app.Dialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.facebook.shimmer.ShimmerFrameLayout
import com.pratama.dany.manager.MenuFragment
import com.pratama.dany.manager.model.KategoriModel

class PageFragmentAdapter(fragmentManager: FragmentManager,
                          val listKategori: ArrayList<KategoriModel>, val shimmer: ShimmerFrameLayout, val vPager: ViewPager)
    : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {

        var fragment: Fragment? = null

        for(i in listKategori.indices){

            if(i == position){

                fragment = MenuFragment(listKategori[i].idKtg, i, listKategori.size, shimmer, vPager)

            }

        }

        return fragment!!

    }

    override fun getCount(): Int {

        return listKategori.size

    }

    override fun getPageTitle(position: Int): CharSequence? {

        var title = ""

        for(i in listKategori.indices){

            if(i == position){

                title = listKategori[i].namaKtg

            }

        }

        return title

    }

}