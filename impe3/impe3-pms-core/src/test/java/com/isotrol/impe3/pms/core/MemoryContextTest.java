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

package com.isotrol.impe3.pms.core;


import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.category.CategoriesService;
import com.isotrol.impe3.pms.api.category.CategoryDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeDTO;
import com.isotrol.impe3.pms.api.category.CategoryTreeIndex;
import com.isotrol.impe3.pms.api.component.ComponentsService;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.edition.EditionsService;
import com.isotrol.impe3.pms.api.minst.DependencyTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.minst.ProviderDTO;
import com.isotrol.impe3.pms.api.page.ColumnDTO;
import com.isotrol.impe3.pms.api.page.ComponentFrameDTO;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.api.session.SessionsService;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.api.type.ContentTypesService;
import com.isotrol.impe3.pms.core.engine.EngineModelLoader;
import com.isotrol.impe3.pms.core.impl.PerformanceReportComponent;
import com.isotrol.impe3.pms.ext.api.Credentials;
import com.isotrol.impe3.samples.component.SimpleComponentModule;
import com.isotrol.impe3.samples.dummy.DummyComponentModule;
import com.isotrol.impe3.samples.dummy.DummyConnectorModule;


/**
 * Abstract class for tests that share an application context with a private memory database.
 * @author Andres Rodriguez
 */
public abstract class MemoryContextTest {
	/** Aplication context. */
	private static AppContextSupport context;
	/** String index. */
	private static int stringIndex = 0;
	/** Simple component. */
	private static final String SIMPLE = SimpleComponentModule.class.getName();

	protected static final String testString() {
		return "test" + (++stringIndex);
	}

	/** Default constructor. */
	public MemoryContextTest() {
	}

	/**
	 * Loads a fresh application context.
	 */
	@BeforeClass
	public static void loadContext() throws Exception {
		context = AppContextSupport.memory();
		assertNotNull(getBean(SessionsService.class).login(
			new Credentials.BasicCredentials().setLogin("root").setPassword("root")));
	}

	@AfterClass
	public static void after() {
		System.out.println(getBean(PerformanceReportComponent.class).getReport());
	}

