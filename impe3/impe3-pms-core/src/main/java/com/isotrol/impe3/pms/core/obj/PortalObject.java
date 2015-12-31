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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Maps.filterKeys;
import static com.isotrol.impe3.pms.core.obj.MessageMappers.transformSE;
import static com.isotrol.impe3.pms.core.obj.ObjectFunctions.NAME2DTO;
import static java.util.Collections.unmodifiableMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.api.DeviceInPortal;
import com.isotrol.impe3.api.DeviceRouter;
import com.isotrol.impe3.api.Devices;
import com.isotrol.impe3.api.ETagMode;
import com.isotrol.impe3.api.LocaleRouter;
import com.isotrol.impe3.api.Name;
import com.isotrol.impe3.api.PageResolver;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.core.BaseModel;
import com.isotrol.impe3.core.PortalCacheModel;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.pbuf.portal.PortalProtos.BasesPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PortalConfigurationPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PortalNamePB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PropertiesPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.SetPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.SetPB.SetTypePB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.SetsPB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageDeviceDTO;
import com.isotrol.impe3.pms.api.portal.DeviceInPortalDTO;
import com.isotrol.impe3.pms.api.portal.DeviceInPortalTreeDTO;
import com.isotrol.impe3.pms.api.portal.PortalCacheDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalInheritableFlag;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.core.CacheKey;
import com.isotrol.impe3.pms.core.PortalTxProvider;
import com.isotrol.impe3.pms.core.support.AbstractSimpleValueLoader;
import com.isotrol.impe3.pms.core.support.Functions;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.MoreLocales;
import com.isotrol.impe3.pms.model.DeviceEntity;
import com.isotrol.impe3.pms.model.PortalCacheValue;
import com.isotrol.impe3.pms.model.PortalConfigurationValue;
import com.isotrol.impe3.pms.model.PortalDeviceValue;
import com.isotrol.impe3.pms.model.PortalDfn;
import com.isotrol.impe3.pms.model.SetFilterValue;

import net.sf.derquinsej.i18n.Locales;


/**
 * Category type domain object.
 * 
 * @author Andres Rodriguez
 */
public final class PortalObject extends AbstractRoutableObject {
	static Function<PortalObject, PortalSelDTO> MAP2SEL = new Function<PortalObject, PortalSelDTO>() {
		public PortalSelDTO apply(PortalObject from) {
			return from.toSelDTO();
		}
	};

	private static final String NULL_NAME = "__NULL__";

	private static Ordering<PageDeviceDTO> BY_NAME = Ordering.natural().onResultOf(
		new Function<PageDeviceDTO, String>() {
			public String apply(PageDeviceDTO from) {
				return from.getDisplayName();
			}
		});

	public static Map<String, String> toNullName(Map<String, String> map) {
		return Maps.transformValues(map, new Function<String, String>() {
			public String apply(String from) {
				return from != null ? from : NULL_NAME;
			}
		});
	}

	public static Map<String, String> fromNullName(Map<String, String> map) {
		return Maps.transformValues(map, new Function<String, String>() {
			public String apply(String from) {
				return NULL_NAME.equals(from) ? null : from;
			}
		});
	}

	private static final BasesLoader BASES_LOADER = new BasesLoader();
	private static final PropertiesLoader PROPERTIES_LOADER = new PropertiesLoader();
	private static final PortalConfigurationLoader PORTAL_CONFIGURATION_LOADER = new PortalConfigurationLoader();
	private static final SetFiltersLoader SETFILTERS_LOADER = new SetFiltersLoader();
	private static final LocalesLoader LOCALES_LOADER = new LocalesLoader();
	private static final DevicesLoader DEVICES_LOADER = new DevicesLoader();

