/***************************************************************************
 *   Copyright (C) 2007 by Jan David Quesel                                *
 *   quesel@informatik.uni-oldenburg.de                                    *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
/**
 * 
 */
package de.uka.ilkd.key.dl.arithmetics;

import java.rmi.RemoteException;

import de.uka.ilkd.key.dl.model.DiffSystem;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.op.LogicVariable;

/**
 * TODO jdq documentation since Aug 17, 2007
 * 
 * @author jdq
 * @since Aug 17, 2007
 * 
 */
public interface IODESolver extends IMathSolver {
    
    public static class ODESolverResult {
        private Term invariantExpression;
        private Term postCondition;
        
        public ODESolverResult(Term invariant, Term post) {
            this.invariantExpression = invariant;
            this.postCondition = post;
        }
        /**
         * @return the invariantExpression
         */
        public Term getInvariantExpression() {
            return invariantExpression;
        }
        /**
         * @param invariantExpression the invariantExpression to set
         */
        public void setInvariantExpression(Term invariantExpression) {
            this.invariantExpression = invariantExpression;
        }
        /**
         * @return the postCondition
         */
        public Term getPostCondition() {
            return postCondition;
        }
        /**
         * @param postCondition the postCondition to set
         */
        public void setPostCondition(Term postCondition) {
            this.postCondition = postCondition;
        }
    }
    
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
    public abstract ODESolverResult odeSolve(DiffSystem form, LogicVariable t,
            LogicVariable ts, Term phi, NamespaceSet nss)
            throws RemoteException;

    /**
     * DiffInd for the given differential equation system.
     * 
     * @param form
     *                the differential equation system
     * @param post
     *                the formula to be sustained.
     * @param nss
     *                the current namespace sets
     * @throws RemoteException
     *                 if there is any problem
     */
    public abstract Term diffInd(DiffSystem form, Term post, NamespaceSet nss)
            throws RemoteException;
}
