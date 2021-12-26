import aoc.string.{AoCPart1String, AoCPart2String}

object Day24 extends App with AoCPart1String with AoCPart2String {

  override def part1(strings: Seq[String]): String = modelNumber(strings.map(Expr(_)), ALU(), retro = false).get

  override def part2(strings: Seq[String]): String = modelNumber(strings.map(Expr(_)), ALU(), retro = true).get

  def modelNumber(instr: Seq[Expr], alu: ALU, retro: Boolean): Option[String] = Option("")
    .filter(_ => instr.isEmpty)
    .orElse((if (retro) (1 to 9) else (1 to 9).reverse)
      .map(i => (i, alu.addInput(i).runUntilInput(instr)))
      .filter(v => v._2._1.z <= getZLimit(v._2._2.size))
      .foldLeft(Option.empty[String])((out, v) => out.orElse(modelNumber(v._2._2, v._2._1, retro).map(t => v._1 + t))))

  case class ALU(inputs: Seq[Long] = Seq(), w: Long = 0, x: Long = 0, y: Long = 0, z: Long = 0) {

    def addInput(input: Int): ALU = ALU(inputs ++ Seq(input), w, x, y, z)

    def step(expr: Expr): Option[ALU] = {
      expr.eval(this)
    }

    def runUntilInput(instr: Seq[Expr]): (ALU, Seq[Expr]) = {
      val (curr, _, run) = instr.foldLeft((this, false, 0))((t, expr) => {
        if (t._2) {
          t
        } else {
          val v = t._1.step(expr)
          v.map(a => (a, false, t._3 + 1))
            .getOrElse((t._1, true, t._3))
        }
      })
      (curr, instr.drop(run))
    }
  }

  trait Expr {
    def eval(alu: ALU): Option[ALU]
  }

  case class Inp(x: String) extends Expr {
    override def eval(alu: ALU): Option[ALU] = {
      if (alu.inputs.isEmpty) {
        None
      } else {
        val in = alu.inputs.head
        val tail = alu.inputs.tail
        Option(x match {
          case "w" => ALU(tail, w = in, x = alu.x, y = alu.y, z = alu.z)
          case "x" => ALU(tail, w = alu.w, x = in, y = alu.y, z = alu.z)
          case "y" => ALU(tail, w = alu.w, x = alu.x, y = in, z = alu.z)
          case "z" => ALU(tail, w = alu.w, x = alu.x, y = alu.y, z = in)
        })
      }
    }
  }

  case class Add(a: String, b: String) extends Expr {
    override def eval(alu: ALU): Option[ALU] = {
      val bVal = b match {
        case "w" => alu.w
        case "x" => alu.x
        case "y" => alu.y
        case "z" => alu.z
        case v => v.toLong
      }
      Option(a match {
        case "w" => ALU(alu.inputs, w = alu.w + bVal, x = alu.x, y = alu.y, z = alu.z)
        case "x" => ALU(alu.inputs, w = alu.w, x = alu.x + bVal, y = alu.y, z = alu.z)
        case "y" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y + bVal, z = alu.z)
        case "z" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z + bVal)
      })
    }
  }

  case class Mul(a: String, b: String) extends Expr {
    override def eval(alu: ALU): Option[ALU] = {
      val bVal = b match {
        case "w" => alu.w
        case "x" => alu.x
        case "y" => alu.y
        case "z" => alu.z
        case v => v.toLong
      }
      Option(a match {
        case "w" => ALU(alu.inputs, w = alu.w * bVal, x = alu.x, y = alu.y, z = alu.z)
        case "x" => ALU(alu.inputs, w = alu.w, x = alu.x * bVal, y = alu.y, z = alu.z)
        case "y" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y * bVal, z = alu.z)
        case "z" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z * bVal)
      })
    }
  }

  case class Div(a: String, b: String) extends Expr {
    override def eval(alu: ALU): Option[ALU] = {
      val bVal = b match {
        case "w" => alu.w
        case "x" => alu.x
        case "y" => alu.y
        case "z" => alu.z
        case v => v.toLong
      }
      Option(a match {
        case "w" => ALU(alu.inputs, w = alu.w / bVal, x = alu.x, y = alu.y, z = alu.z)
        case "x" => ALU(alu.inputs, w = alu.w, x = alu.x / bVal, y = alu.y, z = alu.z)
        case "y" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y / bVal, z = alu.z)
        case "z" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z / bVal)
      })
    }
  }

  case class Mod(a: String, b: String) extends Expr {
    override def eval(alu: ALU): Option[ALU] = {
      val bVal = b match {
        case "w" => alu.w
        case "x" => alu.x
        case "y" => alu.y
        case "z" => alu.z
        case v => v.toLong
      }
      Option(a match {
        case "w" => ALU(alu.inputs, w = alu.w % bVal, x = alu.x, y = alu.y, z = alu.z)
        case "x" => ALU(alu.inputs, w = alu.w, x = alu.x % bVal, y = alu.y, z = alu.z)
        case "y" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y % bVal, z = alu.z)
        case "z" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z % bVal)
      })
    }
  }

  case class Eql(a: String, b: String) extends Expr {
    override def eval(alu: ALU): Option[ALU] = {
      val bVal = b match {
        case "w" => alu.w
        case "x" => alu.x
        case "y" => alu.y
        case "z" => alu.z
        case v => v.toLong
      }
      Option(a match {
        case "w" => ALU(alu.inputs, w = if (alu.w == bVal) 1 else 0, x = alu.x, y = alu.y, z = alu.z)
        case "x" => ALU(alu.inputs, w = alu.w, x = if (alu.x == bVal) 1 else 0, y = alu.y, z = alu.z)
        case "y" => ALU(alu.inputs, w = alu.w, x = alu.x, y = if (alu.y == bVal) 1 else 0, z = alu.z)
        case "z" => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = if (alu.z == bVal) 1 else 0)
      })
    }


  }

  object Expr {
    def apply(string: String): Expr = {
      val spl = string.split(" ")
      spl.head match {
        case "inp" => Inp(spl.last)
        case "add" => Add(spl.tail.head, spl.last)
        case "mul" => Mul(spl.tail.head, spl.last)
        case "div" => Div(spl.tail.head, spl.last)
        case "mod" => Mod(spl.tail.head, spl.last)
        case "eql" => Eql(spl.tail.head, spl.last)
        case e => println("missing"); Inp(spl.head)
      }
    }
  }

  private def getZLimit(instructionSize: Int): Int = instructionSize match {
    case 0 => 0
    case 18 => 26
    case 36 => 600
    case 54 => 15_000
    case 72 => 360_000
    case 90 => 15_000
    case 108 => 600
    case 126 => 15_000
    case 144 => 600
    case 162 => 50
    case 180 => 600
    case 198 => 15_000
    case 216 => 600
    case 234 => 26
    case 252 => 0
  }
}
