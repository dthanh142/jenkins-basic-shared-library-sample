
def call(config){
   
    // deploy to UAT using rancher CLI
    sh "rancher up --pull -d -s ${config.projectName} --upgrade --confirm-upgrade -f ${config.dockerCompose}"
}