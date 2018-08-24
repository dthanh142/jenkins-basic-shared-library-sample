
def call(a) {

    timeout(60) {
        mail to: 'thanh.phamduc@vndirect.com.vn',
                from: 'jenkins-noreply@vndirect.com.vn',
                subject: "Please approve ${a} project to production", mimeType: 'text/html',
                body: """Please <a href="${env.JOB_URL}${env.BUILD_ID}/input/">approve me</a>!"""
        input message: "Approve ${a} to production", submitter: "thanh.phamduc"
        // approvalMap = input id: 'test', message: 'Hello', ok: 'Proceed?', parameters: [choice(choices: 'uat\nstag\nprod', description: 'Select a environment for this build', name: 'ENV'), string(defaultValue: '', description: '', name: 'myparam')], submitter: 'thanh.phamduc,user2,group1', submitterParameter: 'APPROVER'
    }
}