	/** Portal collection key. */
	private final CacheKey portalsKey;
	/** Portal Edition Id. For offline objects is {@code null}. */
	private final UUID editionId;
	/** Parent portal Id. */
	private final UUID parentId;
	/** Routing domain id. */
	private final UUID domainId;
	/** Portal name. */
	private final Name name;
	/** Portal description. */
	private final String description;
	/** Tag path segment. */
	private final String tag;
	/** Router. */
	private final Provider router;
	/** Locale resolver. */
	private final Provider locale;
	/** Device resolver. */
	private final Provider device;
	/** Device capabilities provider. */
	private final Provider deviceCaps;
	/** Default node repository. */
	private final Provider nodeRepository;
	/** Portal bases. */
	private final Supplier<ImmutableMap<String, String>> bases;
	/** Portal properties. */
	private final Supplier<ImmutableMap<String, String>> properties;
	
	/** Portal configuration. */
	private final Supplier<ImmutableMap<String, PortalConfigurationValue>> portalConfiguration;
	
	/** Include uncategorized. */
	private final PortalInheritableFlag uncategorized;
	/** Only due nodes. */
	private final PortalInheritableFlag due;
	/** Default locale. */
	private final Locale defaultLocale;
	/** Available locales. A hash map is used because null values are allowed. */
	private final Supplier<Map<String, String>> locales;
	/** Set filters. */
	private final Supplier<ImmutableMap<String, SetFilterValue>> setFilters;
	/** Devices. */
	private final Supplier<PortalDevices> devices;
	/** Cache information. */
	private final PortalCacheValue cache;
	/** Portal IA. */
	private volatile IA ia = null;
	/** Whether to use session-based CSRF. */
	private final PortalInheritableFlag sessionCSRF;

	private static ImmutableMap<UUID, DiPObj> deviceMap(DeviceEntity entity) {
		return ImmutableBiMap.of(entity.getId(), new DiPObj(entity));
	}

	/**
	 * Constructor.
	 * @param tx Portals tx provider.
	 * @param portalsKey Portals collection key.
	 * @param editionId Edition id.
	 * @param dfn Definition.
	 */
	PortalObject(PortalTxProvider tx, CacheKey portalsKey, UUID editionId, PortalDfn dfn) {
		super(dfn);
		checkArgument((portalsKey == null) != (editionId == null), "Inconsistent portal object.");
		this.portalsKey = portalsKey;
		this.editionId = editionId;
		this.parentId = dfn.getParentId();
		this.domainId = dfn.getRoutingDomain().getId();
		this.name = Functions.value2name(dfn.getName());
		this.description = dfn.getDescription();
		this.tag = dfn.getTag();
		this.router = Provider.of(dfn.getRouterConnector(), dfn.getRouterBean());
		this.locale = Provider.of(dfn.getLocaleConnector(), dfn.getLocaleBean());
		this.device = Provider.of(dfn.getDeviceConnector(), dfn.getDeviceBean());
		this.deviceCaps = Provider.of(dfn.getDeviceCapsConnector(), dfn.getDeviceCapsBean());
		this.nodeRepository = Provider.of(dfn.getNrConnector(), dfn.getNrBean());
		this.bases = tx.getTxSupplier(PortalDfn.class, dfn.getId(), null, BASES_LOADER);
		this.properties = tx.getTxSupplier(PortalDfn.class, dfn.getId(), null, PROPERTIES_LOADER);
		
		this.portalConfiguration = tx.getTxSupplier(PortalDfn.class, dfn.getId(), null, PORTAL_CONFIGURATION_LOADER);
		
		this.uncategorized = PortalInheritableFlag.fromBoolean(dfn.getUncategorized());
		this.due = PortalInheritableFlag.fromBoolean(dfn.getDue());
		this.defaultLocale = MoreLocales.fromString(dfn.getDefaultLocale(), MoreLocales.FALLBACK);
		this.locales = tx.getTxSupplier(PortalDfn.class, dfn.getId(), null, LOCALES_LOADER);
		this.setFilters = tx.getTxSupplier(PortalDfn.class, dfn.getId(), null, SETFILTERS_LOADER);
		this.sessionCSRF = PortalInheritableFlag.fromBoolean(dfn.getSessionCSRF());
		// Devices
		this.devices = tx.getTxSupplier(PortalDfn.class, dfn.getId(), null, DEVICES_LOADER);
		// Cache
		PortalCacheValue cacheValue = dfn.getCache();
		if (cacheValue == null) {
			this.cache = new PortalCacheValue(false, false, false, null, null, ETagMode.OFF);
		} else {
			this.cache = cacheValue;
		}
	}

