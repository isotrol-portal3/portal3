/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.isotrol.impe3.pms.core.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.device.DeviceDTO;
import com.isotrol.impe3.pms.api.device.DeviceSelDTO;
import com.isotrol.impe3.pms.api.device.DeviceTreeDTO;
import com.isotrol.impe3.pms.api.device.DevicesService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for DevicesServiceImpl.
 * @author Andres Rodriguez
 */
public class DevicesServiceImplTest extends MemoryContextTest {
	private DevicesService service;
	private List<DeviceTreeDTO> first;
	private Map<String, DeviceTreeDTO> map;

	private void check(DeviceSelDTO dto) {
		assertNotNull(dto);
		assertNotNull(dto.getId());
		assertNotNull(dto.getName());
		assertNotNull(dto.getType());
	}

	private static void contains(List<DeviceTreeDTO> nodes, DeviceTreeDTO node, int order) {
		assertNotNull(nodes);
		assertTrue(nodes.size() > order);
		DeviceTreeDTO tree = nodes.get(order);
		assertTrue(node == tree);
	}

	private DeviceTreeDTO contains(String id) {
		DeviceTreeDTO node = map.get(id);
		assertNotNull(node);
		return node;
	}

	private DeviceTreeDTO contains(String id, String name) {
		DeviceTreeDTO node = contains(id);
		assertEquals(name, node.getNode().getName());
		return node;
	}

	private void contains(String id, String name, String parent, int order) {
		DeviceTreeDTO node = contains(id, name);
		List<DeviceTreeDTO> list = parent == null ? first : contains(parent).getChildren();
		contains(list, node, order);
	}

	@Before
	public void setUp() {
		service = getBean(DevicesService.class);
	}

	private void loadTree(List<DeviceTreeDTO> nodes) {
		for (DeviceTreeDTO node : nodes) {
			check(node.getNode());
			map.put(node.getNode().getId(), node);
			loadTree(node.getChildren());
		}
	}

	private void loadTree() throws PMSException {
		map = Maps.newHashMap();
		first = service.getDevices();
		loadTree(first);
	}

	/** Tree test. */
	@Test
	public void test() throws PMSException {
		loadTree();
		Assert.assertFalse(map.isEmpty());
	}

	/** Full test. */
	@Test
	public void full() throws PMSException {
		test();
		DeviceSelDTO pd = first.get(0).getNode();
		String pname = pd.getName();
		String pid = pd.getId();
		contains(pid, pname, null, 0);
		final String name = testString();
		DeviceDTO dto = new DeviceDTO();
		dto.setName(name);
		dto.setType(DeviceType.ATOM);
		dto = service.create(dto, null, 1);
		assertNotNull(dto);
		String id = dto.getId();
		assertNotNull(id);
		assertEquals(name, dto.getName());
		loadTree();
		contains(pid, pname, null, 0);
		contains(id, name, null, 1);
		service.move(id, pid, 3);
		loadTree();
		contains(pid, pname, null, 0);
		contains(id, name, pid, 0);
		service.delete(id);
	}

}
