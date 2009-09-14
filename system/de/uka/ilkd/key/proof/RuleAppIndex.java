// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2009 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//


package de.uka.ilkd.key.proof;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uka.ilkd.key.collection.ImmutableList;
import de.uka.ilkd.key.collection.ImmutableSLList;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.Constraint;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.SequentChangeInfo;
import de.uka.ilkd.key.rule.NoPosTacletApp;
import de.uka.ilkd.key.rule.RuleApp;
import de.uka.ilkd.key.rule.TacletApp;

/**
 * manages the possible application of rules (RuleApps) 
 */
public class RuleAppIndex  {

    private Goal                goal;

    private TacletIndex         tacletIndex;

    /**
     * Two <code>TacletAppIndex</code> objects, one of which only contains rules
     * that have to be applied interactively, and the other one for rules that
     * can also be applied automatic. This is used as an optimization, as only
     * the latter index has to be kept up to date while applying rules automated
     */
    private TacletAppIndex      interactiveTacletAppIndex;
    private TacletAppIndex      automatedTacletAppIndex;

    private BuiltInRuleAppIndex builtInRuleAppIndex;

    private List<NewRuleListener>                listenerList =
        Collections.synchronizedList ( new ArrayList<NewRuleListener> ( 10 ) );

    /**
     * The current mode of the index: For <code>autoMode==true</code>, the index
     * <code>interactiveTacletAppIndex</code> is not updated
     */
    private boolean             autoMode;
               

    public RuleAppIndex(TacletAppIndex      p_tacletAppIndex, 
			BuiltInRuleAppIndex p_builtInRuleAppIndex) {
	this ( p_tacletAppIndex.tacletIndex(), p_builtInRuleAppIndex );
    }

    public RuleAppIndex ( TacletIndex         p_tacletIndex,
			  BuiltInRuleAppIndex p_builtInRuleAppIndex ) {
	tacletIndex               = p_tacletIndex;
	automatedTacletAppIndex   = new TacletAppIndex ( tacletIndex );
	interactiveTacletAppIndex = new TacletAppIndex ( tacletIndex );
	builtInRuleAppIndex       = p_builtInRuleAppIndex;
	// default to false to keep compatibility with old code
	autoMode                  = false;

	automatedTacletAppIndex.setRuleFilter 
	    ( AnyRuleSetTacletFilter.INSTANCE );
	interactiveTacletAppIndex.setRuleFilter 
	    ( new NotRuleFilter ( AnyRuleSetTacletFilter.INSTANCE ) );
	
        setNewRuleListeners();
    }

    private RuleAppIndex(TacletIndex         tacletIndex,
			 TacletAppIndex      interactiveTacletAppIndex, 
			 TacletAppIndex      automatedTacletAppIndex,
			 BuiltInRuleAppIndex builtInRuleAppIndex,
			 boolean             autoMode) {
	this.tacletIndex               = tacletIndex;
	this.interactiveTacletAppIndex = interactiveTacletAppIndex;
	this.automatedTacletAppIndex   = automatedTacletAppIndex;
	this.builtInRuleAppIndex       = builtInRuleAppIndex;
	this.autoMode                  = autoMode;
	
        setNewRuleListeners();
    }

    private void setNewRuleListeners() {
	NewRuleListener newRuleListener = new NewRuleListener () {
            public void ruleAdded( RuleApp         taclet,
        			   PosInOccurrence pos ) {
        	informNewRuleListener(taclet, pos);			   	
            }
        };
        interactiveTacletAppIndex.setNewRuleListener ( newRuleListener );
        automatedTacletAppIndex  .setNewRuleListener ( newRuleListener );
	builtInRuleAppIndex      .setNewRuleListener ( newRuleListener );
    }

    public void setup ( Goal p_goal ) {
        goal = p_goal;
	interactiveTacletAppIndex.setup ( p_goal );
	automatedTacletAppIndex  .setup ( p_goal );
    }

