package com.kurilov.vezdecod.ui.FirstTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurilov.vezdecod.data.getMyRepo
import com.kurilov.vezdecod.MyApp
import com.vk.sdk.api.users.dto.UsersUserXtrCounters
import kotlinx.coroutines.launch

class FirstTaskFragmentViewModel : ViewModel() {

    private val myRepo = getMyRepo()

    val userInfo = myRepo.userInfo
    val userPhoto = myRepo.userPhoto
    val friendList = myRepo.friendList

    private val _toast = MutableLiveData<String?>()
    val toast: LiveData<String?>
        get() = _toast

    init {
        doTask()
    }

    private fun doTask() {
        viewModelScope.launch {
            try {
                myRepo.loadUserInfo()
            } catch (e : Throwable) {
                _toast.value = e.message
            }

        }
    }

    fun loadUserPhoto(user : UsersUserXtrCounters) {
        viewModelScope.launch {
            try {
                myRepo.loadUserPhoto(user)
            } catch (e : Throwable) {
                _toast.value = e.message
            }
        }
    }

    fun loadFriendsList(user : UsersUserXtrCounters) {
        viewModelScope.launch {
            try {
                myRepo.loadFriendsList(user)
            } catch (e : Throwable) {
                _toast.value = e.message
            }
        }
    }

    fun onToastShown() {
        _toast.value = null
    }

}