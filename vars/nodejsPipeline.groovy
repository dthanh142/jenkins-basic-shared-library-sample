import com.vndirect.parser.ConfigParser;
import com.vndirect.*;

def call(config) {
    echo "Building ${config.language}-${config.version}"
    stage("Build"){
        build(config)
        env.getEnvironment()    
    }
    stage("Sonar"){
        sonar(config)
    }
    stage("Build docker"){
        dockerBuild(config)
    }
}