	/**
	 * Returns the unique bean of the context with the specified type.
	 * @param type Requested type.
	 * @return The requested bean.
	 */
	protected static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}

	/**
	 * Returns a bean of the context by name.
	 * @param name Bean name.
	 * @return The requested bean.
	 */
	protected static Object getBean(String name) {
		return context.getBean(name);
	}

	protected static NameDTO name(String name) {
		NameDTO dto = new NameDTO(name, name);
		return dto;
	}

	protected static NameDTO name() {
		NameDTO dto = new NameDTO(testString(), testString());
		return dto;
	}

	protected static ContentTypeDTO loadContentType(NameDTO name) throws PMSException {
		ContentTypeDTO dto = new ContentTypeDTO();
		dto.setDefaultName(name);
		dto.setLocalizedNames(new HashMap<String, NameDTO>());
		return getBean(ContentTypesService.class).save(dto);
	}

	protected static ContentTypeDTO loadContentType(String name) throws PMSException {
		ContentTypeDTO dto = new ContentTypeDTO();
		dto.setDefaultName(name(name));
		dto.setLocalizedNames(new HashMap<String, NameDTO>());
		dto.setRoutable(true);
		return getBean(ContentTypesService.class).save(dto);
	}

	protected static ContentTypeDTO loadContentType() throws PMSException {
		return loadContentType(name());
	}

	protected static CategoryDTO loadCategory(NameDTO name, String parentId, int order) throws PMSException {
		CategoryDTO dto = new CategoryDTO();
		dto.setDefaultName(name);
		dto.setLocalizedNames(new HashMap<String, NameDTO>());
		return getBean(CategoriesService.class).create(dto, parentId, order);
	}

	protected static CategoryDTO loadCategory(String parentId, int order) throws PMSException {
		return loadCategory(name(), parentId, order);
	}

	protected static String loadConnector(Class<? extends Module> module) throws PMSException {
		final ConnectorsService service = getBean(ConnectorsService.class);
		ModuleInstanceTemplateDTO dto = service.newTemplate(module.getName());
		dto.setName(testString());
		dto = service.save(dto.toModuleInstanceDTO());
		return dto.getId();
	}

	protected static String loadPortal(NameDTO name, String parentId) throws PMSException {
		final PortalNameDTO dto = new PortalNameDTO();
		dto.setName(name);
		return getBean(PortalsService.class).create(dto, parentId);
	}

	protected static String loadPortal(String parentId) throws PMSException {
		return loadPortal(name(), parentId);
	}

	protected static String loadPortal() throws PMSException {
		return loadPortal(null);
	}

	protected static String loadComponent(String portalId, Class<? extends Module> module) throws PMSException {
		final ComponentsService service = getBean(ComponentsService.class);
		final ModuleInstanceTemplateDTO t = service.newTemplate(module.getName());
		t.setName(testString());
		if (t.getDependencies() != null) {
			for (DependencyTemplateDTO d : t.getDependencies()) {
				d.setCurrent(d.getProviders().get(0));
			}
		}
		return service.save(portalId, t.toModuleInstanceDTO()).getId();
	}

	protected static String loadSimpleComponent(String portalId) throws PMSException {
		return loadComponent(portalId, SimpleComponentModule.class);
	}

	protected static void publish() throws PMSException {
		getBean(EditionsService.class).publish();
	}

	private static String getDefaultDeviceId(PagesService service, String portalId) throws PMSException {
		return service.getPageDevices(portalId).get(0).getDeviceId();
	}

	protected static String getDefaultDeviceId(String portalId) throws PMSException {
		return getBean(PagesService.class).getPageDevices(portalId).get(0).getDeviceId();
	}

	protected static PortalPagesLoc loadPortalWithPalette() throws PMSException {
		final String portalId = loadPortal();
		final ComponentsService service = getBean(ComponentsService.class);
		final ModuleInstanceTemplateDTO template1 = service.newTemplate(SIMPLE);
		template1.setName(testString());
		service.save(portalId, template1.toModuleInstanceDTO());
		return new PortalPagesLoc(portalId, getDefaultDeviceId(portalId));
	}

	protected static EngineModel getOfflineModel() throws PMSException {
		return getBean(EngineModelLoader.class).getOffline(EnvironmentManager.NAME);
	}

	protected static EngineModel getOnlineModel() throws PMSException {
		return getBean(EngineModelLoader.class).getOnline(EnvironmentManager.NAME);
	}

	protected static PortalModel getOfflinePortalModel(String id) throws PMSException {
		return getOfflineModel().getPortal(UUID.fromString(id));
	}

	protected static PortalModel getOnlinePortalModel(String id) throws PMSException {
		return getOnlineModel().getPortal(UUID.fromString(id));
	}

	public static class PageLoader {
		private final PagesService service = getBean(PagesService.class);
		private final PortalPagesLoc ppl;

		public PageLoader(String portalId) throws PMSException {
			final String deviceId = getDefaultDeviceId(service, portalId);
			this.ppl = new PortalPagesLoc(portalId, deviceId);
		}

		public Page create(PageClass pc) throws PMSException {
			return new Page(pc);
		}

		public PageLoc loadSpecial(String name) throws PMSException {
			final Page p = new Page(PageClass.SPECIAL);
			p.setName(name);
			p.putComponents();
			return p.save();
		}

		public PageLoc loadDefault() throws PMSException {
			final Page p = new Page(PageClass.DEFAULT);
			p.putComponents();
			return p.save();
		}

		public PageLoc loadSpecial() throws PMSException {
			return loadSpecial(testString());
		}

		public PageLoc loadTemplate(String name) throws PMSException {
			final Page p = new Page(PageClass.TEMPLATE);
			p.setName(name);
			p.putComponents();
			return p.save();
		}

		public PageLoc loadTemplate() throws PMSException {
			return loadTemplate(testString());
		}

		public PageLoc loadCategory(String categoryId, boolean layout) throws PMSException {
			Page page = create(PageClass.CATEGORY);
			page.setCategory(categoryId);
			page.putComponents();
			page.save();
			if (layout) {
				page.layout();
			}
			return page.loc;
		}

		public Page get(String id) throws PMSException {
			final PageLoc loc = new PageLoc(ppl, id);
			return new Page(loc);
		}

		public Page get(PageLoc loc) throws PMSException {
			assertEquals(ppl.getPortalId(), loc.getPortalId());
			assertEquals(ppl.getDeviceId(), loc.getDeviceId());
			return new Page(loc);
		}

		public List<Inherited<PageSelDTO>> getSpecialPages() throws PMSException {
			return service.getSpecialPages(ppl);
		}

		public void delete(PageLoc loc) throws PMSException {
			service.delete(loc);
		}

		public class Page {
			private PageTemplateDTO template;
			private PageLoc loc;
			private LayoutDTO layout = null;
			private Map<String, ComponentInPageTemplateDTO> cips = Maps.newHashMap();

			public Page(PageClass pc) throws PMSException {
				template = service.newTemplate(ppl, pc);
			}

			private Page(PageLoc loc) throws PMSException {
				this.loc = loc;
				this.template = service.get(loc);
			}

			public PageLoc getLoc() {
				return loc;
			}

			public PageTemplateDTO getDTO() {
				return template;
			}

			public void setName(String name) {
				template.setName(name);
			}

			public void setTemplate(String id) {
				if (id == null) {
					template.setTemplate(null);
					return;
				}
				for (Inherited<PageSelDTO> dto : template.getTemplates()) {
					if (id.equals(dto.getValue().getId())) {
						template.setTemplate(dto.getValue());
						return;
					}
				}
				checkArgument(false);
			}

			public void setContentType(String id) {
				if (id == null) {
					template.setContentType(null);
					return;
				}
				for (ContentTypeSelDTO dto : template.getContentTypes()) {
					if (id.equals(dto.getId())) {
						template.setContentType(dto);
						return;
					}
				}
				checkArgument(false);
			}

			public void setCategory(String id) {
				if (id == null) {
					template.setCategory(null);
					return;
				}
				final CategoryTreeDTO dto = new CategoryTreeIndex(template.getCategories()).getTreeNode(id);
				checkArgument(dto != null);
				template.setCategory(dto.getNode());
			}

			public PageLoc save() throws PMSException {
				template = service.save(template.toPageDTO());
				loc = new PageLoc(template);
				cips = Maps.newHashMap();
				putCIPs(getComponents());
				return loc;
			}

			private void putCIPs(List<ComponentInPageTemplateDTO> list) {
				if (list == null || list.isEmpty()) {
					return;
				}
				for (ComponentInPageTemplateDTO c : list) {
					cips.put(c.getId(), c);
					putCIPs(c.getChildren());
				}
			}

			public List<ComponentInPageTemplateDTO> getComponents() {
				List<ComponentInPageTemplateDTO> cips = template.getComponents();
				if (cips == null) {
					cips = Lists.newArrayList();
					template.setComponents(cips);
				}
				return cips;
			}

			public void putComponents() throws PMSException {
				List<ComponentInPageTemplateDTO> cips = getComponents();
				for (PaletteDTO p : service.getPalette(ppl)) {
					ComponentInPageTemplateDTO cipt = service.newComponentTemplate(ppl, p.getKey());
					cipt.setName(testString());
					cips.add(cipt);
				}
			}

			public LayoutDTO getLayout() throws PMSException {
				if (layout == null) {
					layout = service.getLayout(loc);
				}
				return layout;
			}

			public void layout() throws PMSException {
				final List<FrameDTO> frames = Lists.newArrayList();
				for (LayoutItemDTO item : getLayout().getItems()) {
					final String cipId = item.getId();
					if (cips.containsKey(cipId)) {
						frames.add(new ComponentFrameDTO(cipId));
					}
				}
				layout = service.setLayout(loc, frames);
			}

			public String getSpaceId() {
				return getSpaceId(template.getComponents());
			}

			private String getSpaceId(Iterable<ComponentInPageTemplateDTO> cips) {
				if (cips == null) {
					return null;
				}
				for (ComponentInPageTemplateDTO cip : cips) {
					if (cip.isSpace()) {
						return cip.getId();
					}
					String id = getSpaceId(cip.getChildren());
					if (id != null) {
						return id;
					}
				}
				return null;
			}

			public boolean isCIPinLayout(String id) {
				Preconditions.checkNotNull(id);
				final List<FrameDTO> frames = layout.getFrames();
				return isCIPinFrames(id, frames);
			}

			private boolean isCIPinFrames(String id, Iterable<FrameDTO> frames) {
				if (frames == null) {
					return false;
				}
				for (FrameDTO f : frames) {
					if (f.isComponent()) {
						if (id.equalsIgnoreCase(f.getComponent())) {
							return true;
						}
					} else if (f.isColumns()) {
						final List<ColumnDTO> cols = f.getColumns();
						if (cols != null) {
							for (ColumnDTO c : cols) {
								if (isCIPinFrames(id, c.getFrames())) {
									return true;
								}
							}
						}
					} else {
						if (isCIPinFrames(id, f.getFrames())) {
							return true;
						}
					}
				}
				return false;
			}

		}
	}

	public static class Dummy {
		private Dummy() {
			throw new AssertionError();
		}

		public static void setText(ConfigurationTemplateDTO dto, String text) {
			dto.getItems().get(0).setString(text);
		}

		public static void setDep(List<DependencyTemplateDTO> deps, String cnnId) {
			final DependencyTemplateDTO dt = deps.get(0);
			for (ProviderDTO p : dt.getProviders()) {
				if (cnnId.equals(p.getCurrent().getId())) {
					dt.setCurrent(p);
					return;
				}
			}
			Assert.fail();
		}

		public static String getText(ConfigurationTemplateDTO dto) {
			return dto.getItems().get(0).getString();
		}

		public static String getDep(List<DependencyTemplateDTO> deps) {
			return deps.get(0).getCurrent().getCurrent().getId();
		}

		public static String loadConnector(String text) throws PMSException {
			final ConnectorsService service = getBean(ConnectorsService.class);
			ModuleInstanceTemplateDTO dto = service.newTemplate(DummyConnectorModule.class.getName());
			dto.setName(testString());
			setText(dto.getConfiguration(), text);
			dto = service.save(dto.toModuleInstanceDTO());
			return dto.getId();
		}

		public static String loadComponent(String portalId, String text, String cnnId) throws PMSException {
			final ComponentsService service = getBean(ComponentsService.class);
			ModuleInstanceTemplateDTO dto = service.newTemplate(DummyComponentModule.class.getName());
			dto.setName(testString());
			setText(dto.getConfiguration(), text);
			setDep(dto.getDependencies(), cnnId);
			dto = service.save(portalId, dto.toModuleInstanceDTO());
			return dto.getId();
		}

	}

}
