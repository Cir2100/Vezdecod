package com.kurilov.vezdecod.data

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kurilov.vezdecod.MyApp
import com.vk.sdk.api.users.dto.UsersUserXtrCounters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.lang.Exception
import java.net.URL

class MyRepo {

    private val vkLoader = VKLoader(MyApp.appContext)

    val userInfo = vkLoader.userInfo
    val friendList = vkLoader.friendList

    private val _userPhoto = MutableLiveData<Drawable?>()
    val userPhoto : LiveData<Drawable?>
        get() = _userPhoto


    suspend fun loadUserInfo() {
        vkLoader.loadUserInfo()
    }

    suspend fun loadUserPhoto(user : UsersUserXtrCounters) {
        user.photoMax?.let { _userPhoto.value = loadImage(it) }
    }

    suspend fun loadFriendsList(user : UsersUserXtrCounters) {
        var offset = 0
        val count = 1000
        if (user.counters?.friends!! > count) {
            for (i in 0..(user.counters?.friends!! / count)) {
                vkLoader.loadFriendsList(count = count, offset = offset)
                offset += count
            }
            if (user.counters?.friends!! % count > 0)
                vkLoader.loadFriendsList(count = user.counters?.friends!! % count, offset = offset)
        }
        else
            vkLoader.loadFriendsList(count = user.counters?.friends!!, offset = offset)
    }


    private suspend fun loadImage(url: String?): Drawable? {
        Log.e(ContentValues.TAG, url.toString())
        return withContext(Dispatchers.IO) {
            try {
                val image = URL(url).content as InputStream
                return@withContext Drawable.createFromStream(image, "src name")
            } catch (e: Exception) {
                return@withContext null
            }
        }
    }

}

private lateinit var INSTANCE: MyRepo

fun getMyRepo(): MyRepo {
    synchronized(MyRepo::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = MyRepo()
        }
    }
    return INSTANCE
}