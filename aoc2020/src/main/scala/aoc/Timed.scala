package aoc

trait Timed {

  def time[V](block: => V): V = {
    val start = System.nanoTime()
    val result = block
    println(s"Elapsed time: ${System.nanoTime() - start} ns")
    result
  }

  def systematicCheck[V](method: => V, times: Int):Unit = {
    var totalTime = 0L
    for (t <- Range(0,times)){
      val start = System.nanoTime()
      val result = method
      totalTime += System.nanoTime() - start
    }
    println(s"Times: $times, \t avg time: ${totalTime/times}")
  }

}
