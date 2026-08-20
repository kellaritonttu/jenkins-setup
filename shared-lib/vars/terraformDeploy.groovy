def call(Map config = [:]) {
    def terraformDir  = config.terraformDir  ?: 'terraform'
    def credentialsId = config.credentialsId ?: 'terraform-cloud-token'

    withCredentials([string(
        credentialsId: credentialsId,
        variable: 'TF_TOKEN_app_terraform_io'
    )]) {
        dir(terraformDir) {
            sh """
                terraform init
                terraform taint module.frontend.google_cloud_run_service.this || true
                terraform taint module.backend.google_cloud_run_service.this || true
                terraform apply -auto-approve
            """
        }
    }
}