import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = Path(strings).recursiveDirs.map(_.size).filter(_ < 1e5).sum

  override def part2(strings: Seq[String]): Long = {
    val sizes = Path(strings).recursiveDirs.map(_.size).sorted
    sizes.sorted.find(_ >= 3e7 - (7e7 - sizes.last)).get
  }

  trait Path {
    def name: String

    def size: Long

    def recursiveDirs: Seq[Dir]

    def updated(path: Path): Path
  }

  object Path {
    private val patternCd = """\$ cd (.+)""".r
    private val patternLs = """\$ ls""".r
    private val patternDir = """dir (.+)""".r
    private val patternFile = """(\d+) (.+)""".r

    private def join(currentDir: String, file: String): String = {
      currentDir + (if (currentDir.endsWith("/")) "" else "/") + file
    }

    def apply(strings: Seq[String]): Path = strings
      .foldLeft(("", Dir("/", Seq()).asInstanceOf[Path])) { case (t@(currentDir, tree), string) =>
        string match {
          case patternLs() => t
          case patternCd(path) => path match {
            case ".." => (Some(currentDir.splitAt(currentDir.lastIndexOf('/'))._1).filter(_.nonEmpty).getOrElse("/"), tree)
            case "/" => ("/", tree)
            case v => (join(currentDir, v), tree)
          }
          case patternDir(name) => (currentDir, tree.updated(Dir(join(currentDir, name), Seq())))
          case patternFile(size, name) => (currentDir, tree.updated(File(join(currentDir, name), size.toLong)))
        }
      }._2
  }

  case class File(name: String, size: Long) extends Path {
    override def recursiveDirs: Seq[Dir] = Seq()

    override def updated(path: Path): Path = path match {
      case Dir(_, _) => this
      case File(n, _) => if (this.name == n) path else this
    }
  }

  case class Dir(name: String, tree: Seq[Path]) extends Path {
    override def size: Long = tree.map(_.size).sum

    override def recursiveDirs: Seq[Dir] = Seq(this) ++ tree.flatMap(_.recursiveDirs)

    override def updated(path: Path): Path = if (path.name == this.name) path else if (path.name.startsWith(this.name)) {
      if (path.name.stripPrefix(this.name).tail.contains("/")) {
        Dir(this.name, tree.map(_.updated(path)))
      } else {
        Dir(this.name, Seq(path) ++ tree)
      }
    } else this
  }
}
