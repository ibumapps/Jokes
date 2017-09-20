package com.bumsoft.jokes.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumsoft.jokes.R
import com.bumsoft.jokes.model.StoryModel


class StoryCardAdapter(context: Context) : ArrayAdapter<StoryModel>(context, 0) {

    override fun getView(position: Int, contentView: View?, parent: ViewGroup): View {
        var contentView = contentView
        val holder: ViewHolder

        if (contentView == null) {
            val inflater = LayoutInflater.from(context)
            contentView = inflater.inflate(R.layout.item_story_card, parent, false)
            holder = ViewHolder(contentView)
            contentView.tag = holder
        } else {
            holder = contentView.tag as ViewHolder
        }

        holder.body.text = getItem(position).body

        return contentView!!
    }

    private class ViewHolder(view: View) {
        var body: TextView = view.findViewById(R.id.textBlock)

    }

}