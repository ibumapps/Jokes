package com.ibum.jokes.data.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = 'stories')
class Story {

    @Id
    String id

    @CreatedDate
    Date createDate

    @Indexed
    long code

    String title

    String text

    @Version
    Long version = 0

    long like = 0
    long dislike = 0
}
