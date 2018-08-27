
def call(config) {

    // Write dockerfile
    writeFile file: 'Dockerfile', text: """FROM ${config.language}:${config.version}
WORKDIR /opt/${config.projectName}
ADD . /opt/${config.projectName}
#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
RUN npm i -g pushstate-server
EXPOSE ${config.port}
CMD ["pushstate-server","build","9000"]"""

    // build and push docker image
    withDockerRegistry([credentialsId: 'docker-nexus-credentials', url: "https://repo.vndirect.com.vn"]) {
		    def image = docker.build("repo.vndirect.com.vn/${config.projectName}/${env.BRANCH_NAME}:${env.GIT_COMMIT}", ".")
            	image.push("${env.GIT_COMMIT}")
            	image.push('latest')
          }
}