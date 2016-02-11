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

package com.isotrol.impe3.pms.gui.client.ioc;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.isotrol.impe3.gui.common.widget.IframePanel;
import com.isotrol.impe3.pms.gui.client.store.CategoryPageTreeStore;
import com.isotrol.impe3.pms.gui.client.store.ContentPageTreeStore;
import com.isotrol.impe3.pms.gui.client.store.PageTreeStore;
import com.isotrol.impe3.pms.gui.client.widget.ConfigurationPanel;
import com.isotrol.impe3.pms.gui.client.widget.DependencesPanel;
import com.isotrol.impe3.pms.gui.client.widget.LoginPanel;
import com.isotrol.impe3.pms.gui.client.widget.PmsInfoWindow;
import com.isotrol.impe3.pms.gui.client.widget.PmsViewport;
import com.isotrol.impe3.pms.gui.client.widget.comment.CommentsManagement;
import com.isotrol.impe3.pms.gui.client.widget.comment.CommentsViewport;
import com.isotrol.impe3.pms.gui.client.widget.design.ComponentsTreePanel;
import com.isotrol.impe3.pms.gui.client.widget.design.Design;
import com.isotrol.impe3.pms.gui.client.widget.design.FrameColumn;
import com.isotrol.impe3.pms.gui.client.widget.design.FrameComponent;
import com.isotrol.impe3.pms.gui.client.widget.design.NorthPanel;
import com.isotrol.impe3.pms.gui.client.widget.design.PalettesPanel;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.CommentsExternalServiceManagement;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.IndexersManagement;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.NodesRepositoryManagement;
import com.isotrol.impe3.pms.gui.client.widget.externalservices.PortalUsersManagement;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.LocalesMappingWindow;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoriesExportWindow;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoriesImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoryCreationPanel;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoryEditionPanel;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.categories.CategoryManagement;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.ContentTypeCreation;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.ContentTypeEdition;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.ContentTypeManagement;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.ContentTypesExportWindow;
import com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.ContentTypesImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.nr.NrViewport;
import com.isotrol.impe3.pms.gui.client.widget.nr.RepositoryQueryWidget;
import com.isotrol.impe3.pms.gui.client.widget.nr.RepositorySummaryWidget;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalDefaultPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalPreviewPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalViewport;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalsManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties.PortalCreationPropertiesPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties.PortalEditionPropertiesPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.cache.PortalCachePanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.collection.CollectionCreationPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.collection.CollectionEditionPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.collection.CollectionsManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.AllOwnComponentManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.ComponentDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.ComponentsExportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.ComponentsImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.ErrorOwnComponentManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.InheritPortalConfigurationWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.InheritedComponentManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.InheritedComponentsExportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.OverrideConfigurationWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.OverrideDependencesWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.PortalConfigurationWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.ValidOwnComponentManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.component.WarningOwnComponentManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.configurations.PortalConfigurationsManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.device.PortalDeviceEditionPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.device.PortalDevicesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names.PortalNameCreationPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names.PortalNameEditionPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentInPageNameEditor;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentsInPageConfigWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentsInPageManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentsPaletteWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.PagesImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.CategoryContentPageDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.CategoryPageDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.ContentPageDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.DefaultPageDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.detail.PageDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.CategoryContentPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.CategoryContentTypePagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.CategoryPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ContentPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ContentTypePagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.DefaultPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ErrorPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.SpecialPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.TemplatePagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.parent.ParentPortalManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.AvailableBasesWidget;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.AvailablePropertiesWidget;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.BasesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.properties.PropertiesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.ChangePortalUserPwdWindow;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.PortalUsersList;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.UserEditorPanel;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.UsersViewport;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.config.EnvironmentConfigWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.AllConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ConnectorDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ConnectorsExportWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ConnectorsImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ErroneousConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ValidConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.WarningConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device.DeviceCreationPanel;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device.DeviceEditionPanel;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.device.DevicesManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.edition.EditionManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping.MappingDetailPanel;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping.MappingExportWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping.MappingImportWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.mapping.SourceMappingManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ComponentsPackageDetail;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ComponentsPackagesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ConnectorsDetail;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.ConnectorsView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.InvalidModulesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.NotFoundModulesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.NotModulesView;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains.DefaultRoutingDomain;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains.RoutingDomainDetailsEditor;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.routingdomains.RoutingDomainsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.ChangePwdWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.GrantedAuthoritiesWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.PortalAuthoritiesWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.PortalSelector;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.UserDetails;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.UsersManagement;
import com.isotrol.impe3.pms.gui.client.widget.top.ChangeUserInfoWindow;
import com.isotrol.impe3.pms.gui.client.widget.top.ChangeUserPwdWindow;

