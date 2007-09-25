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

package de.uka.ilkd.key.casetool.together;

import com.togethersoft.openapi.ide.IdeAccess;
import com.togethersoft.openapi.ide.project.IdeProjectManagerAccess;

import de.uka.ilkd.key.casetool.ReprModel;

public class TogetherReprModel implements ReprModel{
    /**
     * @return the path to the xmifile associated to the current project. 
     * If no current project available it returns "".
     */
    public String getActXmifile(){
	String result;
	if(IdeProjectManagerAccess.getProjectManager().getActiveProject() == null){
	    // no open project
	    result = "";
	}else{
	    StringBuffer fileName = 
		new StringBuffer(IdeAccess.getProjectManager().getActiveProject().getFileName());	    
	    fileName.replace( fileName.length() - 3,  fileName.length(), "xml");
	    result = fileName.toString();
	}
	return result;
    }
}
