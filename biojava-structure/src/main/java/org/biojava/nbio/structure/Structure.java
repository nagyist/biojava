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
 * Created on 26.04.2004
 * @author Andreas Prlic
 *
 */
package org.biojava.nbio.structure;

import org.biojava.nbio.structure.io.FileConvert;
import org.biojava.nbio.structure.io.PDBFileReader;

import java.io.Serializable;
import java.util.List;


/**
 * Interface for a structure object. Provides access to the data of a PDB file.
 * <p>
 * A structure object allows to access the PDB header information as well
 * as to the data from the ATOM records. The header information is
 * currently available through the following objects:
 * <ul>
 * <li>{@link PDBHeader}</li>
 * <li>{@link DBRef}</li>
 * <li>{@link EntityInfo}</li>
 * </ul>
 *
 * The structure object provides access to the data from the ATOM records through
 * a hierarchy of sub-object:
 * <pre>
 * Structure
 *         |
 *         {@link Chain}
 *             |
 *             {@link Group}
 *                 |
 *                 {@link Atom}
 * </pre>
 *
 * For more documentation on how to work with the Structure API please
 * see <a href="http://biojava.org/wiki/BioJava:CookBook#Protein_Structure" target="_top">
 * http://biojava.org/wiki/BioJava:CookBook#Protein_Structure</a>
 *
 * <p>
 *  The tutorial for the BioJava structure modules can be found at <a href="https://github.com/biojava/biojava3-tutorial/tree/master/structure">github</a>.
 * <hr/>
 * <p>
 * Q: How can I get a Structure object from a PDB file?
 * <p>
 * A:
 * <pre>
 *  Structure loadStructure(String pathToPDBFile) {
 * 		PDBFileReader pdbreader = new PDBFileReader();
 * 		Structure structure = pdbreader.getStructure(pathToPDBFile);
 * 		System.out.println(structure);
 * 		return structure;
 * 	}
 *  </pre>
 *
 * <hr>
 * <p>
 * Q: How can I calculate Phi and Psi angles of AminoAcids?
 * <p>
 * A:
 * <pre>{@code
 *  void calcPhiPsi(Structure structure) {
 *
 * 		// get the first chain from the structure
 * 		Chain chain  = structure.getChain(0);
 *
 * 		// A protein chain consists of a number of groups. These can be either
 * 		// AminoAcid, HetatomImpl or NucleotideImpl groups.
 * 		//
 * 		// Note: BioJava provides access to both the ATOM and SEQRES data in a PDB file.
 * 		// since we are interested in doing calculations here, we only request the groups
 * 		// from the ATOM records
 *
 * 		//  get the Groups of the chain that are AminoAcids.
 * 		List<Group> groups = chain.getAtomGroups(GroupType.AMINOACID);
 *
 * 		AminoAcid a;
 * 		AminoAcid b;
 * 		AminoAcid c;
 * 		for (int i=0; i < groups.size(); i++) {
 * 			// since we requested only groups of type AMINOACID they will always be amino acids
 * 			// Nucleotide and Hetatom groups will not be present in the groups list.
 * 			b = (AminoAcid)groups.get(i);
 * 			double phi = 360.0;
 * 			double psi = 360.0;
 *
 * 			if (i > 0) {
 * 				a = (AminoAcid)groups.get(i-1) ;
 * 				// the Calc class provides utility methods for various calculations on
 * 				// structures, groups and atoms
 * 				phi = Calc.getPhi(a,b);
 * 			}
 * 			if (i < groups.size()-1) {
 * 				c = (AminoAcid)groups.get(i+1) ;
 * 				psi = Calc.getPsi(b, c);
 * 			}
 * 			System.out.print(b.getPDBCode() + " " + b.getPDBName() + ":"  );
 * 			System.out.println(String.format("\tphi: %+7.2f psi: %+7.2f", phi, psi));
 * 		}
 * 	}
 * }</pre>
 * <hr>
 *
 * @author Andreas Prlic
 * @since 1.4
 */
public interface Structure extends Cloneable, Serializable {


	/**
	 * Return an identical copy of this Structure object
	 *
	 * @return identical copy of this Structure object
	 */
	Structure clone();

	/**
	 * String representation of object.
	 */
	@Override
	String toString();

	/**
	 * Set biological name of Structure .
	 *
	 * @param name  a String specifying the biological name of the Structure
	 * @see #getName
	 */
	void setName(String name);

