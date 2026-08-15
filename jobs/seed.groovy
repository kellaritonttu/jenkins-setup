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
                    }
                    branch('main')
                }
            }
            scriptPath('jobs/infrastructure/dockerhub-cleanup.groovy')
            lightweight(false)
        }
    }
}

// Add pipelineJob definitions below:

//     pipelineJob('folder/job-name') {
//       definition {
//         cpsScm {
//           scm {
//             git {
//               remote {
//                 url('https://github.com/username/repo.git')
//               }
//               branch('branch')
//             }
//           }
//           scriptPath('path/to/Jenkinsfile')
//           lightweight(false)
//         }
//       }
//     }