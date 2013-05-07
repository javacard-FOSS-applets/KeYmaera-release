package edu.cmu.cs.ls.lyusimul

import com.wolfram.jlink.Expr
import edu.cmu.cs.ls._
import edu.cmu.cs.ls.lyusimul._
import scala.annotation.elidable
import scala.annotation.elidable._
import java.util.Random

object hpToExpr {
  
  var rndm = new Random

  def math_sym(s: String): Expr =
    new Expr(Expr.SYMBOL, s)

  def math_int(s: String): Expr =
    new Expr(Expr.INTEGER, s)
  
  def math_str(s: String): Expr =
    new Expr(Expr.STRING, s)

  def un_fun(f: String, arg: Expr): Expr = {
    new Expr(math_sym(f),
             List(arg).toArray)
  }

  def bin_fun(f: String, arg1: Expr, arg2: Expr): Expr = {
    new Expr(math_sym(f),
             List(arg1,arg2).toArray)
  }
  
  def mul_arg_fun(f: String, lst: List[Expr]) : Expr = {
    new Expr(math_sym(f),
             lst.toArray)
  }
  
  def scalListToListExpr(myList: List[Expr]) : Expr = {
    new Expr(math_sym("List"),
        myList.toArray)
  }
  
  @elidable(ASSERTION) def applicable(mdlt: Modality) {
    require(mdlt.m == BoxModality)
  }
  

  def modaToExpr (mdlt : Modality, varisToDisp: List[String], nGrapsPerRow: Int, tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double) : Expr = {
    applicable(mdlt)
    
    // hayquePreguntar: Is it OK to extract variables only from evolve?
    val varisStrisListForEvol = hpToVarisStrisListWoDuplsForEvol(mdlt.hp)
    val varisExprsListForEvol = strisListToExprsList(varisStrisListForEvol)
    var statsList = List(bin_fun("Set", math_sym("glob`tendLimi"), new Expr(tendLimi)))
    statsList = statsList ++ List(bin_fun("Set", math_sym("glob`tends"), un_fun("List", new Expr(0))))
    // Following line may be buggy.
    statsList = statsList ++ List(bin_fun("Set", math_sym("glob`sol"), mul_arg_fun("List", List())))
    
    statsList = statsList ++ hpToStatsList(mdlt.hp, varisStrisListForEvol, tendLimi, nUnroLoop, randMin, randMax)
    
    val safetyExpr = MmtManipulation.mmtToExpr(MmtManipulation.formulaToMmt(mdlt.f), EvolveToExpr.LOCA_T)
    statsList = statsList ++ List(bin_fun("Set", un_fun("glob`safety", math_sym("loca`t_")), safetyExpr))

    statsList = statsList ++ varisToDispToPlotComms(varisToDisp, nGrapsPerRow) 
    val compExpr = mul_arg_fun("CompoundExpression", statsList)
    return bin_fun("Module", scalListToListExpr(varisExprsListForEvol), compExpr) 
  }
  
  def hpToVarisStrisListWoDuplsForEvol (hp: HP) : List[String] = {
    return hpToVarisStrisListWithDuplsForEvol(hp).distinct
  }
  
  def hpsListToVarisStrisListWithDuplsForEvol(hpsList : List[HP]) : List[String] = hpsList match {
    case Nil => Nil
    case x::xs => hpToVarisStrisListWithDuplsForEvol(x) ++ hpsListToVarisStrisListWithDuplsForEvol(xs)
  }
  
  def evolToVarisStrisList(hp: HP): List[String] = hp match {
    case Evolve(h, primes @ _ *) => primsListToVarisList(evolToPrimsList(hp))
    case _ => throw new Exception("evolToVarisStrisList: hp is not of type Evolve")
  }
   
