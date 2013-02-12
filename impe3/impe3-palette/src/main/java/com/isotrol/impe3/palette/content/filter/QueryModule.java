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

package com.isotrol.impe3.palette.content.filter;


import com.isotrol.impe3.api.metadata.Copyright;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.metadata.Version;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.SpringSimple;


/**
 * Content filter module.
 * @author Manuel Ruiz
 */
@Name("M\u00f3dulo de Consultas")
@Description("M\u00f3dulo que exporta componentes para aplicar transformaciones de criterios")
@Copyright("Isotrol, SA. (GPL3)")
@Version("1.0")
@SpringSimple
public interface QueryModule extends Module {

	/** Provided component. */
	@Name("Componente de Consultas")
	@Description("Componente funcional que genera una consulta para aplic\u00e1rsela a un componente que use el ContentLoader")
	QueryComponent query();
	
	/** Provided component. */
	@Name("Componente de consultas con Par\u00e1metro en tiempo de ejecuci\u00e1n")
	@Description("Componente funcional que genera una consulta para aplic\u00e1rsela a un componente que use el ContentLoader")
	ParametrizedQueryComponent parametrizedQuery();
	
	/** Provided component. */
	@Name("Componente de Ordenaciones")
	@Description("Componente funcional que genera una ordenaci\u00f3n para aplic\u00e1rsela a un componente que use el ContentLoader")
	SortComponent sort();
	
	/** Provided component. */
	@Name("Componente de Consulta por Rango")
	@Description("Componente funcional que genera una consulta por rango para aplic\u00e1rsela a un componente que use el ContentLoader")
	RangeComponent range();
	
	/** Provided component. */
	@Name("Componente de Consulta - Primer Resultado")
	@Description("Componente funcional que devuelve el primer resultado encontrado en una consulta")
	QueryFirstComponent queryFirst();
	
	/** Provided component. */
	@Name("Componente de Consulta - P\u00E1gina")
	@Description("Componente funcional que devuelve un listado como resultado de una consulta")
	QueryPageComponent queryPage();
}
