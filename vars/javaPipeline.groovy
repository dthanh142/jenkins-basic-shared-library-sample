import java.util.regex.*

def call(config) {
    echo "Building ${config.language}-${config.version}"

    stage("Build"){
        tool name: "java${config.version}", type: "jdk"
        tool name: "${config.buildTool}", type: "${config.buildTool}"

        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){

        if ( config.buildTool.contains("maven")) {
            def jarfileLocation = "target"
        } else if ( config.buildTool.contains("gradle")) {
            def jarfileLocation = "target/libs"
        }

        println jarfileLocation

        // Write dockerfile
        writeFile file: 'Dockerfile-default', text: """FROM repo.vndirect.com.vn/base-images/java:${config.version}
WORKDIR /opt/${config.projectName}
ENV ${config.environmentVariables}
COPY ${jarfileLocation}/*.jar /opt/${config.projectName}/${config.projectName}.jar
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