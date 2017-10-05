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

    StoryModel getStory(Long id) {

        if (!id) {

            id = new Random().nextInt(storyRepository.count() as int)
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
        story.rating = story.like - story.dislike
        storyRepository.save(story)

        toStoryModel(story)
    }

    StoryModel disLikeStory(long code) {

        def story = storyRepository.findByCode(code)

        story.dislike++
        story.rating = story.like - story.dislike
        storyRepository.save(story)

        toStoryModel(story)
    }

    StoryModel toStoryModel(Story story) {

        if (!story) {
            return null
        }

        new StoryModel(
                id:         story.code as int,
                title:      story.title,
                body:       story.text,
                rating:     story.rating,
                like:       story.like,
                dislike:    story.dislike
        )
    }

    StoryModel getRatingStory(String deviceId, boolean reset) {

        def index = 0
        if (reset) {
            redisTemplate.opsForValue().set("devices:${deviceId}:rating-index".toString(), '0')
        } else {
            index = redisTemplate.opsForValue().increment("devices:${deviceId}:rating-index".toString(), 1)
        }

        def story = storyRepository.findAll(new PageRequest(index as int, 1, new Sort(Sort.Direction.DESC, ['rating']))).find() as Story

        toStoryModel(story)
    }

    StoryModel getNewStory(String deviceId, boolean reset) {

        def index = 0

        if (reset) {

            redisTemplate.opsForValue().set("devices:${deviceId}:new-index".toString(), '0')
        } else {

            index = redisTemplate.opsForValue().increment("devices:${deviceId}:new-index".toString(), 1)
        }

        def story = storyRepository.findAll(new PageRequest(index as int, 1, new Sort(Sort.Direction.DESC, ['createDate']))).find() as Story

        toStoryModel(story)
    }
}
