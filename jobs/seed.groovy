// __ folders ___________________________________________________________________
folder('infrastructure') {
    description('Infrastructure jobs')
}

folder('simple-chat') {
    description('Jobs of the Simple Chat project')
}


// __ infrastructure jobs _______________________________________________________

pipelineJob('infrastructure/dockerhub-cleanup') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/kellaritonttu/jenkins-setup.git')
                        refspec('+refs/heads/*:refs/remotes/origin/*')
                    }
                    branch('main')
                    extensions {
                        cloneOptions {
                            noTags(false)
                            shallow(true)
                            depth(1)
                        }
                    }
                }
            }
            scriptPath('jobs/infrastructure/dockerhub-cleanup.groovy')
            lightweight(false)
        }
    }
}


// __ simple chat jobs _________________________________________________________

pipelineJob('simple-chat/test-backend') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/kellaritonttu/simple-chat.git')
                    }
                    branch('dev')
                }
            }
            scriptPath('backend/Jenkinsfile.test')
            lightweight(false)
        }
    }
}