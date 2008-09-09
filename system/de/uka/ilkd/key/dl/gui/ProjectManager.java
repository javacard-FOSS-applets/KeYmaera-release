/***************************************************************************
 *   Copyright (C) 2008 by Jan David Quesel                                *
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
package de.uka.ilkd.key.dl.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uka.ilkd.key.dl.arithmetics.MathSolverManager;
import de.uka.ilkd.key.dl.utils.XMLReader;
import de.uka.ilkd.key.gui.Main;

/**
 * @author jdq
 * 
 */
public class ProjectManager extends JFrame {

	public static class ProjectManagerAction extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -6854675083351675222L;

		/**
		 * 
		 */
		public ProjectManagerAction() {
			putValue(NAME, "Load Project...");
		}

		public void actionPerformed(ActionEvent e) {
			try {
				ProjectManager projectManager = new ProjectManager();
				projectManager.setVisible(true);
			} catch (XPathExpressionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private static class ExampleInfo {
		private String name;
		private String url;
		private String description;
		private Set<String> requirements;

		/**
		 * @param name
		 * @param url
		 */
		public ExampleInfo(String name, String url, String description,
				Set<String> requirements) {
			super();
			this.name = name;
			this.url = url;
			this.description = description;
			this.requirements = requirements;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the url
		 */
		public String getUrl() {
			return url;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		public Set<String> getRequirements() {
			return requirements;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return getName();
		}
	}

	public static final String EXAMPLES_DESCRIPTION_FILE = "description.xml";

	private JTree tree;

	private JTextArea textArea;

	private JTextArea fileName;

	private JTextArea requirementsArea;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1006322347422958755L;

	public ProjectManager() throws XPathExpressionException {
		super("Project Manager");
		NodeList examples = getExamplesFromFile(EXAMPLES_DESCRIPTION_FILE);
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Examples");

		createNodes(top, examples);

		tree = new JTree(top);
		JScrollPane treeView = new JScrollPane(tree);
		setLayout(new FlowLayout());
		add(treeView);
		final JButton button = new JButton("Load");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				if (lastSelectedPathComponent != null) {
					Object nodeInfo = lastSelectedPathComponent.getUserObject();
					if (lastSelectedPathComponent.isLeaf()) {
						ExampleInfo info = (ExampleInfo) nodeInfo;
						Main.getInstance().loadProblem(
								createTmpFileToLoad(info.getUrl()));
						setVisible(false);
						dispose();
					}
				}
			}

		});
		JPanel buttonTextPanel = new JPanel(new GridLayout(0, 1));
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setAutoscrolls(true);
		textArea.setColumns(50);
		textArea.setEditable(false);

		requirementsArea = new JTextArea();
		requirementsArea.setLineWrap(true);
		requirementsArea.setAutoscrolls(true);
		requirementsArea.setColumns(50);
		requirementsArea.setEditable(false);

		fileName = new JTextArea();
		fileName.setLineWrap(true);
		fileName.setAutoscrolls(true);
		fileName.setColumns(50);
		fileName.setEditable(false);
		buttonTextPanel.add(fileName);
		buttonTextPanel.add(textArea);
		buttonTextPanel.add(requirementsArea);
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) tree
						.getLastSelectedPathComponent();
				if (lastSelectedPathComponent != null) {
					Object nodeInfo = lastSelectedPathComponent.getUserObject();
					if (lastSelectedPathComponent.isLeaf()) {
						ExampleInfo info = (ExampleInfo) nodeInfo;
						fileName.setText(info.getUrl());
						textArea.setText(info.getDescription());
						
						String or = "";
						if (info.requirements.isEmpty()) {
							requirementsArea.setText("No special requirements");
							requirementsArea.setForeground(Color.BLACK);
							button.setEnabled(true);
						} else {
							requirementsArea.setText("You need ");
							button.setEnabled(false);
							requirementsArea.setForeground(Color.RED);
							for (String s : info.getRequirements()) {
								requirementsArea.append(or + s);
								if (MathSolverManager
										.getQuantifierEliminators().contains(s)) {
									requirementsArea.setForeground(Color.BLACK);
									button.setEnabled(true);
								}
								or = " or ";
							}
						}
					}
				}
			}

		});
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(button);
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}

		});
		buttonPanel.add(cancel);
		buttonTextPanel.add(buttonPanel);
		add(buttonTextPanel);
		pack();
	}

	/**
	 * @param top
	 * @param examples
	 * @throws XPathExpressionException
	 */
	private void createNodes(DefaultMutableTreeNode top, NodeList examples)
			throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		for (int i = 0; i < examples.getLength(); i++) {
			Node node = examples.item(i);
			String name = (String) xpath.evaluate("name", node,
					XPathConstants.STRING);
			String path = (String) xpath.evaluate("path", node,
					XPathConstants.STRING);
			String description = (String) xpath.evaluate("description", node,
					XPathConstants.STRING);
			Set<String> requirements = new LinkedHashSet<String>();
			NodeList nodes = (NodeList) xpath.evaluate(
					"requirements/some/quantifiereliminator/text()", node,
					XPathConstants.NODESET);
			for (int j = 0; j < nodes.getLength(); j++) {
				requirements.add(nodes.item(j).getNodeValue());
			}

			DefaultMutableTreeNode tNode = new DefaultMutableTreeNode(
					new ExampleInfo(name, path, description, requirements));
			top.add(tNode);
		}
	}

	/**
	 * @throws XPathExpressionException
	 * 
	 */
	private NodeList getExamplesFromFile(String filename)
			throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		String expression = "/examples/example";
		Document document = new XMLReader(filename).getDocument();
		return (NodeList) xpath.evaluate(expression, document,
				XPathConstants.NODESET);
	}

	private File createTmpFileToLoad(String url) {
		System.out.println("Trying to open " + url);// XXX
		File file = new File(url.substring(1));
		if (file.exists()) {
			return file;
		}
		InputStream resourceAsStream = ProjectManager.class
				.getResourceAsStream(url);
		if (resourceAsStream == null) {
			return null;
		}
		try {
			File tempFile = File.createTempFile("keymaera", ".key");
			tempFile.deleteOnExit();
			System.out.println(tempFile.getCanonicalPath());
			FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
			int i;
			while ((i = resourceAsStream.read()) != -1) {
				fileOutputStream.write((char) i);
			}
			resourceAsStream.close();
			fileOutputStream.close();
			return tempFile;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws XPathExpressionException {
		ProjectManager projectManager = new ProjectManager();
		projectManager.setVisible(true);
	}
}
