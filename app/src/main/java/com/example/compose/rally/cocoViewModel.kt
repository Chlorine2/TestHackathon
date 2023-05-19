package com.example.compose.rally

import ApiCustomService.ApiServiceBuilder2.apiService
import AuthModel
import RegistryModel
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

sealed interface CocoState {
    object Success : CocoState
    object Error : CocoState
    object Loading : CocoState
}

class CocoViewModel() : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var cocoState: CocoState by mutableStateOf(CocoState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getMarsPhotos()
    }

    /**
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMarsPhotos() {
        viewModelScope.launch {
            cocoState = CocoState.Loading
            cocoState = try {
                val response = withContext(Dispatchers.IO) {
                    apiService.Authenticate(
                        AuthModel(
                            username = "1",
                            password = "1"
                        )
                    )
                }
                Log.d("tag", response.token)
                CocoState.Success
            } catch (e: IOException) {

                if(e.localizedMessage?.contains("Unable to resolve host") == true){
                    CocoState.Error
                }
                else {
                    CocoState.Success
                }

            } catch (e: HttpException) {

                if(e.localizedMessage?.contains("Unable to resolve host") == true){
                    CocoState.Error
                }
                else {
                    CocoState.Success
                }
            }
        }
    }
}