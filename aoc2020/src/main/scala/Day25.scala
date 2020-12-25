import aoc.numeric.AoCPart1Test

object Day25 extends App with AoCPart1Test {

  printResultPart1Test
  printResultPart1

  /**
   * cardPublicKey = pow(7, cardLoopsize) mod 20201227
   * doorPublicKey = pow(7, doorLoopsize) mod 20201227
   * encryptionKey = pow(cardPublicKey, doorLoopsize) mod 20201227 = pow(doorPublicKey, cardLoopsize) mod 20201227
   * @param strings the public keys
   * @return the shared encryptionKey
   */
  override def part1(strings: Seq[String]): Long = {
    val inp = strings.map(_.toLong)
    transform(inp.tail.head, bruteForce(inp.head))
  }

  def bruteForce(publicKey: Long): Int = {
    var test = -1L
    var loopSize = 0
    while (test != publicKey) {
      loopSize += 1
      test = transform(7, loopSize)
    }
    loopSize
  }

  def transform(subject: Long, loopSize: Int): Long = {
    BigInt(subject).modPow(loopSize, 20201227).toLong
  }
}
