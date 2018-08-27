
def call(config) {
    config.build.each { command ->
        try {
            sh command
        } catch (err) {
	        currentBuild.result = 'FAILED'
            println ignored
        }
    }
}