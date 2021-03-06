
def call(config) {
    echo "Building ${config.language}"

    stage("Build"){
        // def nodeVersion = config.version.tokenize(".")[0]

        env.buildToolVersion = tool name: "nodejs${config.version}", type: "nodejs"
        env.PATH="${env.buildToolVersion}/bin:${env.PATH}"
        
        sh "node -v"
        sh "npm -v"

        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){
        def runCommand = config.runCommand.split().collect {"\"" +  it.trim() + "\"" }.join(",")
        // Write dockerfile
        writeFile file: 'Dockerfile-default', text: """FROM repo.vndirect.com.vn/base-images/nodejs:${config.version}
WORKDIR /opt/${config.projectName}
ENV ${config.environmentVariables}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i && npm i -g ${config.dependencies}
EXPOSE ${config.port}
CMD [${runCommand}]"""

        dockerBuild(config)
    }

    stage("Create docker-compose file"){
        createDockerCompose(config)
    }

    stage("Deploy to UAT"){
        deployUAT(config)
    }
}