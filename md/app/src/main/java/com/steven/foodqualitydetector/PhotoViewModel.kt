/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.steven.foodqualitydetector

import android.content.Context
import android.net.Uri
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.steven.foodqualitydetector.network.Api
import com.steven.foodqualitydetector.network.createMultipartBodyPart
import com.steven.foodqualitydetector.ui.screens.home.UiState
import com.steven.foodqualitydetector.utils.PhotoUriManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody.Companion.toRequestBody

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class PhotoViewModel(
    private val photoUriManager: PhotoUriManager
) : ViewModel() {
    // Extension function to convert Uri to MultipartBody.Part
    private fun Uri.toMultipartBodyPart(context: Context): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(this)
        val mimeType = contentResolver.getType(this) ?: "image/*"
        val requestBody = inputStream?.readBytes()?.toRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", lastPathSegment, requestBody!!)
    }

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri
        get() = _imageUri.value

    private val _isLoading = mutableStateOf(false)
    val isLoading:
            State<Boolean>
        get() = _isLoading

    private fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    private val _description = mutableStateOf("")
    val description: State<String> get() = _description

    private val _id = mutableStateOf("")
    val id: State<String> get() = _id

    fun onDescriptionChange(desc: String) {
        _description.value = desc
    }

    suspend fun submit(scope: CoroutineScope, context: Context, firebaseToken: String) {
        setIsLoading(true)
        Log.i(TAG, firebaseToken)
        imageUri?.let {
            withContext(Dispatchers.IO) {
                try {
                    val title = _description.value.toRequestBody("text/plain".toMediaTypeOrNull());
                    val modifiedFirebaseToken = firebaseToken.toRequestBody("text/plain".toMediaTypeOrNull());
                    val response = Api.retrofitService.submitFood(title, modifiedFirebaseToken, it.toMultipartBodyPart(context))
                    Log.i(TAG, response.toString())
                    val uploadResponse = response.body()
                    _id.value = uploadResponse?.id.toString()
                } catch (e: Exception) {

                }
            }
        }
        setIsLoading(false)
        _description.value = ""
    }

    fun onImageCaptureResponse(uri: Uri) {
        _imageUri.value = uri
    }
    fun getNewImageCaptureUri() = photoUriManager.buildNewUri()
}

class PhotoViewModelFactory(
    private val photoUriManager: PhotoUriManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            return PhotoViewModel(photoUriManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
