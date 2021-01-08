import Day20.Tile
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Day20Spec extends AnyFlatSpec with Matchers {
  "Part1 Test" should "be correct" in {
    Day20.part1TestResult shouldEqual 20899048083289L
  }
  "Part1" should "be correct" in {
    Day20.part1Result shouldEqual 14986175499719L
  }
  "Part2 Test" should "be correct" in {
    Day20.part2TestResult shouldEqual 273
  }
  "Part2" should "be correct" in {
    Day20.part2Result shouldEqual 2161
  }

  "Tiles" should "modify correctly" in {
    val origTile = Day20.Tile("Tile 1951:\n#.##...##.\n#.####...#\n.....#..##\n#...######\n.##.#....#\n.###.#####\n###.##.##.\n.###....#.\n..#.#..#.#\n#...##.#..".split("\n"))
    val copy = Day20.Tile("Tile 1951:\n#.##...##.\n#.####...#\n.....#..##\n#...######\n.##.#....#\n.###.#####\n###.##.##.\n.###....#.\n..#.#..#.#\n#...##.#..".split("\n"))
    val verticalFlip = Day20.Tile("Tile 1951:\n#...##.#..\n..#.#..#.#\n.###....#.\n###.##.##.\n.###.#####\n.##.#....#\n#...######\n.....#..##\n#.####...#\n#.##...##.".split("\n"))
    origTile shouldEqual copy
    origTile.flipVertically shouldEqual verticalFlip
    origTile.flipVertically shouldEqual verticalFlip
    origTile.flipHorizontally should not be origTile
    origTile.flipHorizontally.flipHorizontally shouldEqual origTile

    val o1 = Day20.Tile("Tile 2473:\n#....####.\n#..#.##...\n#.##..#...\n######.#.#\n.#...#.#.#\n.#########\n.###.#..#.\n########.#\n##...##.#.\n..###.#.#.".split("\n"))
    val rotateflip = Day20.Tile("Tile 2473:\n..#.###...\n##.##....#\n..#.###..#\n###.#..###\n.######.##\n#.#.#.#...\n#.###.###.\n#.###.##..\n.######...\n.##...####".split("\n"))
    o1.rotate.rotate.rotate.flipVertically shouldEqual rotateflip
  }

  "Tiles" should "fit correctly" in {
    val origTile = Day20.Tile("Tile 1951:\n#.##...##.\n#.####...#\n.....#..##\n#...######\n.##.#....#\n.###.#####\n###.##.##.\n.###....#.\n..#.#..#.#\n#...##.#..".split("\n"))
    origTile.fitsToTheBottom(origTile.flipVertically) shouldEqual true
    origTile.fitsToTheRight(origTile.flipHorizontally) shouldEqual true
  }

  "Seamonsters" should "be found" in {
    val seaMonster = Day20.Tile(Day20.read("day20$_seamonster.txt"))
    val image = Day20.Tile(Day20.read("test/day20$_joined.txt"))
    println(seaMonster)
    val res = image.permutations.find(i => {
      val tile = Tile.sweepSeaMonster(i, seaMonster)
      tile != i
    }).map(Tile.sweepSeaMonster(_, seaMonster)).get
    res.grid.sum(v => if (v.v == '#') 1 else 0) shouldEqual 273

  }
}

