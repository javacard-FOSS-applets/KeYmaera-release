package de.uka.ilkd.key.dl.arithmetics.impl.qepcad;

import java.math.BigDecimal;
import java.util.ArrayList;

import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.Function;
import de.uka.ilkd.key.logic.op.Junctor;
import de.uka.ilkd.key.logic.op.LogicVariable;
import de.uka.ilkd.key.logic.op.Metavariable;
import de.uka.ilkd.key.logic.op.Op;
import de.uka.ilkd.key.logic.op.Quantifier;

/**
 * Converts a term to a QepcadInput readable for
 * the Qepcad-Program. 
 * 
 * @author Timo Michelsen
 */
public class Term2QepCadConverter {

	static final String USCOREESCAPE = "uscore";
	static final String DOLLARESCAPE = "dollar";
	private QepCadInput input = new QepCadInput(); // Result
	private ArrayList<String> quantifiedVars = new ArrayList<String>(); // List of quantified Variables
	private ArrayList<String> existingVars = new ArrayList<String>(); // List of existing Variables
	
        /**
         * Standardconstructor.
         */
	public Term2QepCadConverter() {
	}
	
        /**
         * Function to start to convert a given term.
         * 
         * @param form Term to convert
         * @return QepCadInput-Instance of the given term.
         */
	public static QepCadInput convert( Term form ) {
		Term2QepCadConverter converter = new Term2QepCadConverter();
		return converter.convertImpl( form );
	}
	
        /**
         * Implementation of the convert-algorithm
         */
	private QepCadInput convertImpl( Term form ) {
		
		// Getting the string-representation
//		String formula = "(" + convert2String( form ) + ")";
		String formula = convert2String( form );
                
                // extracts additional information for qepcad
		this.input.setVariableList( "(" + array2String(getVariableList()) + ")" );
		this.input.setFreeVariableNum( this.existingVars.size() - this.quantifiedVars.size());
		
		if(!formula.startsWith("(") && !formula.startsWith("[")) {
			formula = "[ " + formula + " ]."; 
		} else {
			formula += ".";
		}
		// Convert formula-String to QepCad-Notation
                // first ( )-Pair, which is no Quantor, must be replaced
                // by [ ]-Pair and a dot.
		// FIXME: this breaks quantified formulas
//		int counter = 0;
//		for( int i = 0; i < formula.length(); i++ ) {
//			if( formula.charAt(i) == '(') {
//				counter++;
//				if( counter == this.quantifiedVars.size() + 1) {
//					formula = formula.substring(0, i) + "[" + formula.substring(i+1, formula.length() - 1) + "].";
//					break;
//				}
//			}
//		}
				
		this.input.setFormula(formula);
		return this.input;
	}
	
