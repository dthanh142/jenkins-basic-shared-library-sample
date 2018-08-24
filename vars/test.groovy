import org.yaml.snakeyaml.Yaml

def call() {

    node {
	    try {
            stage ('Clone') {
	        	checkout scm
	        }
            stage ('Parse Yaml') {
      		    echo 'Loading pipeline definition'
		        Yaml parser = new Yaml()
		        Map configParser = parser.load(new File(pwd() + '/devops.yaml').text)
		        cp = configParser
		        echo "${cp}"
    	    }
	        stage ('Build') {
	        	sh "echo 'building ${cp.name} ...'"
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
	            sh "echo 'deploying to server ${cp.template}...'"
	      	}

	    } catch (err) {
	        currentBuild.result = 'FAILED'
	        throw err
	    }
    }
}

