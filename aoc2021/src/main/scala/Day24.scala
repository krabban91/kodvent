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
                                  expr.eval(alu)
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
                                  expr.eval(alu)
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


    def step(expr: Expr): ALU = {
      expr.eval(this)
    }
  }

  trait Expr {
    def eval(alu: ALU): ALU
  }

  case class Inp(x: String) extends Expr {
    override def eval(alu: ALU): ALU = {
      val in = alu.inputs.head
      val tail = alu.inputs.tail
      x match {
        case "w" => ALU(tail, w = in, x = alu.x, y = alu.y, z = alu.z)
        case "x" => ALU(tail, w = alu.w, x = in, y = alu.y, z = alu.z)
        case "y" => ALU(tail, w = alu.w, x = alu.x, y = in, z = alu.z)
        case "z" => ALU(tail, w = alu.w, x = alu.x, y = alu.y, z = in)
      }
    }
  }

  case class Add(a: String, b: String) extends Expr {
    override def eval(alu: ALU): ALU = {
      (a, b) match {
        case ("w", "w") => ALU(alu.inputs, w = alu.w + alu.w, x = alu.x, y = alu.y, z = alu.z)
        case ("w", "x") => ALU(alu.inputs, w = alu.w + alu.x, x = alu.x, y = alu.y, z = alu.z)
        case ("w", "y") => ALU(alu.inputs, w = alu.w + alu.y, x = alu.x, y = alu.y, z = alu.z)
        case ("w", "z") => ALU(alu.inputs, w = alu.w + alu.z, x = alu.x, y = alu.y, z = alu.z)
        case ("w", d) => ALU(alu.inputs, w = alu.w + d.toLong, x = alu.x, y = alu.y, z = alu.z)
        case ("x", "w") => ALU(alu.inputs, w = alu.w, x = alu.x + alu.w, y = alu.y, z = alu.z)
        case ("x", "x") => ALU(alu.inputs, w = alu.w, x = alu.x + alu.x, y = alu.y, z = alu.z)
        case ("x", "y") => ALU(alu.inputs, w = alu.w, x = alu.x + alu.y, y = alu.y, z = alu.z)
        case ("x", "z") => ALU(alu.inputs, w = alu.w, x = alu.x + alu.z, y = alu.y, z = alu.z)
        case ("x", d) => ALU(alu.inputs, w = alu.w, x = alu.x + d.toLong, y = alu.y, z = alu.z)
        case ("y", "w") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y + alu.w, z = alu.z)
        case ("y", "x") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y + alu.x, z = alu.z)
        case ("y", "y") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y + alu.y, z = alu.z)
        case ("y", "z") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y + alu.z, z = alu.z)
        case ("y", d) => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y + d.toLong, z = alu.z)
        case ("z", "w") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z + alu.w)
        case ("z", "x") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z + alu.x)
        case ("z", "y") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z + alu.y)
        case ("z", "z") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z + alu.z)
        case ("z", d) => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z + d.toLong)
      }
    }
  }

  case class Mul(a: String, b: String) extends Expr {
    override def eval(alu: ALU): ALU = {
      (a, b) match {
        case ("w", "w") => ALU(alu.inputs, w = alu.w * alu.w, x = alu.x, y = alu.y, z = alu.z)
        case ("w", "x") => ALU(alu.inputs, w = alu.w * alu.x, x = alu.x, y = alu.y, z = alu.z)
        case ("w", "y") => ALU(alu.inputs, w = alu.w * alu.y, x = alu.x, y = alu.y, z = alu.z)
        case ("w", "z") => ALU(alu.inputs, w = alu.w * alu.z, x = alu.x, y = alu.y, z = alu.z)
        case ("w", d) => ALU(alu.inputs, w = alu.w * d.toLong, x = alu.x, y = alu.y, z = alu.z)
        case ("x", "w") => ALU(alu.inputs, w = alu.w, x = alu.x * alu.w, y = alu.y, z = alu.z)
        case ("x", "x") => ALU(alu.inputs, w = alu.w, x = alu.x * alu.x, y = alu.y, z = alu.z)
        case ("x", "y") => ALU(alu.inputs, w = alu.w, x = alu.x * alu.y, y = alu.y, z = alu.z)
        case ("x", "z") => ALU(alu.inputs, w = alu.w, x = alu.x * alu.z, y = alu.y, z = alu.z)
        case ("x", d) => ALU(alu.inputs, w = alu.w, x = alu.x * d.toLong, y = alu.y, z = alu.z)
        case ("y", "w") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y * alu.w, z = alu.z)
        case ("y", "x") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y * alu.x, z = alu.z)
        case ("y", "y") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y * alu.y, z = alu.z)
        case ("y", "z") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y * alu.z, z = alu.z)
        case ("y", d) => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y * d.toLong, z = alu.z)
        case ("z", "w") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z * alu.w)
        case ("z", "x") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z * alu.x)
        case ("z", "y") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z * alu.y)
        case ("z", "z") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z * alu.z)
        case ("z", d) => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z * d.toLong)
      }
    }
  }

  case class Div(a: String, b: String) extends Expr {
    override def eval(alu: ALU): ALU = (a, b) match {
      case ("w", "w") => if (alu.w == 0) {
        println("w==0");
        alu
      } else ALU(alu.inputs, w = alu.w / alu.w, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "x") => if (alu.x == 0) {
        println("x==0");
        alu
      } else ALU(alu.inputs, w = alu.w / alu.x, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "y") => if (alu.y == 0) {
        println("y==0");
        alu
      } else ALU(alu.inputs, w = alu.w / alu.y, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "z") => if (alu.z == 0) {
        println("z==0");
        alu
      } else ALU(alu.inputs, w = alu.w / alu.z, x = alu.x, y = alu.y, z = alu.z)
      case ("w", d) => if (d == "0") {
        println("d==0");
        alu
      } else ALU(alu.inputs, w = alu.w / d.toLong, x = alu.x, y = alu.y, z = alu.z)
      case ("x", "w") => if (alu.w == 0) {
        println("w==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x / alu.w, y = alu.y, z = alu.z)
      case ("x", "x") => if (alu.x == 0) {
        println("x==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x / alu.x, y = alu.y, z = alu.z)
      case ("x", "y") => if (alu.y == 0) {
        println("y==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x / alu.y, y = alu.y, z = alu.z)
      case ("x", "z") => if (alu.z == 0) {
        println("z==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x / alu.z, y = alu.y, z = alu.z)
      case ("x", d) => if (d == "0") {
        println("d==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x / d.toLong, y = alu.y, z = alu.z)
      case ("y", "w") => if (alu.w == 0) {
        println("w==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y / alu.w, z = alu.z)
      case ("y", "x") => if (alu.x == 0) {
        println("x==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y / alu.x, z = alu.z)
      case ("y", "y") => if (alu.y == 0) {
        println("y==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y / alu.y, z = alu.z)
      case ("y", "z") => if (alu.z == 0) {
        println("z==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y / alu.z, z = alu.z)
      case ("y", d) => if (d == "0") {
        println("d==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y / d.toLong, z = alu.z)
      case ("z", "w") => if (alu.w == 0) {
        println("w==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z / alu.w)
      case ("z", "x") => if (alu.x == 0) {
        println("x==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z / alu.x)
      case ("z", "y") => if (alu.y == 0) {
        println("y==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z / alu.y)
      case ("z", "z") => if (alu.z == 0) {
        println("z==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z / alu.z)
      case ("z", d) => if (d == "0") {
        println("d==0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z / d.toLong)
    }
  }

  case class Mod(a: String, b: String) extends Expr {
    override def eval(alu: ALU): ALU = (a, b) match {
      case ("w", "w") => if (alu.w < 0) {
        println("w mod<0");
        alu
      } else ALU(alu.inputs, w = alu.w % alu.w, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "x") => if (alu.w < 0 || alu.x < 0) {
        println("x mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w % alu.x, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "y") => if (alu.w < 0 || alu.y < 0) {
        println("y mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w % alu.y, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "z") => if (alu.w < 0 || alu.z < 0) {
        println("z mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w % alu.z, x = alu.x, y = alu.y, z = alu.z)
      case ("w", d) => if (d.toLong < 0) {
        println("d mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w % d.toLong, x = alu.x, y = alu.y, z = alu.z)
      case ("x", "w") => if (alu.w < 0 || alu.x < 0) {
        println("w mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x % alu.w, y = alu.y, z = alu.z)
      case ("x", "x") => if (alu.x < 0) {
        println("x mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x % alu.x, y = alu.y, z = alu.z)
      case ("x", "y") => if (alu.x < 0 || alu.y < 0) {
        println("y mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x % alu.y, y = alu.y, z = alu.z)
      case ("x", "z") => if (alu.x < 0 || alu.z < 0) {
        println("z mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x % alu.z, y = alu.y, z = alu.z)
      case ("x", d) => if (d.toLong < 0) {
        println("d mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x % d.toLong, y = alu.y, z = alu.z)
      case ("y", "w") => if (alu.y < 0 || alu.w < 0) {
        println("w mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y % alu.w, z = alu.z)
      case ("y", "x") => if (alu.y < 0 || alu.x < 0) {
        println("x mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y % alu.x, z = alu.z)
      case ("y", "y") => if (alu.y < 0) {
        println("y mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y % alu.y, z = alu.z)
      case ("y", "z") => if (alu.y < 0 || alu.z < 0) {
        println("z mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y % alu.z, z = alu.z)
      case ("y", d) => if (d.toLong < 0) {
        println("d mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y % d.toLong, z = alu.z)
      case ("z", "w") => if (alu.z < 0 || alu.w < 0) {
        println("w mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z % alu.w)
      case ("z", "x") => if (alu.z < 0 || alu.x < 0) {
        println("x mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z % alu.x)
      case ("z", "y") => if (alu.z < 0 || alu.y < 0) {
        println("y mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z % alu.y)
      case ("z", "z") => if (alu.z < 0) {
        println("z mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z % alu.z)
      case ("z", d) => if (d.toLong < 0) {
        println("d mod <0");
        alu
      } else ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = alu.z % d.toLong)
    }
  }

  case class Eql(a: String, b: String) extends Expr {
    override def eval(alu: ALU): ALU = (a, b) match {
      case ("w", "w") => ALU(alu.inputs, w = if (alu.w == alu.w) 1 else 0, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "x") => ALU(alu.inputs, w = if (alu.w == alu.x) 1 else 0, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "y") => ALU(alu.inputs, w = if (alu.w == alu.y) 1 else 0, x = alu.x, y = alu.y, z = alu.z)
      case ("w", "z") => ALU(alu.inputs, w = if (alu.w == alu.z) 1 else 0, x = alu.x, y = alu.y, z = alu.z)
      case ("w", d) => ALU(alu.inputs, w = if (alu.w == d.toLong) 1 else 0, x = alu.x, y = alu.y, z = alu.z)
      case ("x", "w") => ALU(alu.inputs, w = alu.w, x = if (alu.x == alu.w) 1 else 0, y = alu.y, z = alu.z)
      case ("x", "x") => ALU(alu.inputs, w = alu.w, x = if (alu.x == alu.x) 1 else 0, y = alu.y, z = alu.z)
      case ("x", "y") => ALU(alu.inputs, w = alu.w, x = if (alu.x == alu.y) 1 else 0, y = alu.y, z = alu.z)
      case ("x", "z") => ALU(alu.inputs, w = alu.w, x = if (alu.x == alu.z) 1 else 0, y = alu.y, z = alu.z)
      case ("x", d) => ALU(alu.inputs, w = alu.w, x = if (alu.x == d.toLong) 1 else 0, y = alu.y, z = alu.z)
      case ("y", "w") => ALU(alu.inputs, w = alu.w, x = alu.x, y = if (alu.y == alu.w) 1 else 0, z = alu.z)
      case ("y", "x") => ALU(alu.inputs, w = alu.w, x = alu.x, y = if (alu.y == alu.x) 1 else 0, z = alu.z)
      case ("y", "y") => ALU(alu.inputs, w = alu.w, x = alu.x, y = if (alu.y == alu.y) 1 else 0, z = alu.z)
      case ("y", "z") => ALU(alu.inputs, w = alu.w, x = alu.x, y = if (alu.y == alu.z) 1 else 0, z = alu.z)
      case ("y", d) => ALU(alu.inputs, w = alu.w, x = alu.x, y = if (alu.y == d.toLong) 1 else 0, z = alu.z)
      case ("z", "w") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = if (alu.z == alu.w) 1 else 0)
      case ("z", "x") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = if (alu.z == alu.x) 1 else 0)
      case ("z", "y") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = if (alu.z == alu.y) 1 else 0)
      case ("z", "z") => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = if (alu.z == alu.z) 1 else 0)
      case ("z", d) => ALU(alu.inputs, w = alu.w, x = alu.x, y = alu.y, z = if (alu.z == d.toLong) 1 else 0)
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
