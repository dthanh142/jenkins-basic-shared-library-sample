
def call(config){
  
    sh "rancher up -s ${config.projectName} --upgrade --confirm-upgrade -f docker-compose-default.yml"
}