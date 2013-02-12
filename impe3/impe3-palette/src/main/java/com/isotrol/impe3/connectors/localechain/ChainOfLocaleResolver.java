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

package com.isotrol.impe3.connectors.localechain;

import com.isotrol.impe3.api.LocaleResolutionParams;
import com.isotrol.impe3.api.LocaleResolver;
import com.isotrol.impe3.api.ResolvedLocale;

/**
 * Chain of locale resolvers
 * 
 * @author Emilio Escobar Reyero
 */
public class ChainOfLocaleResolver implements LocaleResolver {

	private LocaleResolver firstStep;
	private LocaleResolver secondStep;
	private LocaleResolver thirdStep;
	private LocaleResolver fourthStep;
	private LocaleResolver fifthStep;

	/**
	 * @see com.isotrol.impe3.api.LocaleResolver#resolveLocale(com.isotrol.impe3.api.LocaleResolutionParams)
	 */
	public ResolvedLocale resolveLocale(LocaleResolutionParams params) {

		ResolvedLocale locale;

		locale = firstStep.resolveLocale(params);
		if (locale != null) {
			return locale;
		}
		locale = secondStep.resolveLocale(params);
		if (locale != null) {
			return locale;
		}
		locale = thirdStep.resolveLocale(params);
		if (locale != null) {
			return locale;
		}
		locale = fourthStep.resolveLocale(params);
		if (locale != null) {
			return locale;
		}
		locale = fifthStep.resolveLocale(params);
		if (locale != null) {
			return locale;
		}

		return null;
	}

	public void setFirstStep(LocaleResolver firstStep) {
		this.firstStep = firstStep;
	}

	public void setSecondStep(LocaleResolver secondStep) {
		this.secondStep = secondStep;
	}

	public void setThirdStep(LocaleResolver thirdStep) {
		this.thirdStep = thirdStep;
	}

	public void setFourthStep(LocaleResolver fourthStep) {
		this.fourthStep = fourthStep;
	}

	public void setFifthStep(LocaleResolver fifthStep) {
		this.fifthStep = fifthStep;
	}

}
