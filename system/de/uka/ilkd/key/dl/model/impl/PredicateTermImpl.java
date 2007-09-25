/* Generated by Together */

package de.uka.ilkd.key.dl.model.impl;

import de.uka.ilkd.key.java.PrettyPrinter;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.java.reference.ExecutionContext;

import java.io.IOException;

import de.uka.ilkd.key.dl.model.DLProgramElement;
import de.uka.ilkd.key.dl.model.Expression;
import de.uka.ilkd.key.dl.model.FreePredicate;
import de.uka.ilkd.key.dl.model.Predicate;
import de.uka.ilkd.key.dl.model.PredicateTerm;

/**
 * Implementation of {@link PredicateTerm}
 * 
 * @author jdq
 * @since Tue Jan 16 16:10:28 CET 2007
 */
public class PredicateTermImpl extends DLNonTerminalProgramElementImpl
        implements PredicateTerm {

    /**
     * Creates a new predicate term with the given predicate and the given
     * parameters.
     * 
     * @param p
     *                the predicate to use
     * @param param
     *                the parameter of the predicate
     */
    public PredicateTermImpl(Predicate p, Expression... param) {
        addChild(p);
        for (Expression e : param) {
            addChild(e);
        }
    }

    /**
     * @see de.uka.ilkd.key.dl.model.impl.DLNonTerminalProgramElementImpl#prettyPrint(de.uka.ilkd.key.java.PrettyPrinter)
     *      prettyPrint
     */
    public void prettyPrint(PrettyPrinter arg0) throws IOException {
        if (getChildAt(0) instanceof FreePredicate) {
            arg0.printCompoundTerm(this, false);
        } else {
            arg0.printCompoundTerm(this, true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.java.ReuseableProgramElement#reuseSignature(de.uka.ilkd.key.java.Services,
     *      de.uka.ilkd.key.java.reference.ExecutionContext)
     */
    public String reuseSignature(Services services, ExecutionContext ec) {
        StringBuilder result = new StringBuilder();
        result.append(getSymbol() + "(");
        for (int i = 0; i < getChildCount(); i++) {
            result.append(((DLProgramElement) getChildAt(i)).reuseSignature(
                    services, ec)
                    + ", ");
        }
        result.append(")");
        return result.toString();
    }
}
