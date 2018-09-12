
def call(config) {
    echo "Building ${config.language}-${config.version}"

    stage("Build"){
        tool name: "java${config.version}", type: "jdk"

        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){
          
        // Write dockerfile
        writeFile file: 'Dockerfile-default', text: """FROM java:${config.version}
WORKDIR /opt/${config.projectName}
ENV ${config.environmentVariables}
COPY target/*.jar /opt/${config.projectName}/${config.projectName}.jar
#VOLUME ["/var/log/${config.projectName}"]
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