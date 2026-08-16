package org.myjenkins

class Git implements Serializable {

    static String authenticatedUrl(String repo, String user, String token) {
        return "https://${user}:${token}@${repo.replace('https://', '')}"
    }
}