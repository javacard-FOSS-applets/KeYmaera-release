//This file is part of KeY - Integrated Deductive Software Design
//Copyright (C) 2001-2009 Universitaet Karlsruhe, Germany
//                    Universitaet Koblenz-Landau, Germany
//                    Chalmers University of Technology, Sweden
//
//The KeY system is protected by the GNU General Public License. 
//See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.smt;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.Sequent;

public class SimplifyTranslator extends AbstractSMTTranslator {

    private static final Logger logger = Logger
    .getLogger(SimplifyTranslator.class.getName());
    
//  counter used for making names unique
    private int counter = 0;

    private static StringBuffer INTSTRING = new StringBuffer("int");

    private static StringBuffer FALSESTRING = new StringBuffer("FALSE");

    private static StringBuffer TRUESTRING = new StringBuffer("TRUE");

    private static StringBuffer ALLSTRING = new StringBuffer("FORALL");

    private static StringBuffer EXISTSTRING = new StringBuffer("EXISTS");

    private static StringBuffer ANDSTRING = new StringBuffer("AND");

    private static StringBuffer ORSTRING = new StringBuffer("OR");

    private static StringBuffer NOTSTRING = new StringBuffer("NOT");

    private static StringBuffer EQSTRING = new StringBuffer("EQ");

    private static StringBuffer IMPLYSTRING = new StringBuffer("IMPLIES");

    private static StringBuffer PLUSSTRING = new StringBuffer("+");

    private static StringBuffer MINUSSTRING = new StringBuffer("-");

    private static StringBuffer MULTSTRING = new StringBuffer("*");

    private static StringBuffer LTSTRING = new StringBuffer("<");

    private static StringBuffer GTSTRING = new StringBuffer(">");

    private static StringBuffer LEQSTRING = new StringBuffer("<=");

    private static StringBuffer GEQSTRING = new StringBuffer(">=");

    private static StringBuffer NULLSTRING = new StringBuffer("null");

    private static StringBuffer NULLSORTSTRING = new StringBuffer("NULLSORT");
    
    
    /**
     * Just a constructor which starts the conversion to Simplify syntax.
     * The result can be fetched with
     * 
     * @param sequent
     *                The sequent which shall be translated.
     *                
     * @param services
     * 		      The services Object belonging to the sequent.
     */
    public SimplifyTranslator(Sequent sequent, Services services) {
	super(sequent, services);
    }
    
    public SimplifyTranslator(Services services) {
	super(services);
    }
    
