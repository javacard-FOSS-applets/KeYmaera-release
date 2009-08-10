package de.uka.ilkd.key.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import de.uka.ilkd.key.gui.configuration.ProofSettings;
import de.uka.ilkd.key.gui.DecisionProcedureSettings.RuleDescriptor;

/**
 * This Dialog is used to make changes in the configuration of Decision Procedures.
 * To make sure, the settings are consistent, it is realized as Singelton.
 * @author Simon Greiner
 *
 */
public class DecissionProcedureSettingsDialog extends JDialog {

    private static DecissionProcedureSettingsDialog instance;
    
    private List <RuleDescriptor> allrules;
    private JTextField nameField;
    private JTextField availableField;
    private JTextField executionField;
    private JList ruleDisplayList;
    private JCheckBox multiuseBox;
    
    
    /**
     * private constructor
     */
    private DecissionProcedureSettingsDialog() {
	super();
	this.setTitle("Decision Procedure Settings");
	this.init();
    }
    
    public static DecissionProcedureSettingsDialog getInstance() {
	if (instance == null) {
	    instance = new DecissionProcedureSettingsDialog();
	}
	return instance;
    }
    
    public static void resetInstance() {
	instance.setSize(400, 300);
	instance.setPreferredSize(new Dimension(490, 340));
	instance.setMaximumSize(new Dimension(490, 340));
	instance.setMinimumSize(new Dimension(490, 340));
	instance.setVisible(true);
    }
    
    private void init() {
	final int labelWidth = 110;
	final int fieldWidth = 270;
	final int labelHeight = 30;
	final int fieldHeight = 30;
	JSplitPane tp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	//add the components to the panel
	JComponent lc = new JPanel();
	lc.setLayout(new BoxLayout(lc, BoxLayout.Y_AXIS));
	lc.setPreferredSize(new Dimension(100, 290));
	lc.setMinimumSize(new Dimension(100, 290));
	lc.setMaximumSize(new Dimension(100, 290));
	
	JComponent rc = new JPanel();
	rc.setLayout(new BoxLayout(rc, BoxLayout.Y_AXIS));
	rc.setPreferredSize(new Dimension(390, 340));
	rc.setMinimumSize(new Dimension(390, 340));
	rc.setMaximumSize(new Dimension(390, 340));
	tp.setLeftComponent(lc);
	tp.setRightComponent(rc);
	//read the available rules from the Settings
	this.allrules = ProofSettings.DEFAULT_SETTINGS.getDecisionProcedureSettings().getAllRules();
	//add the available decision procedures on the left
	Box tempbox = Box.createVerticalBox();
	tempbox.add(Box.createRigidArea(new Dimension(15, 15)));
	Box tempbox2 = Box.createHorizontalBox();
	tempbox2.add(Box.createRigidArea(new Dimension(15, 15)));
	tempbox2.add(new JLabel("available"));
	tempbox2.add(Box.createHorizontalGlue());
	tempbox.add(tempbox2);
	tempbox2 = Box.createHorizontalBox();
	tempbox2.add(Box.createRigidArea(new Dimension(15, 15)));
	tempbox2.add(new JLabel("DecProcs"));
	tempbox2.add(Box.createHorizontalGlue());
	tempbox.add(tempbox2);
	tempbox2 = Box.createHorizontalBox();
	tempbox2.add(Box.createRigidArea(new Dimension(15, 15)));
	tempbox2.add(buildDecprocList());
	tempbox2.add(Box.createHorizontalGlue());
	tempbox.add(tempbox2);
	lc.add(tempbox);
	lc.add(Box.createVerticalGlue());
	
	
	//add the settings-stuff for the selected decproc on the right
	Box globalBox = Box.createVerticalBox();
	
	JLabel nameLabel = new JLabel("name");
	nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
	//nameLabel.setSize(90, 30);
	nameLabel.setMaximumSize(new Dimension(labelWidth, labelHeight));
	nameLabel.setMinimumSize(new Dimension(labelWidth, labelHeight));
	nameLabel.setPreferredSize(new Dimension(labelWidth, labelHeight));
	//rc.add(nameLabel);
	nameField = new JTextField();
	nameField.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
	nameField.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
	nameField.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	nameField.setEditable(false);
	//rc.add(nameField);
	Box b = Box.createHorizontalBox();
	b.add(nameLabel);
	b.add(nameField);
	b.add(Box.createHorizontalGlue());
	
	globalBox.add(b);
	
	//rc.add(Box.createHorizontalGlue());
	JLabel availableLabel = new JLabel("installed");
	availableLabel.setMaximumSize(new Dimension(labelWidth, labelHeight));
	availableLabel.setMinimumSize(new Dimension(labelWidth, labelHeight));
	availableLabel.setPreferredSize(new Dimension(labelWidth, labelHeight));
	//rc.add(availableLabel);
	availableField = new JTextField();
	availableField.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
	availableField.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
	availableField.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	availableField.setEditable(false);
	//rc.add(availableField);
	
	b = Box.createHorizontalBox();
	b.add(availableLabel);
	b.add(availableField);
	b.add(Box.createHorizontalGlue());
	globalBox.add(b);
	
	JLabel execLabel = new JLabel("Command");
	execLabel.setMaximumSize(new Dimension(labelWidth, labelHeight));
	execLabel.setMinimumSize(new Dimension(labelWidth, labelHeight));
	execLabel.setPreferredSize(new Dimension(labelWidth, labelHeight));
	
	//rc.add(execLabel);
	this.executionField = new JTextField();
	this.executionField.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
	this.executionField.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
	this.executionField.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	//rc.add(executionField);
	

	
	b = Box.createHorizontalBox();
	b.add(execLabel);
	b.add(executionField);
	b.add(Box.createHorizontalGlue());
	
	globalBox.add(b);
	
	
	
	this.multiuseBox = new JCheckBox();
	this.multiuseBox.setMaximumSize(new Dimension(fieldWidth, fieldHeight));
	this.multiuseBox.setMinimumSize(new Dimension(fieldWidth, fieldHeight));
	this.multiuseBox.setPreferredSize(new Dimension(fieldWidth, fieldHeight));
	
	
	JLabel multiuseLabel = new JLabel("Multiple provers");
	multiuseLabel.setMaximumSize(new Dimension(labelWidth, labelHeight));
	multiuseLabel.setMinimumSize(new Dimension(labelWidth, labelHeight));
	multiuseLabel.setPreferredSize(new Dimension(labelWidth, labelHeight));
	
	
	b = Box.createHorizontalBox();
	b.add(multiuseLabel);
	b.add(multiuseBox);
	b.add(Box.createHorizontalGlue());
	
	globalBox.add(b);
	
	this.setRuleVals();
	
	
	//add the description
	JTextArea c = new JTextArea("Edit the start command here.\n" +
		"Give the starting command for an external procedure in a\nway, it can be executed " +
		"to love a problem file.\nFeel free to use any parameter to finetune the program.\n\n" +
		"Use %f as placeholder for the filename containing the \nproblemdescription.\n\n" +
		"Use %p as placeholder for the problem directly.\nThis should be needed in special cases only.");
	c.setEditable(false);
	c.setMaximumSize(new Dimension(350, 150));
	c.setMinimumSize(new Dimension(350, 150));
	c.setPreferredSize(new Dimension(350, 150));
	globalBox.add(c);
	
	globalBox.add(Box.createVerticalGlue());
	
	JButton applyButton = new JButton("apply");
	applyButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		storeCommand();
	    }
	});
	JButton closeButton = new JButton("close");
	closeButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		DecissionProcedureSettingsDialog.instance.setVisible(false);
	    }
	});

	b = Box.createHorizontalBox();
	b.add(Box.createHorizontalGlue());
	b.add(applyButton);
	b.add(closeButton);
	
	globalBox.add(b);
	
	rc.add(globalBox);
	
	//rc.add(closeButton);
	//rc.add(applyButton);
	this.add(tp);
	this.setVisible(true);
    }
    
    private void storeCommand() {
	RuleDescriptor rd = allrules.get(ruleDisplayList.getSelectedIndex());
	String command = this.executionField.getText();
	boolean multipleuse = this.multiuseBox.isSelected();
	ProofSettings.DEFAULT_SETTINGS.getDecisionProcedureSettings().setMultipleUse(rd,multipleuse);
	ProofSettings.DEFAULT_SETTINGS.getDecisionProcedureSettings().setExecutionCommand(rd, command);
	
	Main.instance.updateDecisionProcedureSelectMenu();
	this.setRuleVals();
    }
    