	/**
	 * Get biological name of Structure.
	 *
	 * @return a String representing the biological name of the Structure
	 * @see #setName
	 */
	String getName();

	/**
	 * Get an identifier corresponding to this structure
	 * @return The StructureIdentifier used to create this structure
	 */
	StructureIdentifier getStructureIdentifier();

	/**
	 * Set the identifier corresponding to this structure
	 * @param structureIdentifier the structureIdentifier corresponding to this structure
	 */
	void setStructureIdentifier(StructureIdentifier structureIdentifier);

	/**
	 * Return number of polymer Chains in this Structure for first model.
	 * @return the number of polymer Chains in this Structure
	 */
	int size() ;

	/**
	 * Return number of chains of model.
	 *
	 * @param modelIdx  an int specifying the index of the Model that should be used
	 * @return an int representing the number of Chains in this Model
	 */
	int size(int modelIdx);

	/**
	 * Return the number of models .
	 * In this implementation also XRAY structures have "1 model", since
	 * model is the container for the chains.
	 * to test if a Structure is an NMR structure use {@link #isNmr()}.
	 *
	 * @return an int representing the number of models in this Structure
	 * @see #isNmr()
	 */
	int nrModels() ;

	/**
	 * Test if this structure is an NMR structure.
	 *
	 * @return true if this Structure has been solved by NMR
	 * @see #nrModels()
	 */
	boolean isNmr() ;

	/**
	 * Test if this structure is a crystallographic structure, i.e. it is an asymmetric unit
	 * from which it is possible to reconstruct the crystal lattice given cell parameters and
	 * space group.
	 *
	 * @return true if crystallographic, false otherwise
	 */
	boolean isCrystallographic();

	/**
	 * Add a new model.
	 *
	 * @param model  a List object containing the Chains of the new Model
	 */
	void addModel(List<Chain> model);


	/**
	 * A convenience function if one wants to edit and replace the
	 * models in a structure. Allows to set (replace) the model at position
	 * with the new List of Chains.
	 * @param position starting at 0
	 * @param model list of chains representing a model
	 */
	void setModel(int position, List<Chain> model);

	/**
	 * Retrieve all Chains belonging to a model .
	 * @see #getChains(int modelnr)
	 *
	 * @param modelIdx the model index
	 * @return a List object containing the Chains of Model nr. modelnr
	 */
	List<Chain> getModel(int modelIdx);

	/**
	 * Retrieve all chains for the first model.
	 * This is the same as getChains(0);
	 * @see #getModel(int modelnr)
	 * @see #getChains(int modelnr)
	 *
	 * @return a List object containing the Chains of Model nr. modelnr
	 */
	List<Chain> getChains();

	/**
	 * Set the chains of a structure, if this is a NMR structure,
	 * this will only set model 0.
	 *
	 * @see #setChains(int, List)
	 *
	 * @param chains the list of chains for this structure.
	 */
	void setChains(List<Chain> chains);

	/**
	 * Retrieve all chains of a model.
	 * @see #getModel
	 *
	 * @param modelIdx the model index
	 * @return a List object containing the Chains of Model nr. modelnr
	 */
	List<Chain> getChains(int modelIdx);

	/**
	 * Set the chains for a model
	 * @param chains the chains for a model
	 * @param modelIdx the model index
	 */
	void setChains(int modelIdx, List<Chain> chains);

	/**
	 * Return all polymeric chains for the first model
	 *
	 * @return all polymeric chains.
	 * @since 5.0
	 */
	List<Chain> getPolyChains();

	/**
	 * Return all polymeric chains for the given model index.
	 * @param modelIdx the model index
	 * @return all polymeric chains.
	 * @since 5.0
	 */
	List<Chain> getPolyChains(int modelIdx);

	/**
	 * Return all non-polymeric chains for the first model
	 *
	 * @return all non-polymeric chains.
	 * @since 5.0
	 */
	List<Chain> getNonPolyChains();

	/**
	 * Return all non-polymeric chains for the given model index.
	 *
	 * @param modelIdx the model index
	 * @return all non-polymeric chains.
	 * @since 5.0
	 */
	List<Chain> getNonPolyChains(int modelIdx);

	/**
	 * Return all water chains for the first model
	 * @return
	 * @since 5.0
	 */
	List<Chain> getWaterChains();

	/**
	 * Return all water chains for the given model index
	 * @param modelIdx the model index
	 * @return
	 * @since 5.0
	 */
	List<Chain> getWaterChains(int modelIdx);

