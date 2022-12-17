import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart2Test
  printResultPart1
  printResultPart2

  //tetris

  override def part1(strings: Seq[String]): Long = {
    val v = strings.head
    fallingRocks(v, 2022)
  }

  override def part2(strings: Seq[String]): Long = {
    val v = strings.head
    fallingRocks(v, 1000000000000L)
  }

  private def fallingRocks(v: String, goalRocks : Long): Long = {
    //val rockMap = mutable.HashMap[(Int, Int), String]()
    val rockIntMap = mutable.HashMap[Int, Int]()

    val minX = 0
    val maxX = 7
    var inputIndex = 0
    var rockCount = 0L
    val loopFinder = mutable.ListBuffer[Int]()
    val allRocks = mutable.HashMap[Int, Seq[Int]]()
    val firstRock = mutable.HashMap[Int, Int]()
    //(minX until maxX).foreach(x => rockMap.put((x, 0), "_"))
    while (rockCount < goalRocks) {
      val startY = (if (rockIntMap.isEmpty) 0 else rockIntMap.keys.min) - 3
      var currentRock = Tetris.newPiece(rockCount, startY)
      var moving = true
      //println("new Rock")

      while (moving) {
        // move hor
        //logMap(rockMap, currentRock)
        val i = inputIndex % v.length
        v(i) match {
          case '<' =>
            if (!currentRock.collidesLeft(rockIntMap, minX, maxX)) {
              currentRock = currentRock.moveHorizontal(-1)
            }
          case '>' =>
            if (!currentRock.collidesRight(rockIntMap, minX, maxX)) {
              currentRock = currentRock.moveHorizontal(1)
            }
        }

        inputIndex += 1
        //logMap(rockMap, currentRock)

        if (currentRock.collidesDown(rockIntMap, minX, maxX)) {
          moving = false
          // update rockMap
          //currentRock.rock.foreach(rockMap.put(_, "#"))
          currentRock.numRock.foreach{ case (y, v) => rockIntMap.put(y, rockIntMap.getOrElse(y, 0) | v)}
          currentRock.numRock.map(_._1)
            .foreach(y => {
              val str = rockIntMap(y)
              rockIntMap.put(y, str | rockIntMap.getOrElse(y, 0))
              if (-y >= loopFinder.size) {
                loopFinder.append(str)
                allRocks.put(-y, allRocks.getOrElse(-y, Seq()) ++ Seq(rockCount.toInt + 1))
                firstRock.put(-y, rockCount.toInt + 1)

              } else {
                loopFinder.updated(-y, str)
                allRocks.put(-y, allRocks.getOrElse(-y, Seq()) ++ Seq(rockCount.toInt + 1))
              }

            })
        } else {
          currentRock = currentRock.moveVertical(1)
          //logMap(rockMap, currentRock)
        }
      }

      //logMap(rockMap, currentRock)
      rockCount += 1
      val offset = 11
      if (rockCount > offset) {
        val maxSize = (loopFinder.size - offset) / 3
        for (size <- (maxSize/2 to maxSize)) {
          for (start <- (0 to (maxSize - size))) {
            val first = loopFinder.slice(start + size * 0, start + size * 1)
            val rest = loopFinder.slice(start + size * 1, start + size * 2)
            val found = first == rest
            if (found) {
              val validation = loopFinder.slice(start + size * 2, start + size * 3)
              val validated = first == validation
              if (validated) {

                val unitsBeforeLoop = start // in rocks
                val rocksBeforeLoop = firstRock(start)
                val rocksPerLoop = firstRock(start + size) - firstRock(start)
                val unitsPerLoop = size
                val countLoops = (goalRocks - rocksBeforeLoop) / rocksPerLoop.toLong
                val modRocks = (goalRocks - rocksBeforeLoop) % rocksPerLoop
                val modUnits = allRocks // changing to first rock -> +2
                  .filter(kv => kv._1 >= start && kv._1 <= (start + size))
                  .toSeq
                  .findLast(kv => kv._2.contains(firstRock(start) + modRocks))
                  .get
                  ._1 - start
                val output = unitsBeforeLoop.toLong +
                  unitsPerLoop.toLong * countLoops.toLong +
                  (modUnits)
                println(s"Loop found at rock ${rockCount}. StartY Was $startY. Start=$start, size=$size")
                println(s"output $output")
                val diff = 1514285714288L - output
                println(s"diff $diff")
                return output
                println(s"presumed output ${output}")
              }
              // rockCount = rock 169
              // loopStart = unit 28, how many rocks?
              // loopSize  = unit 53, how many rocks?
              // loopEnd   = 28 + 53,
              // starty    = -264
              // rock 244
              // loopStart = unit 84,
              // loopSize  = unit 53
              // loopEnd   = 84 + 53
              // starty    = -377

              // rocks per loop 52 - 17 = 35
              // goal rocks = 1000000000000
              // rocks before = ?? rock 17 updated row -28
              // loops = (1000000000000-17) / 35 = 28 571 428 570
              // rest  = 33
              //169 + 15 => endRock=184, startY (264) to (285) = 21 diff = X

              // total units = (26) + loops * 53 + 15 * X
              // 13
              //firstRock(28) == 17
              //lastRock(28)  == 17
              //firstRock(28) == 52
              //lastRock(28)  == 17


              //689000000047  is too low
              //1999999999960 is too high
              //1999999999959 is too high
            }
          }
        }

      }
      ()
    }
    val ys = rockIntMap.keys
    // building negatively
    -ys.min.toLong
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

    def rock: Seq[(Int, Int)] = shape.map { case (x, y) => (x + location._1, y + location._2) }

    def moveVertical(y: Int): Tetris

    def moveHorizontal(x: Int): Tetris

    def shape: Seq[(Int, Int)]

    def numShape: Seq[Int]
    def numRock: Seq[(Int,Int)] = numShape.zipWithIndex.map{ case (v, dy) => (dy + location._2, v >> location._1)}
    def collidesDown(rockIntMap: mutable.HashMap[Int, Int], minX: Int, maxX: Int): Boolean = {
      if (location._2 + height == 0) {
        true
      } else {
        Seq("").zipWithIndex

        val oBool = numRock.exists{case (y, v) => (v & rockIntMap.getOrElse(y + 1, 0)) > 0}

        //val bool = rock.map { case (x, y) => (x, y + 1) }.exists(rockMap.contains)
        oBool
      }
    }

    def collidesLeft(rockIntMap: mutable.HashMap[Int, Int], minX: Int, maxX: Int): Boolean = {
      if (location._1 == minX) {
        true
      } else {
        val oBool = numRock.exists{case (y, v) => ((v<<1) & rockIntMap.getOrElse(y, 0)) > 0}
        //val bool = rock.map { case (x, y) => (x - 1, y) }.exists(rockMap.contains)
        oBool

      }
    }

    def collidesRight(rockIntMap: mutable.HashMap[Int, Int], minX: Int, maxX: Int): Boolean = {
      if (location._1 + width == maxX) {
        true
      } else {
        val oBool = numRock.exists{case (y, v) => ((v>>1) & rockIntMap.getOrElse(y, 0)) > 0}
        //rock.map { case (x, y) => (x + 1, y) }.exists(rockMap.contains)
        oBool
      }
    }
  }

  case class Minus(location: (Int, Int)) extends Tetris {
    val shape = Seq((0, 0), (1, 0), (2, 0), (3, 0))
    val numShape = Seq(Integer.parseInt("1111000", 2))

    def height: Int = 1

    def width: Int = 4


    override def moveVertical(y: Int): Tetris = Minus((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Minus((location._1 + x, location._2))
  }

  case class Plus(location: (Int, Int)) extends Tetris {
    val shape = Seq((1, 0), (0, 1), (1, 1), (2, 1), (1, 2))
    val numShape = Seq(
      Integer.parseInt("0100000", 2),
      Integer.parseInt("1110000", 2),
      Integer.parseInt("0100000", 2))

    def height: Int = 3

    def width: Int = 3

    override def moveVertical(y: Int): Tetris = Plus((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Plus((location._1 + x, location._2))
  }

  case class ReverseL(location: (Int, Int)) extends Tetris {
    val shape = Seq((2, 0), (2, 1), (0, 2), (1, 2), (2, 2))
    val numShape = Seq(
      Integer.parseInt("0010000", 2),
      Integer.parseInt("0010000", 2),
      Integer.parseInt("1110000", 2))

    def height: Int = 3

    def width: Int = 3

    override def moveVertical(y: Int): Tetris = ReverseL((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = ReverseL((location._1 + x, location._2))
  }

  case class Pipe(location: (Int, Int)) extends Tetris {
    val shape = Seq((0, 0), (0, 1), (0, 2), (0, 3))
    val numShape = Seq(
      Integer.parseInt("1000000", 2),
      Integer.parseInt("1000000", 2),
      Integer.parseInt("1000000", 2),
      Integer.parseInt("1000000", 2))

    def height: Int = 4

    def width: Int = 1

    override def moveVertical(y: Int): Tetris = Pipe((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Pipe((location._1 + x, location._2))
  }

  case class Box(location: (Int, Int)) extends Tetris {
    val shape = Seq((0, 0), (0, 1), (1, 0), (1, 1))
    val numShape = Seq(
      Integer.parseInt("1100000", 2),
      Integer.parseInt("1100000", 2))

    def height: Int = 2

    def width: Int = 2

    override def moveVertical(y: Int): Tetris = Box((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Box((location._1 + x, location._2))
  }

  object Tetris {
    private val shapes: Seq[Tetris] = Seq(Minus((2, 0)), Plus((2, 0)), ReverseL((2, 0)), Pipe((2, 0)), Box((2, 0)))

    def newPiece(i: Long, y: Int): Tetris = {
      val start = shapes((i % shapes.length).toInt)
      start.moveVertical(y - start.height)
    }
  }
}
