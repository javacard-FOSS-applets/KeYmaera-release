// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

/**
 * Generated by the KeY-Tool
 * created: APRIL 17, 2000
 */

package de.uka.ilkd.key.logic;

import de.uka.ilkd.key.logic.op.ArrayOfQuantifiableVariable;
import de.uka.ilkd.key.logic.op.Operator;
import de.uka.ilkd.key.logic.op.QuantifiableVariable;
import de.uka.ilkd.key.logic.op.Quantifier;

class QuantifierTerm extends Term {

    /**
     * array of quantifiable variables
     */
    private final ArrayOfQuantifiableVariable varsBoundHere;
    
    /** sub term */
    private final Term subTerm;

    /** depth of the term */
    private final int depth;

    /**
     * creates a quantifier term
     * @param op Operator representing the Quantifier (all, exist) of this term
     * @param varsBoundHere  an array of Variable containing all variables
     * bound by the quantifier
     * @param sort the Sort of this Term (is bool)
     */
    public QuantifierTerm(Operator op, 
			  QuantifiableVariable[] varsBoundHere, 
			  Term subTerm) {
	this(op, new ArrayOfQuantifiableVariable(varsBoundHere), subTerm);
    }


    /**
     * creates a quantifier term
     * @param op Operator representing the Quantifier (all, exist) of this term
     * @param varsBoundHere  an array of Variable containing all variables
     * bound by the quantifier
     * @param sort the Sort of this Term (is bool)
     */
    public QuantifierTerm(Operator op, 
			  ArrayOfQuantifiableVariable varsBoundHere, 
			  Term subTerm) {
	super(op, ((Quantifier)op).sort(subTerm));
	this.subTerm = subTerm;
	this.depth   = subTerm.depth() + 1;
	this.varsBoundHere = varsBoundHere;
	fillCaches();	
    }

    /**@return depth of the term */
    public int depth() {
	return depth;
    }

    /** @return the variables the term bound direct if it is a Quantifier(term)
     */
    public ArrayOfQuantifiableVariable varsBoundHere(int n) {
	return n==0? varsBoundHere : EMPTY_VAR_LIST;
    }

    /** @return n-th subterm (always the only one) */    
    public Term sub(int n) {
	return subTerm;
    }	

    /** @return arity of the quantifier term 1 as int */
    public int arity() {
	return 1;
    }

}
