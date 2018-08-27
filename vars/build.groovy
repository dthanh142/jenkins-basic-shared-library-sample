
def call(config) {
    config.build.each{command ->
        sh command
    }
}