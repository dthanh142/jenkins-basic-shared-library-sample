
def call(framework) {
    switch(framework) {
        case 'flask':
            echo "Building flask"
        case 'plain':
            echo "Building plain python"
        break
    }
}