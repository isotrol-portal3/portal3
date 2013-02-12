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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties;


import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.portal.PortalTemplateDTO;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalImportWindow;


/**
 * Creates the portal edition form
 * 
 * @author Manuel A. Ruiz Gijon
 */
public class PortalEditionPropertiesPanel extends APortalProperties {

	/**
	 * Just informs wether the "save" action was successful or not.<br/>
	 */
	private AsyncCallback<Void> saveCallback = null;

	/**
	 * Portal name
	 */
	private String name = null;

	/**
	 * Default constructor.
	 */
	public PortalEditionPropertiesPanel() {
	}

	/**
	 * Inits the widget. Must be explicitly called after dependences injection.
	 * 
	 * @param portalTemplate
	 * @return
	 */
	public PortalEditionPropertiesPanel init(PortalTemplateDTO portalTemplate, String name) {
		this.name = name;
		super.initWidget(portalTemplate);

		saveCallback = new AsyncCallback<Void>() {
			public void onFailure(Throwable arg0) {
				getUtilities().unmask();
				getErrorProcessor()
					.processError(arg0, getErrorMessageResolver(), getPmsMessages().msgErrorSavePortal());
			}

			public void onSuccess(Void arg0) {
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSavePortal());
			}
		};

		return this;
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties.APortalProperties#getSaveCallback()
	 */
	@Override
	protected AsyncCallback<Void> getSaveCallback() {
		return saveCallback;
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.basicproperties.APortalProperties#getHeadingText()
	 */
	@Override
	protected String getHeadingText() {
		return name;
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return true;
	}

	/**
	 * Adds export and import buttons
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names.APortalName#addSpecificButtons()
	 */
	@Override
	protected void addSpecificButtons() {
		SelectionListener<ButtonEvent> exportListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				getPortalsService().exportConfig(getPortalTemplate().getId(), new AsyncCallback<String>() {

					public void onSuccess(String result) {
						getUtilities().openDocumentHref(result);
					}

					public void onFailure(Throwable caught) {
						getUtilities().error(getPmsMessages().msgExportError());
					}
				});
			}
		};
		Button bExport = getButtonsSupport().createGenericButton(getPmsMessages().labelExport(),
			getPmsStyles().exportIcon(), exportListener);
		getButtonBar().insert(bExport, 0);
		// TODO enable when working
		bExport.disable();

		SelectionListener<ButtonEvent> importListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				PortalImportWindow w = PmsFactory.getInstance().getPortalImportWindow();
				w.setPortalId(getPortalTemplate().getId());
				w.setPortalImportType(EPortalImportExportType.CONFIGURATION);
				w.show();
			}
		};
		Button bImport = getButtonsSupport().createGenericButton(getPmsMessages().labelImport(),
			getPmsStyles().importIcon(), importListener);
		getButtonBar().insert(bImport, 1);
		// TODO enable when working
		bImport.disable();
	}

}
