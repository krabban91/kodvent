case class Puzzle(input: String) {

  private def toLines(s: String): Seq[String] = {
    s.split("\n").filter(_.nonEmpty)
  }

  private val lines: Seq[String] = toLines(input)


  private val parsed = {
    val l = lines
    l.indices.map(y => (y, l(y).indices.map(x => (x, l(y)(x))).toMap)).toMap
  }

  private val grid = {
    parsed.flatMap { case (y, line) => line.map { case (x, c) => ((x, y), c) } }
  }

  def nextPosition: Option[(Int, Int)] = {
    next.map(_._1)
  }

  def nextIsCirclePiece: Boolean = {
    nextPosition.exists { case (x, y) => grid((x - 1, y)) != 'o' }
  }

  private def next = {
    lines.zipWithIndex.flatMap { case (l, y) => l.zipWithIndex.find{ case (c, x) => c== '-'}.map{case (c, x) => ((x, y), c)}}.headOption
  }

  def canPlace(piece: Piece): Boolean = {
    val (x, y) = nextPosition.get
    val (ddx, ddy) = piece.placeBy.get
    piece.covers.forall { case (dx, dy) => grid.get((x - ddx + dx, y - ddy + dy)).contains('-') }
  }

  def place(piece: Piece): Puzzle = {
    val (x, y) = nextPosition.get
    val sb = new StringBuilder()
    val before = lines.take(y)
    before.foreach { l =>
      sb.append(l)
      sb.append("\n")
    }
    val ddx = piece.placeBy.map(_._1).get
    val pl = toLines(piece.input)
    pl.indices.foreach { dy =>
      val l = pl(dy)
      sb.append(lines(y + dy).substring(0, x - ddx))
      l.indices.foreach { dx =>
        val c = if (l(dx) == '-') lines(y + dy)(x - ddx + dx) else l(dx)
        sb.append(c)
      }
      sb.append(lines(y + dy).substring(x - ddx + l.length))
      sb.append("\n")
    }
    val after = lines.drop(y + pl.size)
    after.foreach { l =>
      sb.append(l)
      sb.append("\n")
    }
    Puzzle(sb.toString.stripSuffix("\n"))
  }


}
