import com.vndirect.parser.ConfigParser
import com.vndirect.*


def call() {
    // echo 'Loading pipeline definition'
    // Yaml parser = new Yaml()
    // Map configParser = parser.load(new File(pwd() + '/devops.yaml').text)
    // yaml = configParser
    stages{
    def yaml = readYaml file: "./devops.yaml"
    
    buildTag = sh( script: 'git rev-parse HEAD', returnStdout: true ).trim()

    stage("Build prepare"){
        when {
			buildingTag()
		}
        steps{
            def buildTag = sh( script: 'git describe --tag', returnStdout: true ).trim()
        }
    }

    // load project's configuration
    ProjectConfiguration projectConfig = ConfigParser.parse(yaml, buildTag);
    
    // filter framework
    switch(projectConfig.language.toLowerCase()) {
        case 'python':
            pythonPipeline(projectConfig)
            break
        case 'java':
            javaPipeline(projectConfig)
            break
        case ['nodejs','node','react']:
            nodejsPipeline(projectConfig)
            break
        default: 
            throw new Exception ("No framework declared")
            break
    }

    // clean up work dir
    stage("Clean up") {
        deleteDir()
        try {
            sh "docker images --filter 'reference=repo.vndirect.com.vn/${projectConfig.projectName}/${env.BRANCH_NAME}:${projectConfig.buildTag}' -q | xargs --no-run-if-empty docker rmi -f"
            sh "docker rmi \$(docker images -f \"dangling=true\" -q) &> /dev/null || true &> /dev/null"
        } catch(ignored) {
            println ignored
            }
    }
    }
}
