import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day06 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val input = strings.head.split(",").map(_.toInt)
    var fishes = input.map(new LanternFish(_))
    println(s"Initial state: ${fishes.foldLeft("")((a,b)=> (s"$a,${b.curr}"))}")
    val days = 80
    for(day <- 1 to 80){
      val newFishes = fishes.flatMap(_.incDay()).toSeq
      fishes = fishes ++ newFishes
      //println(s"After \t$day days: ${fishes.foldLeft("")((a,b)=> (s"$a,${b.curr}"))}")
    }
    fishes.length
  }

  class LanternFish(initVal: Int){

    var curr: Int = initVal

    def incDay(): Option[LanternFish] = {
      curr -= 1
      if (curr < 0){
        curr = 6
        Option(new LanternFish(8))
      } else{
        None
      }
    }
  }

  override def part2(strings: Seq[String]): Long = -1
}