    @Override
    protected StringBuffer buildCompleteText(StringBuffer formula,
	    ArrayList<StringBuffer> assumptions,
	    ArrayList<ContextualBlock> assumptionBlocks,
	    ArrayList<ArrayList<StringBuffer>> functions,
	    ArrayList<ArrayList<StringBuffer>> predicates,
	    ArrayList<ContextualBlock> predicateBlocks,
	    ArrayList<StringBuffer> types, SortHierarchy sortHierarchy) {
	
	StringBuffer toReturn = new StringBuffer();
	
	String [] commentPredicate = new String[2];
	commentPredicate[ContextualBlock.PREDICATE_FORMULA] = "\n;Predicates used in formula:\n";
	commentPredicate[ContextualBlock.PREDICATE_TYPE]    = "\n;Types expressed by predicates:\n";
	String [] commentAssumption = new String[4];
	commentAssumption[ContextualBlock.ASSUMPTION_DUMMY_IMPLEMENTATION] = "\n\n;Assumptions for dummy variables:\n";
	commentAssumption[ContextualBlock.ASSUMPTION_FUNCTION_DEFINTION] = "\n\n;Assumptions for function definitions:\n"; 
	commentAssumption[ContextualBlock.ASSUMPTION_SORT_PREDICATES] = "\n\n;Assumptions for sort predicates:\n";
	commentAssumption[ContextualBlock.ASSUMPTION_TYPE_HIERARCHY] = "\n\n;Assumptions for type hierarchy:\n";
	
	StringBuffer comment = new StringBuffer("\n\n;The formula:\n");
	formula = comment.append(formula);
	
	ArrayList<ArrayList<StringBuffer>> PredicatesToRemove = new ArrayList<ArrayList<StringBuffer>>();

	StringBuffer preds = new StringBuffer();
	
	for(int k=0; k < commentPredicate.length; k++)
	{    
	    ContextualBlock block = predicateBlocks.get(k);

	    if (block.getStart() <= block.getEnd()) {
		preds.append(commentPredicate[block.getType()]);
		for(int j = block.getStart(); j <= block.getEnd(); j++){
		    PredicatesToRemove.add(predicates.get(j));
            preds.append("(DEFPRED (").append(predicates.get(j).get(0));
		    for (int i = 1; i < predicates.get(j).size(); i++) {
			StringBuffer var = new StringBuffer("x");
			var = this.makeUnique(var);
                preds.append(" ").append(var);
		    	}
		    preds.append("))\n");
		}
	    }
	}
	

	predicates.removeAll(PredicatesToRemove);
	if(predicates.size() >0)
	{
	    preds.append("\n;Other predicates:\n");
	    for (ArrayList<StringBuffer> s : predicates) {
            preds.append("(DEFPRED (").append(s.get(0));
		for (int i = 1; i < s.size(); i++) {
		    StringBuffer var = new StringBuffer("x");
		    var = this.makeUnique(var);
            preds.append(" ").append(var);
		}
		preds.append("))\n");
	    }
	} 
	
	toReturn.append(preds);
	toReturn.append("\n");
	
	ArrayList<StringBuffer> AssumptionsToRemove = new ArrayList<StringBuffer>();
        StringBuffer assump = new StringBuffer();
	
        if(assumptions.size() > 0){
           for(int k=0; k < commentAssumption.length; k++){
                ContextualBlock block = assumptionBlocks.get(k);
                
                if (block.getStart() <= block.getEnd()) {
                    
                    // necessary for appending 'ANDs' correctly
                    // Without if-clause the left side of the first logical and could be empty
                    StringBuffer temp = new StringBuffer();
                    int start = block.getStart();
                    if(assump.length() ==0){
                	temp.append(assumptions.get(start));
                	AssumptionsToRemove.add(assumptions.get(start));
                	start++;
                    }
                    assump.append(commentAssumption[block.getType()]);
                    assump.append(temp);
                    
        	    for (int i = start; i <= block.getEnd(); i++) {
        		assump = this.translateLogicalAnd(assump, assumptions.get(i));
        		AssumptionsToRemove.add(assumptions.get(i));
        	    }
            	
                }
    	
    	
            }
           
           assumptions.removeAll(AssumptionsToRemove);
         
           if (assumptions.size() > 0) {
               int start =0;
               StringBuffer temp = new StringBuffer();
               if(assump.length() ==0){
           	temp.append(assumptions.get(start));
           	start++;
               }
               assump.append("\n\n;Other assumptions:\n");
   	       
   	       for (int i = start; i < assumptions.size(); i++) {
   		   assump = this.translateLogicalAnd(assump, assumptions.get(i));
   	       }
   	  
   	    }
     
           
           formula = this.translateLogicalImply(assump, formula);
           formula.append("\n");
        }
     
	
	
	
	
	/* CAUTION!! For some reason, the solver gives the correct result,
	 * if this part is added. The reason, why this is, is not clear to me yet!
	 */
	StringBuffer temp = new StringBuffer ();
	temp.append("(").append(ALLSTRING).append(" () (").append(EXISTSTRING)
		.append(" () ").append(formula).append("))");
	formula = temp;
	/* End of adding part */
	
	toReturn.append(formula);
	
	logger.info("Resulting formula after translation:");
	logger.info(toReturn);
	
	return toReturn;
    }

    @Override
    protected StringBuffer getIntegerSort() {
	return INTSTRING;
    }

    @Override
    protected boolean isMultiSorted() {
	return false;
    }

    @Override
    protected StringBuffer translateFunction(StringBuffer name,
	    ArrayList<StringBuffer> args) {
	return this.buildFunction(name, args);
    }

    @Override
    protected StringBuffer translateFunctionName(StringBuffer name) {
	return makeUnique(name);
    }

    @Override
    protected StringBuffer translateIntegerDiv(StringBuffer arg1,
	    StringBuffer arg2) {
	return new StringBuffer("unknownOp");
    }

