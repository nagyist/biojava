/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 * Created on Jun 30, 2010
 * Author: ap3
 *
 */

package org.biojava.nbio.structure.scop;

import java.io.Serializable;

/** The various categories provided by SCOP.
 *
 * The SCOP node types are 'cl' (Class),
 * 'cf' (Fold), 'sf' (Superfamily), 'fa' (Family), 'dm' (Domain),
 *  'sp' (Species), 'px' (protein).
 *
 * @author Andreas Prlic
 *
 */
public enum ScopCategory implements Serializable
{

	Class,Fold, Superfamily,  Family, Domain, Species, Px;

	public static ScopCategory fromString(String type){
		if ( "cl".equals(type))
			return Class;
		else if ( "cf".equals(type))
			return Fold;
		else if ( "sf".equals(type))
			return Superfamily;
		else if ( "fa".equals(type))
			return Family;
		else if ( "dm".equals(type))
			return Domain;
		else if ( "sp".equals(type))
			return Species;
		else
			return Px;
	}

	@Override
public String toString(){
		switch (this) {
			case Class:
				return "cl";

			case Fold:
				return "cf";

			case Superfamily:
				return "sf";

			case Family:
				return "fa";

			case Domain:
				return "dm";

			case Species:
				return "sp";

			 default:
				 return "px";
		}
	}
}
