
def call(config) {

    // build and push docker image
    def env.BRANCH_NAME = env.BRANCH_NAME.toLowerCase()
    println env.BRANCH_NAME

    withDockerRegistry([credentialsId: 'docker-nexus-credentials', url: "https://repo.vndirect.com.vn"]) {
		    def image = docker.build("repo.vndirect.com.vn/${config.projectName}/${env.BRANCH_NAME}:${config.buildTag}", "--file ${config.dockerfile} .")
            	image.push("${config.buildTag}")
            	image.push('latest')
          }

}