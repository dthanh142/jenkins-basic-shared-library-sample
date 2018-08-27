#!/usr/bin/env groovy

def getTag() {
	echo 'Parsing tag...'
        tag = sh(
            script: 'git branch',
            returnStdout: true
        ).trim()

        env.TAG = tag
        println tag
}

def build(projectConfig) {
        echo 'Buidling commit...'
        projectConfig.each {
                sh "${it}"
        }

}

def sonar() {
        sh "/opt/sonar-runner/bin/sonar-runner -e -Dsonar.host.url=http://202.160.124.141:9000 -Dsonar.projectKey=${name} -Dsonar.sources=."
}

def dockerfile() {
	writeFile file: 'Dockerfile', text: """FROM node:6-alpine
			WORKDIR /opt/${name}
			ADD . /opt/${name}
			#VOLUME ["/var/log/${name}","/opt/${name}"]
			RUN npm i -g pushstate-server
			EXPOSE ${port}
			CMD ["pushstate-server","build","9000"]"""
}

def build_docker_image() {
	script {
          withDockerRegistry([credentialsId: 'docker-nexus-credentials', url: "https://repo.vndirect.com.vn"]) {
		    def image = docker.build("repo.vndirect.com.vn/${name}/${BRANCH_NAME}:${GIT_COMMIT}", ".")
            	image.push("${GIT_COMMIT}")
            	image.push('latest')
          }
        }
}

def deploy_to_uat() {
	    rancher confirm: true, credentialId: 'rancher', endpoint: 'http://10.200.39.30:8080/v2-beta', environmentId: '1a7', environments: 'TZ=Asia/Ho_Chi_Minh', image: "repo.vndirect.com.vn/${name}/${BRANCH_NAME}:${GIT_COMMIT}", ports: "${port}", service: "uat/${name}", timeout: 50
}

def approval(Map config) {
	timeout(60) {
        mail to: 'thanh.phamduc@vndirect.com.vn',
                from: 'jenkins-noreply@vndirect.com.vn',
                subject: "Please approve ${config.name} project to production", mimeType: 'text/html',
                body: """Please <a href="${env.JOB_URL}${env.BUILD_ID}/input/">approve me</a>!"""
        input message: "Approve ${config.name} to production", submitter: "thanh.phamduc"
        // approvalMap = input id: 'test', message: 'Hello', ok: 'Proceed?', parameters: [choice(choices: 'uat\nstag\nprod', description: 'Select a environment for this build', name: 'ENV'), string(defaultValue: '', description: '', name: 'myparam')], submitter: 'thanh.phamduc,user2,group1', submitterParameter: 'APPROVER'
    }
}

def build_tag_to_prod() {
	echo 'Building tag'
        script {
            tag = sh(
                script: 'git describe --tags',
                returnStdout: true
            ).trim()
                env.TAG=tag
                sh 'echo ${TAG}'
        }
        sleep 5
}

return this;