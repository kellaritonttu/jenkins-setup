package org.myjenkins

class Docker implements Serializable {

    def steps

    Docker(steps) { this.steps }

    static void login(String credentialsId) {
        withCredentials([usernamePassword(
            credentialsId: credentialsId,
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
        )]) {
            steps.sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
        }
    }

    void build(Map config = [:]) {
        def image      = config.image      ?: steps.error('Docker.build: image is required')
        def tag        = config.tag        ?: steps.error('Docker.build: tag is required')
        def dockerfile = config.dockerfile ?: 'Dockerfile'
        def context    = config.context    ?: '.'

        steps.sh "docker build -f ${dockerfile} -t ${image}:${tag} ${context}"
    }

    void push(String image, String tag) {
        steps.sh "docker push ${image}:${tag}"
    }

    void logout() {
        steps.sh 'docker logout || true'
    }
    
    void clean(images){
        def imageList = images instanceof List ? images : [images]
    
        imageList.each { image ->
            steps.sh "docker rmi ${image} || true"
        }
    }

}