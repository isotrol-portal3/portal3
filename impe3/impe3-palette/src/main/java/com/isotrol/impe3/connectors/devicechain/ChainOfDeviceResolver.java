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

package com.isotrol.impe3.connectors.devicechain;

import com.isotrol.impe3.api.DeviceResolutionParams;
import com.isotrol.impe3.api.DeviceResolver;
import com.isotrol.impe3.api.ResolvedDevice;

/**
 * Chain of device resolvers
 * 
 * @author Emilio Escobar Reyero
 */
public class ChainOfDeviceResolver implements DeviceResolver {

	private DeviceResolver firstStep;
	private DeviceResolver secondStep;
	private DeviceResolver thirdStep;
	private DeviceResolver fourthStep;
	private DeviceResolver fifthStep;

	/**
	 * @see com.isotrol.impe3.api.DeviceResolver#resolveDevice(com.isotrol.impe3.api.DeviceResolutionParams)
	 */
	public ResolvedDevice resolveDevice(DeviceResolutionParams params) {

		ResolvedDevice device;

		device = firstStep.resolveDevice(params);
		if (device != null) {
			return device;
		}
		device = secondStep.resolveDevice(params);
		if (device != null) {
			return device;
		}
		device = thirdStep.resolveDevice(params);
		if (device != null) {
			return device;
		}
		device = fourthStep.resolveDevice(params);
		if (device != null) {
			return device;
		}
		device = fifthStep.resolveDevice(params);
		if (device != null) {
			return device;
		}

		return null;
	}

	public void setFirstStep(DeviceResolver firstStep) {
		this.firstStep = firstStep;
	}

	public void setSecondStep(DeviceResolver secondStep) {
		this.secondStep = secondStep;
	}

	public void setThirdStep(DeviceResolver thirdStep) {
		this.thirdStep = thirdStep;
	}

	public void setFourthStep(DeviceResolver fourthStep) {
		this.fourthStep = fourthStep;
	}

	public void setFifthStep(DeviceResolver fifthStep) {
		this.fifthStep = fifthStep;
	}

}
