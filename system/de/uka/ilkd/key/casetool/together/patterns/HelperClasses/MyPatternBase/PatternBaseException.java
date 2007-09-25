// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//


package de.uka.ilkd.key.casetool.together.patterns.HelperClasses.MyPatternBase;

import com.togethersoft.openapi.ide.IdeAccess;

public class PatternBaseException extends Exception {
    public PatternBaseException(String message) {
        IdeAccess.setErrorMessageInfo(message);
    }
}
