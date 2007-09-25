/**
 * File created 25.01.2007
 */
package de.uka.ilkd.key.dl.arithmetics.impl.mathematica;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import de.uka.ilkd.key.dl.arithmetics.IODESolver.ODESolverResult;
import de.uka.ilkd.key.dl.arithmetics.IQuantifierEliminator.PairOfTermAndQuantifierType;
import de.uka.ilkd.key.dl.model.DiffSystem;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.LogicVariable;

/**
 * The IMathematicaDLBridge is the interface specifying the interface between
 * KeY and Mathematica.
 * 
 * @author jdq
 * @since 25.01.2007
 * 
 */
public interface IMathematicaDLBridge extends Remote {

    public String IDENTITY = "MethematicaDLBridge";

    /**
     * Solves the given differential equation system
     * 
     * @param form
     *                the system to solve
     * @param t
     *                the logical variable used as time
     * @param ts
     * @param phi
     *                the formula to be updated by the solutions of the
     *                differential equations
     * @param nss
     *                the current namespace sets
     * @return a Term containing an update for the solutions of the differential
     *         equations on the term phi
     * @throws RemoteException
     *                 if there is any problem
     */
    public ODESolverResult odeSolve(DiffSystem form, LogicVariable t,
            LogicVariable ts, Term phi, NamespaceSet nss)
            throws RemoteException;

    public Term diffInd(DiffSystem form, Term post, NamespaceSet nss)
            throws RemoteException;
    
    /**
     * Simplifies the given Term if possible
     * 
     * @param form
     *                the term to simplify
     * @param assumptions
     *                the assumptions used for simplification
     * @return the simplifyed term, this may be the same as the input.
     */
    public Term simplify(Term form, Set<Term> assumptions)
            throws RemoteException;

    /**
     * Simplifies the given Term if possible (using FullSimplify)
     * 
     * @param form
     *                the term to simplify
     * @param assumptions
     *                the assumptions used for simplification
     * @return the simplifyed term, this may be the same as the input.
     */
    public Term fullSimplify(Term form) throws RemoteException;

    /**
     * @param form
     * @return
     * @throws RemoteException
     */
    public String findInstance(Term form) throws RemoteException;

    /**
     * Abort the current calculation. Or the next one started.
     */
    public void abortCalculation() throws RemoteException;

    /**
     * Get the statistics about calculation time
     * 
     * @return the statistics about calculation time
     * @throws RemoteException
     */
    public String getTimeStatistics() throws RemoteException;

    /**
     * Get the total time the server was calculating while started
     * 
     * @return the total time the server was calculating while started
     * @throws RemoteException
     */
    public long getTotalCalculationTime() throws RemoteException;

    /**
     * Get the number of cached answers that were returned since the server was
     * started
     * 
     * @return the number of cached answers that were returned since the server
     *         was started
     * @throws RemoteException
     */
    public long getCachedAnwserCount() throws RemoteException;

    /**
     * Get the number of queries to the server
     * 
     * @return the number of queries to the server
     * @throws RemoteException
     */
    public long getQueryCount() throws RemoteException;

    /**
     * Reset the abort state.
     * 
     * @throws RemoteException
     * 
     */
    public void resetAbortState() throws RemoteException;

    /**
     * Calls Mathematica Reduce on the given Term.
     * 
     * @param form
     *                the Term to reduce
     * @param quantifiedSymbols
     *                the symbols that were quantified
     * @param type
     *                the type of the quantifier to reintroduce before reduction
     * @return the reduced Term. (may be the same as the input)
     * @throws RemoteException
     */
    public Term reduce(Term form, List<String> additionalReduce,
            List<PairOfTermAndQuantifierType> quantifiers)
            throws RemoteException;
}
