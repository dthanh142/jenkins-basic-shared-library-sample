import com.vndirect.parser.ConfigParser;
import com.vndirect.*;

def call(config) {
    switch(config.framework) {
        case 'flask':
            echo "Building flasky"
            stage("Build"){
                build(config)
            }
            stage("Sonar"){
                sonar(config)
            }
            stage("Build docker"){
                dockerBuild(config)
            }
            break
        case 'celery':
            echo "Building celery"
            break
        default:
            echo "Building plain python"
            break

    }
}