	IA getIA(ContextGlobal ctx) {
		if (ia == null) {
			ia = ctx.getIA();
		}
		return ia;
	}

	public boolean isOffline() {
		return portalsKey != null;
	}

	public CacheKey getPortalsKey() {
		return portalsKey;
	}

	public UUID getEditionId() {
		return editionId;
	}

	public UUID getParentId() {
		return parentId;
	}

	public UUID getDeviceId() {
		return devices.get().deviceId;
	}

	public UUID getDomainId() {
		return domainId;
	}

	public Name getName() {
		return name;
	}

	ImmutableMap<String, String> getBases() {
		return bases.get();
	}

	ImmutableMap<String, String> getProperties() {
		return properties.get();
	}
	
	/**
	 * @return the portalConfiguration
	 */
	ImmutableMap<String, PortalConfigurationValue> getPortalConfiguration() {
		return portalConfiguration.get();
	}

	public ImmutableMap<String, SetFilterValue> getSetFilters() {
		return setFilters.get();
	}

	boolean isInheritedDevices() {
		return devices.get().inheritedDevices;
	}

	boolean isInheritedCache() {
		return cache.isInherited();
	}

	public Portal start(BaseModel model, PortalsObject portals) {
		final Map<String, URI> ubases = Maps.newHashMap();
		for (Entry<String, String> b : getActiveBases(portals).entrySet()) {
			try {
				ubases.put(b.getKey(), new URI(b.getValue()));
			} catch (URISyntaxException e) {
				// TODO
			}
		}
		final Devices devicesModel = model.getDevices();
		final Function<DiPObj, DeviceInPortal> u2d = new Function<DiPObj, DeviceInPortal>() {
			public DeviceInPortal apply(DiPObj from) {
				final Device d = devicesModel.get(from.getDeviceId());
				return DeviceInPortal.of(d, from.getName(), from.getUse());
			}
		};
		final Device defaultDevice = devicesModel.get(devices.get().deviceId);
		Set<DeviceInPortal> dm = ImmutableSet.copyOf(Iterables.transform(devices.get().devices.values(), u2d));
		Portal.Builder b = Portal.builder().setId(getId()).setMode(model.getMode()).setDevice(defaultDevice)
			.setName(name.getDisplayName()).setContentTypes(model.getContentTypes())
			.setCategories(model.getCategories()).setBases(ubases).setProperties(getActiveProperties(portals))
			.setUncategorized(isUncategorized(portals)).setDue(isDue(portals))
			.setSetFilters(Maps.transformValues(getSetFilters(), SetFilterValue.TYPE)).setDevices(dm)
			.setSessionCSRF(isSessionCSRF(portals));

		// Locales
		if (defaultLocale != null) {
			Set<Locale> locs = Sets.newHashSet(defaultLocale);
			for (String sl : locales.get().keySet()) {
				locs.add(Locales.fromString(sl));
			}
			b.setDefaultLocale(defaultLocale).setLocales(locs);
		}
		// Done
		return b.get();
	}

	public PortalCacheModel getCacheModel(PortalsObject portals) {
		if (isInheritedCache() && parentId != null) {
			return portals.get(parentId).getCacheModel(portals);
		}
		if (cache.isActive()) {
			return PortalCacheModel.on(cache.isPublicCache(), cache.getModification(), cache.getModification(),
				cache.getETagMode());
		}
		return PortalCacheModel.off();
	}

	/**
	 * Returns whether a connector is used by a provider of this portal.
	 * @param provider Provider to check.
	 * @param id Connector id.
	 * @return True if the connector is used by the provider.
	 */
	private boolean isConnectorUsed(Provider p, UUID id) {
		return p != null && p.getConnectorId().equals(id);
	}

