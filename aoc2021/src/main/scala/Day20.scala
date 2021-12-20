import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.grid.Grid

import java.util.stream.Collectors
import scala.jdk.CollectionConverters.{CollectionHasAsScala, SeqHasAsJava}

object Day20 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (imageEnhancer, initImage) = imageContent(strings)
    enhance(initImage, imageEnhancer, 2).sum(i => i)
  }

  override def part2(strings: Seq[String]): Long = {
    val (imageEnhancer, initImage) = imageContent(strings)
    enhance(initImage, imageEnhancer, 50).sum(i => i)
  }

  private def imageContent(strings: Seq[String]): (Seq[Int], Grid[Int]) = {
    val imageEnhancer = strings.head.chars().mapToObj(c => if (c == '#') 1 else 0)
      .collect(Collectors.toList[Int]()).asScala.toSeq
    val initImage: Grid[Int] = new Grid[Int](strings.tail.tail
      .map(s => s.chars().mapToObj(c => if (c == '#') 1 else 0)
        .collect(Collectors.toList[Int]())).toList.asJava)
    (imageEnhancer, initImage)
  }

  def enhance(image: Grid[Int], imageEnhancer: Seq[Int], times: Int): Grid[Int] = {
    (1 to times).foldLeft(image)((img, i) => step(img, imageEnhancer, 2, i % 2 == 0))
  }

  def step(image: Grid[Int], imageEnhancer: Seq[Int], expandWith: Int, shrink: Boolean): Grid[Int] = {
    val filler = if (imageEnhancer.head == 1) (if (shrink) imageEnhancer.head else imageEnhancer.last) else 0
    val curr = image.expand(_ => filler, expandWith)
    val next = curr.map((_, p) => imageEnhancer(Integer.parseInt(curr.getSurroundingTilesWithPoints(p, true).asScala.map(_.getValue.toString).reduce(_ + _), 2)))
    if (shrink) {
      next.shrink((_, p) => p.x >= expandWith && p.x <= (next.width() - expandWith - 1) && p.y >= expandWith && p.y <= (next.height() - expandWith - 1))
    } else {
      next
    }
  }

}
