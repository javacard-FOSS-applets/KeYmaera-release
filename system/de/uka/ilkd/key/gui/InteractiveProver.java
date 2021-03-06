// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2009 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import de.uka.ilkd.key.gui.thread.ApplyThread;
import de.uka.ilkd.key.gui.thread.IThreadSender;
import de.uka.ilkd.key.gui.thread.ThreadListenerAdapter;
import de.uka.ilkd.key.collection.ImmutableList;
import de.uka.ilkd.key.collection.ImmutableSLList;
import de.uka.ilkd.key.collection.DefaultImmutableSet;
import de.uka.ilkd.key.collection.ImmutableSet;
import de.uka.ilkd.key.dl.arithmetics.MathSolverManager;
import de.uka.ilkd.key.logic.Constraint;
import de.uka.ilkd.key.logic.PIOPathIterator;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.op.IUpdateOperator;
import de.uka.ilkd.key.pp.PosInSequent;
import de.uka.ilkd.key.proof.*;
import de.uka.ilkd.key.rule.*;
import de.uka.ilkd.key.strategy.AutomatedRuleApplicationManager;
import de.uka.ilkd.key.strategy.FocussedRuleApplicationManager;
import de.uka.ilkd.key.util.Debug;

public class InteractiveProver {

    /** the proof the interactive prover works on */
    private Proof proof;

    /** the user focused goal */
    private Goal focusedGoal;

    /** true iff in interactive mode */

    private boolean interactive = true;

    /** the central strategy processor including GUI signalling */
    private ApplyStrategy applyStrategy;
    private final ProverTaskListener focussedAutoModeTaskListener =
        new FocussedAutoModeTaskListener ();

    /** list of proof listeners and interactive proof listeners */
    private List<AutoModeListener> listenerList = 
        Collections.synchronizedList(new ArrayList<AutoModeListener>(10));


    /** listens to the current selected proof and node */
    private KeYSelectionListener selListener;

    /** the mediator */
    private KeYMediator mediator;
    
    private boolean resumeAutoMode = false;

	private ApplyThread applyInteractiveThread;

 
    //private static Logger threadLogger = Logger.getLogger("key.threading");
    
    

    /** creates a new interactive prover object 
     */
    public InteractiveProver(KeYMediator mediator) {
	selListener = new InteractiveProverKeYSelectionListener();
	this.mediator = mediator;
	mediator.addKeYSelectionListener(selListener);
	applyStrategy = new ApplyStrategy(mediator);
        applyStrategy.addProverTaskObserver(mediator().getProverTaskListener());
    }

    /** returns the KeYMediator */
    KeYMediator mediator() {
	return mediator;
    }

    /** sets up a new proof 
     * @param p a Proof that contains the root of the proof tree
     */
    public void setProof(Proof p) {
	proof = p;
    }

    public void addAutoModeListener(AutoModeListener p) { 
		synchronized(listenerList) {
			if (!listenerList.contains(p)) listenerList.add(p);
		}
    }

    public void removeAutoModeListener(AutoModeListener p) { 
		synchronized(listenerList) {	
		    listenerList.remove(p);
		}
    }

    /** fires the event that automatic execution has started */
    protected void fireAutoModeStarted(ProofEvent e) {
	synchronized(listenerList) {
        for (AutoModeListener aListenerList : listenerList) {
            aListenerList.
                    autoModeStarted(e);
        }
	}
    }

    /** fires the event that automatic execution has stopped */
    public void fireAutoModeStopped(ProofEvent e) {
	synchronized(listenerList) {
        for (AutoModeListener aListenerList : listenerList) {
            aListenerList.
                    autoModeStopped(e);
        }
	}
    }
    
    void setResumeAutoMode(boolean b) {
       resumeAutoMode = b;
    }
 

    public boolean resumeAutoMode() {
	return resumeAutoMode;
    }

    public boolean interactiveMode() {
	return interactive;
    }

    public void setInteractive(boolean interact) {
	interactive=interact;
    }


