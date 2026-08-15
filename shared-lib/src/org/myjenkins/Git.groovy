package org.myjenkins

class Git implements Serializable {

    static String sanitizeBranchName(String name) {
        return name.replaceAll('[^a-zA-Z0-9._-]', '-')
    }
}