
def call(config) {
    sh "/opt/sonar-runner/bin/sonar-runner -e -Dsonar.host.url=http://202.160.124.141:9000 -Dsonar.projectKey=${config.projectName} -Dsonar.sources=."

}