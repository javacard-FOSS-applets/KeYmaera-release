package de.uka.ilkd.key.dl.gui.dialogwithsidepane.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Zacho
 * 
 */
public class DecisionPane {

    JPanel pane;

    private JButton buttonExit;
    private JButton buttonOK;
    private JButton buttonApply;

    public DecisionPane() {

	
	pane = new JPanel();
	buttonOK = new JButton("    OK   ");
	buttonApply = new JButton(" Apply ");
	buttonExit = new JButton(" Cancel ");
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(70,3,20,3);
	pane.add(buttonOK,c);
	pane.add(buttonApply,c);
	c.insets = new Insets(70,3,20,15);
	pane.add(buttonExit, c);
    }

    public JPanel getPane() {
	return pane;
    }

    public JButton getButtonExit() {
	return buttonExit;
    }

    public JButton getButtonOK() {
	return buttonOK;
    }

    public JButton getButtonApply() {
	return buttonApply;
    }
    public void addActionListener(ActionListener l) {
	buttonOK.addActionListener(l);
	buttonApply.addActionListener(l);
	buttonExit.addActionListener(l);
    }


}
