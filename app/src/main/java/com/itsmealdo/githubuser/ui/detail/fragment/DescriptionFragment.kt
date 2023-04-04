package com.itsmealdo.githubuser.ui.detail.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.itsmealdo.githubuser.R
import com.itsmealdo.githubuser.data.model.UserDetailResponse
import com.itsmealdo.githubuser.helper.Result
import com.itsmealdo.githubuser.databinding.FragmentDescriptionBinding
import com.itsmealdo.githubuser.ui.detail.DetailActivity
import com.itsmealdo.githubuser.ui.modelview.DetailViewModel
import com.itsmealdo.githubuser.ui.modelview.ViewModelFactory

class DescriptionFragment : Fragment() {

    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

    private val TAG = "Description Fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: DetailViewModel by viewModels {
            factory
        }

        val username = arguments?.getString(DetailActivity.EXTRA_USERNAME)

        binding.btnShare.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "https://github.com/$username")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share To:"))
        }

        if (username != null) {
            viewModel.getUser(username).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            binding.btnFav.isChecked = result.data.favorite
                            setUserDescription(result.data)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Log.e(TAG, result.error)
                            Toast.makeText(
                                context, "Error Occurred, please check your connection.", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            binding.btnFav.setOnClickListener {
                if (!binding.btnFav.isChecked) {
                    viewModel.removeFromFavorite(username)
                    Toast.makeText(context, "User Removed Successfully From Favorite", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addToFavorite(username)
                    Toast.makeText(context, "User Added Successfully to Favorites", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }

    override fun onResume(){
        super.onResume()
        binding.root.requestLayout()
    }

    private fun setUserDescription(user: UserDetailResponse) {
        binding.apply {
            tvName.text = user.name ?: "-"
            tvUsernameValue.text = user.login
            tvRepositoryValue.text = user.publicRepos.toString()
            tvFollowersValue.text = user.followers.toString()
            tvFollowingValue.text = user.following.toString()

        }
        Glide.with(this)
            .load(user.avatarUrl)
            .centerCrop()
            .placeholder(R.drawable.githublogo)
            .into(binding.ivUser)
        binding.apply {
            tvName.visibility = View.VISIBLE
            tvUsernameValue.visibility = View.VISIBLE
            ivUser.visibility = View.VISIBLE
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
