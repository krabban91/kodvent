import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2


  override def part1(strings: Seq[String]): Long = {
    val bags = strings.map(ColorCodedBag(_))
    bags.count(canContain(_, bags.find(b => b.name == "shiny gold").get, bags)) - 1
  }

  override def part2(strings: Seq[String]): Long = {
    val bags = strings.map(ColorCodedBag(_))
    countBagsRecursive(bags.find(b => b.name == "shiny gold").get, bags) - 1
  }

  private def canContain(b: ColorCodedBag, shiny: ColorCodedBag, bags: Seq[ColorCodedBag]): Boolean = {
    val st = mutable.Stack[ColorCodedBag]()
    st.push(b)
    while (st.nonEmpty) {
      val bag = st.pop()
      if (bag.equals(shiny)) {
        return true
      }
      bag.contains.foreach(bb => {
        bags.find(i => i.name == bb._1).map(st.push)
      })
    }
    false
  }

  private def countBags(bag: ColorCodedBag, bags: Seq[ColorCodedBag]): Long = {
    val uncounted = mutable.Stack[ColorCodedBag]()
    uncounted.push(bag)
    var count = 0L
    while (uncounted.nonEmpty) {
      val bag = uncounted.pop()
      count += 1
      bag.contains.foreach(bb => {
        bags.find(i => i.name == bb._1).map(i => Range(0, bb._2).foreach(_ => uncounted.push(i)))
      })
    }
    count
  }

  private def countBagsRecursive(bag: ColorCodedBag, bags: Seq[ColorCodedBag]): Long = 1L + bag.contains
    .map(bb => bags
      .find(i => i.name == bb._1)
      .map(i => bb._2 * countBagsRecursive(i, bags))
      .getOrElse(0L))
    .sum

  case class ColorCodedBag(name: String, contains: Map[String, Int])

  object ColorCodedBag {
    def apply(string: String): ColorCodedBag = {
      val name = string.split("bags contain")(0).trim.replace("bag", "").replace("bags", "")
      val c = string.split("bags contain")(1).trim
      if (c.equals("no other bags.")) {
        ColorCodedBag(name, Map())
      } else {
        ColorCodedBag(name, c.split(",").map(_.trim).map(s => {
          val ss = s.split("\\s+", 2)
          ss(1).replace("bags", "").replace("bag", "").replace(".", "").trim -> ss(0).trim.toInt
        }).toMap)
      }
    }
  }

}
