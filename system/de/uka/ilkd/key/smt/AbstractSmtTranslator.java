package de.uka.ilkd.key.smt;

import java.util.*;

import de.uka.ilkd.key.logic.*;
import de.uka.ilkd.key.logic.op.*;
import de.uka.ilkd.key.logic.sort.Sort;
//import de.uka.ilkd.key.proof.decproc.ConstraintSet;
import de.uka.ilkd.key.rule.SyntacticalReplaceVisitor;
import de.uka.ilkd.key.util.Debug;
import de.uka.ilkd.key.java.Services;

import org.apache.log4j.Logger;

public abstract class AbstractSmtTranslator implements SmtTranslator{
    static Logger logger = Logger.getLogger(AbstractSmtTranslator.class
	    .getName());

    private Sort jbyteSort;

    private Sort jshortSort;

    private Sort jintSort;

    private Sort jlongSort;

    private Sort jcharSort;

    private Sort integerSort;

    // private static long counter = 0;   
    protected static final int YESNOT = 2;

    /** The translation result is stored in this variable. */
    protected String text;

    /** StringBuffer contains all declared predicate symbols. */
    protected final StringBuffer predicate = new StringBuffer();

    /** StringBuffer to store text which could be usefull for the user */
    protected final StringBuffer notes = new StringBuffer();

    /** remember all printed predicates */
    protected final Set<Operator> predicateSet = new HashSet<Operator>();

    /** remember all printed functions */
    protected final Set<Operator> functionSet = new HashSet<Operator>();

    protected final Set<Sort> sortSet = new HashSet<Sort>();

    /** remember all function declarations */

    /** The Constraint under which the sequent is to be proven */
   // protected final ConstraintSet constraintSet;

    protected final HashSet usedGlobalMv = new HashSet();

    protected final HashSet usedLocalMv = new HashSet();

    protected final SetOfMetavariable localMetavariables;

    private HashMap<Operator, ArrayList<Sort>> functionDecls = new HashMap<Operator, ArrayList<Sort>>();

    private HashSet<Function> specialFunctions = new HashSet<Function>();
    
    private HashMap<Operator, ArrayList<Sort>> predicateDecls = new HashMap<Operator, ArrayList<Sort>>();

    private HashMap<Operator, StringBuffer> usedVariableNames = new HashMap<Operator, StringBuffer>();

    private HashMap<Operator, StringBuffer> usedFunctionNames = new HashMap<Operator, StringBuffer>();

    private HashMap<Operator, StringBuffer> usedPredicateNames = new HashMap<Operator, StringBuffer>();

    private HashMap<Sort, StringBuffer> usedDisplaySort = new HashMap<Sort, StringBuffer>();

    private HashMap<Sort, StringBuffer> usedRealSort = new HashMap<Sort, StringBuffer>();

    private HashMap<Sort, StringBuffer> typePredicates = new HashMap<Sort, StringBuffer>();

    // used type predicates for constant values, e.g. 1, 2, ...
    private HashMap<Term, StringBuffer> constantTypePreds = new HashMap<Term, StringBuffer>();

    private StringBuffer nullString = new StringBuffer();

    private boolean nullUsed = false;

    /**
     * Just a constructor which starts the conversion to Simplify syntax.
     * The result can be fetched with
     * 
     * @param sequent
     *                The sequent which shall be translated.
     * @param localmv
     *                The local metavariables, should be the ones introduced
     *                after the last branch.
     */
//    public AbstractSmtTranslator(Sequent sequent, ConstraintSet cs,
//	    SetOfMetavariable localmv, Services services, boolean lightWeight) {
    public AbstractSmtTranslator(Sequent sequent,
	    SetOfMetavariable localmv, Services services, boolean lightWeight) {
    	localMetavariables = localmv;
//	constraintSet = cs;
	jbyteSort = services.getTypeConverter().getByteLDT().targetSort();
	jshortSort = services.getTypeConverter().getShortLDT().targetSort();
	jintSort = services.getTypeConverter().getIntLDT().targetSort();
	jlongSort = services.getTypeConverter().getLongLDT().targetSort();
	jcharSort = services.getTypeConverter().getCharLDT().targetSort();
	integerSort = services.getTypeConverter().getIntegerLDT().targetSort();
    }

//    public AbstractSmtTranslator(Sequent sequent, ConstraintSet cs,
//	    SetOfMetavariable localmv, Services services) {
    public AbstractSmtTranslator(Sequent sequent,
	    SetOfMetavariable localmv, Services services) {
	this(sequent, localmv, services, false);
    }

    /**
     * For translating only terms and not complete sequents.
     */
    public AbstractSmtTranslator(Services s) {
	this(null, null, s, false);
    }

    /**
     * Translate a sequent into a given syntax.
     * @param sequent the sequent to translate.
     * @param services wrapper object for service attributes.
     * @return A StringBuffer representing the sequent in the given syntax.
     * @throws IllegalFormulaException if the sequent could not be translated.
     */
    public final StringBuffer translate(Sequent sequent, Services services)
	    throws IllegalFormulaException {
	return translate(sequent, false, services);
    }

