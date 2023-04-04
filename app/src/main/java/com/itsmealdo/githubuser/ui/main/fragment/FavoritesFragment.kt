package com.itsmealdo.githubuser.ui.main.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.itsmealdo.githubuser.databinding.FragmentFavoritesBinding
import com.itsmealdo.githubuser.ui.adapters.ListGitUserAdapter
import com.itsmealdo.githubuser.ui.detail.DetailActivity
import com.itsmealdo.githubuser.ui.modelview.MainModelView
import com.itsmealdo.githubuser.ui.modelview.ViewModelFactory

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var userAdapter: ListGitUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: MainModelView by viewModels {
            factory
        }

        setupRecyclerView()

        userAdapter.setOnItemClickListener { user ->
            val intent = Intent(activity, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
            startActivity(intent)
        }

        viewModel.getFavoriteUsers().observe(viewLifecycleOwner) { favoriteUsers ->
            if (favoriteUsers.isNotEmpty())
            {
                binding.ivNotFound.visibility = View.GONE
                binding.tvNotFound.visibility = View.GONE
            }
            else
            {
                binding.ivNotFound.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
            userAdapter.differ.submitList(favoriteUsers)
        }
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