import aoc.string.AoCPart1StringTest

object Day25 extends App with AoCPart1StringTest {

  override def part1(strings: Seq[String]): String = {
    toSNAFU(strings.map(fromSNAFU).sum)
  }

  def toSNAFU(v: Long): String = {
    val mapper = Seq(("=", -2), ("-", -1), ("0", 0), ("1", 1), ("2", 2))
    (0 to 20).foldRight(Seq((v, ""))){ case (i, l) =>
      l.flatMap{ case e@(inV, outS) =>
        val divider = math.pow(5, i).toLong
        if(outS == "" && inV / divider == 0) {
          Seq(e)
        } else {
          mapper
            .map { case (c, m) => (inV - (m * divider), outS + c)}
            .filter(kv => math.abs(kv._1) <= divider)
        }
      }
    }
      .find(_._1 == 0)
      .map(_._2).get
  }

  def fromSNAFU(snafu: String): Long = {
    snafu.zipWithIndex.map { case (c, i) =>
      val v = math.pow(5, snafu.length - 1 - i)
      val out = c match {
        case '=' => -2 * v
        case '-' => -1 * v
        case _ => s"$c".toLong * v
      }
      out
    }.map(_.toLong).sum
  }
}