    /**
     * Translates the given sequent into "Simplify" input syntax and adds
     * the resulting string to the StringBuffer sb.
     * 
     * @param sequent
     *                the Sequent which should be written in Simplify syntax
     */
    public final StringBuffer translate(Sequent sequent,
	    boolean lightWeight, Services services)
	    throws IllegalFormulaException {

	// translate
	StringBuffer hb = new StringBuffer();
	StringBuffer ante;
	ante = translate(sequent.antecedent(), SmtTranslator.TERMPOSITION.ANTECEDENT, lightWeight,
		services);
	StringBuffer succ;
	succ = translate(sequent.succedent(), SmtTranslator.TERMPOSITION.SUCCEDENT, lightWeight, services);

	//add one variable for each sort
	for (Sort s : this.usedRealSort.keySet()) {
	    LogicVariable l = new LogicVariable(new Name("dummy_" + s.name().toString()), s);
	    this.addFunction(l, new ArrayList<Sort>(), s);
	    this.translateFunc(l, new ArrayList<StringBuffer>());
	}
	
	ArrayList<StringBuffer> assumptions = this.getAssumptions(services);
	
	hb = this.translateLogicalImply(ante, succ);

	return buildCompleteText(hb, assumptions, this.buildTranslatedFuncDecls(), this
		.buildTranslatedPredDecls(), this.buildTranslatedSorts(), this
		.buildSortHirarchy());
    }

    
    /**
     * get the assumptions made by the logic.
     * @return ArrayList of Formulas, that are assumed to be true.
     */
    private ArrayList<StringBuffer> getAssumptions(Services services) {
	ArrayList<StringBuffer> toReturn = new ArrayList<StringBuffer>();
	
	if (!this.isMultiSorted()) {
	    
	    // add the type definitions
	    //this means all predicates that are needed for functions to define
	    //their result type, all predicates for constants (like number symbols)
	    toReturn.addAll(this.getTypeDefinitions());
	    // add the type hirarchy
	    //this means, add the typepredicates, that are needed to define
	    //for every type, what type they are (direct) subtype of
	    toReturn.addAll(this.getSortHirarchyPredicates());
	    //add the formulas, that make sure, type correctness is kept, also
	    //for interpreted functions
	    toReturn.addAll(this.getSpecialSortPredicates(services));
	}
	
	return toReturn;
    }
    
    /**
     * build the formulas, that make sure, special functions keep
     * typing
     * @return ArrayList of formulas, assuring the assumption.
     */
    private ArrayList<StringBuffer> getSpecialSortPredicates(Services services) {
	ArrayList<StringBuffer> toReturn = new ArrayList<StringBuffer>();
	
	for (Function o : this.specialFunctions) {
	    
	    ArrayList<StringBuffer> varList = new ArrayList<StringBuffer>();
	    ArrayList<StringBuffer> predList = new ArrayList<StringBuffer>();
	    //build the variables and typepredicates for quantification
	    for (int i = 0; i < o.arity(); i++) {
		StringBuffer var = this.translateLogicalVar(new StringBuffer("tvar" + i));
		varList.add(var);
		ArrayList<StringBuffer> templist = new ArrayList<StringBuffer>();
		templist.add(var);
		StringBuffer temppred = this.typePredicates.get(o.argSort(i));
		predList.add(this.translatePredicate(temppred, templist));
	    }
	    
	    //build the left side of the implication
	    StringBuffer leftForm = predList.get(0);
	    for (int i = 1; i < predList.size(); i++) {
		leftForm = this.translateLogicalAnd(leftForm, predList.get(i));
	    }
	    
	    //build the right side of the implication
	    StringBuffer rightForm = new StringBuffer();
	    
	    //use the interpreted function here!!
	    if (o == services.getTypeConverter().getIntegerLDT().getAdd()) {
		rightForm = this.translateIntegerPlus(varList.get(0), varList.get(1));
	    } else if (o == services.getTypeConverter().getIntegerLDT().getSub()) {
		rightForm = this.translateIntegerMinus(varList.get(0), varList.get(1));
	    } else if (o == services.getTypeConverter().getIntegerLDT().getMul()) {
		rightForm = this.translateIntegerMult(varList.get(0), varList.get(1));
	    } else if (o == services.getTypeConverter().getIntegerLDT().getDiv()) {
		rightForm = this.translateIntegerDiv(varList.get(0), varList.get(1));
	    }
	    
	    StringBuffer rightPred = this.typePredicates.get(o.sort());
	    ArrayList<StringBuffer> tempList = new ArrayList<StringBuffer>();
	    tempList.add(rightForm);
	    rightForm = this.translatePredicate(rightPred, tempList);
	    
	    StringBuffer form = this.translateLogicalImply(leftForm, rightForm);
	    
	    for (int i = 0; i < varList.size(); i++) {
		form = this.translateLogicalAll(varList.get(i), this.getIntegerSort(), form);
	    }
	    
	    toReturn.add(form);
	}
	
	return toReturn;
    }
    
    /**
     * Translate s term into the given syntax.
     * @param t The term to translate.
     * @param services a service wrapper object.
     * @return A StringBuffer, representing the term in the given syntax.
     * @throws IllegalArgumentException if the term is not of type FORMULA or could not be translated.
     */
    public final StringBuffer translate(Term t, Services services) 
    		throws IllegalArgumentException {
	//check, if the term is of type formula. otherwise a translation does not make sense
	if (t.sort() != Sort.FORMULA) {
	    throw new IllegalArgumentException("The given Term is not Type of Formula");
	}
//	 translate
	try {
	    //StringBuffer hb = new StringBuffer();
	    StringBuffer form;
	    form = translateTerm(t, new Vector<QuantifiableVariable>(), services);
	    
	    return buildCompleteText(form, this.getAssumptions(services), this.buildTranslatedFuncDecls(), this
		    .buildTranslatedPredDecls(), this.buildTranslatedSorts(), this
		    .buildSortHirarchy());
	} catch (IllegalFormulaException e) {
	    throw new IllegalArgumentException("Illegal formula. Can not be translated");
	}
    }
    
    /**
     * Build the sorthirarchy for the sorts
     * If null was used, add typepredicates for all types.
     * 
     * @return a sort hirarchy for the sorts
     */
    private SortHirarchy buildSortHirarchy() {
	return new SortHirarchy(this.usedDisplaySort, this.typePredicates);
    }

