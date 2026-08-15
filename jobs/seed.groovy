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
                url("https://github.com/kellaritunttu/jenkins-setup")
            }
            branch('main')
            }
        }
        scriptPath('jobs/infrastructure/dockerhub-cleanup.groovy')
        lightweight(true)
        }
    }
    }