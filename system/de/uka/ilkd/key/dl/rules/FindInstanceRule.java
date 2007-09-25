/**
 * File created 01.02.2007
 */
package de.uka.ilkd.key.dl.rules;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import de.uka.ilkd.key.dl.arithmetics.MathSolverManager;
import de.uka.ilkd.key.java.Services;
import de.uka.ilkd.key.logic.ConstrainedFormula;
import de.uka.ilkd.key.logic.Constraint;
import de.uka.ilkd.key.logic.IteratorOfConstrainedFormula;
import de.uka.ilkd.key.logic.Name;
import de.uka.ilkd.key.logic.PosInOccurrence;
import de.uka.ilkd.key.logic.PosInTerm;
import de.uka.ilkd.key.logic.Term;
import de.uka.ilkd.key.logic.TermBuilder;
import de.uka.ilkd.key.logic.Visitor;
import de.uka.ilkd.key.logic.op.Op;
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
 * @author jdq
 * @since 01.02.2007
 * @see de.uka.ilkd.key.proof.DLProfile
 */
public class FindInstanceRule extends Visitor implements BuiltInRule,
        RuleFilter {

    public static final FindInstanceRule INSTANCE = new FindInstanceRule();

    private boolean formulaContainsSearchSymbol;

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.BuiltInRule#isApplicable(de.uka.ilkd.key.proof.Goal,
     *      de.uka.ilkd.key.logic.PosInOccurrence,
     *      de.uka.ilkd.key.logic.Constraint)
     */
    public boolean isApplicable(Goal goal, PosInOccurrence pio,
            Constraint userConstraint) {
        return MathSolverManager.isCounterExampleGeneratorSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.Rule#apply(de.uka.ilkd.key.proof.Goal,
     *      de.uka.ilkd.key.java.Services, de.uka.ilkd.key.rule.RuleApp)
     */
    public synchronized ListOfGoal apply(Goal goal, Services services,
            RuleApp ruleApp) {
        IteratorOfConstrainedFormula it = goal.sequent().antecedent()
                .iterator();
        Term resultTerm = TermBuilder.DF.tt();
        Map<Term, List<PosInOccurrence>> changes = iterate(goal, it,
                resultTerm, true);
        resultTerm = changes.keySet().iterator().next();
        it = goal.sequent().succedent().iterator();
        Map<Term, List<PosInOccurrence>> changes2 = iterate(goal, it,
                TermBuilder.DF.ff(), false);
        resultTerm = TermBuilder.DF.and(resultTerm, TermBuilder.DF.not(changes2
                .keySet().iterator().next()));

        try {
            resultTerm = MathSolverManager.getCurrentSimplifier().simplify(
                    resultTerm);

            final String result = MathSolverManager
                    .getCurrentCounterExampleGenerator().findInstance(
                            resultTerm);

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

    /**
     * Iterates over the given formulas and constructs the conjunction or
     * disjunction of all first order formulas in the sequence.
     * 
     * @param result
     *                the current goal
     * @param it
     *                the iterator used to access the formulas
     * @param resultTerm
     *                the term built so far
     * @param and
     *                if true this function returns the conjunction, otherwise
     *                the disjunction is returned
     * @return the conjunction or disjunction of all first order formulas in the
     *         sequence.
     */
    private Map<Term, List<PosInOccurrence>> iterate(Goal result,
            IteratorOfConstrainedFormula it, Term resultTerm, boolean and) {
        List<PosInOccurrence> changes = new ArrayList<PosInOccurrence>();
        while (it.hasNext()) {
            ConstrainedFormula f = it.next();
            formulaContainsSearchSymbol = true;
            f.formula().execPostOrder(this);
            if (formulaContainsSearchSymbol) {
                changes.add(new PosInOccurrence(f, PosInTerm.TOP_LEVEL, false));
                if (and) {
                    resultTerm = TermBuilder.DF.and(resultTerm, f.formula());
                } else {
                    resultTerm = TermBuilder.DF.or(resultTerm, f.formula());
                }
            }
        }
        HashMap<Term, List<PosInOccurrence>> res = new HashMap<Term, List<PosInOccurrence>>();
        res.put(resultTerm, changes);
        return res;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.Rule#displayName()
     */
    public String displayName() {
        return "Find Counterexample";
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.rule.Rule#name()
     */
    public Name name() {
        return new Name("Find Counterexample");
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.logic.Visitor#visit(de.uka.ilkd.key.logic.Term)
     */
    @Override
    public void visit(Term visited) {
        performSearch(visited);
    }

    /**
     * Sets addFormula to false if the term is a diamond or box operator.
     * 
     * @param visited
     *                the term to check.
     */
    private void performSearch(Term visited) {
        if (visited.op() == Op.BOX || visited.op() == Op.DIA
                || visited.op() == Op.BOXTRA || visited.op() == Op.DIATRA) {
            formulaContainsSearchSymbol = false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uka.ilkd.key.proof.RuleFilter#filter(de.uka.ilkd.key.rule.Rule)
     */
    public boolean filter(Rule rule) {
        return rule instanceof FindInstanceRule;
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