    /**
     * Apply a RuleApp and continue with update simplification or strategy 
     * application according to current settings.
     * @param app
     * @param goal
     */
    public void applyInteractive ( RuleApp app, final Goal goal ) {
        goal.node().getNodeInfo().setInteractiveRuleApplication(true);

        //ImmutableList<Goal> goalList = goal.apply(app);
        
        
        
        applyInteractiveThread = new ApplyThread( app, goal );

        applyInteractiveThread.addThreadListener( new ThreadListenerAdapter() {
            
            @Override
            public void threadFinished(IThreadSender sender) {
                System.out.println("Finishing Thread");
                ImmutableList<Goal> goalList = applyInteractiveThread.getListOfGoal();
                
                if (!getProof ().closed ()) {
                    if ( resumeAutoMode () ) {
                        startAutoMode ();
                    } else {
                        ReuseListener rl = mediator().getReuseListener();
                        rl.removeRPConsumedGoal(goal);
                        rl.addRPOldMarkersNewGoals(goalList);
                        if (rl.reusePossible()) {
                            mediator().indicateReuse(rl.getBestReusePoint());
                        } else {
                            mediator().indicateNoReuse();
                            Goal.applyUpdateSimplifier ( goalList );
                        }
                    }
                }
                Main.getInstance().unfreezeExceptAutoModeButton();
                System.out.println("InteractiveProver: THREAD FINISHED");
            }
            
            @Override
            public void threadInterrupted(IThreadSender sender) {
                System.out.println("InteractiveProver: THREAD INTERRUPTED");
                Main.getInstance().unfreezeExceptAutoModeButton();
           }
            
            @Override
            public void threadException(IThreadSender sender, Exception ex) {
                System.out.println("InteractiveProver: THREAD HAS EXCEPTION");
                ex.printStackTrace();
                Main.getInstance().unfreezeExceptAutoModeButton();
                // show a dialog with the exception
                new ExceptionDialog(Main.getInstance(), ex); 
           }
            
        });
        Main.getInstance().freezeExceptAutoModeButton();
        applyInteractiveThread.start();
        System.out.println("InteractiveProver: THREAD STARTED!");
    }


    private long getTimeout() {
        return mediator().getAutomaticApplicationTimeout();
    }
    
    /**
     *  returns the proof the interactive prover is working on
     * @return the proof the interactive prover is working on
     */
    public Proof getProof() {
	return proof;
    }    

    /** 
     * starts the execution of rules with active strategy 
     */
    public void startAutoMode () {
	startAutoMode( proof.openGoals () );
    }


    /** starts the execution of rules with active strategy. The
     * strategy will only be applied on the goals of the list that
     * is handed over and on the new goals an applied rule adds
     */
    public void startAutoMode ( ImmutableList<Goal> goals ) {
        if ( goals.isEmpty () ) {
            if ( Main.batchMode ) {
                // Everything is already proven.
                // Nothing to be saved. Exit successfully.
            	MathSolverManager.dispose();
                System.exit ( 0 );
            } else {
                mediator ().popupWarning ( "No enabled goals available", "Oops..." );
                return;
            }
        }
        
        if ( Main.batchMode ) {
            interactive = false;
        }
        
        applyStrategy.start ( proof, goals, mediator ().getMaxAutomaticSteps(), getTimeout() );
    }
    
    /** stops the execution of rules */
    public void stopAutoMode () {
        applyStrategy.stop();
        if(applyInteractiveThread != null) {
        	applyInteractiveThread.interrupt();
        }
        Main.getInstance().unfreezeExceptAutoModeButton();
    }
    
    /**
     * starts the execution of rules with active strategy. Restrict the
     * application of rules to a particular goal and (for
     * <code>focus!=null</code>) to a particular subterm or subformula of
     * that goal
     */
    public void startFocussedAutoMode (PosInOccurrence focus, Goal goal) {
        applyStrategy.addProverTaskObserver ( focussedAutoModeTaskListener );
        
        if ( focus != null ) {
            // exchange the rule app manager of that goal to filter rule apps

            // we also apply rules to directly preceding updates (usually this
            // makes sense)
            final PIOPathIterator it = focus.iterator();
            it.next ();
            focus = it.getPosInOccurrence (); 
            while ( it.hasNext () ) {
                if ( it.getSubTerm ().op () instanceof IUpdateOperator ) {
                    final IUpdateOperator op =
                        (IUpdateOperator)it.getSubTerm ().op ();
                    if ( it.getChild () == op.targetPos() ) {
                        it.next ();
                        continue;
                    }
                }

                it.next ();
                focus = it.getPosInOccurrence (); 
            }
                        
            final AutomatedRuleApplicationManager realManager = goal.getRuleAppManager ();
            goal.setRuleAppManager ( null );
            final FocussedRuleApplicationManager focusManager =
                new FocussedRuleApplicationManager ( realManager, goal, focus );
            goal.setRuleAppManager ( focusManager );
        }

        startAutoMode ( ImmutableSLList.<Goal>nil().prepend ( goal ) );
    }

