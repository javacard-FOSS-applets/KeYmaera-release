// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2009 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//

package de.uka.ilkd.key.logic.op;

import de.uka.ilkd.key.collection.ImmutableArray;
import de.uka.ilkd.key.logic.Name;
import de.uka.ilkd.key.logic.sort.Sort;


public class RigidFunction extends Function {
	/**
	 * Creates a rigid function with given signature.
	 * @param name
	 * @param sort
	 * @param argSorts
	 */
	public RigidFunction(Name name, Sort sort, Sort[] argSorts) {
	    super(name, sort, argSorts);
	}

	/**
	 * Creates a rigid function with given signature.
	 * @param name
	 * @param sort
	 * @param argSorts
	 */
	public RigidFunction(Name name, Sort sort, ImmutableArray<Sort> argSorts) {
	    super(name, sort, argSorts);            
	}
}
