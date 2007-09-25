// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2004 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
package de.uka.ilkd.key.casetool;

/**
 * @deprecated (replaced by Association)
 */
public interface UMLOCLAssociation {
    
    UMLOCLClassifier getSource();
    UMLOCLClassifier getTarget();
    Multiplicity getSourceMultiplicity();
    Multiplicity getTargetMultiplicity();
    String getSourceRole();
    String getTargetRole();

}