/**
 * PMS Gin injector. Returns widgets instances with all dependencies injected.
 * 
 * @author Andrei Cojocaru
 *
 */
@GinModules({PmsGinModule.class, PmsViewportGinModule.class})
public interface IPmsFactory extends Ginjector {

	/**
	 * Returns a new instance of the login panel.<br/>
	 * @return
	 */
	LoginPanel getLoginPanel();
	
	/**
	 * @return a new PmsViewport instance
	 */
	PmsViewport getPmsViewport();
	
	/**
	 * @return a new EditionManagement instance
	 */
	EditionManagement getEditionManagement();
	
	/**
	 * @return a new UsersManagement widget instance.
	 */
	UsersManagement getUserManagement();
	
	/**
	 * @return a new Routing Domains management widget instance
	 */
	RoutingDomainsManagement getRoutingDomainsManagement();
	
	/**
	 * @return a new Components packages management widget instance.
	 */
	ComponentsPackagesView getComponentsPackagesManagement();
	
	/**
	 * @return a new Connectors management widget instance.
	 */
	ConnectorsView getConnectorsView();
	
	/**
	 * @return a new Invalid Modules 
	 */
	InvalidModulesView getInvalidModulesView();
	
	/**
	 * <br/>
	 * @return a new Incorrect Modules view widget
	 */
	NotModulesView getIncorrectModulesView();
	
	/**
	 * <br/>
	 * @return a new Not Found Modules view widget.
	 */
	NotFoundModulesView getNotFoundModulesView();
	
	/**
	 * @return a new Source Mappings management widget instance.
	 */
	SourceMappingManagement getSourceMappingManagement();
	
	/**
	 * @return a new ValidConnectorsManagement widget instance.
	 */
	ValidConnectorsManagement getValidConnectorsManagement();
	
	/**
	 * @return a new AllConnectorsManagement widget instance.
	 */
	AllConnectorsManagement getAllConnectorsManagement();
	
	/**
	 * @return a new ErroneousConnectorsManagement widget instance.
	 */
	ErroneousConnectorsManagement getErroneousConnectorsManagement();
	
	/**
	 * @return a new WarningConnectorsManagement widget instance.
	 */
	WarningConnectorsManagement getWarningConnectorsManagement();
	
	/**
	 * @return a new Content Types management widget instance.
	 */
	ContentTypeManagement getContentTypeManagement();
	
	/**
	 * @return a new Categories management widget instance.
	 */
	CategoryManagement getCategoryManagement();
	
	/**
	 * @return a new Routing Domains editor widget instance.
	 */
	RoutingDomainDetailsEditor getRoutingDomainDetailsEditor();
	
	/**
	 * @return the default Routing Domains editor widget instance.
	 */
	DefaultRoutingDomain getDefaultRoutingDomainDetailsEditor();

	/**
	 * @return a new Mapping detail panel instance.
	 */
	MappingDetailPanel getMappingDetailPanel();
	
	/**
	 * @return a new Connector detail panel widget instance.
	 */
	ConnectorDetailPanel getConnectorDetailPanel();
	
	/**
	 * @return a new Component details panel widget instance.
	 */
	ComponentDetailPanel getComponentDetailPanel();
	
	/**
	 * @return a new Content Type creation widget instance.
	 */
	ContentTypeCreation getContentTypeCreation();
	
	/**
	 * @return a new Content Type edition widget instance.
	 */
	ContentTypeEdition getContentTypeEdition();
	
	/**
	 * @return a new Category creation window.
	 */
	CategoryCreationPanel getCategoryCreation();
	
