// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.java.expression.operator;

import de.uka.ilkd.key.java.PrettyPrinter;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.java.abstraction.KeYJavaType;
import de.uka.ilkd.key.java.expression.Operator;
import de.uka.ilkd.key.java.reference.ExecutionContext;
import de.uka.ilkd.key.java.visitor.Visitor;
import de.uka.ilkd.key.util.ExtList;

/**
 *  Logical not.
 */

public class LogicalNot extends Operator {


    /**
     *      Logical not.
     *      @param children an ExtList with all children of this node
     *      the first children in list will be the one on the left
     *      side, the second the one on the  right side.
     */

    public LogicalNot(ExtList children) {
        super(children);
    }


    /**
 *      Get arity.
 *      @return the int value.
     */

    public int getArity() {
        return 1;
    }

    /**
 *      Get precedence.
 *      @return the int value.
     */

    public int getPrecedence() {
        return 1;
    }

    /**
 *      Get notation.
 *      @return the int value.
     */

    public int getNotation() {
        return PREFIX;
    }

    /**
 *        Checks if this operator is left or right associative. Ordinary
 *        unary operators are right associative.
 *        @return <CODE>true</CODE>, if the operator is left associative,
 *        <CODE>false</CODE> otherwise.
     */

    public boolean isLeftAssociative() {
        return false;
    }

    /** calls the corresponding method of a visitor in order to
     * perform some action/transformation on this element
     * @param v the Visitor
     */
    public void visit(Visitor v) {
	v.performActionOnLogicalNot(this);
    }

    public void prettyPrint(PrettyPrinter p) throws java.io.IOException {
        p.printLogicalNot(this);
    }

    public KeYJavaType getKeYJavaType(Services services, ExecutionContext ec) {
	return services.getTypeConverter().getBooleanType();
    }

}
