import com.vndirect.parser.ConfigParser;
import com.vndirect.*;
import org.yaml.snakeyaml.Yaml;


def call() {
    // echo 'Loading pipeline definition'
    // Yaml parser = new Yaml()
    // Map configParser = parser.load(new File(pwd() + '/devops.yaml').text)
    // yaml = configParser
    
    // echo "${yaml}"
    def yaml = readYaml file: ./devops.yaml;
    echo "${yaml}"
    def buildNumber = Integer.parseInt(env.BUILD_ID)

    // load project's configuration
    ProjectConfiguration projectConfig = ConfigParser.parse(yaml, buildNumber);

}
