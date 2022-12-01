import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day01 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = {
    var sum = 0
    var max = 0
    strings.foreach(s => {
      if(s == ""){
        if (sum > max){
          max = sum
        }
        sum = 0
      } else {
        sum += s.toInt
      }
    })

    max
  }

  override def part2(strings: Seq[String]): Long = {
    var sum = 0
    val max = mutable.ListBuffer[Int]()
    strings.foreach(s => {
      if(s == ""){
        if (max.size<3){
          max.append(sum)
        }
        else if (max.exists(v => v<sum)){
          val toRemove = max.min
          max.remove(max.indexOf(toRemove))
          max.append(sum)

        }
        sum = 0
      } else {
        sum += s.toInt
      }
    })
    max.sum
  }
}