	/**
	 * Returns whether a connector is used by this portal.
	 * @param id Connector id.
	 * @return True if the connector is used by this portal.
	 */
	boolean isConnectorUsed(UUID id) {
		return isConnectorUsed(router, id) || isConnectorUsed(locale, id) || isConnectorUsed(device, id)
			|| isConnectorUsed(deviceCaps, id) || isConnectorUsed(nodeRepository, id);
	}

	/**
	 * Transforms the object to a selection DTO.
	 * @return The selection DTO.
	 */
	public PortalSelDTO toSelDTO() {
		final PortalSelDTO dto = new PortalSelDTO();
		dto.setId(getStringId());
		dto.setState(getState());
		dto.setName(name.getDisplayName());
		dto.setDescription(description);
		return dto;
	}

	public boolean isUncategorized(PortalsObject portals) {
		final PortalObject parent = portals.getParent(getId());
		if (uncategorized != PortalInheritableFlag.INHERIT || parent == null) {
			return uncategorized.toBoolean(true);
		}
		return parent.isUncategorized(portals);
	}

	public boolean isDue(PortalsObject portals) {
		final PortalObject parent = portals.getParent(getId());
		if (due != PortalInheritableFlag.INHERIT || parent == null) {
			return due.toBoolean(true);
		}
		return parent.isDue(portals);
	}
	
	private boolean isSessionCSRF(PortalsObject portals) {
		final PortalObject parent = portals.getParent(getId());
		if (sessionCSRF != PortalInheritableFlag.INHERIT || parent == null) {
			return sessionCSRF.toBoolean(false);
		}
		return parent.isSessionCSRF(portals);
	}

	final Map<String, String> getActiveBases(PortalsObject portals) {
		if (parentId == null) {
			return getBases();
		}
		final PortalObject parent = portals.get(parentId);
		Map<String, String> map = Maps.newHashMap(parent.getActiveBases(portals));
		map.putAll(getBases());
		return ImmutableMap.copyOf(map);
	}

	final Map<String, String> getActiveProperties(PortalsObject portals) {
		if (parentId == null) {
			return getProperties();
		}
		final PortalObject parent = portals.get(parentId);
		Map<String, String> map = Maps.newHashMap(parent.getActiveProperties(portals));
		map.putAll(getProperties());
		return ImmutableMap.copyOf(map);
	}

	/**
	 * Transforms the object to a PortalNameDTO.
	 * 
	 * @param logger Logger.
	 * @param portals Portals.
	 * @param cnns Connectors.
	 * @param locale Locale.
	 * @return The template DTO.
	 */
	PortalNameDTO toNameDTO() {
		final PortalNameDTO dto = new PortalNameDTO();
		dto.setId(getStringId());
		dto.setName(NAME2DTO.apply(name));
		dto.setDescription(description);
		dto.setDefaultLocale(defaultLocale.toString());
		dto.setLocales(Maps.newHashMap(locales.get()));
		return dto;
	}

	List<PageDeviceDTO> getPageDeviceDTOs(ContextGlobal ctx) throws PMSException {
		if (isInheritedDevices() && parentId != null) {
			return ctx.getPortals().load(parentId).getPageDeviceDTOs(ctx);
		}
		final DevicesObject devicesObject = ctx.getDevices();
		final Map<UUID, DiPObj> devMap = devices.get().devices;
		final UUID deviceId = devices.get().deviceId;
		List<PageDeviceDTO> list = Lists.newArrayListWithCapacity(devMap.size());
		for (DiPObj dip : devMap.values()) {
			PageDeviceDTO dto = new PageDeviceDTO();
			UUID did = dip.getDeviceId();
			dto.setDevice(devicesObject.load(did).toSelDTO());
			dto.setName(dip.getName());
			dto.setDefaultDevice(deviceId.equals(did));
			list.add(dto);
		}
		Collections.sort(list, BY_NAME);
		return list;
	}

