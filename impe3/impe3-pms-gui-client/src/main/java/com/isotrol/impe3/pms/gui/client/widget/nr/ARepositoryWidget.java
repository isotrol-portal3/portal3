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
package com.isotrol.impe3.pms.gui.client.widget.nr;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Util;
import com.isotrol.impe3.pms.gui.api.service.external.INodeRepositoryExternalServiceAsync;
import com.isotrol.impe3.pms.gui.client.error.ExternalServiceErrorMessageResolver;
import com.isotrol.impe3.pms.gui.client.error.ServiceErrorsProcessor;
import com.isotrol.impe3.pms.gui.client.i18n.NrMessages;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;

/**
 * Represents widgets that must be initialized with repository ID.
 * 
 * @author Andrei Cojocaru
 *
 */
public class ARepositoryWidget extends ContentPanel implements IInitializableWidget {

	/**
	 * The bound Nodes Repository ID.<br/>
	 */
	private String repositoryId = null;
	
	/**
	 * support for {@link IInitializableWidget#init()}<br/>
	 */
	private boolean initialized = false;
	
	/*
	 * Injected deps
	 */
	/**
	 * The service errors processor
	 */
	private ServiceErrorsProcessor errorProcessor = null;
	
	/**
	 * Window utilities<br/>
	 */
	private Util util = null;
	
	/**
	 * Nodes Repository external service.<br/>
	 */
	private INodeRepositoryExternalServiceAsync nrService = null;
	
	/**
	 * Error message resolver for {@link #nrService}.<br/>
	 */
	private ExternalServiceErrorMessageResolver emrNr = null;
	
	/**
	 * Nodes Repository specific message bundle.<br/>
	 */
	private NrMessages nrMessages = null;
	
	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#init()
	 */
	/**
	 * <br/>
	 */
	public ARepositoryWidget init() {
		assert repositoryId != null : " widget must be injected with a repository ID before initialized";
		
		initialized = true;
		
		return this;
	}

	/* (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}
	
	/**
	 * @return the repositoryId
	 */
	protected String getRepositoryId() {
		return repositoryId;
	}
	
	/**
	 * Injects the NR service proxy.<br/>
	 * @param nrService
	 */
	@Inject
	public void setNrService(INodeRepositoryExternalServiceAsync nrService) {
		this.nrService = nrService;
	}

	/**
	 * Injects the NR specific message bundle.<br/>
	 * @param nrMessages
	 */
	@Inject
	public void setNrMessages(NrMessages nrMessages) {
		this.nrMessages = nrMessages;
	}

	/**
	 * Injects the windows utilities.
	 * @param util the util to set
	 */
	@Inject
	public void setUtil(Util util) {
		this.util = util;
	}
	
	/**
	 * Injects the error message resolver.
	 * @param emrNr the emrNr to set
	 */
	@Inject
	public void setEmrNr(ExternalServiceErrorMessageResolver emrNr) {
		this.emrNr = emrNr;
	}

	/**
	 * @return the util
	 */
	protected Util getUtil() {
		return util;
	}
	
	/**
	 * @return the Nodes Repository async service proxy.
	 */
	protected INodeRepositoryExternalServiceAsync getNrService() {
		return nrService;
	}
	
	/**
	 * @return the emrNr
	 */
	protected ExternalServiceErrorMessageResolver getNrErrorMessageResolver() {
		return emrNr;
	}

	/**
	 * @return the NR messages bundle.
	 */
	protected NrMessages getNrMessages() {
		return nrMessages;
	}
	
	/**
	 * @param errorProcessor
	 */
	@Inject
	public void setErrorProcessor(ServiceErrorsProcessor errorProcessor) {
		this.errorProcessor = errorProcessor;
	}
	
	/**
	 * @return the service errors processor.
	 */
	protected final ServiceErrorsProcessor getErrorProcessor() {
		return errorProcessor;
	}
}
