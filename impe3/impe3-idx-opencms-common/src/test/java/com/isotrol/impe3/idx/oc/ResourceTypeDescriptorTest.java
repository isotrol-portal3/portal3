package com.isotrol.impe3.idx.oc;


import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.idx.oc.ResourceTypeDescriptor;


public class ResourceTypeDescriptorTest {

	@Test
	public void fieldTest() {
		ResourceTypeDescriptor.Field f1 = new ResourceTypeDescriptor.Field("name", true, true, "value", null);
		ResourceTypeDescriptor.Field f2 = new ResourceTypeDescriptor.Field("namename", true, true, "value", null);

		Assert.assertNotSame(f1, f2);
	}

}
