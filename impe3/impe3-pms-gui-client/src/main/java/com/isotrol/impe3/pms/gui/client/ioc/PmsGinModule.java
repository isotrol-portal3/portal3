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


import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.isotrol.impe3.gui.common.error.IErrorMessageResolver;
import com.isotrol.impe3.gui.common.event.ComplexListeningStrategy;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.renderer.BooleanCellRenderer;
import com.isotrol.impe3.gui.common.renderer.CenteredValueCellRenderer;
import com.isotrol.impe3.gui.common.renderer.GearCellRenderer;
import com.isotrol.impe3.gui.common.renderer.InformationCellRenderer;
import com.isotrol.impe3.gui.common.util.AlphabeticalStoreSorter;
import com.isotrol.impe3.gui.common.util.Buttons;
import com.isotrol.impe3.gui.common.util.FormSupport;
import com.isotrol.impe3.gui.common.util.NonEmptyStringValidator;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.gui.common.util.ValidatorListener;
import com.isotrol.impe3.gui.common.widget.DateTimePickerWindow;
import com.isotrol.impe3.pms.gui.api.service.ICategoriesServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IComponentsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IConnectorsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IContentTypesServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IDevicesServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IEditionsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IExternalServicesServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IIndexerServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IModuleRegistryServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IPagesServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IPortalsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IRoutingDomainsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.ISessionsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.ISourceMappingsServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.IUsersServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.external.ICommentsExternalServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.external.INodeRepositoryExternalServiceAsync;
import com.isotrol.impe3.pms.gui.api.service.external.IPortalUsersExternalServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.CategoriesServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ComponentsErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ConnectorsErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ContentTypeErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ExternalServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.PageErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.PortalUsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.PortalsServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.error.SessionErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.SimpleErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.UsersServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.i18n.PmsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.PmsSettings;
import com.isotrol.impe3.pms.gui.client.i18n.PmsStyles;
import com.isotrol.impe3.pms.gui.client.i18n.UsersMessages;
import com.isotrol.impe3.pms.gui.client.i18n.UsersSettings;
import com.isotrol.impe3.pms.gui.client.ioc.sp.CategoriesServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.CommentsExternalServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.ComponentsServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.ConnectorsServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.ContentTypesServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.DevicesServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.EditionsServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.ExternalServicesServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.IndexerServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.ModulesRegistryServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.NodeRepositoryExternalServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.PagesServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.PortalUsersServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.PortalsServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.RoutingDomainsServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.SessionsServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.SourceMappingsServiceProvider;
import com.isotrol.impe3.pms.gui.client.ioc.sp.UsersServiceProvider;
import com.isotrol.impe3.pms.gui.client.renderer.CategoryCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.ContentTypeCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.CorrectnessCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.DateCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.InstantiableCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.ModuleNameCellRenderer;
import com.isotrol.impe3.pms.gui.client.renderer.StateCellRenderer;
import com.isotrol.impe3.pms.gui.client.util.AuthoritiesTreeIconProvider;
import com.isotrol.impe3.pms.gui.client.util.CorrectnessResolver;
import com.isotrol.impe3.pms.gui.client.util.Logger;
import com.isotrol.impe3.pms.gui.client.util.PmsListeningStrategy;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.util.StateResolver;
import com.isotrol.impe3.pms.gui.client.widget.DependencesPanel;
import com.isotrol.impe3.pms.gui.client.widget.design.ColumnSelector;
import com.isotrol.impe3.pms.gui.client.widget.design.ColumnsPalette;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalDefaultPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentInPageNameEditor;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentsDependencesTree;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ComponentsInPageConfigWindow;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.DeletePageCallback;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.ToolbarSupport;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.CategoryPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ContentPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ContentTypePagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.DefaultPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.ErrorPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.SpecialPagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.pages.master.TemplatePagesManagement;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.ChangePortalUserPwdWindow;
import com.isotrol.impe3.pms.gui.client.widget.portaluser.UserEditorPanel;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ErroneousConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.connector.ValidConnectorsManagement;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.ChangePwdWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.GrantedAuthoritiesWindow;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.UserDetails;
import com.isotrol.impe3.pms.gui.client.widget.systemmanagement.user.UsersManagement;
import com.isotrol.impe3.pms.gui.common.util.Settings;