    /**
     * Currently the rule app index can either operate in interactive mode (and
     * contain applications of all existing taclets) or in automatic mode (and
     * only contain a restricted set of taclets that can possibly be applied
     * automated). This distinction could be replaced with a more general way to
     * control the contents of the rule app index
     */
    public void autoModeStarted () {
	autoMode = true;
    }

    public void autoModeStopped () {
	autoMode = false;
    }

    /**
     * returns the Taclet index for this ruleAppIndex. 
     */
    public TacletIndex tacletIndex() {
	return tacletIndex;
    }

    /**
     * returns the built-in rule application index for this
     * ruleAppIndex. 
     */
    public BuiltInRuleAppIndex builtInRuleAppIndex() {
	return builtInRuleAppIndex;
    }


    /**
     * adds a change listener to the index
     * @param l the AppIndexListener to add
     */
    public void addNewRuleListener(NewRuleListener l) {
	listenerList.add(l);
    }
   
    /**
     * removes a change listener to the index
     * @param l the AppIndexListener to remove
     */
    public void removeNewRuleListener(NewRuleListener l) {
	listenerList.remove(l);
    }

    /**returns the set of rule applications for
     * the given heuristics 
     * at the given position of the given sequent.
     * @param filter the TacletFiler filtering the taclets of interest
     * @param pos the PosInOccurrence to focus
     * @param services the Services object encapsulating information
     * about the java datastructures like (static)types etc.
     * @param userConstraint the Constraint with user defined instantiations
     * of meta variables
     */
    public ImmutableList<TacletApp> getTacletAppAt(TacletFilter    filter,
					  PosInOccurrence pos,
					  Services        services,
					  Constraint      userConstraint) { 
	ImmutableList<TacletApp> result = ImmutableSLList.<TacletApp>nil();
	if ( !autoMode ) {
	    result = result.prepend 
		( interactiveTacletAppIndex.getTacletAppAt
		  (pos,
		   filter,
		   services,
		   userConstraint) ); 
	}
	result = result.prepend 
	    ( automatedTacletAppIndex.getTacletAppAt
	      (pos,
	       filter,
	       services,
	       userConstraint) ); 
	return result;
    }
    

    /**
     * returns the rule applications at the given PosInOccurrence and at all
     * Positions below this. The method calls getTacletAppAt for all the
     * Positions below.
     * @param filter the TacletFiler filtering the taclets of interest
     * @param pos the position where to start from
     * @param services the Services object encapsulating information
     * about the java datastructures like (static)types etc.
     * @param userConstraint the Constraint with user defined instantiations
     * of meta variables
     * @return the possible rule applications 
     */
    public ImmutableList<TacletApp> getTacletAppAtAndBelow(TacletFilter    filter,
						  PosInOccurrence pos,
						  Services        services,
						  Constraint      userConstraint) {
	ImmutableList<TacletApp> result = ImmutableSLList.<TacletApp>nil();
	if ( !autoMode ) {
	    result = result.prepend 
		( interactiveTacletAppIndex.getTacletAppAtAndBelow
		  (pos,
		   filter,
		   services,
		   userConstraint) ); 
	}
	result = result.prepend 
	    ( automatedTacletAppIndex.getTacletAppAtAndBelow
	      (pos,
	       filter,
	       services,
	       userConstraint) ); 
	return result;
    }


    /** 
     * collects all FindTacletInstantiations for the given
     * heuristics and position
     * @param filter the TacletFiler filtering the taclets of interest
     * @param pos the PosInOccurrence to focus
     * @param services the Services object encapsulating information
     * about the java datastructures like (static)types etc.
     * @param userConstraint the Constraint with user defined instantiations
     * of meta variables
     * @return list of all possible instantiations
     */
    public ImmutableList<NoPosTacletApp> getFindTaclet(TacletFilter    filter,
					      PosInOccurrence pos,
					      Services        services,
					      Constraint      userConstraint) { 
	ImmutableList<NoPosTacletApp> result = ImmutableSLList.<NoPosTacletApp>nil();
	if ( !autoMode ) {
	    result = result.prepend 
		( interactiveTacletAppIndex.getFindTaclet
		  (pos,
		   filter,
		   services,
		   userConstraint) ); 
	}
	result = result.prepend 
	    ( automatedTacletAppIndex.getFindTaclet
	      (pos,
	       filter,
	       services,
	       userConstraint) ); 
	return result;
    }

