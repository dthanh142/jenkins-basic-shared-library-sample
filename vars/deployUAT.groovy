
def call(config){
   
    // deploy to UAT using rancher CLI
    sh "rancher up --pull -d -s ${config.projectName}-${config.environment} --upgrade --confirm-upgrade -f ${config.dockerCompose}"
}