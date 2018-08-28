
def call(config){

    agent {
        label 'docker-slave-uat'
      }
    rancher confirm: true, 
            credentialId: 'rancher', 
            endpoint: 'http://10.200.39.30:8080/v2-beta', 
            environmentId: '1a7', 
            environments: 'TZ=Asia/Ho_Chi_Minh', 
            image: "repo.vndirect.com.vn/${config.projectName}/${env.BRANCH_NAME}:${buildTag}", 
            ports: "", 
            service: "uat/${config.projectName}", 
            timeout: 50

}