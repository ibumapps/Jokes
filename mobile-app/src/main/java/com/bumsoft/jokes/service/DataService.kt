package com.bumsoft.jokes.service

import android.util.Log
import com.bumsoft.jokes.model.StoryModel
import com.bumsoft.jokes.service.repository.ServerRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class DataService (var serverRepository: ServerRepository) {

    val CACHE_SIZE = 5

    private var storyList: MutableList<StoryModel> = ArrayList()

    var cacheData: Observable<StoryModel> = Observable.fromIterable(storyList)
    var serverData: Observable<StoryModel> = serverRepository.getRandomStory("1", CACHE_SIZE).
            doOnNext( {
                storyList.add(it)
            })

    var data: Observable<StoryModel> = Observable.
            concat(cacheData, serverData)
            .doOnError { Log.e("Server", it.message) }

    fun getStories(): StoryModel {

        return serverData.
                    subscribeOn(Schedulers.io())
                    ?.blockingFirst()!!
    }

    fun getAllStories(): List<StoryModel> {

        return try {
            data.
                    subscribeOn(Schedulers.io())
                    ?.blockingIterable()!!.toList()
        } catch (e: Exception) {
            ArrayList()
        }
    }

    fun loadNewStory(count: Int) {

        data.blockingIterable(count)
//
//        serverRepository.getRandomStory("1")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe( {
//
//                    storyList.plus(it)
//                })
    }

    fun dropStory(ids: List<Long>) {

        storyList.removeAll(storyList.filter { ids.contains(it.id) })
    }
}