    /** 
     * collects all NoFindTacletInstantiations for the given
     * heuristics 
     * @param filter the TacletFiler filtering the taclets of interest
     * @param services the Services object encapsulating information
     * about the java datastructures like (static)types etc.
     * @param userConstraint the Constraint with user defined instantiations
     * of meta variables
     * @return list of all possible instantiations
     */
    public ImmutableList<NoPosTacletApp> getNoFindTaclet(TacletFilter    filter,
						Services        services,
						Constraint      userConstraint) { 
	ImmutableList<NoPosTacletApp> result = ImmutableSLList.<NoPosTacletApp>nil();
	if ( !autoMode ) {
	    result = result.prepend 
		( interactiveTacletAppIndex.getNoFindTaclet
		  (filter,
		   services,
		   userConstraint) ); 
	}
	result = result.prepend 
	    ( automatedTacletAppIndex.getNoFindTaclet
	      (filter,
	       services,
	       userConstraint) ); 
	return result;
    }


    /** 
     * collects all RewriteTacletInstantiations for the given
     * heuristics in a subterm of the constraintformula described by a
     * PosInOccurrence
     * @param filter the TacletFiler filtering the taclets of interest
     * @param pos the PosInOccurrence to focus
     * @param services the Services object encapsulating information
     * about the java datastructures like (static)types etc.
     * @param userConstraint the Constraint with user defined instantiations
     * of meta variables
     * @return list of all possible instantiations
     */
    public ImmutableList<NoPosTacletApp> getRewriteTaclet (TacletFilter    filter,
						  PosInOccurrence pos,
						  Services        services,
						  Constraint      userConstraint) { 
	ImmutableList<NoPosTacletApp> result = ImmutableSLList.<NoPosTacletApp>nil();
	if ( !autoMode ) {
	    result = result.prepend 
		( interactiveTacletAppIndex.getRewriteTaclet
		  (pos,
		   filter,
		   services,
		   userConstraint) ); 
	}
	result = result.prepend 
	    ( automatedTacletAppIndex.getRewriteTaclet
	      (pos,
	       filter,
	       services,
	       userConstraint) ); 
	return result;
    }


    /** 
     * returns a list of built-in rule applications applicable
     * for the given goal, user defined constraint and position
     */
    public ImmutableList<RuleApp> getBuiltInRule(Goal            goal, 
					PosInOccurrence pos,
					Constraint      userConstraint) {
	 	 	
	 return builtInRuleAppIndex().getBuiltInRule(goal, pos, 
					 	     userConstraint);
     }

    /**
     * adds a new Taclet with instantiation information to the Taclet Index 
     * of this TacletAppIndex.
     * @param tacletApp the NoPosTacletApp describing a partial instantiated Taclet to add
     */
    public void addNoPosTacletApp(NoPosTacletApp tacletApp) {
        tacletIndex.add ( tacletApp );

        if ( autoMode )
            interactiveTacletAppIndex.clearIndexes ();
    
        interactiveTacletAppIndex.addedNoPosTacletApp ( tacletApp );
        automatedTacletAppIndex.addedNoPosTacletApp ( tacletApp );
    }

    /**
     * remove a Taclet with instantiation information from the Taclet
     * Index of this TacletAppIndex.
     * @param tacletApp the NoPosTacletApp to remove
     */
    public void removeNoPosTacletApp(NoPosTacletApp tacletApp) {
        tacletIndex.remove ( tacletApp );

        if ( autoMode )
            interactiveTacletAppIndex.clearIndexes ();
            
        interactiveTacletAppIndex.removedNoPosTacletApp ( tacletApp );
        automatedTacletAppIndex.removedNoPosTacletApp ( tacletApp );
    }

