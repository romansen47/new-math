/**
 * 
 */
package definitions.structures.abstr.groups.impl;

import org.junit.Test;

import definitions.structures.abstr.groups.Group;
import definitions.structures.abstr.groups.GroupElement;

/**
 * @author RoManski
 *
 */
public class IntegersTest {

	final Integers group = Integers.getInstance();

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.groups.impl.Integers#getInstance()}.
	 */
	@Test
	public final void testGetInstance() {
		final Group ans = this.group;
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.groups.impl.Integers#getOrder()}.
	 */
	@Test
	public final void testGetOrder() {
		final Integer ans = this.group.getOrder();
	}

	/**
	 * Test method for
	 * {@link definitions.structures.abstr.groups.impl.Integers#operation(definitions.structures.abstr.groups.GroupElement, definitions.structures.abstr.groups.GroupElement)}.
	 */
	@Test
	public final void testOperation() {
		final GroupElement a = this.group.get(10);
		final GroupElement b = this.group.get(10);
		final GroupElement c = this.group.operation(a, b);
	}

}
