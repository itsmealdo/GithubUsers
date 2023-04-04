package com.itsmealdo.githubuser.ui.adapters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.itsmealdo.githubuser.ui.detail.fragment.DescriptionFragment
import com.itsmealdo.githubuser.ui.detail.fragment.FollowersFragment
import com.itsmealdo.githubuser.ui.detail.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity, data: Bundle): FragmentStateAdapter(activity) {

    private var fragmentBundle: Bundle

    init {
        fragmentBundle = data
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = DescriptionFragment()
            1 -> fragment = FollowingFragment()
            2 -> fragment = FollowersFragment()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getItemCount() = 3
}
