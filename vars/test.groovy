import org.yaml.snakeyaml.Yaml

def call(projectName,serverDomain) {

    node {
	    // Clean workspace before doing anything
	    deleteDir()

	    try {
	        stage ('Build') {
	        	sh "echo 'building ${projectName} ...'"
	        }
	        stage ('Tests') {
		        parallel 'static': {
		            sh "echo 'shell scripts to run static tests...'"
		        },
		        'unit': {
		            sh "echo 'shell scripts to run unit tests...'"
		        },
		        'integration': {
		            sh "echo 'shell scripts to run integration tests...'"
		        }
	        }
	      	stage ('Deploy') {
	            sh "echo 'deploying to server ${serverDomain}...'"
	      	}
	    } catch (err) {
	        currentBuild.result = 'FAILED'
	        throw err
	    }
    }
}



