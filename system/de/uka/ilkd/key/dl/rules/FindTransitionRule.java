/***************************************************************************
 *   Copyright (C) 2007 by André Platzer                                   *
 *   @informatik.uni-oldenburg.de                                    *
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
 * File created 01.02.2007
 */
package de.uka.ilkd.key.dl.rules;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.uka.ilkd.key.dl.arithmetics.MathSolverManager;
import de.uka.ilkd.key.dl.formulatools.TermTools;
import de.uka.ilkd.key.dl.model.DiffSystem;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.java.StatementBlock;
import de.uka.ilkd.key.logic.Constraint;
import de.uka.ilkd.key.logic.IteratorOfConstrainedFormula;
import de.uka.ilkd.key.logic.JavaBlock;
import de.uka.ilkd.key.logic.Name;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.TermBuilder;
import de.uka.ilkd.key.logic.op.Modality;
import de.uka.ilkd.key.logic.op.Op;
import de.uka.ilkd.key.logic.op.QuanUpdateOperator;
import de.uka.ilkd.key.proof.Goal;
import de.uka.ilkd.key.proof.ListOfGoal;
import de.uka.ilkd.key.proof.RuleFilter;
import de.uka.ilkd.key.rule.BuiltInRule;
import de.uka.ilkd.key.rule.Rule;
import de.uka.ilkd.key.rule.RuleApp;

/**
 * The FindInstance is a Built-In Rule that is applied to a whole sequence. It
 * is used by the DLProfile.
 * 
 * @author ap
 * @see de.uka.ilkd.key.proof.DLProfile
 */
public class FindTransitionRule implements BuiltInRule,
        RuleFilter {

    public static final FindTransitionRule INSTANCE = new FindTransitionRule();

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.BuiltInRule#isApplicable(de.uka.ilkd.key.proof.Goal,
     *      de.uka.ilkd.key.logic.PosInOccurrence,
     *      de.uka.ilkd.key.logic.Constraint)
     */
    public boolean isApplicable(Goal goal, PosInOccurrence pos,
            Constraint userConstraint) {
        if (!MathSolverManager.isCounterExampleGeneratorSet()) {
            return false;
        }
        Term term = pos.subTerm();
        // unbox from update prefix
        while (term.op() instanceof QuanUpdateOperator) {
            term = ((QuanUpdateOperator) term.op()).target(term);
        }
        if (!(term.op() instanceof Modality && term.javaBlock() != null
                && term.javaBlock() != JavaBlock.EMPTY_JAVABLOCK && term
                .javaBlock().program() instanceof StatementBlock)) {
            return false;
        }
        return ((StatementBlock) term
                .javaBlock().program()).getChildAt(0) instanceof DiffSystem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.Rule#apply(de.uka.ilkd.key.proof.Goal,
     *      de.uka.ilkd.key.java.Services, de.uka.ilkd.key.rule.RuleApp)
     */
    @SuppressWarnings("unchecked")
    public synchronized ListOfGoal apply(Goal goal, Services services,
            RuleApp ruleApp) {
        IteratorOfConstrainedFormula it = goal.sequent().antecedent()
                .iterator();
        Term antecedent = TermTools.createJunctorTermNAry(TermBuilder.DF.tt(), Op.AND, it, Collections.EMPTY_SET);
        // ignore succedent

        try {
            final String result = MathSolverManager
                    .getCurrentCounterExampleGenerator().findTransition(
                            antecedent, ruleApp.posInOccurrence().subTerm());

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    final JFrame frame = new JFrame("Counterexample Result");
                    frame.setLayout(new FlowLayout());
                    String label;
                    if (result.equals("")) {
                        label = "No counterexample found.";
                    } else {
                        label = result;
                    }
                    final JTextArea label2 = new JTextArea(label);
                    label2.setEditable(false);
                    label2.setLineWrap(true);
                    label2.setBounds(0, 0, 200, 200);
                    frame.add(label2);
                    JButton button = new JButton("Ok");
                    frame.add(button);
                    button.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent arg0) {
                            frame.setVisible(false);
                        }

                    });
                    frame.pack();
                    frame.setVisible(true);
                }

            });

        } catch (Exception e) {
            // if there is an error invoking the mathsolver we cannot apply this
            // rule.
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.Rule#displayName()
     */
    public String displayName() {
        return "Counterexample Transition";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.Rule#name()
     */
    public Name name() {
        return new Name("Counterexample Transition");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.proof.RuleFilter#filter(de.uka.ilkd.key.rule.Rule)
     */
    public boolean filter(Rule rule) {
        return rule instanceof FindTransitionRule;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return displayName();
    }

}
