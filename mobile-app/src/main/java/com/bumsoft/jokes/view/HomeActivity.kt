package com.bumsoft.jokes.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumsoft.jokes.App
import com.bumsoft.jokes.R
import com.bumsoft.jokes.model.StoryModel
import com.bumsoft.jokes.service.DataService
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.SwipeDirection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast


class HomeActivity : AppCompatActivity() {

    private var currentStory: StoryModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val adapter = StoryCardAdapter(getApplicationContext())


        (application as App).dataService?.getAllStories()?.forEach { adapter.add(it) }

        activity_main_card_stack_view.run {

            setAdapter(adapter)
            visibility = View.VISIBLE
            setCardEventListener(object : CardStackView.CardEventListener {

                override fun onCardReversed() {

                }

                override fun onCardMovedToOrigin() {

                }

                override fun onCardClicked(index: Int) {
                    currentStory = adapter.getItem(index)
                }

                override fun onCardDragging(percentX: Float, percentY: Float) {

                }

                override fun onCardSwiped(direction: SwipeDirection) {

                    currentStory = adapter.getItem(activity_main_card_stack_view.topIndex - 1)

                    Log.i("Story", currentStory.toString())

                    var  reaction = ""
                    when (direction) {

                        SwipeDirection.Left -> reaction = "dislike"
                        SwipeDirection.Right -> reaction = "like"
                        else -> {
                        }
                    }

                    (application as App).serverRepository()?.setRating(currentStory?.id ?: 0, reaction)?.
                            subscribe({}, { toast("${it.message}") })

                    adapter.remove(currentStory)
                    activity_main_card_stack_view.setPaginationReserved()
                    (application as App).dataService?.dropStory(listOf(currentStory?.id) as List<Long>)
                    adapter.add((application as App).dataService?.getStories())
                }
            })
        }
    }
}
