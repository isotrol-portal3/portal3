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
package com.isotrol.impe3.idx.oc;


import java.util.List;
import java.util.Set;



/**
 * Represents a resource type configured by indexer.
 * 
 * @author Alejandro Guerra Cabrera
 */
public class ResourceTypeDescriptor {

	private String name;
	private int id;
	private boolean content;
	private Set<Field> customFields;
	private Set<Groupping> customGroups;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param id
	 * @param content
	 * @param customFields
	 */
	public ResourceTypeDescriptor(String name, int id, boolean content, Set<Field> customFields) {
		this.name = name;
		this.id = id;
		this.content = content;
		this.customFields = customFields;
	}
	
	public ResourceTypeDescriptor(String name, int id, boolean content, Set<Field> customFields,Set<Groupping> customGroups) {
		this.name = name;
		this.id = id;
		this.content = content;
		this.customFields = customFields;
		this.customGroups=customGroups;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public boolean isContent() {
		return content;
	}

	public Set<Field> getCustomFields() {
		return customFields;
	}
	
	public Set<Groupping> getCustomGroups() {
		return customGroups;
	}

	public void setCustomGroups(Set<Groupping> customGroups) {
		this.customGroups = customGroups;
	}


	/**
	 * Represents a index field for a resource type.
	 */
	public static class Field {

		private String name;
		private boolean stored;
		private boolean tokenized;
		private String value;
		private String function;

		public Field(String name, boolean stored, boolean tokenized, String value, String function) {
			this.name = name;
			this.stored = stored;
			this.tokenized = tokenized;
			this.value = value;
			this.function = function;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Field) {
				Field other = (Field) obj;
				return name.equals(other.getName());
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}

		public String getName() {
			return name;
		}

		public boolean isStored() {
			return stored;
		}

		public boolean isTokenized() {
			return tokenized;
		}

		public String getValue() {
			return value;
		}

		public String getFunction() {
			return function;
		}

	}
	
	public static class Groupping {
		private String name;
		private List<Group> groups;
		
		
		
		public Groupping(String name, List<Group> groups) {
			super();
			this.name = name;
			this.groups=groups;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Group> getGroups() {
			return groups;
		}

		public void setGroups(List<Group> groups) {
			this.groups = groups;
		}

		
		
	}
	
	/**
	 * Represents a index group for a resource type.
	 */
	public static class Group {

		private String path;
		private boolean mapped;
		
		public Group(String path, boolean mapped) {
			super();
			this.path = path;
			this.mapped = mapped;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Group) {
				Group other = (Group) obj;
				return path.equals(other.getPath());
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return path.hashCode();
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public boolean isMapped() {
			return mapped;
		}

		public void setMapped(boolean mapped) {
			this.mapped = mapped;
		}

		

	}
	
}
