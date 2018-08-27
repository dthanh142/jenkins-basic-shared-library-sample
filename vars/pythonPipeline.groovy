
def call(projectConfig) {
    switch(projectConfig.framework) {
        case 'flask':
            echo "Building flasky"
            //println projectConfig.build
            // vnds.build(projectConfig)
            break
        case 'celery':
            echo "Building celery"
            break
        default:
            echo "Building plain python"
            break

    }
}