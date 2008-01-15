// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.speclang;

import java.util.Map;

import de.uka.ilkd.key.java.statement.LoopStatement;
import de.uka.ilkd.key.java.visitor.Visitor;
import de.uka.ilkd.key.logic.SetOfLocationDescriptor;
import de.uka.ilkd.key.logic.SetOfTerm;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.ParsableVariable;


/**
 * A loop invariant, consisting of an invariant formula, a set of loop 
 * predicates, a modifier set, and a variant term.
 */
public interface LoopInvariant {
    
    /**
     * Returns the loop to which the invariant belongs.
     */
    public LoopStatement getLoop();

    /**
     * Returns the invariant formula.
     */
    public Term getInvariant(Term selfTerm);
    
    /**
     * Returns the set of loop predicates.
     */
    public SetOfTerm getPredicates(Term selfTerm);
    
    /**
     * Returns the modifier set.
     */
    public SetOfLocationDescriptor getModifies(Term selfTerm);
    
    /**
     * Returns the variant term. 
     */
    public Term getVariant(Term selfTerm);
    
    /**
     * Tells whether using heuristics for generating additional loop predicates 
     * is allowed or not.
     */
    public boolean getPredicateHeuristicsAllowed();
    
    /**
     * Returns the variable used for self.
     */
    public ParsableVariable getSelfVar();

    /**
     * Returns the map of atPre-functions.
     */
    public /*inout*/ Map /*Operator (normal) 
                           -> Function (atPre)*/ getAtPreFunctions();
    
    /**
     * Returns a new loop invariant where the loop reference has been
     * replaced with the passed one.
     */
    public LoopInvariant setLoop(LoopStatement loop); 
    
    /**
     * Returns a new loop invariant where the invariant formula has been
     * repaced with the passed one. Take care: the variables used for
     * the receiver, parameters, and local variables must stay the same!
     */
    public LoopInvariant setInvariant(Term invariant, Term selfTerm); 
    
    /**
     * Returns a new loop invariant where the loop predicates have been 
     * replaced with the passed ones.
     */
    public LoopInvariant setPredicates(SetOfTerm predicates, Term selfTerm);
    
    /**
     * Returns a new loop invariant where the flag for predicate generation
     * heuristics has been set to the passed value. Take care: the variables 
     * used for the receiver, parameters, and local variables must stay the 
     * same!
     */
    public LoopInvariant setPredicateHeuristicsAllowed(
                                        boolean predicateHeuristicsAllowed);
    
    /** 
     * Loop invariants can be visited like source elements:
     * This method calls the corresponding method of a visitor in order to
     * perform some action/transformation on this element.
     */
    public void visit(Visitor v);
}
