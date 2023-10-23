import scala.collection.mutable

case class Puzzle(input: String) {

  private def toLines(s: String): Seq[String] = {
    s.split("\n").filter(_.nonEmpty)
  }



  private val lines: Seq[String] = toLines(input)

  def step: Int = {
    val w = lines(0).length
    nextPosition.map{ case (x, y) => x + y * w}.getOrElse(input.length)
  }
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
    lines.zipWithIndex.flatMap { case (l, y) => l.zipWithIndex.find { case (c, x) => c == '-' }.map { case (c, x) => ((x, y), c) } }.headOption
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


  def hasSolution(pieces: Seq[Piece]): Boolean = {
    val zones = placeableZones
    val tooSmall = () => !zones.forall(z => pieces.exists(p => p.covers.size <= z.size))
    val wideEnough = {
      val holes = zones.map { z =>
        val xs = z.map(_._1)
        xs.max - xs.min + 1
      }
      holes.forall(w => pieces.exists { p => val xs = p.covers.map(_._1); xs.max - xs.min < w })
    }
    val highEnough = {
      val holes = zones.map { z =>
        val ys = z.map(_._2)
        ys.max - ys.min + 1
      }
      holes.forall(h => pieces.exists { p => val ys = p.covers.map(_._2); ys.max - ys.min < h })
    }
    val largeEnough  = () =>  {
      val holes = zones.map { z =>
        val ys = z.map(_._2)
        val xs = z.map(_._1)
        (xs.max - xs.min + 1, ys.max - ys.min + 1)
      }
      holes.forall { case (w, h) => pieces.exists { p =>
        val xs = p.covers.map(_._1);
        val ys = p.covers.map(_._2);
        ys.max - ys.min + 1 <= h && xs.max - xs.min + 1 <= w
      }
      }
    }
    val pieceFits = true
    !tooSmall() && pieceFits && largeEnough()// && wideEnough && highEnough
  }

  private def placeableZones: Set[Set[(Int, Int)]] = {
    var zones = Set[mutable.HashSet[(Int, Int)]]()
    lines.zipWithIndex.foreach { case (l, y) => l.zipWithIndex.foreach { case (c, x) =>
      if (c == '-') {
        val left = if (x > 0) {
          Some(lines(y)(x - 1))
        } else None
        val up = if (y > 0) {
          Some(lines(y - 1)(x))
        } else None
        if (Seq(left, up).flatten.contains('-')) {
          // same zone
          val same = zones.filter(z => z.contains((x - 1, y)) || z.contains((x, y - 1))).toSeq
          if (same.size > 1) {
            same.tail.foreach(same.head.addAll)
            zones = zones.filterNot(s => same.tail.contains(s))
          }
          same.head.add((x, y))
        } else {
          // new zone
          val newZone = mutable.HashSet[(Int, Int)]()
          newZone.add((x, y))
          zones = zones ++ Set(newZone)
        }
      }
    }
    }
    zones.map(_.toSet).toSet
  }

  override def toString: String = {
    val open = unCovered
    val dims = s"${lines.head.length}x${lines.length}"
    s"Puzzle, size=($dims), open=${open}\n$input"

  }

  def unCovered: Int = {
    lines.map(_.count(_ == '-')).sum
  }
}
