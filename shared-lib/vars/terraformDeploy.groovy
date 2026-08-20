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
                terraform apply -auto-approve
            """
        }
    }
}