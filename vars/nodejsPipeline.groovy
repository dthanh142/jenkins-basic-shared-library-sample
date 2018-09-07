
def call(config) {
    echo "Building ${config.language}-${config.version}"

    stage("Build"){
        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){
          
        // Write dockerfile
        writeFile file: 'Dockerfile', text: """FROM node:${config.version}
WORKDIR /opt/${config.projectName}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i -g ${config.dependencies}
EXPOSE ${config.port}
CMD [${config.runCommand}]"""

        // dockerBuild(config)

    }

    stage("Create docker-compose-default file"){

        writeFile file: "docker-compose-default.yml", text: """version: \'2\'
services:
  lb-${config.projectName}:
    image: rancher/lb-service-haproxy:v0.9.1
    expose:
    - 80:80/tcp
    labels:
      io.rancher.container.agent.role: environmentAdmin,agent
      io.rancher.container.agent_service.drain_provider: 'true'
      io.rancher.container.create_agent: \'true\'
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
    image: repo.vndirect.com.vn/${config.projectName}/master:latest
    #volumes:
    #  - /opt/config:/opt/config
    stdin_open: true
    tty: true
    labels:
      io.rancher.container.pull_image: always
    scale: 1"""

        def composeFile = readYaml file: "docker-compose-default.yml"
        println composeFile
        composeFile.services.'test-jenkins'.volumes = ["/opt/config:/opt/config","/opt/test:/opt/test"]
        println composeFile

        sh "rm docker-compose-default.yml"
        writeYaml file: "docker-compose-default.yml", data: composeFile
    }

    // stage("Deploy to UAT"){
    //     deployUAT(config)
    // }
}