    private void finishFocussedAutoMode () {
        applyStrategy.removeProverTaskObserver ( focussedAutoModeTaskListener );

        for (final Goal goal : proof.openGoals()) {
            // remove any filtering rule app managers that are left in the proof
            // goals
            if (goal.getRuleAppManager() instanceof FocussedRuleApplicationManager) {
                final FocussedRuleApplicationManager focusManager =
                        (FocussedRuleApplicationManager) goal.getRuleAppManager();
                goal.setRuleAppManager(null);
                final AutomatedRuleApplicationManager realManager =
                        focusManager.getDelegate();
                realManager.clearCache();
                goal.setRuleAppManager(realManager);
            }
        }
    }
    
    private final class FocussedAutoModeTaskListener implements ProverTaskListener {
        public void taskStarted ( String message, int size ) {}
        public void taskProgress ( int position ) {}
        public void taskFinished (TaskFinishedInfo info) {
            SwingUtilities.invokeLater ( new Runnable () {
                public void run () {
                    finishFocussedAutoMode ();
                }
            } );
        }
    }

    /**
     * collects all built-in rules that are applicable at the given sequent
     * position 'pos'.
     * 
     * @param pos
     *            the PosInSequent where to look for applicable rules
     * @param userConstraint
     *            the user defined constraint
     */
    public ImmutableList<BuiltInRule> getBuiltInRule(PosInOccurrence pos, 
						 Constraint   userConstraint) {
	ImmutableList<BuiltInRule> rules = ImmutableSLList.<BuiltInRule>nil();

        for (RuleApp ruleApp : getInteractiveRuleAppIndex().getBuiltInRule
                (focusedGoal, pos, userConstraint)) {
            BuiltInRule r = (BuiltInRule) ruleApp.rule();
            if (!rules.contains(r)) {
                rules = rules.prepend(r);
            }
        }
	return rules;
    }

    /**
     * @return the <code>RuleAppIndex</code> of the goal currently focussed,
     *         after setting the index to unrestricted (non-automated mode)
     */
    private RuleAppIndex getInteractiveRuleAppIndex () {
        final RuleAppIndex index = focusedGoal.ruleAppIndex ();
        index.autoModeStopped ();
        return index;
    }

    /**
     * collects all built-in rule applications for the given rule that are
     * applicable at position 'pos' and the current user constraint
     * 
     * @param rule
     *            the BuiltInRule for which the applications are collected
     * @param pos
     *            the PosInSequent the position information
     * @param userConstraint
     *            the user defined constraint
     * @return a SetOf<RuleApp> with all possible rule applications
     */
    public ImmutableSet<RuleApp> getBuiltInRuleApp(BuiltInRule rule, 
					  PosInOccurrence     pos,
					  Constraint       userConstraint) {
	
	ImmutableSet<RuleApp> result = DefaultImmutableSet.<RuleApp>nil();

        for (final RuleApp app : getInteractiveRuleAppIndex().
                getBuiltInRule(focusedGoal,
                        pos,
                        userConstraint)) {
            if (app.rule() == rule) {
                result = result.add(app);
            }
        }

	return result;
    }
    