	PortalDevicesTemplateDTO getPortalDevicesTemplateDTO(ContextGlobal ctx) throws PMSException {
		return getPortalDevicesTemplateDTO(ctx, this);
	}

	private PortalDevicesTemplateDTO getPortalDevicesTemplateDTO(ContextGlobal ctx, PortalObject portal)
		throws PMSException {
		final boolean child = parentId != null;
		if (isInheritedDevices() && child) {
			return ctx.getPortals().load(parentId).getPortalDevicesTemplateDTO(ctx, portal, true, true);
		}
		return getPortalDevicesTemplateDTO(ctx, portal, child, isInheritedDevices());
	}

	private PortalDevicesTemplateDTO getPortalDevicesTemplateDTO(ContextGlobal ctx, PortalObject portal, boolean child,
		boolean inherited) throws PMSException {
		PortalDevicesTemplateDTO dto = new PortalDevicesTemplateDTO();
		dto.setId(portal.getStringId());
		dto.setChild(child);
		dto.setInherited(inherited);
		dto.setDevices(getDeviceTree(ctx.getDevices()));
		return dto;
	}

	PortalCacheDTO getPortalCacheDTO(ContextGlobal ctx) throws PMSException {
		return getPortalCacheDTO(ctx, this, false);
	}

	private PortalCacheDTO getPortalCacheDTO(ContextGlobal ctx, PortalObject portal, boolean inherited)
		throws PMSException {
		final boolean child = parentId != null && cache.isInherited();
		if (child) {
			return ctx.getPortals().load(parentId).getPortalCacheDTO(ctx, portal, true);
		}
		final PortalCacheDTO dto = new PortalCacheDTO();
		dto.setId(portal.getStringId());
		dto.setInherited(inherited);
		dto.setActive(cache.isActive());
		dto.setPublicCache(cache.isPublicCache());
		dto.setModification(cache.getModification());
		dto.setExpiration(cache.getExpiration());
		dto.setETagMode(cache.getETagMode());
		return dto;
	}

	private List<DeviceInPortalTreeDTO> getDeviceTree(final DevicesObject devicesObject) throws PMSException {
		final PortalDevices devs = devices.get();
		final Function<DeviceObject, DeviceInPortalTreeDTO> tree = new Function<DeviceObject, DeviceInPortalTreeDTO>() {
			public DeviceInPortalTreeDTO apply(DeviceObject from) {
				final UUID did = from.getId();
				final DeviceInPortalDTO node = new DeviceInPortalDTO();
				if (devs.devices.containsKey(did)) {
					node.setActive(true);
					final DiPObj dip = devs.devices.get(did);
					node.setName(dip.getName());
					node.setUse(dip.getUse());
					node.setDefaultDevice(devs.deviceId.equals(did));
				} else {
					node.setActive(false);
					node.setName(null);
					node.setDefaultDevice(false);
				}
				node.setDevice(from.toSelDTO());
				final DeviceInPortalTreeDTO dto = new DeviceInPortalTreeDTO();
				dto.setNode(node);
				dto.setChildren(Mappers.list(devicesObject.getChildren(did), this));
				return dto;
			}
		};
		return Mappers.list(devicesObject.getFirstLevel(), tree);
	}

	PortalNamePB toNamePB() {
		PortalNamePB.Builder b = PortalNamePB.newBuilder().setName(MessageMappers.name().apply(name));
		if (description != null) {
			b.setDescription(description);
		}
		if (defaultLocale != null) {
			b.setDefaultLocale(defaultLocale.toString());
		}
		b.addAllLocales(transformSE(toNullName(locales.get())));
		return b.build();
	}

	BasesPB exportBases() {
		return BasesPB.newBuilder().addAllBases(transformSE(getBases())).build();
	}

	PropertiesPB exportProperties() {
		return PropertiesPB.newBuilder().addAllProperties(transformSE(getProperties())).build();
	}

	SetsPB exportSets() {
		return SetsPB.newBuilder().addAllSets(transform(getSetFilters().entrySet(), Set2PB.INSTANCE)).build();
	}

