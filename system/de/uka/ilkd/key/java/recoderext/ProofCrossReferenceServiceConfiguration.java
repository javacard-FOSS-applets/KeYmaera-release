// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

// This file is part of the RECODER library and protected by the LGPL.
package de.uka.ilkd.key.java.recoderext;

import recoder.ProgramFactory;
import de.uka.ilkd.key.util.KeYExceptionHandler;

public class ProofCrossReferenceServiceConfiguration
    extends KeYCrossReferenceServiceConfiguration {

    public ProofCrossReferenceServiceConfiguration(KeYExceptionHandler keh) {
	super(keh);
    }

    /** we need another factory for some new program elements */
    protected ProgramFactory makeProgramFactory() {
	return ProofJavaProgramFactory.getInstance();
    }

}

