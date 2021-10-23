package com.kurilov.vezdecod.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kurilov.vezdecod.R
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.sdk.api.users.UsersService
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserXtrCounters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.drawable.Drawable
import com.vk.sdk.api.friends.FriendsService
import com.vk.sdk.api.friends.dto.FriendsGetFieldsResponse
import com.vk.sdk.api.friends.dto.FriendsGetOrder
import java.io.InputStream
import java.net.URL


class VKLoader(private val context: Context) {

    private val res =  context.resources

    private val _userInfo = MutableLiveData<UsersUserXtrCounters>()
    val userInfo : LiveData<UsersUserXtrCounters>
        get() = _userInfo

    private val _friendList : MutableList<String> = mutableListOf()

    private val _friendListLiveData = MutableLiveData<List<String>>()
    val friendList : LiveData<List<String>>
        get() = _friendListLiveData

    suspend fun loadUserInfo() {

        if (!checkInternetConnection(context))
           throw Exception(res.getString(R.string.no_internet_connection))

        withContext(Dispatchers.IO) {

            val fields = listOf(UsersFields.PHOTO_MAX, UsersFields.COUNTERS)
            VK.execute(UsersService().usersGet(fields = fields), object :
                VKApiCallback<List<UsersUserXtrCounters>> {
                override fun success(result: List<UsersUserXtrCounters>) {
                    Log.i(this.javaClass.simpleName, "Load users info successful")
                    _userInfo.value = result[0]
                }

                override fun fail(error: Exception) {
                    Log.e(ContentValues.TAG, error.toString())
                }
            })
        }
    }

    suspend fun loadFriendsList(count : Int, offset : Int) {
        if (!checkInternetConnection(context))
            throw Exception(res.getString(R.string.no_internet_connection))

        withContext(Dispatchers.IO) {
            val fields = listOf(UsersFields.DOMAIN)
            VK.execute(
                FriendsService().friendsGet(fields = fields, count = count, offset = offset,
                order = FriendsGetOrder.HINTS), object: VKApiCallback<FriendsGetFieldsResponse> {
                override fun success(result: FriendsGetFieldsResponse) {

                    Log.e(this.javaClass.simpleName, "Load friends successful")
                    result.items.forEach {
                        _friendList.add(it.firstName!! + " " + it.lastName!!)
                    }
                    _friendListLiveData.value = _friendList
                }
                override fun fail(error: Exception) {
                    Log.e(ContentValues.TAG, error.toString())
                }
            })
        }
    }

}