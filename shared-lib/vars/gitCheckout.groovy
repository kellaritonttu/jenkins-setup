def call(String tag = null) {
    if (tag) {
        checkout([
            $class: 'GitSCM',
            branches: [[name: "refs/tags/${tag}"]],
            userRemoteConfigs: scm.userRemoteConfigs,
            extensions: scm.extensions
        ])
    } else {
        checkout scm
    }
}