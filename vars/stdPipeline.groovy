import com.vndirect.parser.ConfigParser
import com.vndirect.*


def call() {
    // echo 'Loading pipeline definition'
    // Yaml parser = new Yaml()
    // Map configParser = parser.load(new File(pwd() + '/devops.yaml').text)
    // yaml = configParser

    def yaml = readYaml file: "./devops.yaml"
    
    buildTag = sh( script: 'git rev-parse HEAD', returnStdout: true ).trim()

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
        def BRANCH_NAME = env.BRANCH_NAME.toLowerCase()

        // deleteDir()
        try {
            sh "docker images --filter 'reference=repo.vndirect.com.vn/${projectConfig.projectName}/${BRANCH_NAME}:${projectConfig.buildTag}' -q | xargs --no-run-if-empty docker rmi -f"
            sh "docker rmi \$(docker images -f \"dangling=true\" -q) &> /dev/null || true &> /dev/null"
        } catch(ignored) {
            println ignored
            }
    }

    // create dns record for domain name
    stage("Create domain name") {
        sh "python google-dns-create.py ${projectConfig.projectName}-${projectConfig.environment}.vndirect.com.vn. 10.200.39.30"
    }
}