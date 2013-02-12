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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.equalTo;
import static com.google.common.collect.Iterables.any;
import static com.google.common.collect.Iterables.transform;

import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.derquinsej.i18n.Localized;
import net.sf.derquinsej.i18n.Unlocalized;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.nr.api.FilterType;


/**
 * Value describing a portal.
 * @author Andres Rodriguez
 */
public final class Portal extends AbstractIdentifiable implements IAModel, WithDeviceProperty {
	private static final String ONLINE_SUFFIX = "_online";
	private static final String OFFLINE_SUFFIX = "_offline";

	private static <K, V> ImmutableMap<K, V> map(Map<K, V> map) {
		if (map == null || map.isEmpty()) {
			return ImmutableMap.of();
		} else {
			return ImmutableMap.copyOf(map);
		}
	}

	private static String base(String base) {
		return checkNotNull(base, "A base name must be provided");
	}

	private static String property(String name) {
		return checkNotNull(name, "A property name must be provided");
	}

	/**
	 * Returns a new builder.
	 * @return A new builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	private static final String DEVICE_ERROR = "The portal default device must be provided";
	private static final String DEF_DEVICE_ERROR = "The portal default device must be a supported device";
	private static final String NAME_ERROR = "The portal name must be provided";
	private static final String CT_ERROR = "The portal content types must be provided";
	private static final String CG_ERROR = "The portal categories must be provided";
	private static final String AL_ERROR = "The default locale must be available";

	/** Engine mode. */
	private final EngineMode mode;
	/** Default device. */
	private final Device device;
	/** Portal name. */
	private final Localized<String> name;
	/** Content types. */
	private final ContentTypes contentTypes;
	/** Categories. */
	private final Categories categories;
	/** Default locale. */
	private final Locale defaultLocale;
	/** Available locales. */
	private final ImmutableSet<Locale> locales;
	/** Bases. */
	private final ImmutableMap<String, URI> bases;
	/** Properties. */
	private final ImmutableMap<String, String> properties;
	/** Include uncategorized. */
	private final boolean uncategorized;
	/** Only due nodes. */
	private final boolean due;
	/** Set filters. */
	private final ImmutableMap<String, FilterType> setFilters;
	/** Devices. */
	private final DevicesInPortal devices;

	/** Constructor. */
	private Portal(Builder b) {
		super(b);
		this.mode = b.mode;
		this.device = b.device;
		this.name = b.name;
		this.contentTypes = b.contentTypes;
		this.categories = b.categories;
		this.defaultLocale = b.defaultLocale;
		this.locales = b.locales;
		this.bases = b.bases;
		this.properties = b.properties;
		this.uncategorized = b.uncategorized;
		this.due = b.due;
		this.setFilters = b.setFilters;
		this.devices = DevicesInPortal.of(b.devices);
	}

	/**
	 * Returns the engine mode.
	 * @return The engine mode.
	 */
	public EngineMode getMode() {
		return mode;
	}

