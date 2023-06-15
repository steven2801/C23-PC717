package com.steven.foodqualitydetector.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.steven.foodqualitydetector.data.Food
import com.steven.foodqualitydetector.data.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DetailViewModel(private val repository: FoodRepository, private val id: String) : ViewModel() {
    private val _food = MutableStateFlow(
        repository.getFoodById(id)
    )
    val food: StateFlow<Food> get() = _food
}

class ViewModelFactory(private val repository: FoodRepository, private val id: String) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository, id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}