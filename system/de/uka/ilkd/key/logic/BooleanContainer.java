// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

package de.uka.ilkd.key.logic;

/** BooleanContainer wraps primitive bool */
public final class BooleanContainer {
    private boolean bool;
    
    public BooleanContainer() {
	bool=false;
    }
    
    public final boolean val() {
	return bool;
    }
    
    public final void setVal(boolean b) {
	bool=b;
    }
}

