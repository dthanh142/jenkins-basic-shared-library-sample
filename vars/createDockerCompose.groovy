
def call(config){

    def env.BRANCH_NAME = env.BRANCH_NAME.toLowerCase()
    writeFile file: "docker-compose-default.yml", text: """version: \'2\'
services:
  lb-${config.projectName}:
    image: rancher/lb-service-haproxy:v0.9.1
    expose:
    - 80:80/tcp
    labels:
      io.rancher.container.agent.role: environmentAdmin,agent
      io.rancher.container.agent_service.drain_provider: 'true'
      io.rancher.container.create_agent: 'true'
    scale: 1
    #start_on_create: true
    lb_config:
      certs: []
      port_rules:
      - path: /
        priority: 2
        protocol: http
        service: ${config.projectName}
        source_port: 80
        target_port: ${config.port}
    health_check:
      healthy_threshold: 2
      response_timeout: 2000
      port: 42
      unhealthy_threshold: 3
      initializing_timeout: 60000
      interval: 2000
      strategy: recreate
      reinitializing_timeout: 60000
  ${config.projectName}:
    image: repo.vndirect.com.vn/${config.projectName}/${env.BRANCH_NAME}:${config.buildTag}
    environment:
      TZ: Asia/Ho_Chi_Minh
    stdin_open: true
    tty: true
    labels:
      io.rancher.container.pull_image: always
    scale: 1
    mem_limit: ${config.memLimit}
    cpu_shares: ${config.cpuLimit}"""

        
    def composeFile = readYaml file: "docker-compose-default.yml"

    // add config volumes
    config.configFiles.each {
        if ( !composeFile.services."${config.projectName}".volumes){
            composeFile.services."${config.projectName}".volumes = []
        }
        composeFile.services."${config.projectName}".volumes.add("$it:$it")
    }
    
    // add celery container for python-celery task queue
    if (config.language.toLowerCase() == 'python') {
      composeFile.services."${config.projectName}"["command"] = "${config.runCommand}"
    }
    if ( config.celery ) {
        composeFile.services."${config.projectName}"["depends_on"] = ["redis"]

        def celery_redis = [
          "celery": [
            "image": "repo.vndirect.com.vn/${config.projectName}/${env.BRANCH_NAME}:${config.buildTag}",
            "command": "${config.startCelery}",
            "depends_on": [
               "redis"
            ]
          ],
          "redis": [
            "image": "redis"
          ]
        ]

        composeFile.services << celery_redis
    }


    sh "rm docker-compose-default.yml"
    writeYaml file: "docker-compose-default.yml", data: composeFile
}