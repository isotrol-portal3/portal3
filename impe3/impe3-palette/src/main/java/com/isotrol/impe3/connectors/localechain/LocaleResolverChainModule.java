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

import com.isotrol.impe3.api.LocaleResolver;
import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Copyright;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.metadata.Version;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.Path;

/**
 * Locale Resolver Chain Module
 * @author Emilio Escobar Reyero
 */
@Path("localechain.xml")
@Bundle
@Name("name")
@Description("desc")
@Copyright("Isotrol, SA. (GPL3)")
@Version("1.0")
public interface LocaleResolverChainModule extends Module{
	/** Provided service. */
	@Name("resolver")
	LocaleResolver service();

	@Name("resolver.first")
	void firstStep(LocaleResolver resolver);
	
	@Name("resolver.second")
	void secondStep(LocaleResolver resolver);

	@Name("resolver.third")
	void thirdStep(LocaleResolver resolver);

	@Name("resolver.fourth")
	void fourthStep(LocaleResolver resolver);

	@Name("resolver.fifth")
	void fifthStep(LocaleResolver resolver);
}
