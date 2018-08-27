
def call(command) {
    echo   'Building commit...'
    println command
    sh '${command}'
}