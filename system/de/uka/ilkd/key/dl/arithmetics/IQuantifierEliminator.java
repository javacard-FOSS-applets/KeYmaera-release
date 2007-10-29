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
import java.util.List;

import de.uka.ilkd.key.logic.Term;

/**
 * TODO jdq documentation since Aug 17, 2007
 * 
 * @author jdq
 * @since Aug 17, 2007
 * 
 */
public interface IQuantifierEliminator extends IMathSolver {

    /**
     * TODO jdq documentation since Aug 31, 2007
     * 
     * @author jdq
     * @since Aug 31, 2007
     * 
     */
    public class PairOfTermAndQuantifierType {
        /**
         * @param t
         * @param forall
         */
        public PairOfTermAndQuantifierType(Term term, QuantifierType type) {
            this.term = term;
            this.type = type;
        }

        public Term term;

        public QuantifierType type;
    }

    public static enum QuantifierType {
        FORALL, EXISTS;
    }

    /**
     * Reduces the given term if its a mathematical expression
     * 
     */
    public abstract Term reduce(Term form) throws RemoteException;

    // /**
    // * Reduces the given term if its a mathematical expression
    // *
    // */
    // public abstract Term reduce(Term form, List<String> names)
    // throws RemoteException;

    // /**
    // * Reduces the given term if its a mathematical expression
    // *
    // */
    // public abstract Term reduce(Term form, Term quantifedSymbol,
    // QuantifierType type) throws RemoteException;

    /**
     * Reduces the given term if its a mathematical expression
     * 
     */
    public abstract Term reduce(Term form, List<String> names,
            List<PairOfTermAndQuantifierType> quantifiers)
            throws RemoteException;

    /**
     * Reduces the given term if its a mathematical expression
     * 
     * @param query
     * @param orderedList
     * @param singleton
     * @return
     */
    public abstract Term reduce(Term query,
            List<PairOfTermAndQuantifierType> quantifiers)
            throws RemoteException;
}
