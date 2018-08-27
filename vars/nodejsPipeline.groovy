import com.vndirect.parser.ConfigParser;
import com.vndirect.*;

def call(config) {
    echo "Building ${config.language}-${config.version}"
    stage("Build"){
        build(config)
    }
    // stage("Sonar"){
    //     sonar(config)
    // }
    stage("Build docker"){

        sh "echo $env.GIT_COMMIT"

        // Write dockerfile
        writeFile file: 'Dockerfile', text: """FROM node:${config.version}
WORKDIR /opt/${config.projectName}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i -g pushstate-server
EXPOSE ${config.port}
CMD ["pushstate-server","build","9000"]"""

        // dockerBuild(config)
    }
}