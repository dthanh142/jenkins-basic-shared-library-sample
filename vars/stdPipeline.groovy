import org.yaml.snakeyaml.Yaml

def call() {

    node {
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
			}
			stage('Build python'){
				switch(cp.template) {
					case 'python':
						echo "Building python"
					break
				}
			}

	    } catch (err) {
	        currentBuild.result = 'FAILED'
	        throw err
	    }
    }
}