	/**
	 * Add a new chain to the first model
	 *
	 * @param chain  a Chain object
	 */
	void addChain(Chain chain);

	/**
	 * Add a new chain to the model specified by the given index
	 *
	 * @param chain    a Chain object
	 * @param modelIdx  an int specifying to which model the Chain should be added
	 */
	void addChain(Chain chain, int modelIdx);

	/**
	 * Retrieve a chain by its index within the Structure .
	 *
	 * @param chainIndex the index of the desired chain in the structure
	 * @return a Chain object
	 */
	Chain getChainByIndex(int chainIndex);

	/**
	 * Retrieve a chain by its indices within the Structure and model.
	 *
	 * @param chainIndex the index of the desired chain in the structure
	 * @param modelIdx the model index
	 * @return a Chain object
	 */
	Chain getChainByIndex(int modelIdx, int chainIndex);

	/**
	 * Check if a chain with the chainId aymId is contained in this structure.
	 *
	 * @param asymId the Id of the chain
	 * @return true if a chain with the id asymId is found
	 */
	boolean hasChain(String asymId);

	/**
	 * Check if a non polymeric chain with chainId asymId is contained in the structure.
	 *
	 * @param asymId the id of the chain
	 * @return true if a nonpolymeric chain with the asymId is found
	 * @since 5.0
	 */
	boolean hasNonPolyChain(String asymId);


	/**
	 * Check if a chain  with chain name authId is contained in the structure
	 *
	 * @param authId the chain name
	 * @return true if a chain with the name authId is found
	 */
	boolean hasPdbChain(String authId) ;

	/**
	 * Request a particular group from a structure.
	 * by default considers only the first model in the structure.
	 * @param authId the name of the chain to use
	 * @param pdbResnum the PDB residue number of the requested group
	 * @return Group the requested Group
	 * @throws StructureException
	 */
	Group findGroup(String authId, String pdbResnum) throws StructureException;

	/**
	 * Request a particular group from a structure.
	 * considers only model nr X. count starts with 0.
	 * @param authId the chain name of the chain to use
	 * @param pdbResnum the PDB residue number of the requested group
	 * @param modelIdx the model index
	 * @return Group the requested Group
	 * @throws StructureException
	 */
	Group findGroup(String authId, String pdbResnum, int modelIdx) throws StructureException;

	/**
	 * Retrieve a Chain (polymeric, non-polymeric or water) based on
	 * the 'internal' chain id (asymId) for the first model
	 * @param asymId the asymId (chainId)
	 * @return
	 * @see #getPolyChain(String)
	 * @see #getNonPolyChain(String)
	 * @see #getWaterChain(String)
	 */
	Chain getChain(String asymId);

	/**
	 * Retrieve a Chain (polymeric, non-polymeric or water) based on
	 * the 'internal' chain id (asymId) for the given model index
	 * @param asymId the asymId (chainId)
	 * @param modelIdx the index of the required model (0-based)
	 * @return
	 * @see #getPolyChain(String, int)
	 * @see #getNonPolyChain(String, int)
	 * @see #getWaterChain(String, int)
	 */
	Chain getChain(String asymId, int modelIdx);

	/**
	 * Retrieve a polymeric Chain based on the 'internal' chain
	 * id (asymId) for the first model
	 *
	 * <p>See {@link #getPolyChainByPDB(String)} for a similar
	 * method using the chain name (authId).
	 * @param asymId the asymId (chainId)
	 * @return a polymeric Chain or null if it can't be found
	 * @since 5.0
	 */
	Chain getPolyChain(String asymId);

	/**
	 * Retrieve a polymeric Chain based on the 'internal' chain
	 * id (asymId) for the given model index
	 *
	 * <p>See {@link #getPolyChainByPDB(String, int)} for a similar
	 * method using the chain name (authId).
	 * @param asymId the asymId (chainId)
	 * @param modelIdx the index of the required model (0-based)
	 * @return a polymeric Chain or null if it can't be found
	 * @since 5.0
	 */
	Chain getPolyChain(String asymId, int modelIdx);

	/**
	 * Retrieve a polymeric Chain based on the 'public' chain
	 * name (authId) for the first model
	 *
	 * <p>See {@link #getPolyChain(String)} for a similar
	 * method using the chain id (asymId).
	 * @param authId the author id (chainName, public chain id)
	 * @return a polymeric Chain or null if it can't be found
	 * @since 5.0
	 */
	Chain getPolyChainByPDB(String authId);

