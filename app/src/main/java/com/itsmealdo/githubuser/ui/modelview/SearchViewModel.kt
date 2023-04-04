package com.itsmealdo.githubuser.ui.modelview

import androidx.lifecycle.ViewModel
import com.itsmealdo.githubuser.data.repo.UserRepository

class SearchViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getSearchUsers(query: String) = userRepository.getSearchUsers(query)
}