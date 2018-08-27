import com.vndirect.parser.ConfigParser;
import com.vndirect.*;
import org.yaml.snakeyaml.Yaml;


def call() {
    // echo 'Loading pipeline definition'
    // Yaml parser = new Yaml()
    // Map configParser = parser.load(new File(pwd() + '/devops.yaml').text)
    // yaml = configParser
    
    // echo "${yaml}"
    def yaml = readYaml file: "./devops.yaml";
    println yaml
    def buildNumber = Integer.parseInt(env.BUILD_ID)

    println yaml.Docker.runCommand.split().getClass()
    def String command = String.join(",", config.runCommand)
    println command



    // load project's configuration
    ProjectConfiguration projectConfig = ConfigParser.parse(yaml, buildNumber);
    
    switch(projectConfig.language) {
        case 'python':
            pythonPipeline(projectConfig)
            break
        case 'java':
            javaPipeline(projectConfig)
            break
        case 'nodejs':
            nodejsPipeline(projectConfig)
            break
        default: 
            println "No framework declared"
            break
    }

    try {
            def firstImage = sh(
                script: "docker images --filter 'reference=${projectConfig.projectName}:*' --format \"{{.Tag}}\" | sort -n | head -1",
                returnStdout: true
            );
            firstImage = Integer.parseInt(firstImage.trim());
            println firstImage
            for(int i = firstImage; i < buildNumber; i++) {
                try {
                    sh "docker images --filter 'reference=${projectConfig.projectName}:${i}' -q | xargs --no-run-if-empty docker rmi -f"
                } catch(ignored) {
                    println ignored
                }
            }
        } catch(ignored) {
            println ignored
        }
}