	/**
	 * Retrieve a polymeric Chain based on the 'public' chain
	 * name (authId) for the given model index.
	 *
	 * <p>See {@link #getPolyChain(String, int)} for a similar
	 * method using the chain id (asymId).
	 * @param authId the author id (chainName, public chain id)
	 * @param modelIdx the index of the required model (0-based)
	 * @return a polymeric Chain or null if it can't be found
	 * @since 5.0
	 *
	 */
	Chain getPolyChainByPDB(String authId, int modelIdx);


	/**
	 * Retrieve a non-polymeric Chain based on the 'internal' chain
	 * id (asymId) for the first model
	 * @param asymId the asymId (chainId)
	 * @return a non-polymeric chain or null if it can't be found
	 * @since 5.0
	 */
	Chain getNonPolyChain(String asymId);

	/**
	 * Retrieve a non-polymeric Chain based on the 'internal' chain
	 * id (asymId) for the given model index
	 * @param asymId the asymId (chainId)
	 * @param modelIdx the index of the required model (0-based)
	 * @return a non-polymeric Chain or null if it can't be found
	 * @since 5.0
	 */
	Chain getNonPolyChain(String asymId, int modelIdx);

	/**
	 * Retrieve all non-polymeric Chains corresponding to the given 'public' chain
	 * name (authId) for the first model.
	 * @param authId the author id (chainName, public chain id)
	 * @return a list of non-polymeric Chains, if none found the list will be empty
	 * @since 5.0
	 */
	List<Chain> getNonPolyChainsByPDB(String authId);

	/**
	 * Retrieve all non-polymeric Chains corresponding to the 'public' chain
	 * name (authId) and the given model index.
	 * @param authId the author id (chainName, public chain id)
	 * @param modelIdx the index of the required model (0-based)
	 * @return a list of non-polymeric Chains, if none found the list will be empty
	 * @since 5.0
	 */
	List<Chain> getNonPolyChainsByPDB(String authId, int modelIdx);

	/**
	 * Retrieve a water Chain based on the 'internal' chain id (asymId)
	 * for the first model
	 * @param asymId the asymId (chainId)
	 * @return a water Chain or null if it can't be found
	 * @since 5.0
	 */
	Chain getWaterChain(String asymId);

	/**
	 * Retrieve a water chain based on the 'internal' chain id (asymId)
	 * for the given model index
	 * @param asymId the asymId (chainId)
	 * @param modelIdx the index of the required model (0-based)
	 * @return
	 * @since 5.0
	 */
	Chain getWaterChain(String asymId, int modelIdx);

	/**
	 * Retrieve a water Chain based on the 'public' chain name (authId)
	 * for the first model
	 * @param authId the author id (chainName, public chain id)
	 * @return
	 * @since 5.0
	 */
	Chain getWaterChainByPDB(String authId);

	/**
	 * Retrieve a water Chain based on the 'public' chain name (authId)
	 * for the given model index
	 * @param authId the author id (chainName, public chain id)
	 * @param modelIdx the index of the required model (0-based)
	 * @return
	 * @since 5.0
	 */
	Chain getWaterChainByPDB(String authId, int modelIdx);


	/**
	 * Create a String that contains this Structure's contents in PDB file format.
	 *
	 * @return a String that looks like a PDB file
	 * @see FileConvert
	 */
	String toPDB();

	/**
	 * Create a String that contains this Structure's contents in MMCIF file format.
	 * @return a String representation of the Structure object in mmCIF.
	 */
	String toMMCIF();

	/**
	 * Set the EntityInfo
	 *
	 * @param molList list of entityinfo objects
	 */
	void setEntityInfos(List<EntityInfo> molList);

	/**
	 * Get all the EntityInfo for this Structure.
	 *
	 * @return a list of EntityInfos
	 */
	List<EntityInfo> getEntityInfos();

	/**
	 * Add an EntityInfo to this Structure
	 */
	void addEntityInfo(EntityInfo entityInfo);

	/**
	 * Set the list of database references for this structure
	 * @param dbrefs list of DBRef objects
	 *
	 */
	void setDBRefs(List<DBRef> dbrefs);

	/**
	 * Get the list of database references
	 *
	 * @return list of DBRef objects
	 */
	List<DBRef> getDBRefs();

	/**
	 * Request a particular entity by its entity id (mol id in legacy PDB format)
	 *
	 * @param entityId the number of the entity
	 * @return an entity, or null if the molId was not found
	 */
	EntityInfo getEntityById(int entityId);

