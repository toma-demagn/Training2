package com.example.training2.ui.main

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.training2.MainFragment
import com.example.training2.R
import com.example.training2.ui.main.placeholder.PlaceholderContent
import kotlin.random.Random

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager,
                           var mf: MainFragment, var ifr: ItemFragment
) :
    FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        if (position == 0)
            return mf
        else
            return ifr
            //fragment.theAdapter.addItem()
        //fragment.theList = ArrayList(PlaceholderContent.ITEMS.subList(0, bundle.getInt("size")))


    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}