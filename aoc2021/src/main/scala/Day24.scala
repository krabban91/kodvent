import aoc.string.{AoCPart1String, AoCPart2String}

object Day24 extends App with AoCPart1String with AoCPart2String {

  printResultPart1
  printResultPart2

  override def part1(strings: Seq[String]): String = {
    val exprs = strings.map(Expr(_))

    for (i <- (1L to 4L).reverse) {
      for (j <- (1L to 1L).reverse) {
        for (k <- (1L to 2L).reverse) {
          for (l <- Seq(9L, 8L)) {
            for (m <- 9L to 9L) {
              for (n <- 9L to 9L) {
                for (o <- (9L to 9L).reverse) {
                  for (p <- (4L to 4L).reverse) {
                    for (q <- (1L to 9L).reverse) {
                      for (r <- (1L to 7L).reverse) {
                        for (s <- (3L to 9L).reverse) {
                          for (t <- (1L to 9L).reverse) {
                            for (u <- (5L to 9L).reverse) {
                              for (v <- (6L to 9L).reverse) {

                                val inp = Seq(i, j, k, l, m, n, o, p, q, r, s, t, u, v)
                                val res = exprs.foldLeft(ALU(inp))((alu, expr) => {
                                  expr.eval(alu).get
                                })
                                if (res.z == 0) {
                                  return inp.map(_.toString).reduce(_ + _)
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    "N/A"
  }

  override def part2(strings: Seq[String]): String = {
    val exprs = strings.map(Expr(_))

    for (i <- (1L to 1L)) {
      for (j <- (1L to 1L)) {
        for (k <- (1L to 2L)) {
          for (l <- (8L to 9L)) {
            for (m <- 9L to 9L) {
              for (n <- 5L to 9L) {
                for (o <- (6L to 9L)) {
                  for (p <- (1L to 4L)) {
                    for (q <- (1L to 8L)) {
                      for (r <- (1L to 7L)) {
                        for (s <- (3L to 9L)) {
                          for (t <- (2L to 9L)) {
                            for (u <- (1L to 9L)) {
                              for (v <- (6L to 6L)) {

                                val inp = Seq(i, j, k, l, m, n, o, p, q, r, s, t, u, v)
                                val res = exprs.take(18 * 14).foldLeft(ALU(inp))((alu, expr) => {
                                  expr.eval(alu).get
                                })
                                if (res.z == 0) {
                                  return inp.map(_.toString).reduce(_ + _)
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    "N/A"
  }

  case class ALU(inputs: Seq[Long], w: Long = 0, x: Long = 0, y: Long = 0, z: Long = 0) {


    def step(expr: Expr): Option[ALU] = {
      expr.eval(this)
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
}
