package com.ibum.jokes.model

import groovy.transform.ToString

@ToString(includePackage = false, includeNames = true)
class StoryModel {

    Long id

    String title

    String body

    long rating
}
