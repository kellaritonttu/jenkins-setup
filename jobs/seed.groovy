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
                        url('git://github.com/kellaritonttu/jenkins-setup.git')
                    }
                    branch('main')
                }
            }
            scriptPath('jobs/infrastructure/dockerhub-cleanup.groovy')
            lightweight(false)
        }
    }
}