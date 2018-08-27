import org.yaml.snakeyaml.Yaml

def call() {

    node {
		options {
	    	buildDiscarder(logRotator(numToKeepStr: '5'))
  		}

	    try {
            stage('Clone') {
	        	checkout scm
	        }
            stage('Parse config file') {
      		    echo 'Loading pipeline definition'
		        Yaml parser = new Yaml()
		        Map configParser = parser.load(new File(pwd() + '/devops.yaml').text)
		        cp = configParser
		        echo "${cp}"
    	    }
            // stage('Approval') {
            //     approval(cp.name)
            // }
			stage('Build') {
				echo "Building commit..."
				switch(cp.template.language) {
					case 'python':
						echo "Building python"
						python(cp.template.framework)
						break
					case 'java':
						echo "Building java"
						break
					case 'nodejs':
						echo "Building nodejs project..."
						java(cp.template.framework)
						break
					default:
						echo "zip"
						break
				}
			}

	    } catch (err) {
	        currentBuild.result = 'FAILED'
	        throw err
	    }
    }
}

