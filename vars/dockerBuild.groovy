
def call(config) {
    writeFile file: 'Dockerfile', text: """FROM ${config.language}:${config.version}
			WORKDIR /opt/${config.projectName}
			ADD . /opt/${config.projectName}
			#VOLUME ["/var/log/${config.projectName}","/opt/${config.projectName}"]
			RUN npm i -g pushstate-server
			EXPOSE ${config.port}
			CMD ["pushstate-server","build","9000"]"""

    withDockerRegistry([credentialsId: 'docker-nexus-credentials', url: "https://repo.vndirect.com.vn"]) {
		    def image = docker.build("repo.vndirect.com.vn/${config.projectName}/${BRANCH_NAME}:${GIT_COMMIT}", ".")
            	image.push("${GIT_COMMIT}")
            	image.push('latest')
          }
}