package com.itsmealdo.githubuser.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsmealdo.githubuser.databinding.FragmentHomeBinding
import com.itsmealdo.githubuser.ui.adapters.ListGitUserAdapter
import com.itsmealdo.githubuser.helper.Result
import com.itsmealdo.githubuser.ui.detail.DetailActivity
import com.itsmealdo.githubuser.ui.modelview.MainModelView
import com.itsmealdo.githubuser.ui.modelview.ViewModelFactory
import com.itsmealdo.githubuser.ui.search.SearchActivity

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: ListGitUserAdapter
    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: MainModelView by viewModels {
            factory
        }

        viewModel.getThemeSetting().observe(viewLifecycleOwner) { isNightMode ->
            if (isNightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.btnAppTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.btnAppTheme.isChecked = false
            }
        }

        binding.btnAppTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        setupRecyclerView()

        binding.btnSearch.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }

        userAdapter.setOnItemClickListener { UserDetailResponse ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, UserDetailResponse.login)
            startActivity(intent)
        }

        viewModel.getUsers().observe(viewLifecycleOwner) { result ->
            if (result != null)
            {
                when (result)
                {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val users = result.data
                        userAdapter.differ.submitList(users)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Log.e(TAG, result.error)
                        Toast.makeText(
                            context,
                            "Error Occurred, Please Check Your Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        userAdapter = ListGitUserAdapter()
        binding.rvShowUserList.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
