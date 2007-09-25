/* Generated by Together */

package de.uka.ilkd.key.dl.model.impl;

import java.io.IOException;

import de.uka.ilkd.key.dl.model.Function;
import de.uka.ilkd.key.java.NameAbstractionTable;
import de.uka.ilkd.key.java.PrettyPrinter;
import de.uka.ilkd.key.java.SourceElement;

/**
 * Implementation of {@link Function}.
 * 
 * @author jdq
 * @since Tue Jan 16 16:14:13 CET 2007
 */
public abstract class FunctionImpl extends DLTerminalProgramElementImpl
		implements Function {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uka.ilkd.key.dl.model.impl.DLProgramElementImpl#prettyPrint(de.uka.ilkd.key.java.PrettyPrinter)
	 */
	@Override
	public void prettyPrint(PrettyPrinter arg0) throws IOException {
		arg0.printFunctionSymbol(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uka.ilkd.key.dl.model.impl.DLTerminalProgramElementImpl#equalsModRenaming(de.uka.ilkd.key.java.SourceElement,
	 *      de.uka.ilkd.key.java.NameAbstractionTable)
	 */
	@Override
	public boolean equalsModRenaming(SourceElement arg0,
			NameAbstractionTable arg1) {
		return arg0 == this
				|| (getClass() == arg0.getClass() && arg1.sameAbstractName(
						this, arg0));
	}
}
