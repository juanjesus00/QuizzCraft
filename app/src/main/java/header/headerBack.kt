package header

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class headerBack : ViewModel(){
    val islogged = MutableLiveData<Boolean>()

    init {
        islogged.value = false
    }
    fun isLogged(){
        Log.d("HeaderBackViewModel", "showHeader called")
        islogged.value = true
    }

    fun closeSesion(){
        islogged.value = false
    }

}



