
def call(config) {
    echo "Building ${config.language}-${config.version}"

    stage("Build"){
        tool name: 'nodejs6', type: 'nodejs'
        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){
          
        // Write dockerfile
        writeFile file: 'Dockerfile-default', text: """FROM node:${config.version}
WORKDIR /opt/${config.projectName}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i -g ${config.dependencies}
EXPOSE ${config.port}
CMD [${config.runCommand}]"""

        dockerBuild(config)
    }

    stage("Create docker-compose file"){
        createDockerCompose(config)
    }

    stage("Deploy to UAT"){
        deployUAT(config)
    }
}