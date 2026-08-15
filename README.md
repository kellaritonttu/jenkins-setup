# jenkins-setup

My JCasC setup with Docker availability through DinD container and simple Pipeline Job's management.

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

### Installed plugins

| Plugin | Description |
| --- | --- |
| configuration-as-code | Jenkins configuration as code |
| job-dsl | Job DSL for programmatic job creation |
| git | Git SCM integration |
| git-client | Git client library (git dependency) |
| workflow-aggregator | Pipeline suite (includes all core pipeline plugins) |
| pipeline-model-definition | Declarative pipeline syntax |
| credentials-binding | Bind credentials to environment variables |
| ws-cleanup | Workspace cleanup after builds |
| blueocean | Cool UI |

## Pipeline Job management

Add pipeline job definitions at the bottom of `jenkins.scasc.yaml` under the `jobs:`.

### Example of the job definition:

```yaml
  - script: >
      pipelineJob('folder/job-name') {
        definition {
          cpsScm {
            scm {
              git {
                remote {
                  url('https://github.com/github-username/repository-name.git')
                }
                branch('branch')
              }
            }
            scriptPath('path/to/Jenkinsfile')
            lightweight(true)
          }
        }
      }
```