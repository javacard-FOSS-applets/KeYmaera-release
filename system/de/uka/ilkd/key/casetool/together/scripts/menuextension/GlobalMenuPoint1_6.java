// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

/* Generated by Together */

package de.uka.ilkd.key.casetool.together.scripts.menuextension;

import com.togethersoft.openapi.ide.window.IdeWindowManager;

import de.uka.ilkd.key.gui.configuration.ProofSettings;


public class GlobalMenuPoint1_6 implements GlobalMenu{

    public String getMenuEntry(){
	return "Save Settings";
    }

    public void run(IdeWindowManager winMan){
	ProofSettings.DEFAULT_SETTINGS.saveSettings();
    }
   

}
