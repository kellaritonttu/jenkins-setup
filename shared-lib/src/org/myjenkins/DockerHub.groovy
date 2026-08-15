package org.myjenkins

import groovy.json.JsonSlurper

class DockerHub implements Serializable {

    static def parseTags(String json) {
        return new JsonSlurper().parseText(json).results
    }

    static def parseToken(String json) {
        return new JsonSlurper().parseText(json).token
    }
}