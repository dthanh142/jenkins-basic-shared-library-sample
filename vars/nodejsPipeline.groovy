
def call(config) {
    echo "Building ${config.language}"

    stage("Build"){
        // def nodeVersion = config.version.tokenize(".")[0]

        env.buildToolVersion = tool name: "nodejs${config.version}", type: "nodejs"
        println env.PATH
        env.PATH="${env.buildToolVersion}/bin:${env.PATH}"
        println env.PATH
        sh 'printenv'
        sh "node -v"

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
RUN npm i && npm i -g ${config.dependencies}
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