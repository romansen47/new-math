package definitions.structures.dynamicsystems;

import definitions.structures.abstr.algebra.monoids.MonoidElement;
import definitions.structures.abstr.algebra.monoids.OrderedMonoid;
import definitions.structures.abstr.algebra.semigroups.impl.Naturals;
import definitions.structures.abstr.vectorspaces.VectorSpace;
import definitions.structures.abstr.vectorspaces.vectors.Function;

public class DiscreetDynamicSystem implements DynamicSystem {

	final private VectorSpace phaseSpace;
	final private Function evolutionOperator;

	public DiscreetDynamicSystem(final VectorSpace phaseSpace, final Function evolutionOperator) {
		this.phaseSpace = phaseSpace;
		this.evolutionOperator = evolutionOperator;
	}

	@Override
	public Function getEvolutionOperator(final MonoidElement timeelement) {
		return this.evolutionOperator;
	}

	@Override
	public VectorSpace getPhaseSpace() {
		return this.phaseSpace;
	}

	@Override
	public OrderedMonoid getTimeSpace() {
		return Naturals.getInstance();
	}

	@Override
	public String toXml() {
		return "discreet dynamic system.";
	}

}
