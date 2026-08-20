# jenkins-setup

My JCasC setup with shared Groovy library and seed job for personal projects.

## Prerequisites

- Docker
- Docker Compose

## Setup

Create `.env` file from the `.env.example` file and fill in your values.

```sh
cp .env.example .env
```

### Enviriment varibales:

| Variable | Description |
| -- | -- |
| JENKINS_ADMIN_PASSWORD | Admin Password for login to UI |
| DOCKERHUB_USERNAME | DockerHub Repository Username |
| DOCKERHUB_TOKEN | DockerHub Repository Access Token |
| GITHUB_USERNAME | GitHub Username |
| GITHUB_TOKEN | GitHub Fine-grained personal access token |

### Running

```sh
docker-compose up --build
```

Jenkins will be available at `http://localhost:8080`

After startup, run the seed job to create all pipeline jobs defined in `jobs/seed.groovy`.

### Installed plugins

| Plugin | Description |
| --- | --- |
| configuration-as-code | JCasC |
| job-dsl | Programmatic job creation via seed job |
| git | Git SCM integration |
| git-client | git dependency |
| workflow-aggregator | core pipeline plugins |
| pipeline-model-definition | Declarative pipeline syntax |
| pipeline-groovy-lib | Shared Groovy library support |
| docker-workflow | Docker Pipeline plugin |
| docker-commons | Shared Docker utilities |
| credentials-binding | Bind credentials to environment variables |
| ws-cleanup | Workspace cleanup |
| timestamper | Timestamps in build logs |
| ansicolor | Colored output in build logs |
| blueocean | Cool UI |

## Pipeline Job management

Add pipeline job definitions to `jobs/seed.groovy`, then re-run the seed job.

### Job definition example

```groovy
pipelineJob('folder/job-name') {
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/username/repo.git')
                    }
                    branch('branch')
                }
            }
            scriptPath('path/to/Jenkinsfile')
            lightweight(false)
        }
    }
}
```

### Using shared library steps

#### Available shared steps
| Group | steps |
| -- | -- |
| Git | `gitCheckout`<br> `gitConfig`<br> `gitAdd`<br> `gitCommint`<br> `gitPush` |
| Docker | `dockerLogin`<br> `dockerBuildPush`<br> `dockerClean`<br>  `dockerLogout`<br> `pruneDockerTags` |
| Terraform | `terraformDeploy`<br> `updateImageTag` |
| Tests | `runTests` |

#### Example of shared steps usage

```groovy
@Library('shared') _

pipeline {
    stage('Test backend') {
        steps {
            runTests(
                workDir:     'backend',
                composeFile: 'docker-compose.test.yaml'
            )       
        }
    }
}

```