    /**
     * Get the expression for that defines the typepredicates for sort hirarchy.
     * Also the null type is added to the formula if used before.
     * @return The well defined formula.
     */
    private ArrayList<StringBuffer> getSortHirarchyPredicates() {
	SortHirarchy sh = this.buildSortHirarchy();
	//StringBuffer toReturn = new StringBuffer(this.translateLogicalTrue());
	ArrayList<StringBuffer> toReturn = new ArrayList<StringBuffer>();
	
	// add the typepredicates for functions.
	HashMap<StringBuffer, ArrayList<StringBuffer>> predMap = sh
		.getDirectSuperSortPredicate();
	for (StringBuffer leftPred : predMap.keySet()) {
	    StringBuffer form = new StringBuffer();
	    for (StringBuffer rightPred : predMap.get(leftPred)) {
		StringBuffer var = this.translateLogicalVar(new StringBuffer(
			"tvar"));
		ArrayList<StringBuffer> varlist = new ArrayList<StringBuffer>();
		varlist.add(var);
		StringBuffer leftForm = this.translatePredicate(leftPred,
			varlist);

		StringBuffer rightForm = this.translatePredicate(rightPred,
			varlist);

		form = this.translateLogicalImply(leftForm, rightForm);
		form = this.translateLogicalAll(var, this.getIntegerSort(),
			form);
	    }
	    if (form.length() > 0) {
		toReturn.add(form);
	    }
	}

	// add the typepredicates for null
	if (this.nullUsed) {
	    for (StringBuffer s : this.typePredicates.values()) {
		ArrayList<StringBuffer> argList = new ArrayList<StringBuffer>();
		argList.add(this.nullString);
		StringBuffer toAdd = this.translatePredicate(s, argList);
		toReturn.add(toAdd);
	    }
	}

	return toReturn;
    }

    /**
     * Returns a set of formula s, that defines the resulttypes of functions,
     * all constants and other elements (i.e. constant number symbols).
     * @return see above
     */
    private ArrayList<StringBuffer> getTypeDefinitions() {
	ArrayList<StringBuffer> toReturn = new ArrayList<StringBuffer>();

	// add the type definitions for functions
	for (Operator op : functionDecls.keySet()) {
	    StringBuffer currentForm = this.getSingleFunctionDef(
		    this.usedFunctionNames.get(op), functionDecls.get(op));
	    toReturn.add(currentForm);
	}

	//add the type predicates for constant values like number symbols
	for (StringBuffer s : this.constantTypePreds.values()) {
	    toReturn.add(s);
	}
	
	return toReturn;
    }

    /**
     * Get the type predicate definition for a given function
     * @param funName the name of the function.
     * @param sorts the sorts, the function is defined for. 
     * 		Last element is the return type.
     * @return well formed expression that defines the type of the function.
     */
    private StringBuffer getSingleFunctionDef(StringBuffer funName,
	    ArrayList<Sort> sorts) {
	StringBuffer toReturn = new StringBuffer();

	// collect the quantify vars
	ArrayList<StringBuffer> qVar = new ArrayList<StringBuffer>();
	for (int i = 0; i < sorts.size() - 1; i++) {
	    qVar.add(this.translateLogicalVar(new StringBuffer("tvar")));
	}

	// left hand side of the type implication
	if (qVar.size() > 0) {
	    toReturn = this.getTypePredicate(sorts.get(0), qVar.get(0));
	}
	for (int i = 1; i < qVar.size(); i++) {
	    StringBuffer temp = getTypePredicate(sorts.get(i), qVar.get(i));
	    toReturn = this.translateLogicalAnd(toReturn, temp);
	}

	// build the right side
	StringBuffer rightSide;
	rightSide = this.translateFunction(funName, qVar);
	rightSide = getTypePredicate(sorts.get(sorts.size() - 1), rightSide);

	if (qVar.size() > 0) {
	    toReturn = this.translateLogicalImply(toReturn, rightSide);
	} else {
	    toReturn = rightSide;
	}

	// built the forall around it
	for (int i = qVar.size() - 1; i >= 0; i--) {
	    toReturn = this.translateLogicalAll(qVar.get(i),
		    this.usedDisplaySort.get(sorts.get(i)), toReturn);
	}

	return toReturn;
    }

    /**
     * Build the translated function declarations. Each element in the
     * ArrayList represents (functionname | argType1 | ... | argTypen|
     * resultType)
     * 
     * @return structured List of declaration.
     */
    private ArrayList<ArrayList<StringBuffer>> buildTranslatedFuncDecls() {
	ArrayList<ArrayList<StringBuffer>> toReturn = new ArrayList<ArrayList<StringBuffer>>();
	//add the function declarations for each used function
	for (Operator op : this.functionDecls.keySet()) {
	    ArrayList<StringBuffer> element = new ArrayList<StringBuffer>();
	    element.add(usedFunctionNames.get(op));
	    for (Sort s : functionDecls.get(op)) {
		element.add(usedDisplaySort.get(s));
	    }
	    toReturn.add(element);
	}

	if (this.nullUsed) {
	    //add the null constant to the declarations
	    if (this.isMultiSorted()) {
		ArrayList<StringBuffer> a = new ArrayList<StringBuffer>();
		a.add(this.nullString);
		a.add(this.translateNullSort());
		toReturn.add(a);
	    } else {
		ArrayList<StringBuffer> a = new ArrayList<StringBuffer>();
		a.add(this.nullString);
		a.add(this.getIntegerSort());
		toReturn.add(a);
	    }
	}

	return toReturn;
    }