    /**
     * collects all applications of a rule given by its name at a give position in the sequent
     * @param name
     * 				the name of the BuiltInRule for which applications are collected.
     * @param pos
     * 				the position in the sequent where the BuiltInRule should be applied
     * @return
     * 				a SetOf<RuleApp> with all possible applications of the rule
     */
    protected ImmutableSet<RuleApp> getBuiltInRuleAppsForName(String name, PosInOccurrence pos)
    {
        ImmutableSet<RuleApp> result = DefaultImmutableSet.<RuleApp>nil();
        ImmutableList<BuiltInRule> match = ImmutableSLList.<BuiltInRule>nil();
        
        final Constraint userConstraint = mediator.getUserConstraint().getConstraint();
        
        //get all possible rules for current position in sequent
        ImmutableList<BuiltInRule> list = getBuiltInRule(pos, userConstraint);
        
        Iterator<BuiltInRule> iter = list.iterator();
        
        //find all rules that match given name
        while (iter.hasNext()) {
            BuiltInRule rule = iter.next();
            if (rule.name().toString().equals(name)) match = match.append(rule);
        }
        
        iter = match.iterator();
        
        //find all applications for matched rules
        while (iter.hasNext()) {
            result = result.union(getBuiltInRuleApp(iter.next(), pos, userConstraint));
        }
        
        return result;
    }
    
    /**
	 * collects all applicable NoFindTaclets of the current goal (called by the
	 * SequentViewer)
	 * 
	 * @return a list of Taclets with all applicable NoFindTaclets
	 */
    ImmutableList<TacletApp> getNoFindTaclet() {
	return filterTaclet(getInteractiveRuleAppIndex ().
		       getNoFindTaclet(TacletFilter.TRUE,
				       mediator.getServices(),
				       mediator.getUserConstraint().getConstraint()));
    }    

    /** collects all applicable FindTaclets of the current goal
     * (called by the SequentViewer)
     * @return a list of Taclets with all applicable FindTaclets
     */
    ImmutableList<TacletApp> getFindTaclet(PosInSequent pos) {
	if (pos != null && !pos.isSequent() && focusedGoal != null) {
            Debug.out("NoPosTacletApp: Looking for applicables rule at node",
                      focusedGoal.node().serialNr());
            return filterTaclet(getInteractiveRuleAppIndex ().
			      getFindTaclet(TacletFilter.TRUE,
	 	                            pos.getPosInOccurrence(),
		                            mediator.getServices(),
					    mediator.getUserConstraint().getConstraint()));
	}
	return ImmutableSLList.<TacletApp>nil();
    }
    
    /** collects all applicable RewriteTaclets of the current goal
     * (called by the SequentViewer)
     * @return a list of Taclets with all applicable RewriteTaclets
     */
    ImmutableList<TacletApp> getRewriteTaclet(PosInSequent pos) {
	if (!pos.isSequent())  {
	    return filterTaclet(getInteractiveRuleAppIndex ().
		   getRewriteTaclet(TacletFilter.TRUE,
				    pos.getPosInOccurrence(),
				    mediator.getServices(),
				    mediator.getUserConstraint().getConstraint())); 
	}

	return ImmutableSLList.<TacletApp>nil();
    }



    /** 
     * collects all Taclet applications at the given position of the specified
     * taclet
     * @param goal the Goal for which the applications should be returned
     * @param name the String with the taclet names whose applications are looked for
     * @param pos the PosInOccurrence describing the position
     * @return a list of all found rule applications of the given rule at
     * position pos  
     */
    protected ImmutableSet<TacletApp> getAppsForName(Goal goal, String name, 
            PosInOccurrence pos) {
        return getAppsForName(goal, name, pos, TacletFilter.TRUE);
    }
    
    
    /** 
     * collects all taclet applications for the given position and taclet 
     * (identified by its name) matching the filter condition
     * @param goal the Goal for which the applications should be returned
     * @param name the String with the taclet names whose applications are looked for
     * @param pos the PosInOccurrence describing the position
     * @param filter the TacletFilter expressing restrictions 
     * @return a list of all found rule applications of the given rule at
     * position <tt>pos</tt> passing the filter
     */
     protected ImmutableSet<TacletApp> getAppsForName(Goal goal, String name, 
                                            PosInOccurrence pos,
                                            TacletFilter filter) {
	ImmutableSet<TacletApp> result = DefaultImmutableSet.<TacletApp>nil();
        ImmutableList<TacletApp> fittingApps = ImmutableSLList.<TacletApp>nil();
        final RuleAppIndex index          = goal.ruleAppIndex();
	final Constraint   userConstraint =
            mediator.getUserConstraint().getConstraint();
	if ( pos == null ) {
        for (NoPosTacletApp noPosTacletApp : index.getNoFindTaclet(filter,
                mediator.getServices(),
                userConstraint))
            fittingApps = fittingApps.prepend(noPosTacletApp);
        } else 
            fittingApps = index.getTacletAppAt ( filter,
                                                 pos, 
			                         mediator.getServices(),
					         userConstraint );

        // filter fitting applications
        for (TacletApp app : fittingApps) {
            if (app.rule().name().toString().equals(name)) {
                result = result.add(app);
            }
        }
//if (result.size()==0) System.err.println("Available was "+fittingApps);
	return result;
    }
    