	private String convert2String(Term form) {
		String[] args = new String[form.arity()];
		for (int i = 0; i < args.length; i++) {
			args[i] = convert2String(form.sub(i));
		}
		if (form.op() == Op.FALSE) {
			return "FALSE";
		} else if (form.op() == Op.TRUE) {
			return "TRUE";
		} else if (form.op().name().toString().equals("equals")) {
			return "[" + args[0] + "=" + args[1] + "]";
		} else if (form.op() instanceof Function) {
			Function f = (Function) form.op();
			if (f.name().toString().equals("gt")) {
				return "[" + args[0] + ">" + args[1] + "]";
			} else if (f.name().toString().equals("geq")) {
				return "[" + args[0] + ">=" + args[1] + "]";
			} else if (f.name().toString().equals("equals")) {
				return "[" + args[0] + "=" + args[1] + "]"; // 2x EQUALS?
			} else if (f.name().toString().equals("neq")) {
				return "[" + args[0] + "/=" + args[1] + "]";
			} else if (f.name().toString().equals("leq")) {
				return "[" + args[0] + "<=" + args[1] + "]";
			} else if (f.name().toString().equals("lt")) {
				return "[" + args[0] + "<" + args[1] + "]";
			} else if (f.name().toString().equals("add")) {
				return "(" + args[0] + "+" + args[1] + ")";
			} else if (f.name().toString().equals("sub")) {
				return "(" + args[0] + "-" + args[1] + ")";
			} else if (f.name().toString().equals("neg")) {
				return "(-" + args[0] + ")";
			} else if (f.name().toString().equals("mul")) {
				return "(" + args[0] + " " + args[1] + ")"; 
			} else if (f.name().toString().equals("div")) {
				return "(" + args[0] + "/" + args[1] + ")";
			} else if (f.name().toString().equals("exp")) {
				return "(" + args[0] + "^" + args[1] + ")";
			} else {
				try {
					BigDecimal d = new BigDecimal(form.op().name().toString());
					try {
						return String.valueOf(d.intValueExact());
					} catch (ArithmeticException e) {
						return "<TODO>"; // TODO : Change this
						// return new Expr(Expr.SYM_REAL,
						// new Expr[] { new Expr(d) });
					}
				} catch (NumberFormatException e) {
					String name = form.op().name().toString();
					if(name.contains("_")) {
						name = name.replaceAll("_", USCOREESCAPE);
						
					}
					if(name.contains("$")) {
						name = name.replaceAll("\\$", DOLLARESCAPE);
					}
					addExistingVariable(name);
					if (args.length == 0) {
						return "(" + name + ")";
					}
					return "(" + name + "(" + array2String(args) + "))";
				}
			}
		} else if (form.op() instanceof LogicVariable
				|| form.op() instanceof de.uka.ilkd.key.logic.op.ProgramVariable
				|| form.op() instanceof Metavariable) {
			String name = form.op().name().toString();
			if(name.contains("_")) {
				name = name.replaceAll("_", USCOREESCAPE);
			}
			if(name.contains("$")) {
				name = name.replaceAll("\\$", DOLLARESCAPE);
			}
			addExistingVariable(name);
			return "(" + name + ")";
		} else if (form.op() instanceof Junctor) {
			if (form.op() == Junctor.AND) {
				return "[" + args[0] + "/\\" + args[1] + "]";
			} else if (form.op() == Junctor.OR) {
				return "[" + args[0] + "\\/" + args[1] + "]";
			} else if (form.op() == Junctor.IMP) {
				return "[" + args[0] + "==>" + args[1] + "]";
			} else if (form.op() == Junctor.NOT) {
				return "~[" + args[0] + "]";
			}
		} else if (form.op() instanceof Quantifier) {
			
			int varsNum = form.varsBoundHere(0).size();
			String[] vars = new String[varsNum];
			for( int i = 0; i < varsNum; i++ ) {
				String name = form.varsBoundHere(0).getQuantifiableVariable(i).name().toString();
				if(name.contains("_")) {
					name = name.replaceAll("_", USCOREESCAPE);
				}
				if(name.contains("$")) {
					name = name.replaceAll("\\$", DOLLARESCAPE);
				}
				vars[i] = name;
				addExistingVariable(vars[i]);
				addQuantifiedVariable(vars[i]);
			}
			
			if (form.op() == Quantifier.ALL) {
				return "(A " + array2String(vars) + ")" + args[0];
			} else if (form.op() == Quantifier.EX) {
				return "(E " + array2String(vars) + ")" + args[0];
			}
		}
		throw new IllegalArgumentException("Could not convert Term: " + form
				+ "Operator was: " + form.op());
	}

        // Converts an array of Strings in
        // one string. The elements are seperated by
        // ','
	private String array2String(String[] args) {
		if (args == null)
			return "";

		String result = "";
		for (int i = 0; i < args.length; i++) {
			result += args[i];
			if (i != args.length - 1)
				result += ",";
		}

		return result;
	}	
	
        // Inserts a new variable in the list of quantified variables,
        // if is not in the list
	private void addQuantifiedVariable( String varName ) {
		// try to find the variable
		for( String var : this.quantifiedVars ) {
			if( var.equals(varName))
				return;
		}
		
		// no found --> add varName to the list
		this.quantifiedVars.add(varName);
	}
	
        // Inserts a new variable in the list of existing variables,
        // if is not in the list
        private void addExistingVariable( String varName ) {
		// try to find the variable
		for( String var : this.existingVars ) {
			if( var.equals(varName))
				return;
		}
		
		// no found --> add varName to the list
		this.existingVars.add(varName);		
	}
	
        // Gets the variable-list, which is important for qepcad
	private String[] getVariableList() {
		
		ArrayList<String> notQuantifiedVars = new ArrayList<String>();
		ArrayList<String> quantifiedVars = new ArrayList<String>();
		
		for( String var : this.existingVars ) {
			
			boolean quantified = false;
			for( String quantVar : this.quantifiedVars ) {
				if( var.equals(quantVar)) {
					quantified = true;
					break;
				}
			}
			
			if( !quantified ) {
				notQuantifiedVars.add(var);
			} else {
				quantifiedVars.add(var);
			}
			
		}

		String[] result = new String[ this.existingVars.size()];
		for( int i = 0; i < notQuantifiedVars.size(); i++ ) {
			result[i] = notQuantifiedVars.get(i);
		}
		for( int i = 0; i < quantifiedVars.size(); i++ ) {
			result[notQuantifiedVars.size() + i] = quantifiedVars.get(i);
		}
		
		return result;
	}
}
