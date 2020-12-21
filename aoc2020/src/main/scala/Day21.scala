import aoc.numeric.AoCPart1Test
import aoc.string.AoCPart2StringTest

import scala.collection.mutable

object Day21 extends App with AoCPart1Test with AoCPart2StringTest {

  printResultPart1Test
  printResultPart1
  printResultPart2Test
  printResultPart2

  override def part1(strings: Seq[String]): Long = {
    val inp = strings.map(Recipe(_)).sortBy(-_.allergens.size)
    val ingredients = inp.flatMap(_.ingredients).toSet
    val safeIngredients = inertIngredients(inp, ingredients)
    inp.map(_.ingredients).map(is => is.count(i => safeIngredients.contains(i))).sum
  }

  override def part2(strings: Seq[String]): String = {
    val inp = strings.map(Recipe(_)).sortBy(-_.allergens.size)
    val safeIngredients = inertIngredients(inp, inp.flatMap(_.ingredients).toSet)
    val filteredR = inp.map(r => Recipe(r.ingredients.filterNot(safeIngredients.contains), r.allergens))

    val ingredients = filteredR.flatMap(_.ingredients).toSet
    val result = mutable.Map[String, String]()

    var frontier = filteredR.flatMap(_.allergens).toSet[String]
      .map(a => a -> ingredients
        .filter(i => filteredR
          .filter(_.allergens.contains(a))
          .forall(_.ingredients.contains(i))))
    while (frontier.nonEmpty) {
      val pop = frontier.find(t => t._2.size == 1).get
      result(pop._2.head) = pop._1
      frontier = frontier.filter(t => t._1 != pop._1).map(t => (t._1, t._2.filter(s => s != pop._2.head)))
    }
    result.toSeq.sortBy(_._2).map(_._1).reduce((l, r) => s"$l,$r")
  }

  private def inertIngredients(inp: Seq[Recipe], ingredients: Set[String]): Set[String] = {
    val vv = inp.flatMap(r => r.allergens.map(a => a -> r)).groupBy(_._1)
    val vvi = inp.flatMap(r => r.ingredients.map(a => a -> r)).groupBy(_._1)
    ingredients.filterNot(i => {
      val recipes = vvi(i).map(_._2)
      val as = recipes.flatMap(r => r.allergens).toSet
      as.exists(a => {
        val value = vv(a).map(_._2)
        value.forall(r => r.ingredients.contains(i))
      })
    })
  }

  case class Recipe(ingredients: Seq[String], allergens: Seq[String])

  object Recipe {
    def apply(string: String): Recipe = {
      val spl = string.split("\\(contains ")

      Recipe(spl(0).split(" ").map(_.trim), spl(1).replace(")", "").split(",").map(_.trim))
    }
  }

}
