/**
 * 
 */
package definitions.structures.abstr.groups;

/**
 * @author ro
 *
 *         A Group G is a semi group, such that for every group element a there
 *         exists an inverse element
 *
 *         b = getInverseElement(a),
 *
 *
 *         such that
 * 
 *         product(a,b) = product(b,a) = getIdentityElement().
 *
 */
public interface Group extends SemiGroup {

	/**
	 * Getter for the (by definition existing) inverse of an element within the
	 * group.
	 * 
	 * @param element the input
	 * @return the inverse element of the input within the group.
	 */
	GroupElement getInverseElement(GroupElement element);

}