	/**
	 * @return a new Category edition window.
	 */
	CategoryEditionPanel getCategoryEdition();
	
	/**
	 * @return a new BaseManagement window.
	 */
	BasesManagement getBaseManagement();
	
	/**
	 * @return a new PropertiesManagement window.
	 */
	PropertiesManagement getPropertiesManagement();
	
	/**
	 * @return a new instance of PortalCreationPropertiesPanel
	 */
	PortalCreationPropertiesPanel getPortalCreationPropertiesPanel();
	
	/**
	 * @return a new window for Portal properties edition
	 */
	PortalEditionPropertiesPanel getPortalEditionPropertiesPanel();
	
	/**
	 * @return new Page Components management widget.
	 */
	ComponentsInPageManagement getPageComponentsManagement();
	
	/**
	 * @return new Category Content Type Pages management widget instance.
	 */
	CategoryContentPagesManagement getCategoryContentPagesManagement();
	
	/**
	 * @return new Category Content Type Pages management
	 */
	CategoryContentTypePagesManagement getCategoryContentTypePagesManagement();
	
	/**
	 * @return new Category Content Type Page detail widget instance.
	 */
	CategoryContentPageDetailPanel getCategoryContentPageDetailPanel();
	
	/**
	 * @return new Category Page detail panel widget instance.
	 */
	CategoryPageDetailPanel getCategoryPageDetailPanel();
	
	/**
	 * @return new Content Page detail panel widget instance.
	 */
	ContentPageDetailPanel getContentPageDetailPanel();
	
	/**
	 * @return new Page detail panel widget instance
	 */
	PageDetailPanel getPageDetailPanel();

	/**
	 * @return new module configuration fieldset instance. 
	 */
	ConfigurationPanel getConfigurationPanel();
	
	/**
	 * @return new module dependencies configuration fieldset instance
	 */
	DependencesPanel getDependencesPanel();
	
	/**
	 * @return a new instance of {@link CategoryPageTreeStore}
	 */
	CategoryPageTreeStore getCategoryPageTreeStore();
	
	/**
	 * @return a new {@link ContentPageTreeStore}
	 */
	ContentPageTreeStore getContentPageTreeStore();
	
	/**
	 * @return a new Pages tree store.
	 */
	PageTreeStore getPageTreeStore();
	
	/**
	 * @return a north panel instance, used in Design window.
	 */
	NorthPanel getNorthPanel();
	
	/**
	 * @return the right panel at the Design window.
	 */
	ComponentsTreePanel getRightPanel();
	
	/**
	 * @return a new Design instance
	 */
	Design getDesign();
	
	/**
	 * @return a new Palettes Panel instance
	 */
	PalettesPanel getPalettesPanel();
	
	/**
	 * @return a new FrameComponent instance
	 */
	FrameComponent getFrameComponent();
	
	/**
	 * @return a new Frame Column instance.
	 */
	FrameColumn getFrameColumn();
	
	/**
	 * <br/>
	 * @return a new Components Palette window instance
	 */
	ComponentsPaletteWindow getComponentsPaletteWindow();
	
	/**
	 * @return a new Portal Users Management widget instance
	 */
	PortalUsersManagement getPortalUsersManagement();
	
	/**
	 * <br/>
	 * @return a new Nodes Repository Management widget instance
	 */
	NodesRepositoryManagement getNodesRepositoryManagement();
	
	/**
	 * <br/>
	 * @return a new User Details Panel instance
	 */
	UserDetails getUserDetailsPanel();
	
	/**
	 * <br/>
	 * @return a new window that allows to change the password for the user being edited.
	 */
	ChangePwdWindow getChangePwdWindow();
	
	/**
	 * <br/>
	 * @return a new instance of the granted authorities window.
	 */
	GrantedAuthoritiesWindow getGrantedAuthoritiesWindow();
	
	/**
	 * <br/>
	 * @return a new instance of the portal selector window.
	 */
	PortalSelector getPortalSelector();
	
	/**
	 * <br/>
	 * @return a new instance of the portal authorities window.
	 */
	PortalAuthoritiesWindow getPortalAuthoritiesWindow();
	
