
def call(config) {
    config.build.each { command ->
        try {
            sh command
        } catch (ignored) {
	        currentBuild.result = 'FAILED'
            println ignored
        }
    }
}