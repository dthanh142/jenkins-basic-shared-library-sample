
def call(config) {
    echo "Building ${config.language}-${config.version}"

    stage("Build"){
        tool name: "java${config.version}", type: "jdk"
        env.buildToolVersion = tool name: "${config.buildTool}", type: "${config.buildTool}"
        env.PATH="${env.buildToolVersion}/bin:${env.PATH}"

        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){

        if ( config.buildTool.contains("maven")) {
            jarfileLocation = "target"
        } else if ( config.buildTool.contains("gradle")) {
            jarfileLocation = "target/libs"
        }
        def runCommand = config.runCommand.split().collect {"\"" +  it.trim() + "\"" }.join(",")

        // Write dockerfile
        writeFile file: 'Dockerfile-default', text: """FROM repo.vndirect.com.vn/base-images/java:${config.version}
WORKDIR /opt/${config.projectName}
ENV ${config.environmentVariables}
COPY ${jarfileLocation}/*.jar /opt/${config.projectName}/${config.projectName}.jar
#VOLUME ["/var/log/${config.projectName}"]
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
