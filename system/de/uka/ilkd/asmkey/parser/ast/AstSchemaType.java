// This file is part of KeY - Integrated Deductive Software Design
// Copyright (C) 2001-2005 Universitaet Karlsruhe, Germany
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General Public License. 
// See LICENSE.TXT for details.
//
//

package de.uka.ilkd.asmkey.parser.ast;


import de.uka.ilkd.key.parser.Location;
import de.uka.ilkd.key.parser.ParserException;

/**
 * AST node for schema types. All nodes for schema types are instances of
 * subclasses of this class. See {@link #accept} and {@link
 * AstSchemaType} for more information.
 *
 * @author Hubert Schmid
 */

public abstract class AstSchemaType extends AstType {

    public AstSchemaType(Location location) {
        super(location);
    }


    /** This methods calls the corresponding method in visitor. */
    public abstract void accept(AstSchemaTypeVisitor visitor) throws ParserException;

}
