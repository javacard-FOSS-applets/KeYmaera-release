/***************************************************************************
 *   Copyright (C) 2007 by Andre Platzer                                   *
 *   @informatik.uni-oldenburg.de                                          *
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
package de.uka.ilkd.key.dl.rules.metaconstruct;

import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.Collections;

import de.uka.ilkd.key.dl.arithmetics.MathSolverManager;
import de.uka.ilkd.key.dl.arithmetics.exceptions.FailedComputationException;
import de.uka.ilkd.key.dl.arithmetics.exceptions.SolverException;
import de.uka.ilkd.key.dl.arithmetics.exceptions.UnableToConvertInputException;
import de.uka.ilkd.key.dl.arithmetics.exceptions.UnsolveableException;
import de.uka.ilkd.key.dl.model.DiffSystem;
import de.uka.ilkd.key.dl.rules.metaconstruct.DiffFin.RemoveQuantifiersResult;
import de.uka.ilkd.key.java.PrettyPrinter;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.java.StatementBlock;
import de.uka.ilkd.key.logic.Name;
import de.uka.ilkd.key.logic.NamespaceSet;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.TermBuilder;
import de.uka.ilkd.key.logic.op.LogicVariable;
import de.uka.ilkd.key.logic.op.Modality;
import de.uka.ilkd.key.logic.sort.Sort;
import de.uka.ilkd.key.rule.inst.SVInstantiations;

/**
 * @author ap
 */
public class DiffInd extends AbstractDLMetaOperator {

    public static final Name NAME = new Name("#DiffInd");

    public DiffInd() {
        super(NAME, 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.logic.op.AbstractMetaOperator#sort(de.uka.ilkd.key.logic.Term[])
     */
    @Override
    public Sort sort(Term[] term) {
        return Sort.FORMULA;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.logic.op.AbstractMetaOperator#calculate(de.uka.ilkd.key.logic.Term,
     *      de.uka.ilkd.key.rule.inst.SVInstantiations,
     *      de.uka.ilkd.key.java.Services)
     */
    public Term calculate(Term term, SVInstantiations svInst, Services services) {
        try {
            return diffInd(term.sub(0), services);
        } catch (UnsolveableException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (FailedComputationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw (InternalError) new InternalError(e.getMessage()).initCause(e);
        }
    }

    public Term diffInd(Term term, Services services) throws SolverException {
        DiffSystem system = (DiffSystem) ((StatementBlock) term
                .javaBlock().program()).getChildAt(0);
        Term post = term.sub(0);
        final NamespaceSet nss = services.getNamespaces();
        if (term.op() == Modality.BOX
                || term.op() == Modality.TOUT) {
            try {
				RemoveQuantifiersResult r = new RemoveQuantifiersResult(system);
				r = DiffFin.removeQuantifiers(nss, r);
				StringWriter writer = new StringWriter();
				r.getSys().prettyPrint(new PrettyPrinter(writer));
				System.out.println(writer.toString());//XXX
				Term diffInd = MathSolverManager.getCurrentODESolver()
						.diffInd(r.getSys(), post, nss);
				// reintroduce the quantifiers
				Collections.reverse(r.getQuantifiedVariables());
				for (LogicVariable var : r.getQuantifiedVariables()) {
					diffInd= TermBuilder.DF.all(var, diffInd);
				}
				return diffInd;
            } catch (SolverException e) {
                throw e;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw (InternalError) new InternalError(e.getMessage()).initCause(e);
            }
        } else {
            throw new IllegalStateException("Unknown modality "
                    + term.op());
        }
    }
}
