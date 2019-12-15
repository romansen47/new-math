package definitions.structures.abstr.vectorspaces;

import definitions.structures.abstr.groups.Group;
import definitions.structures.abstr.groups.Monoid;
import settings.annotations.Proceed;

/**
 * 
 * @author ro
 *
 *         A ring is a group R,such that r without the identity element is a
 *         monoid M.
 * 
 *         Moveover, the operation-mappings must collaborate in the manner of
 *         associativity. That means,
 * 
 *         for every ring element a, every ring element b and every monoid
 *         element c we have
 * 
 *         M.operation(R.operation(a, b), c) =
 *         R.operation(M.operation(a,c),M.operation(b,c))
 *
 */
public interface Ring extends Group {

	/**
	 * Getter for the multiplicative monoid of the ring.
	 * 
	 * @return the multiplicative monoid of the ring
	 */
	@Proceed
	Monoid getMuliplicativeMonoid();

}