	/**
	 * Transforms the object to a template DTO.
	 * 
	 * @param ctx Global context.
	 * @return The template DTO.
	 */
	PortalTemplateDTO toDTO(ContextGlobal ctx) {
		final PortalTemplateDTO dto = new PortalTemplateDTO();
		final ConnectorsObject cnns = ctx.getConnectors();
		final Locale locale = ctx.getLocale();
		final RoutingDomainsObject rds = ctx.getDomains();
		dto.setId(getStringId());
		dto.setState(getState());
		dto.setRoutable(isRoutable());
		dto.setTag(tag);
		dto.setRouter(cnns.getProvidedTemplate(PageResolver.class, this.router, locale));
		dto.setLocale(cnns.getProvidedTemplate(LocaleRouter.class, this.locale, locale));
		dto.setDevice(cnns.getProvidedTemplate(DeviceRouter.class, this.device, locale));
		dto.setDeviceCaps(cnns.getProvidedTemplate(DeviceCapabilitiesProvider.class, this.deviceCaps, locale));
		dto.setNodeRepository(cnns.getProvidedTemplate(NodeRepository.class, this.nodeRepository, locale));
		dto.setDefaultLocale(defaultLocale.toString());
		dto.setLocales(Maps.newHashMap(locales.get()));
		dto.setUncategorized(uncategorized);
		dto.setDue(due);
		dto.setSessionCSRF(sessionCSRF);
		dto.setDomain(rds.get(domainId).toSel());
		dto.setAvailableDomains(rds.map2sel(true));
		return dto;
	}

	/**
	 * Transforms the object to a configuration PB.
	 * 
	 * @param ctx Global context.
	 * @return The configuration PB.
	 */
	PortalConfigurationPB toConfigurationPB(ContextGlobal ctx) {
		PortalConfigurationPB.Builder b = PortalConfigurationPB.newBuilder();
		b.setRoutable(isRoutable());
		if (domainId != null) {
			b.setDomain(ctx.getDomains().get(domainId).getName());
		}
		if (tag != null) {
			b.setTag(tag);
		}

		return null; // TODO
	}

	List<SetFilterDTO> getSetFilterDTOs() {
		ImmutableMap<String, SetFilterValue> filters = setFilters.get();
		final List<SetFilterDTO> list = Lists.newArrayListWithCapacity(filters.size());
		if (filters.isEmpty()) {
			return list;
		}
		for (String set : Ordering.natural().sortedCopy(filters.keySet())) {
			SetFilterValue v = filters.get(set);
			SetFilterDTO dto = new SetFilterDTO();
			dto.setName(set);
			dto.setType(v.getType());
			dto.setDescription(v.getDescription());
			list.add(dto);
		}
		return list;
	}

	private enum Set2PB implements Function<Entry<String, SetFilterValue>, SetPB> {
		INSTANCE;

		public SetPB apply(Entry<String, SetFilterValue> from) {
			final SetPB.Builder b = SetPB.newBuilder().setName(from.getKey());
			final SetFilterValue v = from.getValue();
			switch (v.getType()) {
				case REQUIRED:
					b.setType(SetTypePB.REQUIRED);
					break;
				case OPTIONAL:
					b.setType(SetTypePB.OPTIONAL);
					break;
				case FORBIDDEN:
					b.setType(SetTypePB.FORBIDDEN);
					break;
			}
			if (v.getDescription() != null) {
				b.setDescription(v.getDescription());
			}
			return b.build();
		}
	}

	/** Bases loader. */
	private static final class BasesLoader extends AbstractSimpleValueLoader<PortalDfn, ImmutableMap<String, String>> {
		BasesLoader() {
			super("Portal Bases");
		}

		@Override
		protected ImmutableMap<String, String> load(PortalDfn dfn) {
			return ImmutableMap.copyOf(dfn.getBases());
		}
	}

	/** Properties loader. */
	private static final class PropertiesLoader extends
		AbstractSimpleValueLoader<PortalDfn, ImmutableMap<String, String>> {
		PropertiesLoader() {
			super("Portal Properties");
		}

		@Override
		protected ImmutableMap<String, String> load(PortalDfn dfn) {
			return ImmutableMap.copyOf(dfn.getProperties());
		}
	}
	
