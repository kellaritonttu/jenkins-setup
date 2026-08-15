// __ folders ___________________________________________________________________
folder('infrastructure') {
    description('Infrastructure jobs')
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