package de.uka.ilkd.key.logic;

import de.uka.ilkd.key.logic.op.ArrayOfQuantifiableVariable;
import de.uka.ilkd.key.logic.op.NumericalQuantifier;

class NumericalQuantifierTerm extends OpTerm.ArbitraryOpTerm {

    private ArrayOfQuantifiableVariable varsBoundHere;

    public NumericalQuantifierTerm(NumericalQuantifier op, Term[] subTerm, 
            ArrayOfQuantifiableVariable varsBoundHere) {
        super(op, subTerm);
	this.varsBoundHere = varsBoundHere;
    }

    public ArrayOfQuantifiableVariable varsBoundHere(int n) {
	return varsBoundHere;
    }
    
}
