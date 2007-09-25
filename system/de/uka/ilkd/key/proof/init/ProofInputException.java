// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//
package de.uka.ilkd.key.proof.init;


/** Reading prover input failed
 */
public class ProofInputException extends Exception {

    public static final ProofInputException USER_ABORT_EXCEPTION =
        new ProofInputException("User cancelled problem loading.");

    String reason;

    public ProofInputException(Exception e) {
        super(e.getMessage());
        reason = e.toString();
        initCause(e);
    }

    public ProofInputException(String s) {
        super(s);
        reason = s;
    }

    public String toString() {
        return reason;
    }

}
