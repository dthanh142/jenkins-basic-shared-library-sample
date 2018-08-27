import com.vndirect.parser.ConfigParser;
import com.vndirect.*;

def call(config) {
    switch(config.framework) {
        case 'flask':
            echo "Building flasky"
            println config.build
            vnds.build(config)
            break
        case 'celery':
            echo "Building celery"
            break
        default:
            echo "Building plain python"
            break

    }
}