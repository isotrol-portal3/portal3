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

package com.isotrol.impe3.mappings;


import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import net.derquinse.common.log.ContextLog;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;


/**
 * Criteria utils for evaluate mappins
 * @author Emilio Escobar Reyero
 */
abstract class Criteria {
	private static final ContextLog LOG = ContextLog.of("com.isotrol.impe3.mapping");

	/**
	 * Evaluate if a content matches with a mapping.
	 * @param cnt local content type
	 * @param path local content path
	 * @param cats local categories
	 * @return true if matches
	 */
	public abstract boolean evaluate(final String cnt, final String path, final Set<String> cats, final Document xml);

	/**
	 * Create a new criteria or
	 * @param right right value
	 * @return criteria with this || right
	 */
	public Criteria or(final Criteria right) {
		return or(this, right);
	}

	/**
	 * Create a new criteria or
	 * @param type mapping type
	 * @param mapped mapping string
	 * @return criteria with this || simple
	 */
	public Criteria or(final MappedType type, final String mapped) {
		return or(this, simple(type, mapped));
	}

	/**
	 * Create a new criteria and
	 * @param right right value
	 * @return criteria with this && right
	 */
	public Criteria and(final Criteria right) {
		return and(this, right);
	}

	/**
	 * Create a new criteria and
	 * @param type mapping type
	 * @param mapped mapping string
	 * @return criteria with this && simple
	 */
	public Criteria and(final MappedType type, final String mapped) {
		return and(this, simple(type, mapped));
	}

	/**
	 * Create a new criteria not
	 * @return criteria ! this
	 */
	public Criteria not() {
		return not(this);
	}

	/**
	 * Create a base criteria
	 * @param type mapping type
	 * @param mapped mapping string
	 * @return base criteria
	 */
	public static Criteria simple(final MappedType type, final String mapped) {
		return new SIMPLE(type, mapped);
	}

	/**
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Criteria or(final Criteria left, final Criteria right) {
		return new OR(left, right);
	}

	/**
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	public static Criteria and(final Criteria left, final Criteria right) {
		return new AND(left, right);
	}

	/**
	 * 
	 * @param cri
	 * @return
	 */
	public static Criteria not(final Criteria cri) {
		return new NOT(cri);
	}

	/**
	 * Simple criteria (base)
	 * @author Emilio Escobar Reyero
	 */
	private static final class SIMPLE extends Criteria {
		private final MappedType type;
		private final String mapped;

		private SIMPLE(final MappedType type, final String mapped) {
			this.mapped = mapped;
			this.type = type;
		}

		/**
		 * 
		 * @see com.isotrol.impe3.mappings.Criteria#evaluate(java.lang.String, java.lang.String, java.util.Set)
		 */
		@Override
		public boolean evaluate(final String cnt, final String path, final Set<String> cats, final Document xml) {
			boolean ok = false;

			try {
				if (MappedType.CNT.equals(type)) {
					ok = mapped.equals(cnt);
				} else if (MappedType.CAT.equals(type)) {
					Iterator<String> it = cats.iterator();
					while (!ok && it.hasNext()) {
						ok = Pattern.matches(mapped, it.next());
					}
				} else if (MappedType.XML.equals(type)) {

					final int ini = mapped.indexOf("{");
					final int end = mapped.indexOf("}");

					if (ini == 0 && end > ini) {
						final String xpath = mapped.substring(ini + 1, end);
						final String value = end == mapped.length() ? null : mapped.substring(end + 1);

						final Nodes nodes = xml.query(xpath);

						if (value != null && !"".equals(value.trim())) {
							if (nodes != null && nodes.size() > 0) {
								for (int i = 0; i < nodes.size() && !ok; i++) {
									final Node node = nodes.get(i);

									if (node.getValue() != null && value.equals(node.getValue().trim())) {
										ok = true;
									}
								}
							}
						} else {
							ok = nodes != null && nodes.size() > 0;
						}
					}
				} else {
					ok = Pattern.matches(mapped, path);
				}
			} catch (Exception e) {
				LOG.error(e, "Unable to evaluate criteria. Content Type: [%s]. Path: [%s]. Categories %s", cnt, path,
					cats);

			}
			return ok;
		}
	}

	/**
	 * 
	 * @author Emilio Escobar Reyero
	 */
	private static final class OR extends Criteria {
		private final Criteria left;
		private final Criteria right;

		private OR(final Criteria left, final Criteria right) {
			this.left = left;
			this.right = right;
		}

		/**
		 * 
		 * @see com.isotrol.impe3.mappings.Criteria#evaluate(java.lang.String, java.lang.String, java.util.Set)
		 */
		@Override
		public boolean evaluate(final String cnt, final String path, final Set<String> cats, final Document xml) {
			return left.evaluate(cnt, path, cats, xml) || right.evaluate(cnt, path, cats, xml);
		}
	}

	/**
	 * 
	 * @author Emilio Escobar Reyero
	 */
	private static final class AND extends Criteria {
		private final Criteria left;
		private final Criteria right;

		private AND(final Criteria left, final Criteria right) {
			this.left = left;
			this.right = right;
		}

		/**
		 * 
		 * @see com.isotrol.impe3.mappings.Criteria#evaluate(java.lang.String, java.lang.String, java.util.Set)
		 */
		@Override
		public boolean evaluate(final String cnt, final String path, final Set<String> cats, final Document xml) {
			return left.evaluate(cnt, path, cats, xml) && right.evaluate(cnt, path, cats, xml);
		}
	}

	/**
	 * 
	 * @author Emilio Escobar Reyero
	 */
	private static final class NOT extends Criteria {
		private final Criteria cri;

		private NOT(final Criteria cri) {
			this.cri = cri;
		}

		/**
		 * 
		 * @see com.isotrol.impe3.mappings.Criteria#evaluate(java.lang.String, java.lang.String, java.util.Set)
		 */
		@Override
		public boolean evaluate(final String cnt, final String path, final Set<String> cats, final Document xml) {
			return !cri.evaluate(cnt, path, cats, xml);
		}
	}
}
