
def call(config){

    sh "rancher up -d -s ${config.projectName} --upgrade --confirm-upgrade -f docker-compose-default.yml"
}