/**
 * Defines the dependency relations used for IoC purposes.
 * 
 * @author Andrei Cojocaru
 * 
 */
public class PmsGinModule extends AbstractGinModule {

	/**
	 * The Gin module constructor.
	 */
	public PmsGinModule() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.inject.client.AbstractGinModule#configure()
	 */
	/**
	 * Configures the dependencies bindings<br/>
	 */
	@Override
	protected final void configure() {

		/*
		 * var
		 */
		bind(Util.class).in(Singleton.class);
		bind(Buttons.class).in(Singleton.class);
		bind(AlphabeticalStoreSorter.class).in(Singleton.class);
		bind(FormSupport.class).in(Singleton.class);
		bind(NonEmptyStringValidator.class).in(Singleton.class);
		bind(ValidatorListener.class).in(Singleton.class);
		bind(StateResolver.class).in(Singleton.class);
		bind(AuthoritiesTreeIconProvider.class).in(Singleton.class);
		bind(PmsUtil.class).in(Singleton.class);
		bind(CorrectnessResolver.class).in(Singleton.class);

		/*
		 * Renderers
		 */
		bind(BooleanCellRenderer.class).in(Singleton.class);
		bind(GearCellRenderer.class).in(Singleton.class);
		bind(InstantiableCellRenderer.class).in(Singleton.class);
		bind(ModuleNameCellRenderer.class).in(Singleton.class);
		bind(DateCellRenderer.class).in(Singleton.class);
		bind(CategoryCellRenderer.class).in(Singleton.class);
		bind(ContentTypeCellRenderer.class).in(Singleton.class);
		bind(InformationCellRenderer.class).in(Singleton.class);
		bind(CenteredValueCellRenderer.class).in(Singleton.class);
		bind(CorrectnessCellRenderer.class).in(Singleton.class);

		// System Management widgets:
		bind(UsersManagement.class);

		// widgets instantiated in the Users Management:
		bind(GrantedAuthoritiesWindow.class);
		bind(ChangePwdWindow.class);
		bind(UserDetails.class);

		// widgets instantiated in the Connectors Management:
		bind(ValidConnectorsManagement.class);
		bind(ErroneousConnectorsManagement.class);

		// widgets injected in the portal management:
		bind(DefaultPagesManagement.class);
		bind(TemplatePagesManagement.class);
		bind(SpecialPagesManagement.class);
		bind(ErrorPagesManagement.class);
		bind(CategoryPagesManagement.class);
		bind(ContentPagesManagement.class);
		bind(ContentTypePagesManagement.class);
		bind(ComponentsInPageConfigWindow.class);
		bind(ComponentInPageNameEditor.class);
		bind(PortalDefaultPanel.class);
		bind(DependencesPanel.class);

		// portal users
		bind(UserEditorPanel.class);
		bind(ChangePortalUserPwdWindow.class);

		bind(ToolbarSupport.class);
		bind(ComponentsDependencesTree.class);

		bind(DeletePageCallback.class).in(Singleton.class);

		bind(ColumnsPalette.class);
		bind(ColumnSelector.class);
		
		// other widgets
		bind(DateTimePickerWindow.class);

		/*
		 * i18n and Constants bundles
		 */
		bind(Settings.class).in(Singleton.class);
		bind(GuiCommonStyles.class).in(Singleton.class);
		bind(GuiCommonMessages.class).in(Singleton.class);
		bind(PmsSettings.class).in(Singleton.class);
		bind(PmsMessages.class).in(Singleton.class);
		bind(PmsStyles.class).in(Singleton.class);
		bind(UsersSettings.class).in(Singleton.class);
		bind(UsersMessages.class).in(Singleton.class);

		bind(Logger.class).in(Singleton.class);

		bind(StateCellRenderer.class).in(Singleton.class);

		// the tree selection model used in portals tree is shared by the portals tree
		// and the tab items manager.
		bind(TreePanelSelectionModel.class).in(Singleton.class);

		bind(PmsListeningStrategy.class).in(Singleton.class);
		bind(IComponentListeningStrategy.class).to(ComplexListeningStrategy.class).in(Singleton.class);

		/*
		 * Async service providers
		 */
		bind(ICategoriesServiceAsync.class).toProvider(CategoriesServiceProvider.class).in(Singleton.class);
		bind(IComponentsServiceAsync.class).toProvider(ComponentsServiceProvider.class).in(Singleton.class);
		bind(IConnectorsServiceAsync.class).toProvider(ConnectorsServiceProvider.class).in(Singleton.class);
		bind(IContentTypesServiceAsync.class).toProvider(ContentTypesServiceProvider.class).in(Singleton.class);
		bind(IEditionsServiceAsync.class).toProvider(EditionsServiceProvider.class).in(Singleton.class);
		bind(IModuleRegistryServiceAsync.class).toProvider(ModulesRegistryServiceProvider.class).in(Singleton.class);
		bind(IPagesServiceAsync.class).toProvider(PagesServiceProvider.class).in(Singleton.class);
		bind(IPortalsServiceAsync.class).toProvider(PortalsServiceProvider.class).in(Singleton.class);
		bind(IRoutingDomainsServiceAsync.class).toProvider(RoutingDomainsServiceProvider.class).in(Singleton.class);
		bind(ISessionsServiceAsync.class).toProvider(SessionsServiceProvider.class).in(Singleton.class);
		bind(ISourceMappingsServiceAsync.class).toProvider(SourceMappingsServiceProvider.class).in(Singleton.class);
		bind(IExternalServicesServiceAsync.class).toProvider(ExternalServicesServiceProvider.class).in(Singleton.class);
		bind(IIndexerServiceAsync.class).toProvider(IndexerServiceProvider.class).in(Singleton.class);
		bind(IUsersServiceAsync.class).toProvider(UsersServiceProvider.class).in(Singleton.class);
		bind(INodeRepositoryExternalServiceAsync.class).toProvider(NodeRepositoryExternalServiceProvider.class).in(
			Singleton.class);
		bind(IPortalUsersExternalServiceAsync.class).toProvider(PortalUsersServiceProvider.class).in(Singleton.class);
		bind(IDevicesServiceAsync.class).toProvider(DevicesServiceProvider.class).in(Singleton.class);
		bind(ICommentsExternalServiceAsync.class).toProvider(CommentsExternalServiceProvider.class).in(Singleton.class);

		/*
		 * Errors processor
		 */
		bind(IErrorMessageResolver.class).to(SimpleErrorMessageResolver.class);
		bind(SimpleErrorMessageResolver.class).in(Singleton.class);
		bind(ServiceErrorsProcessor.class).in(Singleton.class);
		/*
		 * PMS specific error message resolvers
		 */
		bind(CategoriesServiceErrorMessageResolver.class).in(Singleton.class);
		bind(ComponentsErrorMessageResolver.class).in(Singleton.class);
		bind(ConnectorsErrorMessageResolver.class).in(Singleton.class);
		bind(ContentTypeErrorMessageResolver.class).in(Singleton.class);
		bind(PageErrorMessageResolver.class).in(Singleton.class);
		bind(UsersServiceErrorMessageResolver.class).in(Singleton.class);
		bind(PortalsServiceErrorMessageResolver.class).in(Singleton.class);
		bind(ExternalServiceErrorMessageResolver.class).in(Singleton.class);
		bind(PortalUsersServiceErrorMessageResolver.class).in(Singleton.class);
		bind(SessionErrorMessageResolver.class).in(Singleton.class);
	}

}
