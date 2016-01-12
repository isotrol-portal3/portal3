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


import static com.isotrol.impe3.pms.core.support.Mappers.pconfig2seldto;
import static com.isotrol.impe3.pms.core.support.Mappers.pconfig2def;
import static com.isotrol.impe3.pms.core.support.Mappers.prop2dto;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.PortalConfiguration;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.pbuf.portal.PortalProtos.BasesPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PortalNamePB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.PropertiesPB;
import com.isotrol.impe3.pbuf.portal.PortalProtos.SetsPB;
import com.isotrol.impe3.pms.api.Correctness;
import com.isotrol.impe3.pms.api.EntityNotFoundException;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.component.InheritedComponentInstanceSelDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.minst.DependencyDTO;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceSelDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProvidedDTO;
import com.isotrol.impe3.pms.api.page.PageDeviceDTO;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.BasesDTO;
import com.isotrol.impe3.pms.api.portal.PortalCacheDTO;
import com.isotrol.impe3.pms.api.portal.PortalConfigurationSelDTO;
import com.isotrol.impe3.pms.api.portal.PortalDevicesTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PropertiesDTO;
import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.core.PortalLoader;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.core.support.NotFoundProviders;
import com.isotrol.impe3.pms.model.CIPValue;


/**
 * Portal level context.
 * @author Andres Rodriguez
 */
public class ContextPortal extends Context2 {
	static ContextPortal of(ContextGlobal global, UUID id) throws PMSException {
		final PortalObject portal = global.getPortals().load(id);
		final Context1 c1 = new Context1(global, portal.getIA(global));
		return new ContextPortal(global, c1, global.getPortals(), portal);
	}

	/** Global context. */
	private final ContextGlobal global;
	/** Portals. */
	private final PortalsObject portals;
	/** Current portal. */
	private final PortalObject portal;
	/** Components. */
	private ComponentsObject components = null;
	/** Pages. */
	private PortalPagesObject portalPages = null;

	/**
	 * Constructor
	 * @param global Global context.
	 * @param ctx Level 1 source context.
	 * @param portals Portals.
	 * @param portal Portal.
	 */
	private ContextPortal(ContextGlobal global, Context1 ctx, PortalsObject portals, PortalObject portal) {
		super(ctx);
		this.global = global;
		this.portals = portals;
		this.portal = portal;
	}

	/**
	 * Copy constructor
	 * @param ctx Base context.
	 */
	ContextPortal(ContextPortal ctx) {
		super(ctx);
		this.global = ctx.global;
		this.portals = ctx.portals;
		this.portal = ctx.portal;
		this.components = ctx.components;
		this.portalPages = ctx.portalPages;
	}

	final ContextGlobal getGlobal() {
		return global;
	}

	final PortalLoader getPortalLoader() {
		return global.getPortalLoader();
	}

	public final PortalsObject getPortals() {
		return portals;
	}

	public final PortalObject getPortal() {
		return portal;
	}

	public final PortalObject getParent() {
		return getParentPortal(portal.getId());
	}

	public final UUID getPortalId() {
		return portal.getId();
	}

	public final UUID getParentPortalId(UUID id) {
		return portals.getParentKey(id);
	}

	public final PortalObject getParentPortal(UUID id) {
		return portals.getParent(id);
	}

	public final boolean isInheritedDevices() {
		return portal.isInheritedDevices();
	}

	public final DeviceObject getDefaultDevice() throws PMSException {
		return getDevices().load(portal.getDeviceId());
	}

	public List<PageDeviceDTO> getPageDeviceDTOs() throws PMSException {
		return portal.getPageDeviceDTOs(global);
	}

	public PortalDevicesTemplateDTO getPortalDevicesTemplateDTO() throws PMSException {
		return portal.getPortalDevicesTemplateDTO(global);
	}

	public final boolean isInheritedCache() {
		return portal.isInheritedCache();
	}

	public final PortalCacheDTO getPortalCacheDTO() throws PMSException {
		return portal.getPortalCacheDTO(global);
	}

	// ////////////////// BEGIN Context2

	@Override
	public boolean isInstantiable(ModuleDefinition<?> module) {
		return global.isInstantiable(module);
	}

	@Override
	List<DependencyTemplateDTO> getDependencies(ModuleDefinition<?> md, ModuleObject mi) {
		return global.getDependencies(md, mi);
	}

	@Override
	public Map<String, Provider> validateDependencies(ModuleDefinition<?> md, ModuleObject mi, List<DependencyDTO> deps)
		throws PMSException {
		return global.validateDependencies(md, mi, deps);
	}

	@Override
	public ConnectorObject checkProvided(Class<?> type, ProvidedDTO dto) throws PMSException {
		return global.checkProvided(type, dto);
	}

	@Override
	public ConnectorObject loadConnector(UUID id) throws EntityNotFoundException {
		return global.loadConnector(id);
	}

	@Override
	public boolean isContentTypeUsed(UUID id) throws PMSException {
		return getComponents().isContentTypeUsed(id) || getPortalPages().isContentTypeUsed(id);
	}

	@Override
	public boolean isCategoryUsed(UUID id) throws PMSException {
		return getComponents().isCategoryUsed(id) || getPortalPages().isCategoryUsed(id);
	}

	@Override
	public boolean isConnectorUsed(UUID id) throws PMSException {
		return portal.isConnectorUsed(id) || getComponents().isConnectorUsed(id);
	}

