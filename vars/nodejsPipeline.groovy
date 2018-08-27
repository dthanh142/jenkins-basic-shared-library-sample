
def call(config) {
    echo "Building ${config.language}-${config.version}"
    stage("Build"){
        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){

        // def String command = String.join(",", config.runCommand)
        String command = (String) config.runCommand
        println command
        def command = config.runCommand.toString().split().collect {"\"" +  it.trim() + "\"" }.join(",")
    
        // Write dockerfile
        writeFile file: 'Dockerfile', text: """FROM node:${config.version}
WORKDIR /opt/${config.projectName}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i -g pushstate-server
EXPOSE ${config.port}
CMD $command"""

        // dockerBuild(config)
    }
}