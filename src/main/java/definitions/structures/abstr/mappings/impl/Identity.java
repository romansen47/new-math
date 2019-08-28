package definitions.structures.abstr.mappings.impl;

import definitions.structures.abstr.mappings.Endomorphism;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Vector;

public abstract class Identity extends LinearMapping implements Endomorphism {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6209038203547448760L;

	public Identity(VectorSpace source) {
		super(source, source);
	}

	@Override
	public Vector get(Vector vec) {
		return vec;
	}

}
