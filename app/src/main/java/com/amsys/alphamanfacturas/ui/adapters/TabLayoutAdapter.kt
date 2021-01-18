package com.amsys.alphamanfacturas.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.amsys.alphamanfacturas.ui.fragments.*

abstract class TabLayoutAdapter {

    class Form(fm: FragmentManager, private val numberOfTabs: Int, var id: Int)
        : FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> Aviso1Fragment.newInstance(id,"")
                1 -> Aviso2Fragment.newInstance(id,"")
                2 -> Aviso3Fragment.newInstance("","")
                3 -> Aviso4Fragment.newInstance("","")
                4 -> Aviso5Fragment.newInstance("","")
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }
}