	/**
	 * Returns a new instance of the component in page name editor.<br/>
	 * @return a new instance of the component in page name editor.<br/>	 
	 */
	ComponentInPageNameEditor getComponentInPageNameEditor();

	/**
	 * @return Configuration popup window for Components in Page
	 */
	ComponentsInPageConfigWindow getComponentsInPageConfigWindow();
	
	/**
	 * Returns the entry point viewport.<br/>
	 * @return the entry point viewport.
	 */
	NrViewport getNRViewport();
	
	/**
	 * Returns the summary widget instance.<br/>
	 * @return the summary widget instance.<br/>
	 */
	RepositorySummaryWidget getRepositorySummaryWidget();

	/**
	 * Returns the repository query widget instance.<br/>
	 * @return the repository query widget instance.<br/>
	 */
	RepositoryQueryWidget getRepositoryQueryWidget();
	
	/**
	 * @return the Users app viewport
	 */
	UsersViewport getUsersViewport();
	
	/**
	 * @return a new Users editor window.
	 */
	UserEditorPanel getUserEditorPanel();
	
	/**
	 * @return a new Change Password window.
	 */
	ChangePortalUserPwdWindow getChangePortalUserPwdWindow();
	
	/**
	 * @return a new Users Management widget.
	 */
	PortalUsersList getPortalUsersList();
	
	/**
	 * Returns the portals viewport.<br/>
	 * @return the portals viewport.
	 */
	PortalViewport getPortalViewport();
	
	/**
	 * @return a AllOwnComponentManagement panel instance
	 */
	AllOwnComponentManagement getAllOwnComponentManagement();
	
	/**
	 * @return a ValidOwnComponentManagement panel instance
	 */
	ValidOwnComponentManagement getValidOwnComponentManagement();
	
	/**
	 * @return a WarningOwnComponentManagement panel instance
	 */
	WarningOwnComponentManagement getWarningOwnComponentManagement();
	
	/**
	 * @return a ErrorOwnComponentManagement panel instance
	 */
	ErrorOwnComponentManagement getErrorOwnComponentManagement();
	
	/**
	 * @return
	 */
	InheritedComponentManagement getInheritedComponentManagement();
	
	/**
	 * @return
	 */
	DefaultPagesManagement getDefaultPagesManagement();
	
	/**
	 * @return
	 */
	TemplatePagesManagement getTemplatePagesManagement();
	
	/**
	 * @return
	 */
	SpecialPagesManagement getSpecialPagesManagement();
	
	/**
	 * @return
	 */
	CategoryPagesManagement getCategoryPagesManagement();
	
	/**
	 * @return
	 */
	ContentTypePagesManagement getContentTypePagesManagement();
	
	/**
	 * @return
	 */
	ContentPagesManagement getContentPagesManagement();
	
	/**
	 * @return the Error Pages Management Panel
	 */
	ErrorPagesManagement getErrorPagesManagement();
	
	/**
	 * @return a new Default Page Detail Panel
	 */
	DefaultPageDetailPanel getDefaultPageDetailPanel();
	
	/**
	 * @return the portal preview panel
	 */
	PortalPreviewPanel getPortalPreviewWidget();
	
	/**
	 * @return the portal default panel
	 */
	PortalDefaultPanel getPortalDefaultPanel();

	/**
	 * @return the localized names window
	 */
	LocalesMappingWindow getLocalizedNamesWindow();

	/**
	 * @return the portal name creation panel
	 */
	PortalNameCreationPanel getPortalNameCreationPanel();
	
	/**
	 * @return the portal name edition panel
	 */
	PortalNameEditionPanel getPortalNameEditionPanel();
	
	/**
	 * @return the panel with the portal tree
	 */
	ParentPortalManagement getParentPortalManagement();

	/**
	 * @return the override dependences w¡ndow
	 */
	OverrideDependencesWindow getOverrideDependencesWindow();

	/**
	 * @return the override configuration w¡ndow
	 */
	OverrideConfigurationWindow getOverrideConfigurationWindow();

	/**
	 * @return the devices management
	 */
	DevicesManagement getDeviceManagement();

	/**
	 * @return the device creation detail panel
	 */
	DeviceCreationPanel getDeviceCreationPanel();