	// ////////////////// END Context2

	// ////////////////// Begin Portal operations.

	/**
	 * Transforms the object to a PortalNameDTO.
	 * @return The name DTO.
	 */
	public PortalNameDTO toPortalNameDTO() {
		return portal.toNameDTO();
	}

	/**
	 * Transforms the object to a PortalNameDTO.
	 * @return The name DTO.
	 */
	public PortalNamePB toPortalNamePB() {
		return portal.toNamePB();
	}

	/**
	 * Transforms the object to a template DTO.
	 * @return The template DTO.
	 */
	public PortalTemplateDTO toPortalTemplateDTO() {
		return portal.toDTO(global);
	}

	public final List<BaseDTO> getAvailableBases() {
		return base2dto(portal.getActiveBases(portals));
	}

	public final BasesDTO getBases() {
		final PortalObject parent = getParent();
		final Map<String, String> inherited;
		if (parent == null) {
			inherited = null;
		} else {
			inherited = parent.getActiveBases(portals);
		}
		final BasesDTO dto = new BasesDTO();
		dto.setInherited(base2dto(inherited));
		dto.setBases(base2dto(portal.getBases()));
		return dto;
	}

	public final BasesPB exportBases() {
		return portal.exportBases();
	}

	public final PropertiesPB exportProperties() {
		return portal.exportProperties();
	}

	public final SetsPB exportSets() {
		return portal.exportSets();
	}

	private List<BaseDTO> base2dto(Map<String, String> map) {
		final List<BaseDTO> list;
		if (map == null || map.isEmpty()) {
			list = Lists.newArrayListWithCapacity(0);
		} else {
			list = Lists.newArrayListWithCapacity(map.size());
			for (final Entry<String, String> base : map.entrySet()) {
				list.add(new BaseDTO(base.getKey(), base.getValue()));
			}
		}
		return list;
	}

	public final List<PropertyDTO> getAvailableProperties() {
		return prop2dto(portal.getActiveProperties(portals));
	}

	public final PropertiesDTO getProperties() {
		final PortalObject parent = getParent();
		final Map<String, String> inherited;
		if (parent == null) {
			inherited = null;
		} else {
			inherited = parent.getActiveProperties(portals);
		}
		final PropertiesDTO dto = new PropertiesDTO();
		dto.setInherited(prop2dto(inherited));
		dto.setProperties(prop2dto(portal.getProperties()));
		return dto;
	}

	public List<SetFilterDTO> getSetFilters() {
		return portal.getSetFilterDTOs();
	}

	// ////////////////// End Portal operations.

	// ////////////////// Begin Component operations.

	public final ComponentsObject getComponents() throws PMSException {
		if (components == null) {
			components = getPortalLoader().loadComponents(portals, getPortal());
		}
		return components;
	}

	public final List<ModuleInstanceSelDTO> getOwnComponentsSel() throws PMSException {
		return getComponents().getOwnedSel(this);
	}

	public final List<ModuleInstanceSelDTO> getOwnComponentsSel(Correctness correctness) throws PMSException {
		return getComponents().getOwnedSel(this, correctness);
	}

	public final List<InheritedComponentInstanceSelDTO> getInheritedComponentsSel() throws PMSException {
		return getComponents().getInheritedSel(this);
	}

	public ModuleInstanceTemplateDTO getOwnedComponentTemplate(String id) throws PMSException {
		return getComponents().loadOwned(id).toTemplateDTO(this);
	}

	public final List<PaletteDTO> getPaletteDTO() throws PMSException {
		return Mappers.list(getComponents().getPalette(), PaletteItem.map2dto(getLocale()));
	}

	public final PaletteItem getPaletteItem(PaletteKey key) throws PMSException {
		if (key.isSpace()) {
			return PaletteItem.space();
		}
		return getComponents().getPaletteItem(key);
	}

	public final PaletteItem getPaletteItem(CIPValue v) throws PMSException {
		PaletteKey key = PaletteKey.fromCIPValue(v);
		return getPaletteItem(key);
	}

	// ////////////////// End Component operations.

	// ////////////////// Begin Page operations.

	final PortalPagesObject getPortalPages() throws PMSException {
		if (portalPages == null) {
			portalPages = getPortalLoader().loadPages(portals, getPortal());
		}
		return portalPages;
	}

	public ContextDevicePages toDevice(UUID deviceId) throws PMSException {
		getDevices().load(deviceId);
		return new ContextDevicePages(this, getPortalPages().byDevice(deviceId));
	}

	public ContextDevicePages toDevice(String deviceId) throws PMSException {
		return toDevice(NotFoundProviders.DEVICE.toUUID(deviceId));
	}


	public final List<PortalConfigurationSelDTO> getPortalConfigurations() throws PMSException {
		
		PortalConfigurationObject.template(getPortalConfigurationsDef().get("es.impe3.componentes.servertime.PortalNombreSedeConfig"), getGlobal());
		
		return pconfig2seldto(getComponents().delegate(), portal);
	}
	
	public final Map<String, PortalConfigurationDefinition<?>> getPortalConfigurationsDef() throws PMSException {
		return pconfig2def(getComponents().delegate(), portal);
	}
	
	public ConfigurationTemplateDTO getPortalConfiguration(String beanName) throws PMSException {
		return PortalConfigurationObject.template(getPortalConfigurationsDef().get(beanName), getGlobal());
	}

	// ////////////////// End Page operations.

}
