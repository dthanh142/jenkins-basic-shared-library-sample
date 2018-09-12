
def call(config) {
    echo "Building ${config.language}-${config.version}"

    stage("Build"){
        def nodeVersion = config.version.split(".")[0]
        tool name: "nodejs${nodeVersion}", type: "nodejs"
        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){
          
        // Write dockerfile
        writeFile file: 'Dockerfile-default', text: """FROM node:${config.version}
WORKDIR /opt/${config.projectName}
ENV ${config.environmentVariables}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i && npm i ${config.dependencies}
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