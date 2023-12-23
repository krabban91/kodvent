import scala.collection.mutable

object LogMap {

  def printMapWithPath[V, V2](map: Map[(Long, Long), V], path: Seq[V2], converter1: V => String, converter2: V2 => ((Long, Long), String),  default: String = "."): Unit = {
    val newMap = mutable.HashMap[(Long, Long), String]()
    newMap.addAll(map.map{case (k, e) => (k, converter1(e))})
    newMap.addAll(path.map(converter2))
    printMap[String](newMap.toMap, identity, default)
  }

  def printMap[V](map: Map[(Long, Long), V], v: V => String, default: String = "."): Unit = {
    val (minX, maxX) = (map.map(_._1._1).min, map.map(_._1._1).max)
    val (minY, maxY) = (map.map(_._1._2).min, map.map(_._1._2).max)
    val strB = new StringBuilder()

    val yWidth = maxY.toString.length
    val ySpace = " " * (yWidth + 1)
    //header
    var xdPow = 6
    while (math.pow(10, xdPow).toInt > 0) {
      if (maxX / math.pow(10, xdPow).toInt > 0) {
        strB.append(ySpace)
        (minX to maxX).foreach { x =>
          val l = (x % math.pow(10, xdPow + 1)).toInt / math.pow(10, xdPow).toInt
          strB.append(l)
        }
        strB.append("\n")
      }
      xdPow -= 1
    }
    strB.append("\n")

    (minY to maxY).foreach { y =>
      strB.append(s"$y".padTo(yWidth + 1, ' '))
      // line
      (minX to maxX).foreach { x =>
        strB.append(map.get((x, y)).map(v).getOrElse(default))
      }
      strB.append("\n")

    }


    println(strB)
  }
}
