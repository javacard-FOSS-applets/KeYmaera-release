// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.proof.proofevent;


import de.uka.ilkd.key.proof.Node;
import de.uka.ilkd.key.rule.RuleApp;

/**
 * More specific information about a rule application (currently
 * information about added and removed formulas)
 */
public class RuleAppInfo {

    RuleAppInfo ( RuleApp               p_appliedRule,
		  Node                  p_originalNode,
		  ListOfNodeReplacement p_newNodes ) {
	app          = p_appliedRule;
	originalNode = p_originalNode;
	newNodes     = p_newNodes;
    }
    
    
    /**
     * RuleApp this event reports
     */
    RuleApp               app          = null;
    
    /**
     * Node the rule has been applied on
     */
    Node                  originalNode = null;

    /**
     * New nodes that have been introduced by this rule application
     */
    ListOfNodeReplacement newNodes     = null;

    public RuleApp                   getRuleApp          () {
	return app;
    }

    /**
     * @return Node the rule has been applied on
     */
    public Node                      getOriginalNode     () {
	return originalNode;
    }

    /**
     * @return Nodes by which the original one has been replaced (the
     * original node, if only the closure constraint of this node has
     * been changed)
     */
    public IteratorOfNodeReplacement getReplacementNodes () {
	return newNodes.iterator ();
    }
    

    public String toString () {
	return
	    "RuleApp: " + getRuleApp () +
	    "\nNode: " + getOriginalNode () +
	    "\nResulting nodes: " + newNodes;
    }
}
