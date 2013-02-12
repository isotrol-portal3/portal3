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

package com.isotrol.impe3.hib.query;


import java.util.EnumMap;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.google.common.collect.Maps;
import com.isotrol.impe3.dto.StringFilterDTO;
import com.isotrol.impe3.dto.StringMatchMode;


/**
 * Support class for criteria building using filters.
 * @author Andres Rodriguez.
 */
public final class FilterSupport {
	/** Not instantiable. */
	private FilterSupport() {
		throw new AssertionError();
	}

	private static final EnumMap<StringMatchMode, MatchMode> MATCH_MAP;

	static {
		MATCH_MAP = Maps.newEnumMap(StringMatchMode.class);
		MATCH_MAP.put(StringMatchMode.PREFIX, MatchMode.START);
		MATCH_MAP.put(StringMatchMode.SUFFIX, MatchMode.END);
		MATCH_MAP.put(StringMatchMode.IN, MatchMode.ANYWHERE);
		MATCH_MAP.put(StringMatchMode.EXACT, MatchMode.EXACT);
	}

	/**
	 * Adds a restriction. The restriction is only added if the filter and the mode are not {@code null}.
	 * @param c Criteria.
	 * @param property Property name.
	 * @param filter Filter value.
	 * @param mode Match mode.
	 * @return The criteria for method chaining.
	 */
	public static Criteria add(Criteria c, String property, String filter, StringMatchMode mode) {
		if (filter != null && mode != null) {
			c.add(Restrictions.ilike(property, filter, MATCH_MAP.get(mode)));
		}
		return c;
	}

	/**
	 * Adds a restriction. The restriction is only added if the filter and the mode are not {@code null}.
	 * @param c Criteria.
	 * @param property Property name.
	 * @param filter Filter value.
	 * @param mode Match mode to force.
	 * @return The criteria for method chaining.
	 */
	public static Criteria add(Criteria c, String property, StringFilterDTO filter, StringMatchMode mode) {
		if (filter != null && mode != null) {
			return add(c, property, filter.getValue(), mode);
		}
		return c;
	}

	/**
	 * Adds a restriction. The restriction is only added if the filter and the mode are not {@code null}.
	 * @param c Criteria.
	 * @param property Property name.
	 * @param filter Filter value.
	 * @return The criteria for method chaining.
	 */
	public static Criteria add(Criteria c, String property, StringFilterDTO filter) {
		if (filter != null) {
			return add(c, property, filter.getValue(), filter.getMatchMode());
		}
		return c;
	}

	/**
	 * Creates a criterion. If filter is {@code null} then returns {@code null}.
	 * @param property Property name.
	 * @param filter Filter value.
	 * @return The criterion for method chaining.
	 */
	public static Criterion criterion(String property, StringFilterDTO filter) {
		if (filter == null) {
			return null;
		}
		return criterion(property, filter.getValue(), filter.getMatchMode());
	}

	/**
	 * Creates a criterion. If filter or mode is {@code null} then returns {@code null}.
	 * @param property Property name.
	 * @param filter Filter value.
	 * @param mode Match mode to force.
	 * @return The criterion for method chaining.
	 */
	public static Criterion criterion(String property, StringFilterDTO filter, StringMatchMode mode) {
		if (filter == null || mode == null) {
			return null;
		}
		return criterion(property, filter.getValue(), mode);
	}

	/**
	 * Creates a criterion. If filter or mode is {@code null} then returns {@code null}.
	 * @param property Property name.
	 * @param filter Filter value.
	 * @param mode Match mode to force.
	 * @return The criterion for method chaining.
	 */
	public static Criterion criterion(String property, String filter, StringMatchMode mode) {
		if (filter == null || mode == null) {
			return null;
		}
		return Restrictions.ilike(property, filter, MATCH_MAP.get(mode));
	}

	/**
	 * Adds an or criteria.
	 * @param c Criteria.
	 * @param values The criterions.
	 * @return The criteria for method chaining.
	 */
	public static Criteria or (Criteria c, Criterion ... values) {
		boolean added = false;
		final Disjunction d = Restrictions.disjunction();
		
		for (Criterion value : values) {
			if (value != null) {
				d.add(value);
				added = true;
			}
		}
		
		if (added) {
			c.add(d);
		}
		
		return c;
	}
	
	/**
	 * Creates an or criteria.
	 * @param c Criteria.
	 * @param p1 Property name.
	 * @param f1 Filter value.
	 * @param p2 Property name.
	 * @param f2 Filter value.
	 * @return The criteria for method chaining.
	 */
	public static Criteria or(Criteria c, String p1, StringFilterDTO f1, String p2, StringFilterDTO f2) {
		return or(c, criterion(p1, f1), criterion(p2, f2));
	}

	/**
	 * Creates an or criteria.
	 * @param c Criteria.
	 * @param p1 Property name.
	 * @param f1 Filter value.
	 * @param p2 Property name.
	 * @param f2 Filter value.
	 * @param p3 Property name.
	 * @param f3 Filter value.
	 * @return The criteria for method chaining.
	 */
	public static Criteria or(Criteria c, String p1, StringFilterDTO f1, String p2, StringFilterDTO f2, String p3,
		StringFilterDTO f3) {
		return or(c, criterion(p1, f1), criterion(p2, f2), criterion(p3, f3));
	}

	/**
	 * Creates an or criteria.
	 * @param c Criteria.
	 * @param p1 Property name.
	 * @param f1 Filter value.
	 * @param p2 Property name.
	 * @param f2 Filter value.
	 * @param p3 Property name.
	 * @param f3 Filter value.
	 * @param p4 Property name.
	 * @param f4 Filter value.
	 * @return The criteria for method chaining.
	 */
	public static Criteria or(Criteria c, String p1, StringFilterDTO f1, String p2, StringFilterDTO f2, String p3,
		StringFilterDTO f3, String p4, StringFilterDTO f4) {
		return or(c, criterion(p1, f1), criterion(p2, f2), criterion(p3, f3), criterion(p4, f4));
	}

}