	/**
	 * Return the header information for this PDB file.
	 * <b>N.B.</b> Take care when you blindly use the returned object from this method,
	 * because it might be null in some cases.
	 *
	 * @return the PDBHeader object
	 */
	PDBHeader getPDBHeader();

	/**
	 * Return whether or not the entry has an associated journal article
	 * or ation. The JRNL section is not mandatory and thus may not be
	 * present.
	 * @return flag if a JournalArticle has been found.
	 */
	boolean hasJournalArticle();

	/**
	 * Get the associated publication as defined by the JRNL records in a PDB
	 * file.
	 * @return a JournalArticle
	 */
	JournalArticle getJournalArticle();

	/**
	 * Set the associated publication as defined by the JRNL records in a PDB
	 * file.
	 * @param journalArticle a JournalArticle object
	 */
	void setJournalArticle(JournalArticle journalArticle);

	/**
	 * Get the list of disulfide Bonds as they have been defined in the PDB files
	 *
	 * @return a list of Bonds
	 */
	List<Bond> getSSBonds();

	/**
	 * Set the list of SSBonds for this structure
	 *
	 * @param ssbonds
	 */
	void setSSBonds(List<Bond> ssbonds);

	/**
	 * Add a single disulfide Bond to this structure
	 *
	 * @param ssbond a disulfide bond
	 */
	void addSSBond(Bond ssbond);

	/**
	 * Set the the header information for this PDB file
	 *
	 * @param header the PDBHeader object
	 */
	void setPDBHeader(PDBHeader header);

	/**
	 * @param sites the sites to set in the structure
	 */
	void setSites(List<Site> sites);

	/**
	 * @return the sites contained in this structure
	 */
	List<Site> getSites();

	/**
	 * Set a flag to indicate if this structure is a biological assembly
	 * @param biologicalAssembly true if biological assembly, otherwise false
	 * @since 3.2
	 */
	void setBiologicalAssembly(boolean biologicalAssembly);

	/**
	 * Get flag that indicates if this structure is a biological assembly
	 * @return  true if biological assembly, otherwise false
	 * @since 3.2
	 */
	boolean isBiologicalAssembly();

	/**
	 * Set crystallographic information for this structure
	 * @param crystallographicInfo crystallographic information
	 * @since 3.2
	 */
	void setCrystallographicInfo(PDBCrystallographicInfo crystallographicInfo);

	/**
	 * Get crystallographic information for this structure
	 * @return PDBCrystallographicInfo crystallographic information
	 * @since 3.2
	 */
	PDBCrystallographicInfo getCrystallographicInfo();

	/**
	 * Resets all models of this Structure
	 * @since 4.0.1
	 */
	void resetModels();

	/**
	 * Get a string representing this structure's contents. The following places
	 * are searched for a non-null value, with the first being returned:
	 * <ol>
	 * <li>{@link #getStructureIdentifier()}.getIdentifier(), which should give
	 *     the string originally used to create the structure
	 * <li>{@link #getName()}
	 * <li>A combination of {@link #getPDBCode()} with a heuristic description
	 *     of the residue ranges, in {@link SubstructureIdentifier} format.
	 * </ol>
	 * @return A {@link SubstructureIdentifier}-format string describing the residue ranges in this structure
	 * @since The behavior of this method changed in BioJava 4.2. Previously it
	 *  returned the same value as {@link #getPDBCode()}
	 */
	String getIdentifier();

	/**
	 * Get PDB code of structure.
	 *
	 * @return a String representing the PDBCode value
	 * @see #setPDBCode
	 * @deprecated use {@link #getPdbId()} to get a {@link PdbId} object or getPdbId().getId() to get a {@link String}
	 */
	@Deprecated
	String getPDBCode () ;

	/**
	 * Set PDB code of structure .
	 *
	 * @param pdb_id  a String specifying the PDBCode
	 * @see #getPDBCode
	 * @deprecated use {@link #setPdbId(PdbId)}
	 */
	@Deprecated
	void setPDBCode (String pdb_id);

	/**
	 * Returns the PDB identifier associated with this StructureIdentifier.
	 * @return the {@link PdbId} object
	 * @since 6.0.0
	 */
	PdbId getPdbId();
	
	/**Sets the {@link PdbId} identifier associated with this structure.
	 * @param pdbId the {@link PdbId} identifier object to set
	 * @since 6.0.0
	 */
	void setPdbId(PdbId pdbId);

}
