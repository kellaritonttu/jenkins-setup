package org.kellaritonttu

import groovy.json.JsonSlurper

class DockerHub implements Serializable {

    private final Script steps
    private String apiToken

    DockerHub(Script steps) {
        this.steps = steps
    }

    // __ CLI login — for docker push/pull _____________________________________

    def login(String credentialsId = 'dockerhub-credentials') {
        steps.withCredentials([
            steps.usernamePassword(
                credentialsId: credentialsId,
                usernameVariable: 'DOCKERHUB_USER',
                passwordVariable: 'DOCKERHUB_TOKEN'
            )
        ]) {
            steps.sh 'echo $DOCKERHUB_TOKEN | docker login -u $DOCKERHUB_USER --password-stdin'
        }
    }

    // __ API login — for tag management REST API _______________________________

    def apiLogin(String credentialsId = 'dockerhub-credentials') {
        steps.withCredentials([
            steps.usernamePassword(
                credentialsId: credentialsId,
                usernameVariable: 'DOCKERHUB_USER',
                passwordVariable: 'DOCKERHUB_TOKEN'
            )
        ]) {
            def response = steps.sh(
                script: '''
                    curl -fsSL -X POST https://hub.docker.com/v2/users/login/ \
                        -H 'Content-Type: application/json' \
                        -d '{"username":"'"$DOCKERHUB_USER"'","password":"'"$DOCKERHUB_TOKEN"'"}'
                ''',
                returnStdout: true
            ).trim()

            apiToken = new JsonSlurper().parseText(response).token
        }
    }

    // __ Push __________________________________________________________________

    def push(String image, String tag) {
        steps.sh "docker push ${image}:${tag}"
    }

    // __ Tag management ________________________________________________________

    def getTags(String username, String repo, int pageSize = 100) {
        assertApiLoggedIn()

        def response = steps.sh(
            script: """
                curl -fsSL \\
                    "https://hub.docker.com/v2/repositories/${username}/${repo}/tags/?page_size=${pageSize}&ordering=-last_updated" \\
                    -H "Authorization: JWT ${apiToken}"
            """,
            returnStdout: true
        ).trim()

        return new JsonSlurper().parseText(response).results
    }

    def deleteTag(String username, String repo, String tag) {
        assertApiLoggedIn()

        def exitCode = steps.sh(
            script: """
                curl -fsSL -X DELETE \\
                    "https://hub.docker.com/v2/repositories/${username}/${repo}/tags/${tag}/" \\
                    -H "Authorization: JWT ${apiToken}"
            """,
            returnStatus: true
        )

        if (exitCode != 0) {
            steps.echo "Warning: failed to delete tag ${tag} (exit code ${exitCode})"
        } else {
            steps.echo "Deleted: ${tag}"
        }
    }

    def pruneTags(String username, String repo, int keepLast = 3, List<String> protectedTags = ['latest']) {
        assertApiLoggedIn()

        def tags = getTags(username, repo)
        steps.echo "Found ${tags.size()} tag(s), keeping ${keepLast} most recent"

        if (tags.size() <= keepLast) {
            steps.echo "Nothing to delete"
            return
        }

        tags[keepLast..-1].each { tag ->
            if (protectedTags.contains(tag.name)) {
                steps.echo "Skipping protected tag: ${tag.name}"
                return
            }
            deleteTag(username, repo, tag.name)
        }
    }

    // __ Private _______________________________________________________________

    private void assertApiLoggedIn() {
        if (!apiToken) {
            throw new IllegalStateException("DockerHub: call apiLogin() before using API methods")
        }
    }
}