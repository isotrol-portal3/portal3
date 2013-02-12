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

package com.isotrol.impe3.connectors.locale;

import java.util.Locale;

import net.sf.derquinsej.i18n.Locales;

import com.isotrol.impe3.api.LocaleResolutionParams;
import com.isotrol.impe3.api.LocaleResolver;
import com.isotrol.impe3.api.ResolvedLocale;
import com.isotrol.impe3.api.support.WithDefaultLocale;
import com.isotrol.impe3.connectors.Loggers;

/**
 * Fallback locale resolver.
 * 
 * @author Andres Rodriguez Chamorro
 */
public class FallbackLocaleResolver implements LocaleResolver {
	private static final Locale SPANISH = new Locale("es", "ES");

	/** Fallback locale. */
	private Locale locale = SPANISH;

	public FallbackLocaleResolver() {
	}

	public void setConfig(WithDefaultLocale config) {
		if (config != null) {
			final String fallback = config.defaultLocale();
			try {
				this.locale = Locales.fromString(fallback);
			} catch (RuntimeException e) {
				Loggers.connectors().error(
						String.format("Unable to parse fallback locale [%s]",
								fallback));
			}
		}
	}

	/**
	 * @see com.isotrol.impe3.api.LocaleResolver#resolveLocale(com.isotrol.impe3.api.LocaleResolutionParams)
	 */
	public ResolvedLocale resolveLocale(LocaleResolutionParams params) {
		return new ResolvedLocale(params.getPath(), locale,
				params.getParameters());
	}

}
