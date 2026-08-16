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
        steps.sh "docker build -f ${config.dockerfile} -t ${config.image}:${config.tag} ${config.context}"
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