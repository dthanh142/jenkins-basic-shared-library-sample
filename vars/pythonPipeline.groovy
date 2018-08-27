
def call(config) {
    switch(config.framework) {
        case 'flask':
            echo "Building flask"
            break
        case 'celery':
            echo "Building celery"
            break
        default:
            echo "Building plain python"
            break

    }
}