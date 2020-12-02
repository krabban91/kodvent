import scala.io.Source

trait Inputs {

  def getInput: Seq[String] = {
    val source = Source.fromResource(s"${this.getClass.getName.toLowerCase}.txt")
    try {
      source.getLines().toSeq
    } finally {
      source.close()
    }
  }
}
