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

package com.isotrol.impe3.tickets.server;


import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.junit.Test;

import com.isotrol.impe3.tickets.server.support.SessionFactoryBean;



/**
 * Fake tests used to print DB schemas. Upload to version control with "abstract" modifier uncommented.
 * @author Andres Rodriguez
 */
public class SchemaTest extends MemoryContextTest {
	private void showSchema(Dialect dialect) {
		String prefix = "\n\n***** " + dialect.getClass().getSimpleName() + " ***: ";
		System.out.println(prefix + "START");
		String[] script = ((SessionFactoryBean) getBean("&sessionFactory")).getConfiguration()
			.generateSchemaCreationScript(dialect);
		for (String s : script) {
			System.out.println(s);
		}
		System.out.println(prefix + "END");
	}

	@Test
	public void oracle() {
		showSchema(new Oracle10gDialect());
	}

	@Test
	public void mysql() {
		showSchema(new MySQL5InnoDBDialect());
	}

}
