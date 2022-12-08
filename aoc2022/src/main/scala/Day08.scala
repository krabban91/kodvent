import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable
object Day08 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {


    val v = strings.map(_.map(_.toInt).map(_ - 48).map(Tree))
    val zipped = v.zipWithIndex.map(t => (t._1.zipWithIndex, t._2))
    val visible = zipped.map { case (l, y) => l.filter { case (tree, x) =>
      val left = l.zipWithIndex.filter(_._2 < x).map(_._1).map(_._1)
      val right = l.zipWithIndex.filter(_._2 > x).map(_._1).map(_._1)
      val mutUp = mutable.ListBuffer[Tree]()
      val mutDown = mutable.ListBuffer[Tree]()
      zipped.foreach(t => {
        val o = t._1(x)._1
        if (t._2 < y) {
          mutUp.append(o)
        } else if (t._2 > y) {
          mutDown.append(o)
        }
      })
      //val up = zipped.flatMap(t => t._1.filter(_._2 < y).map(_._1))
      //val down = zipped.flatMap(t => t._1.filter(_._2 > y).map(_._1))

      Seq(left, right, mutUp, mutDown).exists(other => other.forall(p =>p.height < tree.height))
    }
    }
    visible.map(_.count(_=>true)).sum
    // 1300 is wrong
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  case class Tree(height: Int)
}
