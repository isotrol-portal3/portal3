package com.isotrol.impe3.idx.oc;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.idx.XML;


/**
 * OpenCms 7 Content DTO
 * 
 * @author Alejandro Guerra Cabrera
 * @author Emilio Escobar Reyero
 * @modified Juan Manuel Valverde Ramírez
 */
public class OpenCmsContent {

	private final String id;
	private final String path;
	private final XML xml;
	private final Date dateCreated;
	private final Date dateLastModified;
	private final Date dateReleased;
	private final Date dateExpired;
	private final int state;
	private final int type;
	private final ImmutableList<String> channels;
	private final ImmutableMap<String, String> properties;
	/**
	 * Lista con el texto crudo de los ficheros adjuntos.
	 */
	private final List<String> attached;

	private OpenCmsContent(OpenCmsContentBuilder builder) {
		this.id = builder.id;
		this.path = builder.path;
		this.xml = builder.xml;
		this.dateCreated = builder.dateCreated;
		this.dateLastModified = builder.dateLastModified;
		this.dateReleased = builder.dateReleased;
		this.dateExpired = builder.dateExpired;
		this.state = builder.state;
		this.type = builder.type;
		this.channels = ImmutableList.copyOf(builder.channels);
		this.properties = ImmutableMap.copyOf(builder.properties);
		this.attached = builder.attached;
	}

	public static OpenCmsContentBuilder builder() {
		return new OpenCmsContentBuilder();
	}

	public String getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public List<String> getChannels() {
		return channels;
	}

	public int getState() {
		return state;
	}

	public int getType() {
		return type;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public XML getXml() {
		return xml;
	}

	public Date getDateReleased() {
		return dateReleased;
	}

	public Date getDateExpired() {
		return dateExpired;
	}

	/**
	 * @return the attached
	 */
	public final List<String> getAttached() {
		return this.attached;
	}

	/**
	 * Clase Interna
	 */
	public static class OpenCmsContentBuilder {
		private String id;
		private String path;
		private XML xml;
		private Date dateCreated;
		private Date dateLastModified;
		private Date dateReleased;
		private Date dateExpired;
		private List<String> channels = new ArrayList<String>();
		private int state;
		private int type;
		private Map<String, String> properties = new HashMap<String, String>();
		/**
		 * Lista con el texto crudo de los ficheros adjuntos.
		 */
		private List<String> attached = new ArrayList<String>();

		/**
		 * Adds or modifies a property.
		 * 
		 * @param key property key
		 * @param value property value
		 */
		public OpenCmsContentBuilder addProperty(String key, String value) {
			this.properties.put(key, value);
			return this;
		}

		/**
		 * Adds a channel in channels list.
		 * 
		 * @param channel channel id.
		 */
		public OpenCmsContentBuilder addChannel(String channel) {
			this.channels.add(channel);
			return this;
		}

		public OpenCmsContentBuilder setDateReleased(Date dateReleased) {
			this.dateReleased = dateReleased;
			return this;
		}

		public OpenCmsContentBuilder setProperties(Map<String, String> properties) {
			this.properties = properties;
			return this;
		}

		public OpenCmsContentBuilder setDateExpired(Date dateExpired) {
			this.dateExpired = dateExpired;
			return this;
		}

		public OpenCmsContentBuilder setState(int estate) {
			this.state = estate;
			return this;
		}

		public OpenCmsContentBuilder setType(int type) {
			this.type = type;
			return this;
		}

		public OpenCmsContentBuilder setPath(String path) {
			this.path = path;
			return this;
		}

		public OpenCmsContentBuilder setXml(XML xml) {
			this.xml = xml;
			return this;
		}

		public OpenCmsContentBuilder setDateCreated(Date dateCreated) {
			this.dateCreated = dateCreated;
			return this;
		}

		public OpenCmsContentBuilder setDateLastModified(Date dateLastModified) {
			this.dateLastModified = dateLastModified;
			return this;
		}

		public OpenCmsContentBuilder setId(String id) {
			this.id = id;
			return this;
		}

		public int getType() {
			return this.type;
		}

		public OpenCmsContent build() {
			return new OpenCmsContent(this);
		}

		/**
		 * @return the attached
		 */
		public final List<String> getAttached() {
			return this.attached;
		}

		/**
		 * @param attached the attached to set
		 */
		public final OpenCmsContentBuilder addAttached(String attached) {
			this.attached.add(attached);
			return this;
		}
	}

}