  def hpToVarisStrisListWithDuplsForEvol(hp: HP) : List[String] = hp match {
    case ComposedHP(Sequence, hps @ _ *) => hpsListToVarisStrisListWithDuplsForEvol(seqToHpsList(hp))
    case ComposedHP(Star, hps @ _ *) => hpsListToVarisStrisListWithDuplsForEvol(starToHpsList(hp, 1))
    case ComposedHP(Choice, hps @ _ *) => hpsListToVarisStrisListWithDuplsForEvol(choiToHpsList(hp))
    case Evolve(h, primes @ _*) => evolToVarisStrisList(hp)
    case Assign(v, t) => Nil
    case AssignAny(v) => Nil
    case Check(h) => Nil
    case _ => throw new Exception("not implemented yet")
  }
  
//  def hpToVarisStrisListWithDuplsForEvol(hp: HP) : List[String] = hp match {
//    case ComposedHP(Sequence, hps @ _ *) => (Sequence.flatten(hp).toList) match {
//      case hpsList => hpsListToVarisStrisListWithDuplsForEvol(hpsList)
//    }
//    case ComposedHP(Star, hps @ _ *) => (Star.flatten(hp).toList) match {
//      case hpsList => hpsListToVarisStrisListWithDuplsForEvol(hpsList)
//    }
//    case ComposedHP(Choice, hps @ _ *) => (Choice.flatten(hp).toList) match {
//      case hpsList => hpsListToVarisStrisListWithDuplsForEvol(hpsList)
//    }
//    case Evolve(h, primes @ _*) => primsToVarisStrisList(primes:_*)    
//    case Assign(v, t) => Nil
//    case AssignAny(v) => Nil
//    case Check(h) => Nil
//    case _ => throw new Exception("not implemented yet")
//  }
  
//  def seqToHpsList(hp : HP) : List[HP] = hp match {
//    case ComposedHP(Sequence, hps @ _ *) => 
//      seqToHpsList(hps.head) ++ hps.tail.map(hp => seqToHpsList(hp)).flatten 
//      // Sequence.flatten(hp)
//      // hp.asInstanceOf[ComposedHP].flatten
//    case x => List(x)
//  }
  
//  def seqToHpsList(hp : HP) : List[HP] = hp match {
//    case ComposedHP(Sequence, hps @ _ *) => 
//      seqToHpsList(hps.head) ++ hps.tail.map(hp => seqToHpsList(hp)).flatten 
//      // Sequence.flatten(hp)
//      // hp.asInstanceOf[ComposedHP].flatten
//    case x => List(x)
//  }
  
  def evolToPrimsList(evolve: HP) : List[(Var, Term)] = evolve match {
    case Evolve(h, primes @ _*) => primes.toList
    
//    case Evolve(h, primes @ _*) => primes.head match {
//      case (myVar : Var, myTerm : Term) => primes.head :: primes.tail.toList 
//      case _ => Nil
//    }
    case _ => throw new Exception("evolToPrimsList: input parameter is not of type Evolve")
  }
  
  def primsListToVarisList(primes : List[(Var, Term)]) : List[String] = primes match {
    case Nil => Nil
    // hayquePreguntar: Include what is in the "term" part?
    case (myVar, myTerm) :: xs => myVar match {
      case Var(s) => s :: primsListToVarisList(xs)
    }
//      
//      case (myVar, myTerm) => myVar match {
//    	case Var(s) => s :: primsToVarisStrisList(rest:_*)
//        // hayquePreguntar: Include what is in the "term" part?
//
//        case _ => throw new Exception("myVar is not of form Var(s)")
//      }
//      case _ => Nil
//
//    }
  }
    
//  def primsToVarisStrisList(primes : (Var, Term)*) : List[String] = primes match {
//    case Seq(varTermPair, rest @ _ *) => varTermPair match {
//      case (myVar, myTerm) => myVar match {
//    	case Var(s) => s :: primsToVarisStrisList(rest:_*)
//        // hayquePreguntar: Include what is in the "term" part?
//
//        case _ => throw new Exception("myVar is not of form Var(s)")
//      }
//      case _ => Nil
//
//    }
//    
//    case Seq((myVar, myTerm), rest @ _ *) => myVar match {
//      case Var(s) => s :: primsToVarisStrisList(rest:_*)
//      // hayquePreguntar: Include what is in the "term" part?
//
//      case _ => Nil
//    }
//  } 
  