	/**
	 * Returns the portal default device.
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 * Returns the portal name.
	 * @return The portal name.
	 */
	public Localized<String> getName() {
		return name;
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getContentTypes()
	 */
	public ContentTypes getContentTypes() {
		return contentTypes;
	}

	/**
	 * @see com.isotrol.impe3.api.IAModel#getCategories()
	 */
	public Categories getCategories() {
		return categories;
	}

	/**
	 * Returns the default locale.
	 * @return The default locale.
	 */
	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	/**
	 * Returns the available locales.
	 * @return The available locales.
	 */
	public ImmutableSet<Locale> getLocales() {
		return locales;
	}

	/**
	 * Returns a mode dependent key.
	 * @param key Mode independent key.
	 * @return Mode dependent key.
	 */
	private String getMDKey(String key) {
		String mkkey = key + (mode == EngineMode.ONLINE ? ONLINE_SUFFIX : OFFLINE_SUFFIX);
		return mkkey;
	}

	/**
	 * Returns a portal base.
	 * @param base Base name.
	 * @return The base or {@code null} if not found.
	 */
	public URI getBase(String base) {
		return bases.get(base(base));
	}

	/**
	 * Returns a mode-dependent portal base.
	 * @param base Base name.
	 * @return The base or {@code null} if not found.
	 */
	public URI getMDBase(String base) {
		checkNotNull(base);
		String mdbase = getMDBaseName(base);
		final URI u = bases.get(mdbase);
		return u != null ? u : bases.get(base);
	}

	/**
	 * Returns a mode-dependent portal base name.
	 * @param base Mode-independent base name.
	 * @return The mode-dependent portal base name.
	 */
	public String getMDBaseName(String base) {
		return getMDKey(base(base));
	}

	/**
	 * Returns a portal property.
	 * @param name Property name.
	 * @return The property value or {@code null} if not found.
	 */
	public String getProperty(String name) {
		return properties.get(property(name));
	}

	/**
	 * Returns a mode-dependent portal property.
	 * @param name Base name.
	 * @return The base or {@code null} if not found.
	 */
	public String getMDProperty(String name) {
		final String value = properties.get(getMDKey(property(name)));
		return value != null ? value : properties.get(name);
	}

	public boolean isDue() {
		return due;
	}

	public boolean isUncategorized() {
		return uncategorized;
	}

	/**
	 * Returns the set filters.
	 * @return The set filters.
	 */
	public ImmutableMap<String, FilterType> getSetFilters() {
		return setFilters;
	}

	/**
	 * Returns the devices registered in the portal.
	 * @return The devices registered in the portal.
	 */
	public DevicesInPortal getDevices() {
		return devices;
	}

	/**
	 * Builder for Portal.
	 * @author Andres Rodriguez
	 */
	public static final class Builder extends AbstractIdentifiable.Builder<Builder, Portal> {
		/** Engine mode. */
		private EngineMode mode = EngineMode.OFFLINE;
		/** Default device. */
		private Device device;
		/** Portal name. */
		private Localized<String> name;
		/** Content types. */
		private ContentTypes contentTypes;
		/** Categories. */
		private Categories categories;
		/** Default locale. */
		private Locale defaultLocale;
		/** Available locales. */
		private ImmutableSet<Locale> locales;
		/** Bases. */
		private ImmutableMap<String, URI> bases = ImmutableMap.of();
		/** Properties. */
		private ImmutableMap<String, String> properties = ImmutableMap.of();
		/** Include uncategorized. */
		private boolean uncategorized = true;
		/** Only due nodes. */
		private boolean due = true;
		/** Set filters. */
		private ImmutableMap<String, FilterType> setFilters = ImmutableMap.of();
		/** Devices. */
		private Set<DeviceInPortal> devices;

		/** Constructor. */
		private Builder() {
			defaultLocale = new Locale("es");
			locales = ImmutableSet.of(defaultLocale);
		}

		/**
		 * Sets the engine mode.
		 * @param mode The engine mode.
		 * @return This builder.
		 */
		public Builder setMode(EngineMode mode) {
			this.mode = checkNotNull(mode);
			return thisValue();
		}

		/**
		 * Sets the default device.
		 * @param device The default device.
		 * @return This builder.
		 */
		public Builder setDevice(Device device) {
			this.device = checkNotNull(device);
			return thisValue();
		}

		/**
		 * Sets the portal name.
		 * @param mode The portal name.
		 * @return This builder.
		 */
		public Builder setName(Localized<String> name) {
			this.name = checkNotNull(name, NAME_ERROR);
			return thisValue();
		}

		/**
		 * Sets the portal name. An unlocalized value is used.
		 * @param mode The portal name.
		 * @return This builder.
		 */
		public Builder setName(String name) {
			this.name = Unlocalized.of(checkNotNull(name, NAME_ERROR));
			return thisValue();
		}

		/**
		 * Sets the portal content types.
		 * @param mode The portal content types.
		 * @return This builder.
		 */
		public Builder setContentTypes(ContentTypes contentTypes) {
			this.contentTypes = checkNotNull(contentTypes);
			return thisValue();
		}

		/**
		 * Sets the portal categories.
		 * @param mode The portal categories.
		 * @return This builder.
		 */
		public Builder setCategories(Categories categories) {
			this.categories = checkNotNull(categories);
			return thisValue();
		}

		/**
		 * Sets the default locale.
		 * @param mode The default locale.
		 * @return This builder.
		 */
		public Builder setDefaultLocale(Locale defaultLocale) {
			this.defaultLocale = checkNotNull(defaultLocale);
			return thisValue();
		}

		/**
		 * Sets the available locales.
		 * @param mode The available locales.
		 * @return This builder.
		 */
		public Builder setLocales(Iterable<Locale> locales) {
			this.locales = ImmutableSet.copyOf(locales);
			return thisValue();
		}

		/**
		 * Sets the portal bases.
		 * @param mode The portal bases.
		 * @return This builder.
		 */
		public Builder setBases(Map<String, URI> bases) {
			this.bases = map(bases);
			return thisValue();
		}

		/**
		 * Sets the portal properties.
		 * @param mode The portal properties.
		 * @return This builder.
		 */
		public Builder setProperties(Map<String, String> properties) {
			this.properties = map(properties);
			return thisValue();
		}

		/**
		 * Sets whether uncategorized contents are included by default.
		 * @param uncategorized The flag value.
		 * @return This builder.
		 */
		public Builder setUncategorized(boolean uncategorized) {
			this.uncategorized = uncategorized;
			return thisValue();
		}

		/**
		 * Sets whether only due contents are included by default.
		 * @param due The flag value.
		 * @return This builder.
		 */
		public Builder setDue(boolean due) {
			this.due = due;
			return thisValue();
		}

		/**
		 * Sets the portal set filters.
		 * @param filters The portal set filters.
		 * @return This builder.
		 */
		public Builder setSetFilters(Map<String, FilterType> filters) {
			this.setFilters = map(filters);
			return thisValue();
		}

		/**
		 * Sets the portal devices.
		 * @param devices The portal devices.
		 * @return This builder.
		 */
		public Builder setDevices(Set<DeviceInPortal> devices) {
			this.devices = checkNotNull(devices);
			return thisValue();
		}

		/**
		 * Returns the built portal.
		 * @return The built portal.
		 * @throws IllegalStateException in case of an error.
		 */
		protected Portal doGet() {
			return new Portal(this);
		}

		@Override
		protected void checkState() {
			Preconditions.checkState(device != null, DEVICE_ERROR);
			if (devices == null) {
				final DeviceInPortal dip = DeviceInPortal.of(device);
				devices = ImmutableSet.of(dip);
			} else {
				Preconditions.checkState(any(transform(devices, DeviceInPortal.DEVICE), equalTo(device)),
					DEF_DEVICE_ERROR);
			}
			Preconditions.checkState(name != null, NAME_ERROR);
			Preconditions.checkState(contentTypes != null, CT_ERROR);
			Preconditions.checkState(categories != null, CG_ERROR);
			Preconditions.checkState(locales.contains(defaultLocale), AL_ERROR);
		}
	}

}
