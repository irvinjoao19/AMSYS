package com.amsys.alphamanfacturas.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.amsys.alphamanfacturas.ui.fragments.*

abstract class ViewPagerAdapter {

    class FormAvisos(
        fm: FragmentManager,
        private val numberOfTabs: Int, var id: Int, var tipo: Int, var token: String, var user: Int
    ) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> Aviso1Fragment.newInstance(id, tipo, user)
                1 -> Aviso2Fragment.newInstance(id, token, user)
                2 -> Aviso3Fragment.newInstance(id, tipo)
                3 -> when (tipo) {
                    1 -> Aviso6Fragment.newInstance(id, user)
                    else -> Aviso4Fragment.newInstance(id, token, user, tipo)
                }
                4 -> Aviso6Fragment.newInstance(id, user)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }

    class FormInspeccion(
        fm: FragmentManager,
        private val numberOfTabs: Int, var id: Int, var token: String, var user: Int
    ) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> Inspeccion1Fragment.newInstance(id, user)
                1 -> Inspeccion2Fragment.newInstance(id, user)
                2 -> Inspeccion3Fragment.newInstance(id, user)
                3 -> Inspeccion4Fragment.newInstance(id, user)
                4 -> Inspeccion5Fragment.newInstance(id, user)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }
}