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

package com.isotrol.impe3.pms.model;


import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

import com.google.common.base.Objects;


/**
 * An hibernate user type to store a set of columns as a String.
 * @author Andres Rodriguez
 */
public class ColumnsUserType implements UserType {
	/** SQL Types. */
	private static final int[] TYPES = {Types.VARCHAR};

	private static int[] copy(int[] source) {
		if (source == null || source.length == 0) {
			return source;
		}
		final int[] ret = new int[source.length];
		System.arraycopy(source, 0, ret, 0, source.length);
		return ret;
	}

	private static int[] copy(Object source) {
		return copy((int[]) source);
	}

	/** Default constructor. */
	public ColumnsUserType() {
	}

	/**
	 * @see org.hibernate.usertype.UserType#assemble(java.io.Serializable, java.lang.Object)
	 */
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return copy(cached);
	}

	/**
	 * @see org.hibernate.usertype.UserType#deepCopy(java.lang.Object)
	 */
	public Object deepCopy(Object value) throws HibernateException {
		return copy(value);
	}

	/**
	 * @see org.hibernate.usertype.UserType#disassemble(java.lang.Object)
	 */
	public Serializable disassemble(Object value) throws HibernateException {
		return copy(value);
	}

	/**
	 * @see org.hibernate.usertype.UserType#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(Object x, Object y) throws HibernateException {
		return Objects.equal(x, y);
	}

	/**
	 * @see org.hibernate.usertype.UserType#hashCode(java.lang.Object)
	 */
	public int hashCode(Object x) throws HibernateException {
		return x == null ? 0 : x.hashCode();
	}

	/**
	 * @see org.hibernate.usertype.UserType#isMutable()
	 */
	public boolean isMutable() {
		return true;
	}

	/**
	 * @see org.hibernate.usertype.UserType#nullSafeGet(java.sql.ResultSet, java.lang.String[], java.lang.Object)
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		final String text = rs.getString(names[0]);
		if (text == null) {
			return null;
		}
		final String[] split = text.split("-");
		final int n = split.length;
		final int[] widths = new int[n];
		for (int i = 0; i < n; i++) {
			widths[i] = Integer.parseInt(split[i]);
		}
		return widths;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		final String text;
		if (value == null) {
			text = null;
		} else {
			final int[] widths = (int[]) value;
			final int n = widths.length;
			if (n == 0) {
				text = null;
			} else {
				final StringBuilder b = new StringBuilder();
				for (int i = 0; i < n; i++) {
					if (i > 0) {
						b.append('-');
					}
					b.append(widths[i]);
				}
				text = b.toString();
			}

		}
		st.setString(index, text);
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return copy(original);
	}

	public Class<?> returnedClass() {
		return int[].class;
	}

	public int[] sqlTypes() {
		return TYPES;
	}
}