	/** Portal configurations loader. */
	private static final class PortalConfigurationLoader extends
		AbstractSimpleValueLoader<PortalDfn, ImmutableMap<String, PortalConfigurationValue>> {
		PortalConfigurationLoader() {
			super("Portal Properties");
		}

		@Override
		protected ImmutableMap<String, PortalConfigurationValue> load(PortalDfn dfn) {
			return ImmutableMap.copyOf(dfn.getPortalConfiguration());
		}
	}

	/** Set Filters loader. */
	private static final class SetFiltersLoader extends
		AbstractSimpleValueLoader<PortalDfn, ImmutableMap<String, SetFilterValue>> {
		SetFiltersLoader() {
			super("Portal Set Filters");
		}

		@Override
		protected ImmutableMap<String, SetFilterValue> load(PortalDfn dfn) {
			return ImmutableMap.copyOf(dfn.getSetFilters());
		}
	}

	/** Locales loader. */
	private static final class LocalesLoader extends AbstractSimpleValueLoader<PortalDfn, Map<String, String>> {
		LocalesLoader() {
			super("Portal Locales");
		}

		@Override
		protected Map<String, String> load(PortalDfn dfn) {
			Map<String, String> locs = Maps.newHashMap();
			for (String loc : Iterables.filter(dfn.getLocales(), MoreLocales.VALID)) {
				locs.put(loc, null);
			}
			locs.putAll(filterKeys(dfn.getL7DNames(), MoreLocales.VALID));
			return unmodifiableMap(locs);
		}
	}

	/** Portal devices. */
	private static final class PortalDevices {
		/** Default device id. */
		private final UUID deviceId;
		/** Devices. */
		private final ImmutableMap<UUID, DiPObj> devices;
		/** Use parent portal devices. */
		private final boolean inheritedDevices;

		/** Inherited. */
		PortalDevices(DeviceEntity defaultDevice) {
			this.inheritedDevices = true;
			this.deviceId = defaultDevice.getId();
			this.devices = deviceMap(defaultDevice);
		}

		/** Not inherited. */
		PortalDevices(DeviceEntity defaultDevice, Map<DeviceEntity, PortalDeviceValue> devices) {
			this.inheritedDevices = false;
			UUID defaultDeviceId = null;
			final int n = devices.size();
			final Map<UUID, DiPObj> deviceMap = Maps.newHashMapWithExpectedSize(n);
			final Set<String> names = Sets.newHashSetWithExpectedSize(n);
			for (Entry<DeviceEntity, PortalDeviceValue> e : devices.entrySet()) {
				final DiPObj dip = new DiPObj(e);
				if (!names.add(dip.getName())) {
					defaultDeviceId = null;
					break;
				}
				UUID did = dip.getDeviceId();
				if (e.getValue().isDefaultDevice()) {
					if (defaultDeviceId != null) {
						defaultDeviceId = null;
						break;
					}
					defaultDeviceId = did;
				}
				deviceMap.put(did, dip);
			}
			if (defaultDeviceId != null) {
				this.deviceId = defaultDeviceId;
				this.devices = ImmutableBiMap.copyOf(deviceMap);
			} else {
				this.deviceId = defaultDevice.getId();
				this.devices = deviceMap(defaultDevice);
			}
		}
	}

	/** Portal devices loader. */
	private static final class DevicesLoader extends AbstractSimpleValueLoader<PortalDfn, PortalDevices> {
		DevicesLoader() {
			super("Portal Devices");
		}

		@Override
		protected PortalDevices load(PortalDfn dfn) {
			final DeviceEntity defaultDevice = dfn.getEntity().getDefaultDevice();
			if (dfn.isInheritedDevices()) {
				return new PortalDevices(defaultDevice);
			} else {
				return new PortalDevices(defaultDevice, dfn.getDevices());
			}
		}
	}

}