    /** 
     * called if a formula has been replaced
     * @param goal the Goal which sequent has been changed
     * @param sci SequentChangeInfo describing the change of the sequent 
     */  
    public void sequentChanged ( Goal goal, SequentChangeInfo sci ) {
	if ( !autoMode )
            // the TacletAppIndex is able to detect modification of the
            // sequent itself, it is not necessary to clear the index
	    interactiveTacletAppIndex.sequentChanged ( goal, sci );
	automatedTacletAppIndex.sequentChanged ( goal, sci );
	builtInRuleAppIndex().sequentChanged( goal, sci );
    }

    /**
     * Empties all caches
     */
    public void clearAndDetachCache () {
        // Currently this only applies to the taclet index
        interactiveTacletAppIndex.clearAndDetachCache ();
        automatedTacletAppIndex  .clearAndDetachCache ();
    }

    /**
     * Empties all caches
     */
    public void clearIndexes () {
        // Currently this only applies to the taclet index
        interactiveTacletAppIndex.clearIndexes ();
        automatedTacletAppIndex  .clearIndexes ();
    }

    /**
     * Ensures that all caches are fully up-to-date
     */
    public void fillCache () {
	if ( !autoMode )
	    interactiveTacletAppIndex.fillCache ();
	automatedTacletAppIndex.fillCache ();
    }

    /**
     * Report all rule applications that are supposed to be applied
     * automatically, and that are currently stored by the index
     * 
     * @param l the NewRuleListener 
     * @param services the Services
     * @param userConstraint the Constraint capturing user defined instantiations
     * of meta variables
     */
    public void reportAutomatedRuleApps (NewRuleListener l,
                                         Services services,
                                         Constraint userConstraint) {
        automatedTacletAppIndex.reportRuleApps ( l, services, userConstraint );
        builtInRuleAppIndex().reportRuleApps ( l, goal, userConstraint );
    }
    
    /**
     * Report builtin rules to all registered NewRuleListener instances.
     * @param p_goal the Goal which to scan
     * @param p_userConstraint the Constraint capturing user defined instantiations
     * of meta variables 
     */
    public void scanBuiltInRules (Goal p_goal,
                                  Constraint p_userConstraint) {
        builtInRuleAppIndex().scanApplicableRules(p_goal, p_userConstraint);
    }

    /** 
     * informs all observers, if a formula has been added, changed or 
     * removed
     */ 
    private void informNewRuleListener(RuleApp         p_app,
                                       PosInOccurrence p_pos) {
	for (final NewRuleListener listener : listenerList) {
	    listener.ruleAdded(p_app, p_pos);
	}
    }

    
    /**
     * returns a new RuleAppIndex with a copied TacletIndex.
     * Attention: the listener lists are not copied
     */
    public RuleAppIndex copy() {
	TacletIndex copiedTacletIndex = tacletIndex.copy();
	TacletAppIndex copiedInteractiveTacletAppIndex = 
	    interactiveTacletAppIndex.copyWithTacletIndex
	    ( copiedTacletIndex );
	TacletAppIndex copiedAutomatedTacletAppIndex   =
	    automatedTacletAppIndex.copyWithTacletIndex
	    ( copiedTacletIndex );
	return new RuleAppIndex( copiedTacletIndex,
				 copiedInteractiveTacletAppIndex,
				 copiedAutomatedTacletAppIndex,
				 builtInRuleAppIndex().copy(),
				 autoMode );
    }


    public String toString() {
	return "RuleAppIndex with indexing, getting Taclets from"
	    +" TacletAppIndex "+interactiveTacletAppIndex+" and "
	    +automatedTacletAppIndex;
    }
}
