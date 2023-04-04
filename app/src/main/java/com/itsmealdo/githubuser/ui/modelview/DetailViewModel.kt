package com.itsmealdo.githubuser.ui.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itsmealdo.githubuser.data.repo.UserRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getUser(username: String) = userRepository.getUser(username)

    fun getUserFollowing(username: String) = userRepository.getUserFollowing(username)

    fun getUserFollowers(username: String) = userRepository.getUserFollowers(username)

    fun addToFavorite(username: String) {
        viewModelScope.launch {
            userRepository.setUserFavorite(username, true)
        }
    }
    fun removeFromFavorite(username: String) {
        viewModelScope.launch {
            userRepository.setUserFavorite(username, false)
        }
    }
}