    @Override
    protected StringBuffer translateIntegerGeq(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(GEQSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerGt(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(GTSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerLeq(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(LEQSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerLt(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(LTSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerMinus(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(MINUSSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerMod(StringBuffer arg1,
	    StringBuffer arg2) {
	return new StringBuffer("unknownOp");
    }

    @Override
    protected StringBuffer translateIntegerMult(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(MULTSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerPlus(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(PLUSSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerUnaryMinus(StringBuffer arg) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	StringBuffer n = new StringBuffer("0");
	args.add(n);
	args.add(arg);
	return buildFunction(MINUSSTRING, args);
    }

    @Override
    protected StringBuffer translateIntegerValue(long val) {
	return new StringBuffer(""+val);
    }

    @Override
    protected StringBuffer translateLogicalAll(StringBuffer var,
	    StringBuffer type, StringBuffer form) {
	StringBuffer toReturn = new StringBuffer("(" + ALLSTRING + " ");
        toReturn.append("(").append(var).append(") ").append(form).append(")");
	return toReturn;
    }

    @Override
    protected StringBuffer translateLogicalAnd(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(ANDSTRING, args);
    }

    @Override
    protected StringBuffer translateLogicalConstant(StringBuffer name) {
	return makeUnique(name);
    }

    @Override
    protected StringBuffer translateLogicalEquivalence(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);

	ArrayList<StringBuffer> argsrev = new ArrayList<StringBuffer>();
	argsrev.add(arg2);
	argsrev.add(arg1);

	ArrayList<StringBuffer> forms = new ArrayList<StringBuffer>();
	forms.add(buildFunction(IMPLYSTRING, args));
	forms.add(buildFunction(IMPLYSTRING, argsrev));
	return buildFunction(ANDSTRING, forms);
    }

    @Override
    protected StringBuffer translateLogicalExist(StringBuffer var,
	    StringBuffer type, StringBuffer form) {
	StringBuffer toReturn = new StringBuffer("(" + EXISTSTRING + " ");
        toReturn.append("(").append(var).append(") ").append(form).append(")");
	return toReturn;
    }

    @Override
    protected StringBuffer translateLogicalFalse() {
	return FALSESTRING;
    }

    @Override
    protected StringBuffer translateLogicalImply(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(IMPLYSTRING, args);
    }

    @Override
    protected StringBuffer translateLogicalNot(StringBuffer arg) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg);
	return this.buildFunction(NOTSTRING, args);
    }

    @Override
    protected StringBuffer translateLogicalOr(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(ORSTRING, args);
    }

    @Override
    protected StringBuffer translateLogicalTrue() {
	return TRUESTRING;
    }

    @Override
    protected StringBuffer translateLogicalVar(StringBuffer name) {
	return makeUnique(name);
    }

    @Override
    protected StringBuffer translateNull() {
	return NULLSTRING;
    }

    @Override
    protected StringBuffer translateNullSort() {
	return NULLSORTSTRING;
    }

    @Override
    protected StringBuffer translateLogicalIfThenElse(StringBuffer cond, StringBuffer ifterm, StringBuffer elseterm) {
        StringBuffer toReturn = this.translateLogicalImply(cond, ifterm);
        StringBuffer temp = this.translateLogicalNot(cond);
        temp = this.translateLogicalImply(temp, elseterm);
        toReturn = this.translateLogicalAnd(toReturn, temp);
        return toReturn;
    }
    
    @Override
    protected StringBuffer translateObjectEqual(StringBuffer arg1,
	    StringBuffer arg2) {
	ArrayList<StringBuffer> args = new ArrayList<StringBuffer>();
	args.add(arg1);
	args.add(arg2);
	return this.buildFunction(EQSTRING, args);
    }

    @Override
    protected StringBuffer translatePredicate(StringBuffer name,
	    ArrayList<StringBuffer> args) {
	return this.buildFunction(name, args);
    }

    @Override
    protected StringBuffer translatePredicateName(StringBuffer name) {
	return makeUnique(name);
    }

    @Override
    protected StringBuffer translateSort(String name, boolean isIntVal) {
	return makeUnique(new StringBuffer(name));
    }

    private StringBuffer buildFunction(StringBuffer name,
	    ArrayList<StringBuffer> args) {
	StringBuffer toReturn = new StringBuffer();
	    toReturn.append("(");
	    toReturn.append(name);
        for (StringBuffer arg : args) {
            toReturn.append(" ");
            toReturn.append(arg);
        }
	    toReturn.append(")");
	return toReturn;
    }
    
    private StringBuffer removeIllegalChars(StringBuffer template, ArrayList<String> toReplace, ArrayList<String> replacement) {
	//replace one String
	for (int i = 0; i < toReplace.size(); i++) {
	    String toRep = toReplace.get(i);
	    String replace = replacement.get(i);
	    int index = template.indexOf(toRep);
	    while (index >= 0) {
		template.replace(index, index + toRep.length(), replace);
		index = template.indexOf(toRep);
	    }
	}
	
	return template;
    }
    
    private StringBuffer makeUnique(StringBuffer name) {
	StringBuffer toReturn = new StringBuffer(name);
	
	//build the replacement pairs
	ArrayList<String> toReplace = new ArrayList<String>();
	ArrayList<String> replacement = new ArrayList<String>();
	
	toReplace.add("[]");
	replacement.add("_Array");
	
	toReplace.add("<");
	replacement.add("_abo_");
	
	toReplace.add(">");
	replacement.add("_abc_");
	
	toReplace.add("{");
	replacement.add("_cbo_");
	
	toReplace.add("}");
	replacement.add("_cbc_");
	
	toReplace.add(".");
	replacement.add("_dot_");
	
	toReplace.add(":");
	replacement.add("_col_");
	
	toReplace.add("\\");
	replacement.add("_");
	
	toReturn = this.removeIllegalChars(toReturn, toReplace, replacement);
	
	toReturn.append("_").append(counter);
	counter++;
	return toReturn;
    }
}
