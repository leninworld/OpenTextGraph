package crawler


class convertGBAD2JSON {
  def hello() { println("Hello (class)") } // [1]
}

object convertGBAD2JSON extends  App{

    def hallo() {
      println("Hallo (object)")
    } // [2]

    def hello() {
      println("Hello (object)")
    } // [3]

    hallo()

}