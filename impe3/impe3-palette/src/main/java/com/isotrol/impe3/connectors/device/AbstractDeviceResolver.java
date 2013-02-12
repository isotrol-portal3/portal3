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


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.isotrol.impe3.api.DeviceInPortal;
import com.isotrol.impe3.api.DeviceNameUse;
import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.DeviceResolver;
import com.isotrol.impe3.api.DeviceType;
import com.isotrol.impe3.api.DevicesInPortal;
import com.isotrol.impe3.api.ResolvedDevice;


/**
 * Abstract class for device resolvers in the module.
 * @author Andres Rodriguez
 */
abstract class AbstractDeviceResolver implements DeviceResolver {
	/** Include devices with name resolving. */
	private final boolean uses;
	/** Device filter. */
	private final Predicate<DeviceInPortal> filter;

	AbstractDeviceResolver(DeviceResolverModuleConfig config) {
		final boolean html;
		final boolean xhtml;
		final boolean others;
		if (config == null) {
			uses = false;
			html = true;
			xhtml = true;
			others = false;
		} else {
			uses = config.includeDeviceNameUses();
			html = config.includeHTML();
			xhtml = config.includeXHTML();
			others = config.includeOthers();
		}
		if (html && xhtml && others) {
			this.filter = Predicates.alwaysTrue();
		} else {
			this.filter = new Predicate<DeviceInPortal>() {
				public boolean apply(DeviceInPortal input) {
					final DeviceType type = input.getDevice().getType();
					final boolean isHtml = type == DeviceType.HTML;
					final boolean isXHtml = type == DeviceType.XHTML;
					return (html && isHtml) || (xhtml && isXHtml) || (others && !isHtml && !isXHtml);
				}
			};
		}
	}

	/**
	 * @see com.isotrol.impe3.api.DeviceResolver#resolveDevice(com.isotrol.impe3.api.DeviceResolutionParams)
	 */
	public final ResolvedDevice resolveDevice(DeviceResolutionParams params) {
		DevicesInPortal dips = params.getPortal().getDevices();
		if (!uses) {
			dips = dips.filterByUse(DeviceNameUse.NONE);
		}
		return resolve(params, Iterables.filter(dips.values(), filter));
	}

	abstract ResolvedDevice resolve(DeviceResolutionParams params, Iterable<DeviceInPortal> devices);
}
