package de.uka.ilkd.key.smt.taclettranslation;

import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.rule.Taclet;

class DefaultTacletFormula implements TacletFormula {

    Taclet taclet;
    Term   formula;
    String status;
    TacletConditions conditions;
    
        
    public TacletConditions getConditions() {
        return conditions;
    }
    
    public DefaultTacletFormula(Taclet taclet, Term formula,
	    			String status){
	this(taclet,formula,status,new TacletConditions(taclet));
    }

    public DefaultTacletFormula(Taclet taclet, Term formula,
	    String status, TacletConditions conditions) {
	super();
	this.taclet = taclet;
	this.formula = formula;
	this.status = status;
	this.conditions = 
	    conditions == null ? new TacletConditions(taclet):conditions;
	    
    	}

    public Term getFormula() {
	return formula;
    }

    public Taclet getTaclet() {
	return taclet;
    }

    public String getStatus() {
	return status;
    }
    
    

}
