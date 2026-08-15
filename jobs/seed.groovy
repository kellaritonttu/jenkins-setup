pipelineJob('infrastructure/dockerhub-cleanup') {
    definition {
        cpsScm {
        scm {
            git {
            remote {
                url("https://github.com/kellaritunttu/jenkins-setup")
            }
            branch('main')
            }
        }
        scriptPath('jobs.infrastructure/dockerhub-cleanup.groove')
        lightweight(true)
        }
    }
    }