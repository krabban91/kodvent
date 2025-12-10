import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day10 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val machines = strings.map(Machine(_))
    val started = machines.map(optimizeStartup)
    started.map(_.buttonPresses.values.sum).sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }


  def optimizeStartup(input: Machine): Machine = {
    val candidates = mutable.PriorityQueue[Machine]()(Ordering.by(m => -m.distance))
    val visited = mutable.HashSet[Machine]()
    candidates.addOne(input)
    while (candidates.nonEmpty) {
      val pop = candidates.dequeue()
      if (!visited(pop)) {
        visited.add(pop)
        if (pop.lights == pop.target) {
          return pop
        }
        val next = pop.buttonPresses.keys.map(b => pop.press(b))
          .filter(!visited(_))
        candidates.addAll(next)
      }
    }
    null
  }


  case class Button(activates: Seq[Int])

  case class Machine(lights: Map[Int, Boolean], target: Map[Int, Boolean], buttonPresses: Map[Button, Long], joltage: Map[Int, Long]) {

    def press(button: Button): Machine = {
      val adjustedLights = lights.map{ case (i, v) => (i, if (button.activates.contains(i)) !v else v)}
      val pressed = buttonPresses.map{ case (b, c) => (b, if (b == button) c + 1 else c)}
      this.copy(lights = adjustedLights, buttonPresses = pressed)
    }

    def cost: Long = this.buttonPresses.values.sum
    def heuristic: Long = this.lights.map{ case (i, v) => if (v == target(i)) 0 else 1 }.sum
    def distance: Long = cost // + heuristic
  }

  object Button {
    def apply(str: String): Button = {
      Button(str.stripPrefix("(").stripSuffix(")").split(",").map(_.toInt))
    }
  }

  object Machine {
    def apply(str: String): Machine = {
      val words = str.split(" ")
      val targetLights = words.head.stripPrefix("[").stripSuffix("]").zipWithIndex.map { case (c, i) => (i, c == '#') }.toMap
      val lights = targetLights.map(v => (v._1, false))
      val buttons = words.drop(1).dropRight(1).map(Button(_)).map((_, 0L)).toMap
      val joltage = words.last.stripSuffix("}").stripPrefix("{").split(",").zipWithIndex.map { case (s, i) => (i, s.toLong) }.toMap
      Machine(lights, targetLights, buttons, joltage)
    }
  }

}
