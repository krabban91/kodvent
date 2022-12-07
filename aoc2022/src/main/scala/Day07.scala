import aoc.numeric.{AoCPart1Test, AoCPart2Test}

object Day07 extends App with AoCPart1Test with AoCPart2Test {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {

    val tree: Path = parseTree(strings)
    val dirs = tree.recursiveDirs
    val sizes = dirs.map(_.size)
    val value = sizes.filter(_ < 100000)
    val sum = value.sum
    sum
  }

  override def part2(strings: Seq[String]): Long = {
    -1
  }

  def parseTree(strings: Seq[String]): Path = {
    var currentDir = ""
    var fullTree: Path = Dir("/", Seq())

    strings.foreach { s =>
      if (s.startsWith("$")) {
        val v = s.replace("$ ", "")
        if (v.startsWith("cd")) {
          v.split(" ").last match {
            case ".." =>
              val i = currentDir.lastIndexOf('/')
              val next = currentDir.splitAt(i)._1
              currentDir = Some(next).filter(_.nonEmpty).getOrElse("/")
            case "/" => currentDir = "/"
            case v => currentDir = join(currentDir, v)
          }
        } else {
          // ls
        }
      } else {
        if (s.startsWith("dir")) {
          val name = s.replace("dir ", "")
          val str = join(currentDir, name)
          val dir = Dir(str, Seq())
          val next = fullTree.updated(dir)
          fullTree = next
        } else {

          val v = s.split(" ").head.toLong
          val file = s.split(" ").last
          val fullName = join(currentDir, file)
          fullTree = fullTree.updated(File(fullName, v))

        }
      }
    }
    fullTree
  }

  private def join(currentDir: String, file: String) = {
    currentDir + (if (currentDir.endsWith("/")) "" else "/") + file
  }

  trait Path {
    def name: String

    def size: Long

    def recursiveDirs: Seq[Dir]

    def recursiveFiles: Seq[File]

    def updated(path: Path): Path

    def log(level: Int): String
  }

  case class File(name: String, size: Long) extends Path {
    override def recursiveDirs: Seq[Dir] = Seq()

    override def updated(path: Path): Path = path match {
      case Dir(n, t) => this
      case File(n, size) => if (this.name == n) path else this
    }

    override def recursiveFiles: Seq[File] = Seq(this)

    override def log(level: Int): String = "-".repeat(level) + s"$name ($size)"
  }

  case class Dir(name: String, tree: Seq[Path]) extends Path {
    override def size: Long = tree.map(_.size).sum

    override def recursiveDirs: Seq[Dir] = Seq(this) ++ (tree.flatMap(_.recursiveDirs)).distinct

    override def recursiveFiles: Seq[File] = tree.flatMap(_.recursiveFiles).distinct

    override def updated(path: Path): Path = {
      if (path.name == this.name) path else if (path.name.contains(this.name)) {

        val str = path.name.replaceFirst(this.name, "")
        if (str.tail.contains("/")) {
          Dir(this.name, tree.map(_.updated(path)))
        } else {
          Dir(this.name, (Seq(path) ++ tree.filterNot(_.name == path.name)).distinct)
        }
      } else this
    }

    override def log(level: Int): String = {
      "-".repeat(level) + s"$name ($size)" + "\n" + tree.map(_.log(level + 1)).mkString("\n")
    }
  }
}
