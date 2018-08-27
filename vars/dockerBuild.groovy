
def call(config) {

    // build and push docker image
    withDockerRegistry([credentialsId: 'docker-nexus-credentials', url: "https://repo.vndirect.com.vn"]) {
		    def image = docker.build("repo.vndirect.com.vn/${config.projectName}/${env.BRANCH_NAME}:${env.GIT_COMMIT}", ".")
            	image.push("${env.GIT_COMMIT}")
            	image.push('latest')
          }
}