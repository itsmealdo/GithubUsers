package com.itsmealdo.githubuser.ui.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.itsmealdo.githubuser.data.repo.UserRepository
import kotlinx.coroutines.launch

class MainModelView(private val  userRepository: UserRepository): ViewModel() {
    fun getUsers() = userRepository.getUsers()

    fun getFavoriteUsers() = userRepository.getFavoriteUsers()

    fun getThemeSetting(): LiveData<Boolean> = userRepository.getThemeSetting().asLiveData()

    fun saveThemeSetting(newSetting: Boolean) {
        viewModelScope.launch {
            userRepository.saveThemeSetting(newSetting)
        }
    }

}