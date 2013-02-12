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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;


/**
 * @author Andrei Cojocaru
 * 
 */
public class ContentTypeCreation extends AContentTypeEdition {

	/**
	 * Constructor needed for any ADataBoundContentPanel.<br/>
	 * 
	 * @param model
	 */
	public ContentTypeCreation() {
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.AContentTypeEdition#getHeadingText()
	 */
	@Override
	protected String getHeadingText() {
		return getPmsMessages().headerContentTypeCreation();
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.AContentTypeEdition#getOperationSuffix()
	 */
	@Override
	protected String getOperationSuffix() {
		return "_creation";
	}

	/**
	 * <br/> (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.client.widget.infarchitecture.contenttypes.AContentTypeEdition #getSaveCallback()
	 */
	@Override
	protected AsyncCallback<ContentTypeDTO> getSaveCallback() {
		return new AsyncCallback<ContentTypeDTO>() {
			public void onFailure(Throwable arg0) {
				getUtilities().unmask();
				getErrorProcessor().processError(arg0, getErrorMessageResolver(),
					getPmsMessages().msgErrorSaveContentType());
			}

			public void onSuccess(ContentTypeDTO arg0) {
				hide();
				getUtilities().unmask();
				getUtilities().info(getPmsMessages().msgSuccessSaveContentType());
			}
		};
	}

	/**
	 * <br/> (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.util.databinding.IDetailPanel#isEdition()
	 */
	public boolean isEdition() {
		return false;
	}
}
