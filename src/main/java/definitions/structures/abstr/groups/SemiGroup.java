/**
 * 
 */
package definitions.structures.abstr.groups;

import java.io.Serializable;

import definitions.Proceed;
import definitions.settings.XmlPrintable;
import settings.Measurable;

/**
 * @author RoManski
 *
 *         A semi group G is a special monoid with an Element called identity
 *         element. The mappings
 *
 *         alpha: Vector vec -> product(Vector vec,identityElement) beta: Vector
 *         vec -> product(identityElement,Vector vec)
 *
 *         are identically equal to the identity mapping on G.
 */
public interface SemiGroup extends Serializable, XmlPrintable {

	/**
	 * Getter for the order of the monoid - the amount of elements.
	 * 
	 * @return null, if infinitely many, order otherwise.
	 */
	Integer getOrder();

	/**
	 * the operation on the monoid.
	 * 
	 * @param first  first monoid element
	 * @param second second monoid element
	 * @return product of both of them
	 */
	@Proceed
	@Measurable
	MonoidElement operation(MonoidElement first, MonoidElement second);

}
