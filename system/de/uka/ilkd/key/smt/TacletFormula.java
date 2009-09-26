package de.uka.ilkd.key.smt;

import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.rule.Taclet;

/**
 * Represents the formula of a taclet.<br>
 * It provides methods to get the formula and its corresponding taclet.<br>
 * The setting of the formula and the taclet is dedicated to the constructor of the implementing class.
 * This ensures that the relationship between both is not changed afterwards.
 */
public interface TacletFormula {
    
    /**
     * 
     * @return the taclet of the instance.
     */
    Taclet getTaclet();
    /**
     * 
     * @return the formula of the instance.
     */
    Term   getFormula();

}
