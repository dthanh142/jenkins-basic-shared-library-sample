
def call(config){

    sh "rancher up --pull -d -s ${config.projectName} --upgrade --confirm-upgrade -f ${config.dockerCompose}"
}