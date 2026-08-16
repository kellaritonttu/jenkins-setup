import org.myjenkins.Docker

def call() {
    new Docker(this).logout()
}