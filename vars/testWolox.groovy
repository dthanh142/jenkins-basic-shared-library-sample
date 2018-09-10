import com.vndirect.parser.ConfigParser;
import com.vndirect.*;
import org.yaml.snakeyaml.Yaml;


def call() {
    // echo 'Loading pipeline definition'
    // Yaml parser = new Yaml()
    // Map configParser = parser.load(new File(pwd() + '/devops.yaml').text)
    // yaml = configParser
    
    def yaml = readYaml file: "./devops.yaml";
    println yaml
    def buildNumber = Integer.parseInt(env.BUILD_ID)
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
        case ['nodejs','node']:
            nodejsPipeline(projectConfig)
            break
        default: 
            throw new Exception ("No framework declared")
            break
    }

    // clean up docker images
    try {
        sh "docker rmi \$(docker images -f \"dangling=true\" -q)"
        sh "docker images --filter 'reference=repo.vndirect.com.vn/${projectConfig.projectName}/${env.BRANCH_NAME}:${projectConfig.buildTag}' -q | xargs --no-run-if-empty docker rmi -f"

        // def firstImage = sh(
        //     script: "docker images --filter 'reference=${projectConfig.projectName}:*' --format \"{{.Tag}}\" | sort -n | head -1",
        //     returnStdout: true
        // );
        // firstImage = Integer.parseInt(firstImage.trim());
        // println firstImage
        // for(int i = firstImage; i < buildNumber; i++) {
        //     try {
        //         sh "docker images --filter 'reference=${projectConfig.projectName}:${i}' -q | xargs --no-run-if-empty docker rmi -f"
        //     } catch(ignored) {
        //         println ignored
        //     }
        // }
    } catch(ignored) {
        println ignored
        }
}