  def hpToStatsList (hp : HP, varisStrisListFromHp : List[String], tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double) : List[Expr] = hp match {
    case ComposedHP(Sequence, hps @ _ *) => hpsListToStatsList(seqToHpsList(hp), varisStrisListFromHp : List[String], tendLimi, nUnroLoop, randMin, randMax)
    case ComposedHP(Star, hps @ _ *) => hpsListToStatsList(starToHpsList(hp, nUnroLoop), varisStrisListFromHp : List[String], tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double)
    case ComposedHP(Choice, hps @ _ *) =>
      hpToStatsList(Choice.flatten(hp).toList(
        rndm.nextInt(Choice.flatten(hp).toList.size)
      ), varisStrisListFromHp : List[String], tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double)
    case Evolve(h, primes @ _*) => List(
      bin_fun("Set",
        math_sym("glob`sol"),
        bin_fun("Append",
          math_sym("glob`sol"),
          mul_arg_fun("NDSolve", List(
            scalListToListExpr(
              EvolveToExpr.hpToOdesList(hp, EvolveToExpr.LOCA_T) ++
              varisStrisListToCurrsFetcsList(varisStrisListFromHp) ++
              EvolveToExpr.hToWhenEvents(h, 10, EvolveToExpr.LOCA_T)
            ),  
            scalListToListExpr(
              strisListToExprsList(varisStrisListFromHp)
            ),
            scalListToListExpr(List(
              math_sym("loca`t"),
              un_fun("Evaluate", un_fun("Last", math_sym("glob`tends"))),
              math_sym("glob`tendLimi")
            ))
          ))         
        )
      )) ++ varisStrisListToUpdaCurrStats(varisStrisListFromHp)
    
    case Assign(v, t) => List(bin_fun("Set", math_sym("curr`" + v.s), EvolveToExpr.termToExpr(t, EvolveToExpr.CURR_BACKTICK)))
    case AssignAny(v) =>
      List(bin_fun("Set",
        math_sym("curr`" + v.s),
        un_fun("RandomReal",
          scalListToListExpr(List(
            new Expr(randMin), new Expr(randMax)
          ))
        )
      ))
    case Check(h) => throw new Exception("Error: singleton h without nothing after it?")
      
//      seqToEvolsList(hps.head) ++ hps.tail.map(hp => seqToEvolsList(hp)).flatten    
//    case Evolve(_, _ @ _*) => (hp.asInstanceOf[Evolve] :: Nil)
    case _ => throw new Exception("not implemented yet")
  }
  
  def varisStrisListToUpdaCurrStats(varisStrisList : List[String]) : List[Expr] = varisStrisList match {
    case x :: xs =>
      bin_fun("Set",
        math_sym("curr`" + x),
        // First[Evaluate[
        //   ReplaceAll[y[Evaluate[Last[glob`tends]]], Last[glob`sol]]
        // ]]
        un_fun("First", un_fun("Evaluate", bin_fun("ReplaceAll",
            un_fun(x, un_fun("Evaluate", un_fun("Last", math_sym("glob`tends")))),
            un_fun("Last", math_sym("glob`sol"))
            )))
      ) :: varisStrisListToUpdaCurrStats(xs)
    case Nil => Nil
  }
  
  def strisListToExprsList(strisList: List[String]) : List[Expr] = strisList match {
    case x :: xs => math_sym(x) :: strisListToExprsList(xs)
      case Nil => Nil
  }
  
  def varisStrisListToCurrsFetcsList(varisStrisList : List[String]) : List[Expr] = varisStrisList match {
    case x :: xs =>
      bin_fun("Equal",
        un_fun(x, un_fun("Evaluate", un_fun("Last", math_sym("glob`tends")))),
        math_sym("curr`" + x)
      ) :: varisStrisListToCurrsFetcsList(xs)
    case Nil => Nil
  }
  
  def seqToHpsList(hp : HP) : List[HP] = hp match {
    case ComposedHP(Sequence, hps @ _ *) => 
      seqToHpsList(hps.head) ++ hps.tail.map(hp => seqToHpsList(hp)).flatten 
      // Sequence.flatten(hp)
      // hp.asInstanceOf[ComposedHP].flatten
    case x => List(x)
  }
  
  def starToHpsList(hp : HP, nUnroLoopLeft: Integer) : List[HP] = {
    if (0 == nUnroLoopLeft) {
      return Nil
    } else {
      return hp match {
        case ComposedHP(Star, hps @ _ *) => 
          hps.head :: starToHpsList(hp, nUnroLoopLeft - 1)
          // Sequence.flatten(hp)
          // hp.asInstanceOf[ComposedHP].flatten
        case x => List(x)
      }
    }
    
  }
  