/*    private void performTest() {
	RuleDescriptor rd = allrules.get(ruleDisplayList.getSelectedIndex());
	String command = this.executionField.getText();
	boolean result = ProofSettings.DEFAULT_SETTINGS.getDecisionProcedureSettings().checkCommand(rd, command);
	if (result) {
	    System.out.println("### Test successful");
	} else {
	    System.out.println("### The test was not successfull");
	}
    }*/
    
    /**
     * set the value fields according to the values of the selected decproc.
     * @param selectedIndex the index which decproc is said to be selected.
     */
    private void setRuleVals() {
	int selectedIndex = ruleDisplayList.getSelectedIndex();
	RuleDescriptor rd = this.allrules.get(selectedIndex);
	this.nameField.setText(rd.getDisplayName());
	if (ProofSettings.DEFAULT_SETTINGS.getDecisionProcedureSettings().isInstalled(rd)) {
	    this.availableField.setText("Yes");
	} else {
	    this.availableField.setText("No");
	}
	this.executionField.setText(ProofSettings.DEFAULT_SETTINGS.getDecisionProcedureSettings().getExecutionCommand(rd));
	this.multiuseBox.setSelected(ProofSettings.DEFAULT_SETTINGS.getDecisionProcedureSettings().getMultipleUse(rd));
    }
    
    /**
     * Build a list that displays all available decprocs.
     * @return
     */
    private JList buildDecprocList() {
	ArrayList<String> rulenames = new ArrayList<String>();
	for (RuleDescriptor rd : this.allrules) {
		rulenames.add(rd.getDisplayName());
	}
	ruleDisplayList = new JList(rulenames.toArray());
	ruleDisplayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	ruleDisplayList.setSelectedIndex(0);
	
	//add the selectionListener
	ruleDisplayList.addListSelectionListener(new ListSelectionListener () {
	    public void valueChanged(ListSelectionEvent e) {
		setRuleVals();
	    }
	});
	
	return ruleDisplayList;
    }
    
}
