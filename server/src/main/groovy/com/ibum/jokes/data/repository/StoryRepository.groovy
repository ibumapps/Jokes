package com.ibum.jokes.data.repository

import com.ibum.jokes.data.domain.Story
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface StoryRepository extends MongoRepository<Story, String> {

    Story findByCode(long code)
}