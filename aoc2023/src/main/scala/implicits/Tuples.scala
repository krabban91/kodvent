package implicits

object Tuples {

  final val ZERO2: (Long, Long) = (0L, 0L)
  final val NORTH: (Long, Long) = (0L, -1L)
  final val WEST: (Long, Long) = (-1L, 0L)
  final val SOUTH: (Long, Long) = (0L, 1L)
  final val EAST: (Long, Long) = (1L, 0L)
  final val DIRECTIONS: Seq[(Long, Long)] = Seq(NORTH, SOUTH, WEST, EAST)


  implicit class RichTuples2Longs(val l: (Long, Long)) extends AnyVal {


    def to(r: (Long, Long)): Seq[(Long,Long)] = (0L to r._1)
      .flatMap(x => (0L to r._2).map(y => (x, y)))

    def until(r: (Long, Long)): Seq[(Long, Long)] = (0L until  r._1)
      .flatMap(x => (0L until r._2).map(y => (x, y)))

    def add(r: (Long, Long)): (Long, Long) = (l._1 + r._1, l._2 + r._2)

    def +(r: (Long, Long)): (Long, Long) = l add r

    def sub(r: (Long, Long)): (Long, Long) = (l._1 - r._1, l._2 - r._2)

    def -(r: (Long, Long)): (Long, Long) = l sub r

    def delta(r: (Long, Long)): (Long, Long) = (math.abs(l._1 - r._1), math.abs(l._2 - r._2))

    def manhattan(r: (Long, Long)): Long = {
      val (x, y) = l delta r
      x + y
    }

    def mul(r: (Long, Long)): (Long, Long) = (l._1 * r._1, l._2 * r._2)

    def *(r: (Long, Long)): (Long, Long) = l mul r

    def div(r: (Long, Long)): (Long, Long) = (l._1 - r._1, l._2 - r._2)

    def /(r: (Long, Long)): (Long, Long) = l div r

    def square: (Long, Long) = l mul l


    def mod(r: (Long, Long)): (Long, Long) = (l._1 % r._1, l._2 % r._2)

    def %(r: (Long, Long)): (Long, Long) = l mod r

  }

  implicit class RichTuples3Longs(val l: (Long, Long, Long)) extends AnyVal {

    def add(r: (Long, Long, Long)): (Long, Long, Long) = (l._1 + r._1, l._2 + r._2, l._3 + r._3)

    def +(r: (Long, Long, Long)): (Long, Long, Long) = l add r

    def sub(r: (Long, Long, Long)): (Long, Long, Long) = (l._1 - r._1, l._2 - r._2, l._3 - r._3)

    def -(r: (Long, Long, Long)): (Long, Long, Long) = l sub r

    def delta(r: (Long, Long, Long)): (Long, Long, Long) = (math.abs(l._1 - r._1), math.abs(l._2 - r._2), math.abs(l._3 - r._3))

    def manhattan(r: (Long, Long, Long)): Long = {
      val (x, y, z) = l delta r
      x + y + z
    }

    def mul(r: (Long, Long, Long)): (Long, Long, Long) = (l._1 * r._1, l._2 * r._2, l._3 * r._3)

    def *(r: (Long, Long, Long)): (Long, Long, Long) = l mul r

    def div(r: (Long, Long, Long)): (Long, Long, Long) = (l._1 / r._1, l._2 / r._2, l._3 / r._3)

    def /(r: (Long, Long, Long)): (Long, Long, Long) = l div r

    def mod(r: (Long, Long, Long)): (Long, Long, Long) = (l._1 % r._1, l._2 % r._2, l._3 % r._3)

    def %(r: (Long, Long, Long)): (Long, Long, Long) = l mod r

    def square: (Long, Long, Long) = l mul l
  }
}
