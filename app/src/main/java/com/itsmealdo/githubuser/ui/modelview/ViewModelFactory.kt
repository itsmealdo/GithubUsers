package com.itsmealdo.githubuser.ui.modelview

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itsmealdo.githubuser.data.repo.UserRepository
import com.itsmealdo.githubuser.helper.Injection

class ViewModelFactory private constructor(private val userRepository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainModelView::class.java))
        {
            return MainModelView(userRepository) as T
        }
        else if (modelClass.isAssignableFrom(DetailViewModel::class.java))
        {
            return DetailViewModel(userRepository) as T
        }
        else if (modelClass.isAssignableFrom(SearchViewModel::class.java))
        {
            return SearchViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}