    /** listener to KeYSelection Events in order to be informed if the
     * current proof changed
     */
    private class InteractiveProverKeYSelectionListener 
	implements KeYSelectionListener {

	/** focused node has changed */
	public void selectedNodeChanged(KeYSelectionEvent e) {
	    focusedGoal = e.getSource().getSelectedGoal();
	}

	/** the selected proof has changed (e.g. a new proof has been
	 * loaded) */ 
	public void selectedProofChanged(KeYSelectionEvent e) {
	    Debug.out("InteractiveProver: initialize with new proof");
            Proof newProof = e.getSource().getSelectedProof();
	    setProof(newProof); // this is null-safe
            if (newProof != null) {
	        focusedGoal = e.getSource().getSelectedGoal();
            } else {
                focusedGoal = null;
            }
	}

    }
    
    /**The purpose is to reset the interactiveProver to prevent memory leaking. This 
     * method is used, e.g., by {@code TaskTree.removeTaskWithoutInteraction}. 
     * An alternative would be to reset the InteractiveProver in 
     * {@code InteractiveProverKeYSelectionListener.selectedProofChanged} but 
     * there we don't know whether the proof has been abandoned or not. 
     * @author gladisch */
    public void clear(){
        if(applyStrategy!=null){
            applyStrategy.clear();
        }
        proof.clearAndDetachRuleAppIndexes();
        proof = null;
        focusedGoal = null;
        //probably more clean up has to be done here.
    }

    /**
     * takes NoPosTacletApps as arguments and returns a duplicate free list of
     * the contained TacletApps
     */
    private ImmutableList<TacletApp> filterTaclet(ImmutableList<NoPosTacletApp> tacletInstances) {
        java.util.HashSet<Taclet> applicableRules = new java.util.HashSet<Taclet>();
        ImmutableList<TacletApp> result = ImmutableSLList.<TacletApp>nil();
        for (NoPosTacletApp app : tacletInstances) {
            if (mediator().stupidMode()) {
                ImmutableList<TacletApp> ifCandidates =
                        app.findIfFormulaInstantiations(
                                mediator().getSelectedGoal().sequent(),
                                mediator().getServices(),
                                mediator().getUserConstraint().getConstraint());
                if (ifCandidates.size() == 0) continue; // skip this app
            }

            // for the moment, just remove taclets which are
            // inconsistent with user constraint 
            // (introduction of new sorts not allowed)                     
            if (mediator().getUserConstraint().getConstraint()
                    .join(app.constraint(), null).isSatisfiable()) {
                Taclet taclet = app.taclet();
                if (!applicableRules.contains(taclet)) {
                    applicableRules.add(taclet);
                    result = result.prepend(app);
                }
            }
        }
       	return result;
    }

    /**     
     * adds a proverTaskListener to apply strategy. 
     * 
     * @param ptl the ProverTaskListener to be added
     */
    public void addProverTaskListener(ProverTaskListener ptl) {
        applyStrategy.addProverTaskObserver(ptl);
    }

    /**
     * removes <code>ptl</code> from the list of proverTaskListeners
     *  
     * @param ptl the proverTaskListener to be removed
     */
    public void removeProverTaskListener(ProverTaskListener ptl) {      
        applyStrategy.removeProverTaskObserver(ptl);        
    }
    
}
