
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
        writeFile file: 'Dockerfile-default', text: """FROM repo.vndirect.com.vn/base-images/python:${config.version}
WORKDIR /opt/${config.projectName}
ENV ${config.environmentVariables}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN pip install -r requirements.txt && pip install ${config.dependencies}
EXPOSE ${config.port}
CMD ["${config.runCommand}".split().collect {"\"" +  it.trim() + "\"" }.join(",")]"""


        dockerBuild(config)
    }

    stage("Create docker-compose file"){
        createDockerCompose(config)
    }

    stage("Deploy to UAT"){
        deployUAT(config)
    }
}