    /**
     * Build the translated predicate declarations. Each element in the
     * ArrayList represents (predicatename | argType1 | ... | argTypen)
     * 
     * @return structured List of declaration.
     */
    private ArrayList<ArrayList<StringBuffer>> buildTranslatedPredDecls() {
	ArrayList<ArrayList<StringBuffer>> toReturn = new ArrayList<ArrayList<StringBuffer>>();
	// add the predicates
	for (Operator op : this.predicateDecls.keySet()) {
	    ArrayList<StringBuffer> element = new ArrayList<StringBuffer>();
	    element.add(usedPredicateNames.get(op));
	    for (Sort s : predicateDecls.get(op)) {
		element.add(usedDisplaySort.get(s));
	    }
	    toReturn.add(element);
	}

	// add the typePredicates
	if (!this.isMultiSorted()) {
	    for (Sort s : this.typePredicates.keySet()) {
		ArrayList<StringBuffer> element = new ArrayList<StringBuffer>();
		element.add(typePredicates.get(s));
		element.add(this.usedDisplaySort.get(s));
		toReturn.add(element);
	    }
	}

	return toReturn;
    }

    /**
     * ArrayList of all sorts, that were used as sorts. Including the
     * integer sort.
     * 
     * @return ArrayList of the names of sorts.
     */
    private ArrayList<StringBuffer> buildTranslatedSorts() {
	ArrayList<StringBuffer> toReturn = new ArrayList<StringBuffer>();
	for (Sort s : this.usedDisplaySort.keySet()) {
	    StringBuffer newSort = this.usedDisplaySort.get(s);
	    // make sure, no sort is added twice!!
	    boolean alreadyIn = false;
	    for (int i = 0; !alreadyIn && i < toReturn.size(); i++) {
		if (toReturn.get(i).equals(newSort)) {
		    alreadyIn = true;
		}
	    }
	    if (!alreadyIn) {
		toReturn.add(newSort);
	    }
	}
	return toReturn;
    }

    /**
     * Build the text, that can be read by the final decider.
     * If the assumptions should be added to the formula, add them like
     * assumtions impliy formula.
     * 
     * @param formula
     *                The formula, that was built out of the internal
     *                representation. It is built by ante implies succ.
     * @param assumptions
     * 		      Assumptions made in this logic. Set of formulas, that
     * 		      are assumed to be true.             
     * @param functions
     *                List of functions. Each Listelement is built up like
     *                (name | sort1 | ... | sortn | resultsort)
     * @param predicates
     *                List of predicates. Each Listelement is built up like
     *                (name | sort1 | ... | sortn)
     * @param types
     *                List of the used types.
     * @return The Stringbuffer that can be read by the decider
     */
    protected abstract StringBuffer buildCompleteText(StringBuffer formula,
	    ArrayList<StringBuffer> assumptions,
	    ArrayList<ArrayList<StringBuffer>> functions,
	    ArrayList<ArrayList<StringBuffer>> predicates,
	    ArrayList<StringBuffer> types, SortHirarchy sortHirarchy);

    protected final StringBuffer translate(Semisequent ss, SmtTranslator.TERMPOSITION skolemization,
	    Services services) throws IllegalFormulaException {
	return translate(ss, skolemization, false, services);
    }

    /**
     * Translates the given Semisequent into "Simplify" input syntax and
     * adds the resulting string to the StringBuffer sb.
     * 
     * @param semi
     *                the SemiSequent which should be written in Simplify
     *                syntax
     */
    private final StringBuffer translate(Semisequent semi, SmtTranslator.TERMPOSITION skolemization,
	    boolean lightWeight, Services services)
	    throws IllegalFormulaException {
	StringBuffer hb = new StringBuffer();

	// if the sequent is empty, return true/false as formula
	if (semi.size() == 0) {
	    if (skolemization == SmtTranslator.TERMPOSITION.ANTECEDENT) {
		hb.append(translateLogicalTrue());
	    } else {
		hb.append(translateLogicalFalse());
	    }
	    return hb;
	}

	// translate the first semisequence
	hb = translate(semi.get(0), lightWeight, services);

	// translate the other semisequences, juncted with AND or OR
	for (int i = 1; i < semi.size(); ++i) {
	    if (skolemization == SmtTranslator.TERMPOSITION.ANTECEDENT) {
		hb = translateLogicalAnd(hb, translate(semi.get(i),
			lightWeight, services));
	    } else {
		hb = translateLogicalOr(hb, translate(semi.get(i), lightWeight,
			services));
	    }
	}

	return hb;
    }
    
    /**
     * Translates the given ConstrainedFormula into "Simplify" input syntax
     * and adds the resulting string to the StringBuffer sb.
     * 
     * @param cf
     *                the ConstrainedFormula which should be written in
     *                Simplify syntax
     * 
     * TODO overwork. makes sense?
     */
    private final StringBuffer translate(ConstrainedFormula cf,
	    boolean lightWeight, Services services)
	    throws IllegalFormulaException {
	StringBuffer hb = new StringBuffer();
	Term t;
	// if the cnstraintSet contains cf, make a syntactical
	// replacement
	/*if (constraintSet.used(cf)) {
	    SyntacticalReplaceVisitor srVisitor = new SyntacticalReplaceVisitor(
		    constraintSet.chosenConstraint());
	    cf.formula().execPostOrder(srVisitor);
	    t = srVisitor.getTerm();
	} else {
	    t = cf.formula();
	}*/
	t = cf.formula();
	Operator op = t.op();
	if (!lightWeight || !(op instanceof Modality)
		&& !(op instanceof IUpdateOperator)
		&& !(op instanceof IfThenElse) && op != Op.ALL && op != Op.EX) {
	    hb.append(translateTerm(t, new Vector<QuantifiableVariable>(), services));
	}
	return hb;
    }
    
