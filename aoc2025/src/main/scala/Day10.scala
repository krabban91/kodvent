import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable
import scala.collection.parallel.CollectionConverters._

object Day10 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val machines = strings.map(Machine(_))
    val started = machines.par.map(optimizeStartup)
    started.map(_.buttonPresses.values.sum).sum
  }

  override def part2(strings: Seq[String]): Long = {
    val machines = strings.map(Machine(_))
    val started = machines.map(optimizeJoltage)
    started.map(_.buttonPresses.values.sum).sum
  }


  def optimizeStartup(input: Machine): Machine = {
    val candidates = mutable.PriorityQueue[(Machine, Long)]()(Ordering.by(t => -t._2))
    val visited = mutable.HashSet[Map[Button, Long]]()
    candidates.addOne((input, 0L))
    while (candidates.nonEmpty) {
      val t@(pop, cost) = candidates.dequeue()
      if (!visited(pop.buttonPresses)) {
        visited.add(pop.buttonPresses)
        if (pop.lights == pop.target) {
          return pop
        }
        val next = pop.buttonPresses.keys.par.map(b => pop.press(b))
          .filterNot(m => visited(m.buttonPresses))
        candidates.addAll(next.map(m => (m, m.distance)))
      }
    }
    null
  }

  def optimizeJoltage(input: Machine): Machine = {
    val candidates = mutable.PriorityQueue[(Machine, Long)]()(Ordering.by(t => -t._2))
    val visited = mutable.HashSet[Map[Button, Long]]()
    candidates.addOne((input, 0L))
    while (candidates.nonEmpty) {
      val t@(pop, cost) = candidates.dequeue()
      if (!visited(pop.buttonPresses)) {
        visited.add(pop.buttonPresses)
        val vSize = visited.size
        val u = 100000
        if(vSize % u == 0) {
          println(f"Machine ${input.printTargetLights}%s: Visited ${vSize/u*0.1}%2.1fmil candidates. prio-queue: ${candidates.size/100/10.0}%2.1fk")
        }
        if (pop.joltage == pop.targetJoltage) {
          println(s"Machine ${input.printTargetLights}: Found optimal Joltage: [${pop.buttonPresses.values.mkString(",")}]")
          return pop
        }
        val next = pop.buttonPresses.keys.par.flatMap{b =>
          val max = pop.maxPressCount(b)
          val steps = Seq(1,5,10,25,50,75,100).filter(_ <= max)
          steps.par.map(i => (0 until i).foldLeft(pop){case (v, _) => v.press(b)})
        }.filterNot(m => visited(m.buttonPresses)).toSeq
          .filter(_.stillPossible)
        candidates.addAll(next.map(m =>(m, m.distanceJoltage)))
      }
    }
    null
  }


  case class Button(activates: Seq[Int])

  case class Machine(lights: Map[Int, Boolean], target: Map[Int, Boolean], buttonPresses: Map[Button, Long], joltage: Map[Int, Long], targetJoltage: Map[Int, Long]) {

    def printTargetLights: String = {
      s"[${target.map(v => if(v._2) '#' else '.').mkString("")}]"
    }

    def press(button: Button): Machine = {
      val adjustedLights = lights.map{ case (i, v) => (i, if (button.activates.contains(i)) !v else v)}
      val pressed = buttonPresses.map{ case (b, c) => (b, if (b == button) c + 1 else c)}
      val adjustedJoltage = joltage.map{ case (i, v) => (i, if (button.activates.contains(i)) v + 1 else v)}

      this.copy(lights = adjustedLights, joltage = adjustedJoltage, buttonPresses = pressed)
    }

    def maxPressCount(button: Button): Long = {
      button.activates.map{ i =>
        targetJoltage(i) - joltage(i)
      }.min
    }

    def stillPossible: Boolean = {
      joltage.forall{ case (i, v) => v <= targetJoltage(i) }
    }

    def cost: Long = this.buttonPresses.values.sum
    def heuristic: Long = this.lights.map{ case (i, v) => if (v == target(i)) 0 else 1 }.sum / this.buttonPresses.keys.map(_.activates.size).max
    def heuristicJoltage: Long = this.joltage.map{ case (i, v) => (targetJoltage(i) - v)}.sum / 2 // buttonPresses.keys.map(_.activates.size).max
    def distance: Long = cost + heuristic
    def distanceJoltage: Long = cost + heuristicJoltage
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
      val targetJoltage = words.last.stripSuffix("}").stripPrefix("{").split(",").zipWithIndex.map { case (s, i) => (i, s.toLong) }.toMap
      val joltage = targetJoltage.map(v => (v._1, 0L))

      Machine(lights, targetLights, buttons, joltage, targetJoltage)
    }
  }

}
