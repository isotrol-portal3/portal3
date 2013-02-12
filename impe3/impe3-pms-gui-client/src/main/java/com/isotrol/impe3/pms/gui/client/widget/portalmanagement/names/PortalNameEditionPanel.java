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

package com.isotrol.impe3.pms.gui.client.widget.portalmanagement.names;


import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.util.PmsUtil;
import com.isotrol.impe3.pms.gui.client.widget.NavigationPanel;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.EPortalImportExportType;
import com.isotrol.impe3.pms.gui.client.widget.portalmanagement.PortalImportWindow;


/**
 * Panel to edit a portal name
 * 
 * @author Manuel A. Ruiz Gijon
 */
public class PortalNameEditionPanel extends APortalName {

	/** Navigation (breadcumb) panel */
	private NavigationPanel navigation = null;
	
	/**
	 * Shared objects container.<br/>
	 */
	private PmsUtil pmsUtil = null;

	/**
	 * Default constructor.
	 */
	public PortalNameEditionPanel() {
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.portalmanagement.config.APortalProperties#getHeadingText()
	 */
	@Override
	protected String getHeadingText() {
		return getPortalNameDto().getName().getDisplayName();
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return true;
	}

	@Override
	protected void savePortalName(PortalNameDTO portalNameDto) {

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {

			public void onSuccess(Void result) {
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSavePortal());
				TextField<String> tfname = PortalNameEditionPanel.this.getTfName();
				if (tfname.isDirty()) {
					navigation.changeLastItem(tfname.getValue());
				}
			}

			public void onFailure(Throwable caught) {
				getUtilities().unmask();
				getErrorProcessor().processError(caught, getErrorMessageResolver(),
					getPmsMessages().msgErrorSavePortal());
			}
		};
		getPortalsService().setName(portalNameDto, callback);
	}

	/**
	 * @param navigation the navigation to set
	 */
	@Inject
	public void setNavigation(NavigationPanel navigation) {
		this.navigation = navigation;
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
				getPortalsService().exportName(getPortalNameDto().getId(), new AsyncCallback<String>() {
					
					public void onSuccess(String result) {
						pmsUtil.exportPmsFile(result);
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
		
		SelectionListener<ButtonEvent> importListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				PortalImportWindow w = PmsFactory.getInstance().getPortalImportWindow();
				w.setPortalId(getPortalNameDto().getId());
				w.setPortalImportType(EPortalImportExportType.NAMES);
				w.show();
			}
		};
		Button bImport = getButtonsSupport().createGenericButton(getPmsMessages().labelImport(),
			getPmsStyles().importIcon(), importListener);
		getButtonBar().insert(bImport, 1);
	}

	/**
	 * @param pmsUtil the pmsUtil to set
	 */
	@Inject
	public void setPmsUtil(PmsUtil pmsUtil) {
		this.pmsUtil = pmsUtil;
	}

}