    /**
     * Returns, wheather the Structer, this translator creates should be a
     * Structur, that is multi sorted with inheritance of Sorts. If false, a
     * single sorted structure is created.
     * 
     * @return true, if multi sorted logic is supported.
     */
    protected abstract boolean isMultiSorted();

    /**
     * The String used for integer values. This sort is also used in single
     * sorted logics.
     * 
     * @return The String used for integers.
     */
    protected abstract StringBuffer getIntegerSort();

    /**
     * Build the Stringbuffer for a logical NOT.
     * 
     * @param arg
     *                The Formula to be negated.
     * @return The StringBuffer representing the resulting Formular
     */
    protected abstract StringBuffer translateLogicalNot(StringBuffer arg);

    /**
     * Build the logical konjunction.
     * 
     * @param arg1
     *                The first formula.
     * @param arg2
     *                The second formula.
     * @return The StringBuffer representing the resulting formular.
     */
    protected abstract StringBuffer translateLogicalAnd(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Build the logical disjunction.
     * 
     * @param arg1
     *                The first formula.
     * @param arg2
     *                The second formula.
     * @return The StringBuffer representing the resulting formular.
     */
    protected abstract StringBuffer translateLogicalOr(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Build the logical implication.
     * 
     * @param arg1
     *                The first formula.
     * @param arg2
     *                The second formula.
     * @return The StringBuffer representing the resulting formular
     */
    protected abstract StringBuffer translateLogicalImply(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Build the logical equivalence.
     * 
     * @param arg1
     *                The first formula.
     * @param arg2
     *                The second formula.
     * @return The StringBuffer representing the resulting formular
     */
    protected abstract StringBuffer translateLogicalEquivalence(
	    StringBuffer arg1, StringBuffer arg2);

    /**
     * Build the logical forall formula.
     * 
     * @param var
     *                the bounded variable.
     * @param type
     *                the type of the bounded variable.
     * @param form
     *                The formula containing the bounded variable.
     * @return The resulting formula.
     */
    protected abstract StringBuffer translateLogicalAll(StringBuffer var,
	    StringBuffer type, StringBuffer form);

    /**
     * Build the logical exists formula.
     * 
     * @param var
     *                the bounded variable.
     * @param type
     *                the type of the bounded variable.
     * @param form
     *                The formula containing the bounded variable.
     * @return The resulting formula.
     */
    protected abstract StringBuffer translateLogicalExist(StringBuffer var,
	    StringBuffer type, StringBuffer form);

    /**
     * Translate the logical true.
     * 
     * @return The StringBuffer the logical true value.
     */
    protected abstract StringBuffer translateLogicalTrue();

    /**
     * Translate the logical false.
     * 
     * @return The StringBuffer the logical false value.
     */
    protected abstract StringBuffer translateLogicalFalse();

    /**
     * Build the Stringbuffer for an object equivalence.
     * 
     * @param arg1
     *                The first formula of the equivalence.
     * @param arg2
     *                The second formula of the equivalence.
     * @return The StringBuffer representing teh resulting Formular
     */
    protected abstract StringBuffer translateObjectEqual(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Build the Stringbuffer for an variable.
     * 
     * @return The StringBuffer representing the resulting Formular
     */
    protected abstract StringBuffer translateLogicalVar(StringBuffer name);

    /**
     * Build the Stringbuffer for an constant.
     * 
     * @return The StringBuffer representing the resulting Formular
     */
    protected abstract StringBuffer translateLogicalConstant(StringBuffer name);

    /**
     * Translate a predicate.
     * 
     * @param name
     *                The Symbol of the predicate.
     * @param args
     *                The arguments of the predicate.
     * @return the formula representing the predicate.
     */
    protected abstract StringBuffer translatePredicate(StringBuffer name,
	    ArrayList<StringBuffer> args);

    /**
     * Get the name for a predicate symbol.
     * 
     * @param name
     *                The name that can be used to create the symbol.
     * @return The unique predicate symbol.
     */
    protected abstract StringBuffer translatePredicateName(StringBuffer name);

    /**
     * Translate a function.
     * 
     * @param name
     *                The Symbol of the function.
     * @param args
     *                The arguments of the function.
     * @return the formula representing the function.
     */
    protected abstract StringBuffer translateFunction(StringBuffer name,
	    ArrayList<StringBuffer> args);

    /**
     * Get the name for a function symbol.
     * 
     * @param name
     *                The name that can be used to create the symbol.
     * @return The unique function symbol.
     */
    protected abstract StringBuffer translateFunctionName(StringBuffer name);

    /**
     * Translate the integer plus.
     * 
     * @param arg1
     *                first val of the sum.
     * @param arg2
     *                second val of the sum.
     * @return The formula representing the integer plus.
     */
    protected abstract StringBuffer translateIntegerPlus(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the integer minus.
     * 
     * @param arg1
     *                The first val of the substraction.
     * @param arg2
     *                second val of the substraction.
     * @return The formula representing the integer substraction.
     */
    protected abstract StringBuffer translateIntegerMinus(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate a unary minus.
     * 
     * @param arg
     *                the argument of the unary minus.
     * @return the formula representing tha unary minus function.
     */
    protected abstract StringBuffer translateIntegerUnaryMinus(StringBuffer arg);

    /**
     * Translate the integer multiplication.
     * 
     * @param arg1
     *                The first val of the multiplication.
     * @param arg2
     *                second val of the multiplication.
     * @return The formula representing the integer multiplication.
     */
    protected abstract StringBuffer translateIntegerMult(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the integer division.
     * 
     * @param arg1
     *                The first val of the division.
     * @param arg2
     *                second val of the division.
     * @return The formula representing the integer division.
     */
    protected abstract StringBuffer translateIntegerDiv(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the integer modulo.
     * 
     * @param arg1
     *                The first val of the modulo.
     * @param arg2
     *                second val of the modulo.
     * @return The formula representing the integer modulo.
     */
    protected abstract StringBuffer translateIntegerMod(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the greater than.
     * 
     * @param arg1
     *                The first val of the greater than.
     * @param arg2
     *                second val of the greater than.
     * @return The formula representing the greater than.
     */
    protected abstract StringBuffer translateIntegerGt(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the less than.
     * 
     * @param arg1
     *                The first val of the less than.
     * @param arg2
     *                second val of the less than.
     * @return The formula representing the less than.
     */
    protected abstract StringBuffer translateIntegerLt(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the greater or equal.
     * 
     * @param arg1
     *                The first val of the greater or equal.
     * @param arg2
     *                second val of the greater or equal.
     * @return The formula representing the greater or equal.
     */
    protected abstract StringBuffer translateIntegerGeq(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the less or equal.
     * 
     * @param arg1
     *                The first val of the less or equal.
     * @param arg2
     *                second val of the less or equal.
     * @return The formula representing the less or equal.
     */
    protected abstract StringBuffer translateIntegerLeq(StringBuffer arg1,
	    StringBuffer arg2);

    /**
     * Translate the NULL element
     * 
     * @return the Stringbuffer used for nullelement
     */
    protected abstract StringBuffer translateNull();

    /**
     * Translate the NULL element's Sort.
     * 
     * @return the StringBuffer used for Null.
     */
    protected abstract StringBuffer translateNullSort();

    /**
     * Translate a sort.
     * 
     * @param name
     *                the sorts name
     * @param isIntVal
     *                true, if the sort should represent some kind of
     *                integer
     * 
     * @return The String used for this sort. If Multisorted in
     *         Declarations, esle for the typepredicates.
     */
    protected abstract StringBuffer translateSort(String name, boolean isIntVal);

    /**
     * Translate a sort. Return the StringBuffer, that should be displayed
     * at definitionpart. i.e. the name used for typepredicates an similair.
     * 
     * @return the sorts name
     */
    protected abstract StringBuffer translateIntegerValue(long val);

    /**
     * Translates the given term into input syntax and adds the resulting
     * string to the StringBuffer sb.
     * 
     * @param term
     *                the Term which should be written in Simplify syntax
     * @param quantifiedVars
     *                a Vector containing all variables that have been bound
     *                in super-terms. It is only used for the translation of
     *                modulo terms, but must be looped through until we get
     *                there.
     */
    private final StringBuffer translateTerm(Term term, Vector<QuantifiableVariable> quantifiedVars,
	    Services services) throws IllegalFormulaException {
	Operator op = term.op();
	if (op == Op.NOT) {
	    StringBuffer arg = translateTerm(term.sub(0), quantifiedVars, services);
	    return this.translateLogicalNot(arg);
	} else if (op == Op.AND) {
	    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars, services);
	    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars, services);
	    return this.translateLogicalAnd(arg1, arg2);
	} else if (op == Op.OR) {
	    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars, services);
	    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars, services);
	    return this.translateLogicalOr(arg1, arg2);
	} else if (op == Op.IMP) {
	    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars, services);
	    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars, services);
	    return this.translateLogicalImply(arg1, arg2);
	} else if (op == Op.EQV) {
	    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars, services);
	    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars, services);
	    return this.translateLogicalEquivalence(arg1, arg2);
	} else if (op == Op.EQUALS) {
	    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars, services);
	    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars, services);
	    return this.translateObjectEqual(arg1, arg2);

	} else if (op == Op.ALL) {
	    ArrayOfQuantifiableVariable vars = term.varsBoundHere(0);
	    Debug.assertTrue(vars.size() == 1);

	    quantifiedVars.add(vars.getQuantifiableVariable(0));

	    StringBuffer qv = this.translateVariable(vars
		    .getQuantifiableVariable(0));
	    StringBuffer sort = this.translateSort(vars
		    .getQuantifiableVariable(0).sort());
	    StringBuffer form = this.translateTerm(term.sub(0), quantifiedVars,
		    services);

	    if (!this.isMultiSorted()) {
		// add the typepredicate
		form = this.translateLogicalImply(this.getTypePredicate(vars
			.getQuantifiableVariable(0).sort(), qv), form);
	    }

	    quantifiedVars.remove(vars.getQuantifiableVariable(0));

	    return this.translateLogicalAll(qv, sort, form);

	} else if (op == Op.EX) {
	    ArrayOfQuantifiableVariable vars = term.varsBoundHere(0);
	    Debug.assertTrue(vars.size() == 1);

	    quantifiedVars.add(vars.getQuantifiableVariable(0));

	    StringBuffer qv = this.translateVariable(vars
		    .getQuantifiableVariable(0));
	    StringBuffer sort = this.translateSort(vars
		    .getQuantifiableVariable(0).sort());
	    StringBuffer form = this.translateTerm(term.sub(0), quantifiedVars,
		    services);

	    if (!this.isMultiSorted()) {
		// add the typepredicate
		//a and is needed!!
		form = this.translateLogicalAnd(this.getTypePredicate(vars
			.getQuantifiableVariable(0).sort(), qv), form);
	    }
	    quantifiedVars.remove(vars.getQuantifiableVariable(0));

	    return this.translateLogicalExist(qv, sort, form);
	} else if (op == Op.TRUE) {
	    return this.translateLogicalTrue();
	} else if (op == Op.FALSE) {
	    return this.translateLogicalFalse();
	} else if (op == Op.NULL) {
	    this.nullString = this.translateNull();
	    this.nullUsed = true;
	    return this.nullString;
	} else if (op instanceof LogicVariable || op instanceof ProgramVariable) {
	    // translate as variable or constant
	    if (quantifiedVars.contains(op)) {
		// This variable is used in the scope of a
		// quantifier
		// so translate it as a variable
		return (translateVariable(op));
	    } else {
		// this Variable is a free Variable.
		// translate it as a constant.
		ArrayList<StringBuffer> subterms = new ArrayList<StringBuffer>();
		for (int i = 0; i < op.arity(); i++) {
		    subterms.add(translateTerm(term.sub(i), quantifiedVars,
			    services));
		}

		addFunction(op, new ArrayList<Sort>(), term.sort());

		return translateFunc(op, subterms);
	    }
	} else if (op instanceof Function) {
	    Function fun = (Function) op;
	    if (fun.sort() == Sort.FORMULA) {
		// This Function is a predicate, so translate it
		// as such
		if (fun == services.getTypeConverter().getIntegerLDT()
			.getLessThan()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
		    return this.translateIntegerLt(arg1, arg2);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getGreaterThan()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
		    return this.translateIntegerGt(arg1, arg2);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getLessOrEquals()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
		    return this.translateIntegerLeq(arg1, arg2);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getGreaterOrEquals()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
		    return this.translateIntegerGeq(arg1, arg2);
		} else {

		    ArrayList<StringBuffer> subterms = new ArrayList<StringBuffer>();
		    for (int i = 0; i < op.arity(); i++) {
			subterms.add(translateTerm(term.sub(i), quantifiedVars,
				services));
		    }
		    ArrayList<Sort> sorts = new ArrayList<Sort>();
		    for (int i = 0; i < fun.arity(); i++) {
			sorts.add(fun.argSort(i));
		    }
		    this.addPredicate(fun, sorts);

		    return translatePred(op, subterms);
		}
	    } else {
		// this Function is a function, so translate it
		// as such
		if (fun == services.getTypeConverter().getIntegerLDT().getAdd()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
		    //add the function to the used ones
		    this.addSpecialFunction(fun);
		    //return the final translation
		    return this.translateIntegerPlus(arg1, arg2);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getSub()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
//		    //add the function to the used ones
		    this.addSpecialFunction(fun);
//		    //return the final translation
		    return this.translateIntegerMinus(arg1, arg2);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getNeg()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    return this.translateIntegerUnaryMinus(arg1);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getMul()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
//		  add the function to the used ones
		    this.addSpecialFunction(fun);
//		    //return the final translation
		    return this.translateIntegerMult(arg1, arg2);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getDiv()) {
		    StringBuffer arg1 = translateTerm(term.sub(0), quantifiedVars,
			    services);
		    StringBuffer arg2 = translateTerm(term.sub(1), quantifiedVars,
			    services);
//		  add the function to the used ones
		    this.addSpecialFunction(fun);
//		    //return the final translation
		    return this.translateIntegerDiv(arg1, arg2);
		} else if (fun == services.getTypeConverter().getIntegerLDT()
			.getNumberSymbol()) {
		    Debug.assertTrue(term.arity() == 1);
		    long num = NumberTranslation.translate(term.sub(0))
			    .longValue();
		    StringBuffer numVal = translateIntegerValue(num);
		    
		    // add the type predicate for this
		    // constant
		    if (!this.constantTypePreds.containsKey(term)) {
			this.translateSort(term.sort());
			StringBuffer typePred = this.getTypePredicate(term
				.sort(), numVal);
			this.constantTypePreds.put(term, typePred);
		    }

		    return numVal;
		} else {
		    // an uninterpreted function. just
		    // translate it as such
		    ArrayList<StringBuffer> subterms = new ArrayList<StringBuffer>();
		    for (int i = 0; i < fun.arity(); i++) {
			subterms.add(translateTerm(term.sub(i), quantifiedVars,
				services));
		    }
		    ArrayList<Sort> sorts = new ArrayList<Sort>();
		    for (int i = 0; i < fun.arity(); i++) {
			sorts.add(fun.argSort(i));
		    }
		    this.addFunction(fun, sorts, fun.sort());

		    return translateFunc(fun, subterms);
		}
	    }

	} else if (op instanceof ArrayOp) {
	    ArrayOp operation = (ArrayOp) op;
	    StringBuffer refPrefix = this.translateTerm(operation
		    .referencePrefix(term), quantifiedVars, services);
	    StringBuffer loc = this.translateTerm(operation.index(term),
		    quantifiedVars, services);
	    ArrayList<StringBuffer> subterms = new ArrayList<StringBuffer>();
	    subterms.add(refPrefix);
	    subterms.add(loc);

	    ArrayList<Sort> sorts = new ArrayList<Sort>();
	    sorts.add(operation.referencePrefix(term).sort());
	    sorts.add(operation.index(term).sort());

	    this.addFunction(operation, sorts, operation.sort());

	    return translateFunc(operation, subterms);
	} else if (op instanceof AttributeOp) {
	    AttributeOp atop = (AttributeOp) op;
	    ArrayList<StringBuffer> subterms = new ArrayList<StringBuffer>();
	    for (int i = 0; i < atop.arity(); i++) {
		subterms.add(translateTerm(term.sub(i), quantifiedVars, services));
	    }
	    ArrayList<Sort> sorts = new ArrayList<Sort>();
	    for (int i = 0; i < op.arity(); i++) {
		sorts.add(term.sub(i).sort());
	    }
	    this.addFunction(atop, sorts, atop.sort());

	    return translateFunc(atop, subterms);
	} else {
	    return translateUnknown(term);
	}
    }

    /**
     * Add an interpreted function to the set of special functions.
     * Caution: If added here, make sure to handle the function in 
     * getSpecialSortPredicates()
     * @param fun the interpreted function to be added.
     */
    private void addSpecialFunction(Function fun) {
	if (!specialFunctions.contains(fun)) {
		specialFunctions.add(fun);
	    }
    }
    
    /**
     * Get the type predicate for the given sort and the given expression.
     * @param s The sort, the type predicate is wanted for.
     * @param arg The expression, whose type should be defined.
     * @return The well formed type predicate expression.
     */
    private StringBuffer getTypePredicate(Sort s, StringBuffer arg) {
	ArrayList<StringBuffer> arguments = new ArrayList<StringBuffer>();
	arguments.add(arg);
	StringBuffer toReturn = this.translatePredicate(typePredicates.get(s),
		arguments);

	return toReturn;
    }

    /**
     * Takes care of sequent tree parts that were not matched in
     * translate(term, skolemization). In this class it just produces a
     * warning, nothing more. It is provided as a hook for subclasses.
     * 
     * @param term
     *                The Term given to translate
     * @throws IllegalFormulaException
     */
    protected final StringBuffer translateUnknown(Term term)
	    throws IllegalFormulaException {
	throw new IllegalFormulaException(
		"Formular contains unsupported arguments");
    }

    protected final StringBuffer translateVariable(Operator op) {
	if (usedVariableNames.containsKey(op)) {
	    return usedVariableNames.get(op);
	} else {
	    StringBuffer var = this.translateLogicalVar(new StringBuffer(op
		    .name().toString()));
	    usedVariableNames.put(op, var);
	    return var;
	}
    }

    /**
     * translate a function.
     * 
     * @param o
     *                the Operator used for this function.
     * @param sub
     *                The StringBuffers representing the arguments of this
     *                Function.
     * @return a StringBuffer representing the complete Function.
     */
    protected final StringBuffer translateFunc(Operator o,
	    ArrayList<StringBuffer> sub) {
	StringBuffer name;
	if (usedFunctionNames.containsKey(o)) {
	    name = usedFunctionNames.get(o);
	} else {
	    name = translateFunctionName(new StringBuffer(o.name().toString()));
	    usedFunctionNames.put(o, name);
	}
	return translateFunction(name, sub);
    }

    /**
     * 
     * @param op the operator used for this function.
     * @param sorts the list of sort parameter of this function
     * @result the function's result sort
     */
    private void addFunction(Operator op, ArrayList<Sort> sorts, Sort result) {
	if (!this.functionDecls.containsKey(op)) {
	    sorts.add(result);
	    this.functionDecls.put(op, sorts);
	    //                   add all sorts
	    for (Sort s : sorts) {
		this.translateSort(s);
	    }
	}
    }

    private void addPredicate(Operator op, ArrayList<Sort> sorts) {
	if (!this.predicateDecls.containsKey(op)) {
	    this.predicateDecls.put(op, sorts);
	    //add all sorts
	    for (Sort s : sorts) {
		this.translateSort(s);
	    }
	}
    }

    /**
     * Translate a predicate.
     * @param o the operator used for this predicate.
     * @param sub The terms representing the arguments.
     * @return a StringBuffer representing the complete predicate.
     */
    protected final StringBuffer translatePred(Operator o,
	    ArrayList<StringBuffer> sub) {
	StringBuffer name;
	if (usedPredicateNames.containsKey(o)) {
	    name = usedPredicateNames.get(o);
	} else {
	    name = translatePredicateName(new StringBuffer(o.name().toString()));
	    usedPredicateNames.put(o, name);
	}
	return translatePredicate(name, sub);
    }

    protected final StringBuffer translateSort(Sort s) {
	if (usedDisplaySort.containsKey(s)) {
	    return usedDisplaySort.get(s);
	} else {
	    StringBuffer sortname = this.translateSort(s.name().toString(),
		    isSomeIntegerSort(s));
	    StringBuffer displaysort;
	    if (this.isMultiSorted()) {
		displaysort = sortname;
	    } else {
		displaysort = this.getIntegerSort();
	    }
	    StringBuffer realsort = sortname;

	    usedDisplaySort.put(s, displaysort);
	    usedRealSort.put(s, realsort);
	    if (!this.isMultiSorted()) {
		addTypePredicate(realsort, s);
	    }

	    return displaysort;
	}
    }

    /**
     * Create a type predicate for a given sort.
     * @param sortname the name, that should be used for the sort.
     * @param s the sort, this predicate belongs to.
     */
    private void addTypePredicate(StringBuffer sortname, Sort s) {
	if (!this.typePredicates.containsKey(s)) {
	    StringBuffer predName = new StringBuffer("type_of_");
	    predName.append(sortname);
	    predName = this.translatePredicateName(predName);
	    this.typePredicates.put(s, predName);
	}
    }


    /** 
     * Used just to be called from DecProcTranslation
     * @see de.uka.ilkd.key.proof.decproc.DecProcTranslation#translate(Semisequent, int)
     */
    private final StringBuffer translate(Term term, int skolemization,
	    Vector<QuantifiableVariable> quantifiedVars, Services services)
	    throws IllegalFormulaException {
	return translateTerm(term, quantifiedVars, services);
    }

    private boolean isSomeIntegerSort(Sort s) {
	if (s == jbyteSort || s == jshortSort || s == jintSort
		|| s == jlongSort || s == jcharSort || s == integerSort)
	    return true;
	return false;
    }

}
