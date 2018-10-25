
def call(config) {

    // build and push docker image

    withDockerRegistry([credentialsId: 'docker-nexus-credentials', url: "https://repo.vndirect.com.vn"]) {
		    def image = docker.build("repo.vndirect.com.vn/${config.projectName}/${env.BRANCH_NAME}:${config.buildTag}", "--file ${config.dockerfile} .")
            	image.push("${config.buildTag}")
            	image.push('latest')
          }

}