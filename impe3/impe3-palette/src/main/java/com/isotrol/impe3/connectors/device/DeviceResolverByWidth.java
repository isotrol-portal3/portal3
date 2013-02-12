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

package com.isotrol.impe3.connectors.device;


import java.util.List;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.DeviceInPortal;
import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.ResolvedDevice;


/**
 * Device resolver based in device width.
 * @author Andres Rodriguez
 */
public class DeviceResolverByWidth extends AbstractDeviceResolver {
	private static final Function<DeviceInPortal, Integer> WIDTH = new Function<DeviceInPortal, Integer>() {
		public Integer apply(DeviceInPortal input) {
			return input.getDevice().getWidth();
		}
	};
	private static final Predicate<DeviceInPortal> WITH_WIDTH = Predicates.compose(Predicates.notNull(), WIDTH);
	private static final Ordering<DeviceInPortal> ORDER = Ordering.natural().onResultOf(WIDTH).reverse();

	/**
	 * Constructor.
	 * @param config module configuration.
	 */
	public DeviceResolverByWidth(DeviceResolverModuleConfig config) {
		super(config);
	}

	@Override
	ResolvedDevice resolve(DeviceResolutionParams params, Iterable<DeviceInPortal> devices) {
		final DeviceCapabilities caps = params.getCapabilities();
		if (caps == null || devices == null) {
			return null;
		}
		List<DeviceInPortal> ordered = ORDER.sortedCopy(Iterables.filter(devices, WITH_WIDTH));
		if (ordered == null || ordered.isEmpty()) {
			return null;
		}
		int width = caps.getWidth();
		for (DeviceInPortal dip : ordered) {
			if (width <= dip.getDevice().getWidth()) {
				return params.resolve(dip.getDevice());
			}
		}
		return params.resolve(ordered.get(ordered.size() - 1).getDevice());
	}
}
