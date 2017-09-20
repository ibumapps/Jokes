package com.ibum.jokes.data.service

import com.ibum.jokes.data.domain.Story
import com.ibum.jokes.data.repository.StoryRepository
import com.ibum.jokes.model.StoryModel
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Slf4j
@Service
class StoryService {

    @Autowired
    StoryRepository storyRepository

    @Autowired
    StringRedisTemplate redisTemplate

    StoryModel getStory(long id) {

        def size = storyRepository.count()

        if (id > size) {

            id = id - (long)(id / size) * size
        }

        def story = storyRepository.findAll(new PageRequest(id as int, 1)).find() as Story

        toStoryModel(story)
    }

    StoryModel addNewStory(String text) {

        def code = redisTemplate.opsForValue().increment('counters:story-index', 1)

        def story = storyRepository.save(new Story(
                code: code,
                title: text.substring(0, 25),
                text: text
        ))

        toStoryModel(story)
    }

    StoryModel likeStory(long code) {

        def story = storyRepository.findByCode(code)

        story.like++
        storyRepository.save(story)

        toStoryModel(story)
    }

    StoryModel disLikeStory(long code) {

        def story = storyRepository.findByCode(code)

        story.dislike++
        storyRepository.save(story)

        toStoryModel(story)
    }

    StoryModel toStoryModel(Story story) {

        new StoryModel(
                id: story.code as int,
                title: story.title,
                body: story.text,
                rating: story.like - story.dislike
        )
    }

    StoryModel getRatingStory(int index) {

        def count = storyRepository.count()

        if (index > count) {
            index = count - 1
        }

        def story = storyRepository.findAll(new PageRequest(index, 1, new Sort(Sort.Direction.ASC, ['like', 'dislike']))).find() as Story

        toStoryModel(story)
    }

    StoryModel getNewStory(int index) {

        def count = storyRepository.count()

        if (index > count) {
            index = count - 1
        }

        def story = storyRepository.findAll(new PageRequest(index, 1, new Sort(Sort.Direction.DESC, ['createDate']))).find() as Story

        toStoryModel(story)
    }
}
