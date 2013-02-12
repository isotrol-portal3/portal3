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

package com.isotrol.impe3.pms.gui.client.widget.comment;


import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.inject.Inject;
import com.isotrol.impe3.pms.gui.client.widget.ALeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.ICenterWidget;
import com.isotrol.impe3.pms.gui.client.widget.PmsViewport;


/**
 * @author Manuel Ruiz
 * 
 */
public class CommentsViewport extends PmsViewport {

	/**
	 * the passed current service id
	 */
	private String commentsServiceId = null;
	private CommentsLeftPanel commentsLeftPanel = null;
	private CommentsCenterWidget commentsCenterWidget = null;

	/**
	 * Constructor
	 */
	public CommentsViewport() {
		super();

		// add a listener to show the center panel
		addListener(Events.Show, new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent be) {
				commentsLeftPanel.showInitWidget();
			}
		});
	}

	@Override
	protected boolean isNorthVisible() {
		return true;
	}

	@Override
	protected ALeftPanel getLeftPanel() {
		return commentsLeftPanel;
	}

	@Override
	protected ICenterWidget getCenterWidget() {
		return commentsCenterWidget;
	}

	/**
	 * Injects the center widget.<br/>
	 * @param centerWidget
	 */
	@Inject
	public void setCenterWidget(CommentsCenterWidget centerWidget) {
		this.commentsCenterWidget = centerWidget;
	}

	/**
	 * @param nrLeftPanel the nrLeftPanel to set
	 */
	@Inject
	public void setCommentsLeftPanel(CommentsLeftPanel nrLeftPanel) {
		this.commentsLeftPanel = nrLeftPanel;
	}

	/**
	 * @return the commentsServiceId
	 */
	public String getCommentsServiceId() {
		return commentsServiceId;
	}

	/**
	 * @param commentsServiceId the commentsServiceId to set
	 */
	public void setCommentsServiceId(String commentsServiceId) {
		this.commentsServiceId = commentsServiceId;
		commentsLeftPanel.setCommentsServiceId(commentsServiceId);
	}
}