	/**
	 * @return the device edition detail panel
	 */
	DeviceEditionPanel getDeviceEdition();

	/**
	 * @return the widget with the available portal bases
	 */
	AvailableBasesWidget getAvailableBasesWidget();
	
	
	PortalConfigurationsManagement getPortalConfigurationsManagement();
	
	/**
	 * @return the widget with the available portal properties
	 */
	AvailablePropertiesWidget getAvailablePropertiesWidget();

	/**
	 * @return the widget that manages a portal collections
	 */
	CollectionsManagement getCollectionsManagement();

	/**
	 * @return the portals set filters edition panel
	 */
	CollectionEditionPanel getCollectionEditionPanel();
	
	/**
	 * @return the portals set filters creation panel
	 */
	CollectionCreationPanel getCollectionCreationPanel();

	/**
	 * @return the comments management panel
	 */
	CommentsExternalServiceManagement getCommentsExternalServiceManagement();

	/**
	 * @return the comments external service viewport
	 */
	CommentsViewport getCommentsViewport();

	/**
	 * @return the comments management panel
	 */
	CommentsManagement getCommentsManagement();

	/**
	 * @return the content types export window
	 */
	ContentTypesExportWindow getContentTypeExportWindow();

	/**
	 * @return the content types import window
	 */
	ContentTypesImportWindow getContentTypeImportWindow();

	/**
	 * @return the categories export window
	 */
	CategoriesExportWindow getCategoriesExportWindow();

	/**
	 * @return the categories import window
	 */
	CategoriesImportWindow getCategoriesImportWindow();

	/**
	 * @return the conenctors export window
	 */
	ConnectorsExportWindow getConnectorsExportWindow();

	/**
	 * @return the connectors import window
	 */
	ConnectorsImportWindow getConnectorsImportWindow();

	/**
	 * @return the portal names import window
	 */
	PortalImportWindow getPortalImportWindow();

	/**
	 * @return the portal components export window
	 */
	ComponentsExportWindow getComponentsExportWindow();

	/**
	 * @return the portal components import window
	 */
	ComponentsImportWindow getComponentsImportWindow();

	/**
	 * @return the portal inherited components export window
	 */
	InheritedComponentsExportWindow getInheritedComponentsExportWindow();

	/**
	 * @return the source mapping export window
	 */
	MappingExportWindow getMappingExportWindow();

	/**
	 * @return the source mapping import window
	 */
	MappingImportWindow getMappingImportWindow();

	/**
	 * @return the portal pages import window
	 */
	PagesImportWindow getPagesImportWindow();
	
	/**
	 * @return a new PortalsManagement widget instance
	 */
	PortalsManagement getPortalsManagement();

	/**
	 * @return a ComponentsPackageDetail panel
	 */
	ComponentsPackageDetail getComponentsPackageDetail();

	/**
	 * @return a ConnectorsDetail panel
	 */
	ConnectorsDetail getConnectorsDetail();

	/**
	 * @return a PortalDevicesManagement panel
	 */
	PortalDevicesManagement getPortalDevicesManagement();
	
	/**
	 * @return a PortalDeviceEditionPanel panel
	 */
	PortalDeviceEditionPanel getPortalDeviceEditionPanel();

	/**
	 * @return the window with a portal cache info
	 */
	PortalCachePanel getPortalCachePanel();
	
	/**
	 * @return the "About Port@l" window
	 */
	PmsInfoWindow getPmsInfoWindow();

	/**
	 * @return a new IframePanel instance
	 */
	IframePanel getIframePanel();

	/**
	 * @return the window with the environment configuration
	 */
	EnvironmentConfigWindow getEnvironmentConfigWindow();
	
	/**
	 * @return the window with the user info
	 */
	ChangeUserInfoWindow getChangeUserInfoWindow();
	
	/**
	 * @return the window to change the user password
	 */
	ChangeUserPwdWindow getChangeUserPwdWindow();
	
	/**
	 * 
	 * @return @return the window to charge the portal configuration
	 */
	PortalConfigurationWindow getPortalConfigurationWindow();
	
	InheritPortalConfigurationWindow getInheritPortalConfigurationWindow();
	
	IndexersManagement getIndexersManagement();
}
