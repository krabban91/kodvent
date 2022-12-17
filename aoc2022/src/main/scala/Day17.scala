import Day17.Tetris.shapes
import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import krabban91.kodvent.kodvent.utilities.logging.LogUtils

import java.awt.Point
import scala.collection.mutable
import scala.jdk.CollectionConverters.MapHasAsJava

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  //tetris

  override def part1(strings: Seq[String]): Long = {
    val rockMap = mutable.HashMap[(Int, Int), String]()

    val minX = 0
    val maxX = 7
    val v = strings.head
    var inputIndex = 0
    var rockCount = 0
    (minX until maxX).foreach(x =>rockMap.put((x,0), "_"))
    while (rockCount < 2022){
      val startY = (if(rockMap.isEmpty) 0 else rockMap.keys.map(_._2).min) - 3
      var currentRock = Tetris.newPiece(rockCount, startY)
      var moving = true
      //println("new Rock")

      while (moving){
        // move hor
        logMap(rockMap, currentRock)
        val i = inputIndex % v.length
        v(i) match {
          case '<' =>
            if (!currentRock.collidesLeft(rockMap, minX, maxX)) {
              currentRock = currentRock.moveHorizontal(-1)
            }
          case '>' =>
            if (!currentRock.collidesRight(rockMap, minX, maxX)) {
              currentRock = currentRock.moveHorizontal(1)
            }
        }

        inputIndex += 1
        logMap(rockMap, currentRock)

        if (currentRock.collidesDown(rockMap, minX, maxX)){
          moving = false
          // update rockMap
          currentRock.rock.foreach(rockMap.put(_,"#"))
        } else {
          currentRock = currentRock.moveVertical(1)
          logMap(rockMap, currentRock)
          /*
          if (currentRock.collidesDown(rockMap, minX, maxX)) {
            moving = false
            // update rockMap
            currentRock.rock.foreach(rockMap.put(_,"#"))
          }
          */
        }
      }

      logMap(rockMap, currentRock)
      rockCount += 1
    }
    val ys = rockMap.keys.map(_._2)
    // building negatively
    - ys.min
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  private def logMap(rockMap: mutable.HashMap[(Int, Int), String], rock: Tetris) = {
    /*

    val other = mutable.HashMap.from(rockMap)
    rock.rock.foreach(v => if (!other.contains(v)) other.put(v, "@") else other.put(v, "X"))
    val java = other.map(kv => (new Point(kv._1._1, kv._1._2), kv._2)).asJava
    println(new LogUtils[String].mapToText(java, v => if (v == null) "." else v))
     */
  }


  trait Tetris {
    def height: Int
    def location: (Int, Int)
    def width: Int
    def rock: Seq[(Int, Int)] = shape.map{case (x, y) => (x+location._1, y + location._2)}
    def moveVertical(y: Int): Tetris
    def moveHorizontal(x: Int): Tetris
    def shape: Seq[(Int, Int)]

    def collidesDown(rockMap: mutable.HashMap[(Int, Int), String], minX: Int, maxX: Int): Boolean = {
      if (location._2 + height == 0) {
        true
      } else {
        rock.map{case (x, y) => (x, y+1)}.exists(rockMap.contains)
      }
    }
    def collidesLeft(rockMap: mutable.HashMap[(Int, Int), String], minX: Int, maxX: Int): Boolean = {
      if (location._1 == minX) {
        true
      } else {
        rock.map{case (x, y) => (x-1, y)}.exists(rockMap.contains)
      }
    }
    def collidesRight(rockMap: mutable.HashMap[(Int, Int), String], minX: Int, maxX: Int): Boolean = {
      if (location._1 + width == maxX){
        true
      } else {
        rock.map{case (x, y) => (x+1, y)}.exists(rockMap.contains)

      }
    }
  }

  case class Minus(location: (Int, Int)) extends Tetris {
    val shape = Seq((0,0), (1,0), (2,0), (3,0))
    def height: Int = 1
    def width: Int = 4


    override def moveVertical(y: Int): Tetris = Minus((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Minus((location._1 + x, location._2))
  }
  case class Plus(location: (Int, Int)) extends Tetris {
    val shape = Seq((1,0), (0,1), (1,1), (2,1), (1,2))
    def height: Int = 3
    def width: Int = 3

    override def moveVertical(y: Int): Tetris = Plus((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Plus((location._1 + x, location._2))
  }

  case class ReverseL(location: (Int, Int)) extends Tetris {
    val shape = Seq((2,0), (2,1), (0,2), (1,2), (2,2))
    def height: Int = 3
    def width: Int = 3

    override def moveVertical(y: Int): Tetris = ReverseL((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = ReverseL((location._1 + x, location._2))
  }

  case class Pipe(location: (Int, Int)) extends Tetris {
    val shape = Seq((0,0), (0,1), (0,2), (0,3))
    def height: Int = 4
    def width: Int = 1

    override def moveVertical(y: Int): Tetris = Pipe((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Pipe((location._1 + x, location._2))
  }

  case class Box(location: (Int, Int)) extends Tetris {
    val shape = Seq((0,0), (0,1), (1,0), (1,1))
    def height: Int = 2
    def width: Int = 2

    override def moveVertical(y: Int): Tetris = Box((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Box((location._1 + x, location._2))
  }

  object Tetris {
    private val shapes: Seq[Tetris] = Seq(Minus((2,0)), Plus((2,0)), ReverseL((2,0)), Pipe((2,0)), Box((2,0)))
    def newPiece(i: Int, y: Int): Tetris = {
      val start = shapes(i%shapes.length)
      start.moveVertical(y - start.height)
    }
  }
}
