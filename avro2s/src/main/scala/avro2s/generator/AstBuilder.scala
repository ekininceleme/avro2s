package avro2s.generator

import org.scalafmt.Scalafmt
import org.scalafmt.config.ScalafmtConfig

import scala.meta._

private[avro2s] object AstBuilder {

  private val fmtConfig: ScalafmtConfig = ScalafmtConfig.default.copy(
    maxColumn = 200
  )

  def parseStat(code: String): Stat =
    code.parse[Stat].get

  def parseTerm(code: String): Term =
    code.parse[Term].get

  def parseType(code: String): Type =
    code.parse[Type].get

  /** Render using Scala 3 dialect so that Scala 3 keywords (enum, export, given, then)
   * get backticks automatically. Backticking these is also valid Scala 2. */
  def render(tree: Tree): String =
    dialects.Scala3(tree).syntax

  def format(code: String): String =
    Scalafmt.format(code, fmtConfig).get

  case class CaseParam(name: String, tpe: String, mod: Option[String] = None)

  /** Strip backticks from identifier — the Scala 3 dialect will re-add them for all keywords. */
  private def cleanName(name: String): String =
    if (name.startsWith("`") && name.endsWith("`")) name.substring(1, name.length - 1)
    else name

  def buildSource(pkg: Option[String], imports: List[String], stats: List[Stat]): Source = {
    val importStats: List[Stat] = imports.map(i => parseStat(s"import $i"))
    val allStats = importStats ++ stats

    val body = pkg match {
      case Some(p) =>
        val pkgRef = p.parse[Term].get.asInstanceOf[Term.Ref]
        List(Pkg(pkgRef, allStats))
      case None =>
        allStats
    }

    Source(body)
  }

  def buildCaseClass(
    name: String,
    params: List[CaseParam],
    parents: List[String],
    body: List[Stat]
  ): Defn.Class = {
    val termParams = params.map { p =>
      val mods: List[Mod] = p.mod match {
        case Some("var") => List(Mod.VarParam())
        case _ => Nil
      }
      Term.Param(mods, Term.Name(cleanName(p.name)), Some(parseType(p.tpe)), None)
    }

    val inits = parents.map { p =>
      Init(parseType(p), Name.Anonymous(), Nil: List[List[Term]])
    }

    Defn.Class(
      mods = List(Mod.Case()),
      name = Type.Name(name),
      tparamClause = Type.ParamClause(Nil),
      ctor = Ctor.Primary(Nil, Name.Anonymous(), List(Term.ParamClause(termParams, None))),
      templ = Template(Nil, inits, Self(Name.Anonymous(), None), body, Nil)
    )
  }

  def buildObject(name: String, body: List[Stat]): Defn.Object = {
    Defn.Object(
      mods = Nil,
      name = Term.Name(name),
      templ = Template(Nil, Nil, Self(Name.Anonymous(), None), body, Nil)
    )
  }

  def buildSecondaryConstructor(body: String): Stat =
    parseStat(body)

  def renderFile(pkg: Option[String], comment: String, imports: List[String], stats: List[Stat]): String = {
    val source = buildSource(pkg, imports, stats)
    val raw = render(source)
    val formatted = format(raw)
    s"$comment\n\n$formatted"
  }

}
