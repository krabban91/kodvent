import Day21.neighbors2
import aoc.numeric.{AoCPart1Test, AoCPart2Test}
import com.google.gson.{Gson, JsonParser}
import implicits.Tuples._

import java.io.{DataOutputStream, File, FileOutputStream, FileWriter}
import java.util
import scala.collection.mutable

object Day21 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  //printResultPart2Test
  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val (map, start) = parse(strings)



    val value = Graph.stepAround[(Long, Long)](Seq(start), p => neighbors (p, map), 64)
      value.size

  }

  def primeFactors(stepGoal: Int):Seq[Int] = {
    var n = 2
    while ( stepGoal % n != 0) {
      n += 1
    }
    if (n == stepGoal) {
      Seq(stepGoal)
    } else {
      Seq(n) ++ primeFactors(stepGoal / n)
    }
  }


  override def part2(strings: Seq[String]): Long = {
    val (map, start) = parse(strings)
    val (w, h) = (strings.head.length, strings.length)
    println(s"node count: ${map.filter(kv => kv._2 == "." || kv._2 == "S").size}")
    val stepGoal = 26501365
    val factors = primeFactors(stepGoal)
    val stepSize = factors.dropRight(1).product
    val reachingDynamic = mutable.HashMap[(Long, Long), Map[(Long, Long), Set[(Long, Long)]]]()

    val out = (1 to factors.last).foldLeft(Map(start -> Set((0L, 0L)))) { case (out, time) =>
      println(time)
      move(out, reachingDynamic, map, w, h, stepSize)
    }
    out.map(kv => kv._2.size).sum
  }

  def newReaching(k : (Long, Long), map: Map[(Long, Long), String], w: Long, h: Long, stepSize: Long): Map[(Long, Long), Set[(Long, Long)]] = {
    //println(s"reaching for ${k}")
    Graph.stepAround[((Long, Long), (Long, Long))](Seq((k, (0L, 0L))), p => neighbors2(p, map, w, h), stepSize)
      .groupBy(_._1).map(kv => (kv._1, kv._2.map(_._2).toSet))
  }

  def reachingInterface(p: (Long, Long), map: Map[(Long, Long), String], w: Long, h: Long, stepSize: Long): Map[(Long, Long), Set[(Long, Long)]] = {

    import scala.jdk.CollectionConverters._

    val fileName = s"data/x${p._1}y${p._2}.json"
    val file = new File(fileName)
    val g = new Gson()
    val clazz = new util.HashMap[String, util.ArrayList[String]]().getClass
    if (file.exists()) {
      val name = scala.io.Source.fromFile(fileName)
      // read
      val value = name.getLines().toSeq.head
      name.close()
      println(s"$p\t reading json")
      g.fromJson(value, clazz).asScala.toMap.map{kv =>
        val tuple: (Long, Long) = parseTuple(kv._1)
        (tuple, kv._2.asScala.toSet.map(parseTuple))
      }
    } else {
      //write
      var writer: FileWriter = null
      try {
        writer = new FileWriter(fileName)
        val m = newReaching(p, map, w, h, stepSize)
        //println(s"$p\t writing json")
        val java = m.toSeq.map(kv => (stringTuple(kv._1), kv._2.map(stringTuple).asJava)).asJava
        g.toJson(java, writer)
        m
      } finally {
        if (writer != null) {
          writer.close()
        }
      }

    }
  }

  private def stringTuple(p: (Long, Long)): String = {
    s"${p._1},${p._1}"
  }

  private def parseTuple(str: String): (Long, Long) = {
    val spl = str.split(",");
    val tuple = (spl.head.toLong, spl.last.toLong)
    tuple
  }

  def move(current: Map[(Long, Long), Set[(Long, Long)]], reaching: mutable.HashMap[(Long, Long), Map[(Long, Long), Set[(Long, Long)]]], map: Map[(Long, Long), String], w: Long, h: Long, stepSize: Long): Map[(Long, Long), Set[(Long, Long)]] = {
    val tot = current.size
    var curr = 1
    val nextPoses = mutable.HashMap[(Long, Long), mutable.HashSet[(Long, Long)]]()
    current.foreach { case (p, maps) =>

      if (!reaching.contains(p)) {
        reaching.put(p, newReaching(p, map, w, h, stepSize))
        println(s"${curr} / $tot\t-> $p reached")
      }
      //val next = reachingInterface(p, map, w, h, stepSize)
      //val nn = reachingInterface(p, map, w, h, stepSize)
      val next = reaching(p)
      println(s"${curr} / $tot\t-> $p reached ${next.size} points")
      //val withOffsets =
      maps.foreach(m0 =>
        next.foreach { case (pos, m1) =>
          if(!nextPoses.contains(pos)) {
            nextPoses.put(pos, mutable.HashSet())
          }
          nextPoses(pos).addAll(m1.map(_ + m0))
        })
        //.groupBy(_._1).map{case (k, vs) => (k, vs.flatMap(_._2))}
      //println(s"${curr} / $tot\t-> $p grouped")
      curr += 1
      //withOffsets
    }
    //val value: Map[(Long, Long), Set[((Long, Long), Set[(Long, Long)])]] = v.toSet.groupBy(v => v._1)
    //println(s"all keys grouped (value.size=${value.size})")

    //val value1 = value.map { case (k, vs) => (k, vs.flatMap(_._2)) }
    //println(s"all keys reduced (value1.size=${value1.size})")
    //value1
    nextPoses.map(kv => (kv._1, kv._2.toSet)).toMap
  }


  def neighbors(p: (Long, Long), map: Map[(Long, Long), String]): Seq[((Long, Long), Long)] = {
    DIRECTIONS.map(_ + p).filter(map.get(_).exists(v => v == "." || v == "S")).map((_, 1))
  }

  def neighbors2(p: ((Long, Long), (Long, Long)), map: Map[(Long, Long), String], width: Long, height: Long): Seq[(((Long, Long), (Long, Long)), Long)] = {
    DIRECTIONS.map { d =>
      val v = d + p._1
      val newP = (v + (width, height)) % (width, height)
      val newMap = v match {
        case (x, y) if x < 0 => (p._2) + (-1L, 0)
        case (x, y) if y < 0 => (p._2) + (0, -1L)
        case (x, y) if x >= width => (p._2) + (1L, 0)
        case (x, y) if y >= height => (p._2) + (0, 1L)
        case point => (p._2)
      }
      (newP, newMap)
    }.filter(v => map.get(v._1).exists(v => v == "." || v == "S")).map((_, 1))
  }


  private def parse(strings: Seq[String]) = {
    val map = strings.zipWithIndex.flatMap { case (str, y) => str.zipWithIndex.map { case (c, x) => ((x.toLong, y.toLong), s"$c") } }.toMap
    val start = map.find(_._2 == "S").map(_._1).get

    (map, start)
  }
}