  def choiToHpsList(hp : HP) : List[HP] = hp match {
    case ComposedHP(Choice, hps @ _ *) => 
      choiToHpsList(hps.head) ++ hps.tail.map(hp => choiToHpsList(hp)).flatten 
      // Sequence.flatten(hp)
      // hp.asInstanceOf[ComposedHP].flatten
    case x => List(x)
  }
  
  def hpsListToStatsList(hpsList: List[HP], varisStrisListFromHp : List[String], tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double) : List[Expr] = hpsList match {
    case x :: xs => x match {
      case Check(h) => List(mul_arg_fun("If", List(
        EvolveToExpr.formulaToExpr(h, EvolveToExpr.CURR_BACKTICK),
        mul_arg_fun("CompoundExpression", hpsListToStatsList(xs, varisStrisListFromHp : List[String], tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double)),
        bin_fun("Set", math_sym("loca`nop"), new Expr(0))
      )))
      case _ => hpToStatsList(x, varisStrisListFromHp : List[String], tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double) ++
          hpsListToStatsList(xs, varisStrisListFromHp : List[String], tendLimi : Double, nUnroLoop : Int, randMin : Double, randMax : Double)
    }
        
    case Nil => Nil
  }
  
  def nth(index: Int, aList: List[Int]) = {
    if (index > aList.length - 1) println("Index out of range of list")
    else aList(index)
  }

  def varisToDispToPlotComms(varisToDisp: List[String], nGrapsPerRow : Int) : List[Expr] = {    
    return List(un_fun("GraphicsGrid", scalListListToListListExpr(
        List() :: // If you don't append List() to the beginning, the axis labels of the uppermost plots are sometimes not visible  
        varisToDispToPlotCommsScalListList(varisToDisp: List[String], nGrapsPerRow : Int))))
  }
    
  def varisToDispToPlotCommsScalListList(varisToDisp: List[String], nGrapsPerRow : Int) : List[List[Expr]] = varisToDisp match {
	    case Nil => List(List())
	    case x::xs =>
	      return varisToDispToPlotCommScalList(varisToDisp.take(nGrapsPerRow)) ::
	          varisToDispToPlotCommsScalListList(varisToDisp.drop(nGrapsPerRow), nGrapsPerRow)
  }
  
  def varisToDispToPlotCommScalList (varisToDisp: List[String]) : List[Expr] = varisToDisp match {
    case Nil => Nil
    case x::xs =>
      mul_arg_fun("Plot", List(
        un_fun("Piecewise",
          bin_fun("Table",
            mul_arg_fun("List", List(
              un_fun("Evaluate",
                bin_fun("ReplaceAll",
                  un_fun(x, math_sym("loca`t")),
                  bin_fun("Part", math_sym("glob`sol"), math_sym("loca`i"))
                )
              ),
              bin_fun("And",
                bin_fun("GreaterEqual",
                  math_sym("loca`t"),
                  bin_fun("Part",
                    math_sym("glob`tends"),
                    math_sym("loca`i")
                  )
                ),
                bin_fun("Less",
                  math_sym("loca`t"),
                  bin_fun("Part", 
                    math_sym("glob`tends"),
                    bin_fun("Plus", math_sym("loca`i"), new Expr(1))
                  )
                )
                
              )
            )),
            mul_arg_fun("List", List(
              math_sym("loca`i"),
              new Expr(1),
              bin_fun("Plus",
                un_fun("Length", math_sym("glob`tends")),
                new Expr(-1)
              )
            ))
          )
        ),
        mul_arg_fun("List", List(
          math_sym("loca`t"),
          un_fun("Evaluate", un_fun("First", math_sym("glob`tends"))),
          un_fun("Evaluate", un_fun("Last", math_sym("glob`tends")))
        )),
        bin_fun("Rule",
          math_sym("AxesLabel"),
          scalListToListExpr(List(
            bin_fun("Style", math_sym("t"), math_int("20")),
            bin_fun("Style", math_str(x), math_int("20"))
          ))
        )
      )) :: varisToDispToPlotCommScalList(xs)
  }
    
  def scalListListToListListExpr(scalListList : List[List[Expr]]) : Expr = {
    return scalListToListExpr(scalListListToScalListOfListExpr(scalListList))
  } 
  
  def scalListListToScalListOfListExpr(scalListList : List[List[Expr]]) : List[Expr] = scalListList match {
    case Nil => Nil
    case x :: xs => scalListToListExpr(x) :: scalListListToScalListOfListExpr(xs)
  }
 
}
