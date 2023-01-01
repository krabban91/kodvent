import aoc.numeric.{AoCPart1Test, AoCPart2Test}

import scala.collection.mutable

object Day17 extends App with AoCPart1Test with AoCPart2Test {

  override def part1(strings: Seq[String]): Long = fallingRocks(strings.head, 2022)

  override def part2(strings: Seq[String]): Long = fallingRocks(strings.head, 1000000000000L)

  private def fallingRocks(v: String, goalRocks: Long): Long = {
    val tower = mutable.HashMap[Int, Int]()
    val lockedTower = mutable.ListBuffer[Int]()
    val minX = 0
    val maxX = 7
    var inputIndex = 0
    var rockCount = 0L
    val allRocks = mutable.HashMap[Int, Seq[Int]]()
    while (rockCount < goalRocks) {
      val topOfTower = if (tower.isEmpty) 0 else tower.keys.min
      val startY = topOfTower - 3
      var currentRock = Tetris.newPiece(rockCount, startY)
      var moving = true

      while (moving) {
        val i = inputIndex % v.length
        v(i) match {
          case '<' =>
            if (!currentRock.collidesLeft(tower, minX)) {
              currentRock = currentRock.moveHorizontal(-1)
            }
          case '>' =>
            if (!currentRock.collidesRight(tower, maxX)) {
              currentRock = currentRock.moveHorizontal(1)
            }
        }

        inputIndex += 1

        if (currentRock.collidesDown(tower)) {
          moving = false
          currentRock.rock.foreach { case (y, v) =>
            tower.put(y, tower.getOrElse(y, 0) | v)
            allRocks.put(-y, allRocks.getOrElse(-y, Seq()) ++ Seq(rockCount.toInt + 1))
          }
          rockCount += 1

          currentRock.rock
            .map(_._1)
            .map(y => (y + 4, (y to y + 3).map(tower.getOrElse(_, 0)).reduce(_ | _)))
            .collectFirst { case (y, value) if value == 127 => y }
            .foreach(lockedPoint => {
              (tower.keys.max to lockedPoint by -1).foreach(i => tower.remove(i).foreach(lockedTower.append))
              findLoop(lockedTower).foreach { case (start, size) =>
                println(s"Loop found starting at $start. loop size: $size")
                logMap(tower)
                return calculateHeight(goalRocks, allRocks, start, size)
              }
            })
        } else {
          currentRock = currentRock.moveVertical(1)
        }
      }
    }
    val ys = tower.keys
    // building negatively
    -ys.min.toLong
  }

  def logMap(rocks: mutable.HashMap[Int, Int]): Unit = {
    println(s"Tower height: ${-rocks.keys.min}, current dropSize: ${rocks.size}. ")
    println("The mutable part of the tower:")
    rocks.toSeq.sortBy(_._1).foreach { case (i, v) =>
      val str = Integer.toBinaryString(v)
      println(f"${"0" * (7 - str.length)}${str} ${-i}")
    }
  }

  private def calculateHeight(goalRocks: Long, allRocks: mutable.HashMap[Int, Seq[Int]], start: Int, size: Int): Long = {
    val unitsBeforeLoop = start
    val startingRock = allRocks(start)
    val rocksBeforeLoop = startingRock.head
    val endingRock = allRocks(start + size)
    val rocksPerLoop = endingRock.head - startingRock.head
    val unitsPerLoop = size
    val countLoops = (goalRocks - rocksBeforeLoop) / rocksPerLoop.toLong
    val modRocks = (goalRocks - rocksBeforeLoop) % rocksPerLoop
    val modUnits = allRocks
      .filter(kv => kv._1 >= start && kv._1 <= (start + size))
      .toSeq
      .findLast(kv => kv._2.contains(startingRock.head + modRocks))
      .get
      ._1 - start
    val towerHeight = unitsBeforeLoop.toLong +
      unitsPerLoop.toLong * countLoops +
      (modUnits)
    towerHeight
  }

  def findLoop(lockedTower: mutable.ListBuffer[Int]): Option[(Int, Int)] = {
    val minLoopSize = 50
    if (lockedTower.size > minLoopSize * 2) {
      val chunk = lockedTower.takeRight(minLoopSize)
      val remainder = lockedTower.dropRight(minLoopSize)
      val ind = remainder.sliding(minLoopSize).toSeq.zipWithIndex.findLast(kv => kv._1 == chunk).map(_._2)
      ind.flatMap(i => {
        val newChunkStart = i + minLoopSize
        val biggerChunk = lockedTower.drop(newChunkStart)
        val chunkSize = biggerChunk.size
        val newRemainder = lockedTower.dropRight(chunkSize)
        val toFindIn = newRemainder.sliding(chunkSize).toSeq
        val found = toFindIn.zipWithIndex.findLast(kv => kv._1 == biggerChunk)
        found.map { case (_, start) => (start, chunkSize) }
      })
    } else None
  }

  trait Tetris {
    def height: Int

    def location: (Int, Int)

    def width: Int

    def moveVertical(y: Int): Tetris

    def moveHorizontal(x: Int): Tetris

    def shape: Seq[Int]

    def rock: Seq[(Int, Int)] = shape.zipWithIndex.map { case (v, dy) => (dy + location._2, v >> location._1) }

    def collidesDown(rockIntMap: mutable.HashMap[Int, Int]): Boolean = location._2 + height == 0 ||
      rock.exists { case (y, v) => (v & rockIntMap.getOrElse(y + 1, 0)) > 0 }

    def collidesLeft(rockIntMap: mutable.HashMap[Int, Int], minX: Int): Boolean = location._1 == minX ||
      rock.exists { case (y, v) => ((v << 1) & rockIntMap.getOrElse(y, 0)) > 0 }

    def collidesRight(rockIntMap: mutable.HashMap[Int, Int], maxX: Int): Boolean = location._1 + width == maxX ||
      rock.exists { case (y, v) => ((v >> 1) & rockIntMap.getOrElse(y, 0)) > 0 }
  }

  case class Minus(location: (Int, Int)) extends Tetris {
    val shape = Seq(Integer.parseInt("1111000", 2))

    def height: Int = 1

    def width: Int = 4


    override def moveVertical(y: Int): Tetris = Minus((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Minus((location._1 + x, location._2))
  }

  case class Plus(location: (Int, Int)) extends Tetris {
    val shape = Seq(
      Integer.parseInt("0100000", 2),
      Integer.parseInt("1110000", 2),
      Integer.parseInt("0100000", 2))

    def height: Int = 3

    def width: Int = 3

    override def moveVertical(y: Int): Tetris = Plus((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = Plus((location._1 + x, location._2))
  }

  case class ReverseL(location: (Int, Int)) extends Tetris {
    val shape = Seq(
      Integer.parseInt("0010000", 2),
      Integer.parseInt("0010000", 2),
      Integer.parseInt("1110000", 2))

    def height: Int = 3

    def width: Int = 3

    override def moveVertical(y: Int): Tetris = ReverseL((location._1, location._2 + y))

    override def moveHorizontal(x: Int): Tetris = ReverseL((location._1 + x, location._2))
  }

  case class Pipe(location: (Int, Int)) extends Tetris {
    val shape = Seq(
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
    val shape = Seq(
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
