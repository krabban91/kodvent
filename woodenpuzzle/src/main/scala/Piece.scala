case class Piece(input: String, index: Int, isRotated: Boolean) {

  private val parsed = {
    val lines = input.split("\n").filter(_.nonEmpty)
    lines.indices.map(i => (i, lines(i).indices.map(j => (j, lines(i)(j))).toMap)).toMap
  }

  private val grid = {
    parsed.flatMap { case (y, line) => line.map { case (x, c) => ((x, y), c) } }
  }

  private val maxY = parsed.keySet.max
  private val maxX = parsed(0).keySet.max

  def isCirclePiece: Boolean = {
    placingPosition.map(_._2).contains('o')
  }

  private def placingPosition = {
    parsed(0).toSeq.sortBy(_._1).find { case (_, c) => c != '-' }
  }

  def placeBy: Option[(Int, Int)] = {
    placingPosition.map { case (x, _) => (x, 0) }
  }

  def isRotatedCirclePiece: Boolean = {
    parsed(maxY).toSeq.sortBy(_._1).map(_._2).find(c => c != '-').contains('o')
  }


  def covers: Set[(Int, Int)] = {
    grid.filter(_._2 != '-').keySet
  }


  def pieceName: String = s"$index${if (isRotated) "D" else "U"}"

  def rotated: Piece = {
    val rotatedInput = input.split("\n").map(_.reverse).reverse.mkString("\n")
    Piece(rotatedInput, index, !isRotated)
  }

  override def toString: String = {
    val header = s"Piece #$pieceName"
    s"$header\n$input"
  }

  def isValid: Boolean = {
    covers.nonEmpty &&
      covers.forall { case (x, y) =>
        val c = grid((x, y))
        !Seq(grid.get((x - 1, y)), grid.get((x + 1, y)), grid.get((x, y - 1)), grid.get((x, y + 1))).flatten.contains(c)
      }
  }
}
