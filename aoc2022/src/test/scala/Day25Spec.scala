import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day25Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day25.part1TestResult shouldEqual "2=-1=0"
  }
  "Part1" should "be correct" in {
    Day25.part1Result shouldEqual "2=001=-2=--0212-22-2"
  }

  "snafu" should "parse 2=-01" in {
    Day25.fromSNAFU("2=-01") shouldEqual 976L
  }

  "snafu" should "parse 1121-1110-1=0" in {
    Day25.fromSNAFU("1121-1110-1=0") shouldEqual 314159265L
  }

  "to snafu" should "parse 2=-01" in {
    Day25.toSNAFU(976L) shouldEqual "2=-01"
  }

  "to snafu" should "parse 1121-1110-1=0" in {
    Day25.toSNAFU(314159265) shouldEqual "1121-1110-1=0"
  }

}
