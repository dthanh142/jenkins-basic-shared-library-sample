
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
        config.dependencies.each {
            writeFile file: 'Dockerfile', text: """FROM node:${config.version}
WORKDIR /opt/${config.projectName}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i -g ${it}
EXPOSE ${config.port}
CMD [${config.runCommand}]"""
        }

        // dockerBuild(config)

    }
    // stage("Deploy to UAT"){
    //     deployUAT(config)
    // }
}