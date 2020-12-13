import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day13 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val start = strings.head.toInt
    var time = start
    val buses = strings(1).split(",").filterNot(_ == "x").map(_.toInt).map(Bus)
    while(!buses.exists(_.departs(time))){
      time += 1
    }
    buses.find(_.departs(time)).get.id * (time - start)
  }

  override def part2(strings: Seq[String]): Long = -1

  case class Bus(id: Int){

  def departs(time: Int):Boolean = time%id == 0
  }

}
