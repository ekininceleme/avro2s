package avro2s.generator

import scala.meta._

private[avro2s] object AstBuilder {

  def parseStat(code: String): Stat =
    code.parse[Stat].get

  def parseTerm(code: String): Term =
    code.parse[Term].get

  def parseType(code: String): Type =
    code.parse[Type].get

  def render(tree: Tree): String =
    tree.syntax

  case class CaseParam(name: String, tpe: String, mod: Option[String] = None)

  /** Strip backticks from an identifier — scalameta adds them automatically when needed */
  private def cleanName(name: String): String =
    if (name.startsWith("`") && name.endsWith("`")) name.substring(1, name.length - 1)
    else name

  def buildSource(pkg: Option[String], comment: Option[String], imports: List[String], stats: List[Stat]): Source = {
    val importStats: List[Stat] = imports.map(i => parseStat(s"import $i"))
    val allStats = importStats ++ stats

    val body = pkg match {
      case Some(p) =>
        val pkgRef = p.parse[Term].get.asInstanceOf[Term.Ref]
        List(Pkg(pkgRef, allStats))
      case None =>
        allStats
    }

    val withComment = comment match {
      case Some(c) =>
        val commentStat = parseStat(s"""val __comment__ = "$c"""")
        // We'll prepend the comment as raw text during rendering instead
        body
      case None => body
    }

    Source(withComment)
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
      Init(parseType(p), Name.Anonymous(), Nil)
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

  /**
   * Renders a companion object as a string with proper newline separation between stats.
   * Scalameta's .syntax puts all object body stats on one line, which breaks triple-quoted strings.
   */
  def renderObject(name: String, body: List[Stat]): String = {
    val bodyStr = body.map(s => "  " + render(s)).mkString("\n")
    s"object $name {\n$bodyStr\n}"
  }

  def buildVal(name: String, tpe: String, rhs: String): Defn.Val = {
    Defn.Val(
      mods = Nil,
      pats = List(Pat.Var(Term.Name(name))),
      decltpe = Some(parseType(tpe)),
      rhs = parseTerm(rhs)
    )
  }

  def buildOverrideVal(name: String, tpe: String, rhs: String): Defn.Val = {
    Defn.Val(
      mods = List(Mod.Override()),
      pats = List(Pat.Var(Term.Name(name))),
      decltpe = Some(parseType(tpe)),
      rhs = parseTerm(rhs)
    )
  }

  def buildOverrideDef(name: String, params: List[(String, String)], returnType: String, body: String): Defn.Def = {
    val termParams = params.map { case (pName, pType) =>
      Term.Param(Nil, Term.Name(pName), Some(parseType(pType)), None)
    }

    Defn.Def(
      mods = List(Mod.Override()),
      name = Term.Name(name),
      paramClauseGroups = List(
        Member.ParamClauseGroup(
          Type.ParamClause(Nil),
          List(Term.ParamClause(termParams, None))
        )
      ),
      decltpe = Some(parseType(returnType)),
      body = parseTerm(body)
    )
  }

  def buildDef(name: String, params: List[(String, String)], returnType: Option[String], body: String): Defn.Def = {
    val termParams = params.map { case (pName, pType) =>
      Term.Param(Nil, Term.Name(pName), Some(parseType(pType)), None)
    }

    Defn.Def(
      mods = Nil,
      name = Term.Name(name),
      paramClauseGroups = List(
        Member.ParamClauseGroup(
          Type.ParamClause(Nil),
          List(Term.ParamClause(termParams, None))
        )
      ),
      decltpe = returnType.map(parseType),
      body = parseTerm(body)
    )
  }

  def buildSecondaryConstructor(body: String): Stat = {
    parseStat(body)
  }

  def buildMatchExpr(scrutinee: String, cases: List[String]): Term.Match = {
    val caseNodes = cases.map { c =>
      parseStat(c) match {
        case cs: Case => cs
        case other => throw new IllegalArgumentException(s"Expected case clause, got: ${other.structure}")
      }
    }
    Term.Match(
      parseTerm(scrutinee),
      caseNodes,
      Nil
    )
  }

  def renderFile(pkg: Option[String], comment: String, imports: List[String], stats: List[Stat]): String = {
    val source = buildSource(pkg, None, imports, stats)
    val rendered = render(source)
    s"$comment\n\n$rendered"
  }

  /**
   * Renders a file where some parts are AST stats and others are pre-rendered strings.
   * This is needed because scalameta's .syntax renders object bodies on one line,
   * which breaks triple-quoted strings.
   */
  def renderFileWithRawParts(pkg: Option[String], comment: String, imports: List[String], astStats: List[Stat], rawParts: List[String]): String = {
    val pkgLine = pkg.map(p => s"package $p").getOrElse("")
    val importLines = imports.map(i => s"import $i")
    val astRendered = astStats.map(render)

    val parts = List(comment, "", pkgLine) ++
      importLines ++
      astRendered ++
      rawParts

    parts.filter(_.nonEmpty).mkString("\n")
  }
}
