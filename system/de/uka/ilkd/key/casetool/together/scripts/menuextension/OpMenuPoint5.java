// This file is part of KeY - Integrated Deductive Software Design
//Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                      Universitaet Koblenz-Landau, Germany
//                      Chalmers University of Technology, Sweden
//
//The KeY system is protected by the GNU General Public License. 
//See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.casetool.together.scripts.menuextension;

import de.uka.ilkd.key.casetool.FunctionalityOnModel;
import de.uka.ilkd.key.casetool.ModelMethod;


public class OpMenuPoint5 extends OpMenu {
    public String getMenuEntry() {
        return "StrongOperationContract";
    }

    public String getSubMenuName() {
        return "horizontal";
    }

    protected String runCore(ModelMethod modelMethod) {
        return FunctionalityOnModel.proveStrongOperationContract(modelMethod);
    }
}
