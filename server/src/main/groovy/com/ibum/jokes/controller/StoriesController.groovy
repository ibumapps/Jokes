package com.ibum.jokes.controller

import com.ibum.jokes.data.service.StoryService
import com.ibum.jokes.model.StoryModel
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Slf4j
@RestController
@RequestMapping('/api/stories')
class StoriesController {

    @Autowired
    StoryService storyService

    @Autowired
    StringRedisTemplate redisTemplate

    @RequestMapping('/random')
    StoryModel getRandomStory(@RequestParam('device_id') String deviceId) {

        storyService.getStory(null)
    }

    @RequestMapping(method = RequestMethod.POST)
    def addStory(@RequestBody String text) {

        storyService.addNewStory(text)
    }

    @RequestMapping(value = '{code}/rating', method = RequestMethod.POST)
    def setStoryRating(@PathVariable('code') long code,
                       @RequestParam('reaction') String reaction) {

        if (code < 1) return ResponseEntity.badRequest()

        switch (reaction) {

            case 'like':
                storyService.likeStory(code)
                break

            case 'dislike':
                storyService.disLikeStory(code)
                break

            default: ResponseEntity.badRequest()
        }
    }

    @RequestMapping(value = '/rating')
    def getRatingStory(@RequestParam('device_id') String deviceId,
                       @RequestParam(value = 'reload', defaultValue = 'false') boolean reload) {

        storyService.getRatingStory(deviceId, reload)
    }

    @RequestMapping('/new')
    def getNewStory(@RequestParam('device_id') String deviceId,
                    @RequestParam(value = 'reload', defaultValue = 'false') boolean reload) {

        storyService.getNewStory(deviceId, reload)
    }
}
