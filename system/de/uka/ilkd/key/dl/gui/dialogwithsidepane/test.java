package de.uka.ilkd.key.dl.gui.dialogwithsidepane;

import javax.swing.JFrame;
import de.uka.ilkd.key.dl.gui.dialogwithsidepane.gui.InitialDialogBeans;
import de.uka.ilkd.key.dl.gui.dialogwithsidepane.gui.PropertiesCard;
import de.uka.ilkd.key.gui.Main;




/**
 * 
 * The PropertyConfiguratorSample class create and instance of property
 * confirugration Frame, whereby the properties can be created or modified.
 * 
 * @author zacho
 */
public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialDialogBeans dialog = new InitialDialogBeans(args);
		if (!PropertiesCard.getCheckboxState()) {
			final JFrame frame = dialog.getPathPanel();
			frame.setVisible(true);
		} else { 
			final JFrame frame = dialog.getPathPanel();
			frame.dispose();
			Main.main(args);
		}
	}

}