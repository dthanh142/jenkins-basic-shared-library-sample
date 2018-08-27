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

    // load project's configuration
    ProjectConfiguration projectConfig = ConfigParser.parse(yaml, buildNumber);

    def firstImage = sh(
                script: "docker images --filter 'reference=${projectConfig.projectName}:*' --format \"{{.Tag}}\" | sort -n | head -1",
                returnStdout: true
            );
            firstImage = Integer.parseInt(firstImage.trim());
            println firstImage
}
