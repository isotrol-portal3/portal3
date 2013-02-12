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
package com.isotrol.impe3.pms.gui.client.widget.comment;


import java.util.LinkedList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.data.MenuItemModelData;
import com.isotrol.impe3.pms.gui.client.i18n.CommentsMessages;
import com.isotrol.impe3.pms.gui.client.i18n.CommentsStyles;
import com.isotrol.impe3.pms.gui.client.ioc.IPmsFactory;
import com.isotrol.impe3.pms.gui.client.ioc.PmsFactory;
import com.isotrol.impe3.pms.gui.client.widget.ALeftPanel;
import com.isotrol.impe3.pms.gui.client.widget.IInitializableWidget;


/**
 * Comments left panel
 * @author Manuel Ruiz
 * 
 */
public class CommentsLeftPanel extends ALeftPanel {

	/**
	 * supports {@link IInitializableWidget#isInitialized()}<br/>
	 */
	private boolean initialized = false;

	/**
	 * "Comments" item ID.<br/>
	 */
	private static final String ID_COMMENTS_ITEM = "comments-item";

	/*
	 * Injected deps
	 */
	/**
	 * NR specific messages bundle.<br/>
	 */
	private CommentsMessages commentsMessages = null;

	/**
	 * Comments specific styles bundle.<br/>
	 */
	private CommentsStyles commentsStyles = null;

	/**
	 * The center widget.<br/>
	 */
	private CommentsCenterWidget centerWidget = null;

	private ListView<MenuItemModelData> menuList = null;

	private MenuItemModelData iComments = null;

	/**
	 * The Comments Service ID passed through the URL.<br/>
	 */
	private String commentsServiceId = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.ALeftPanel
	 * #configMenuPanel(com.extjs.gxt.ui.client.widget.LayoutContainer)
	 */
	/**
	 * <br/>
	 */
	@Override
	protected void configMenuPanel(LayoutContainer container) {
		this.commentsServiceId = PmsFactory.getInstance().getCommentsViewport().getCommentsServiceId();

		ContentPanel panel = new ContentPanel();
		panel.setAutoHeight(true);
		panel.setHeading(commentsMessages.menuHeaderComments());
		panel.setIconStyle(commentsStyles.menuIconCommentHeader());
		panel.setBodyBorder(false);
		panel.getHeader().addStyleName(getStyles().noSideBorders());
		container.add(panel);

		menuList = new ListView<MenuItemModelData>();
		menuList.setBorders(false);
		menuList.setStore(new ListStore<MenuItemModelData>());
		menuList.setSimpleTemplate(getSettings().tplListView());
		menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		List<MenuItemModelData> lModels = new LinkedList<MenuItemModelData>();
		// item: Comments management
		iComments = new MenuItemModelData(commentsMessages.menuItem2CommentsManagement(), commentsStyles
			.menuIconCommentsManagement(), ID_COMMENTS_ITEM);
		lModels.add(iComments);

		menuList.getStore().add(lModels);

		menuList.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<MenuItemModelData>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<MenuItemModelData> se) {
				if (se.getSelectedItem() != null) {
					String itemId = se.getSelectedItem().get(MenuItemModelData.PROPERTY_ID);
					CommentsManagement widget = null;
					IPmsFactory factory = PmsFactory.getInstance();
					if (itemId.equals(ID_COMMENTS_ITEM)) {
						widget = factory.getCommentsManagement();
					}
					// maybe init the component
					if (!widget.isInitialized()) {
						widget.setCommentsServiceId(commentsServiceId);
						widget.init();
					}
					centerWidget.show((Component) widget);
				}
			}
		});

		panel.add(menuList);

		// show summary widget:
		// showInitWidget();

	}

	protected void showInitWidget() {
		if (menuList != null) {
			if (!menuList.getSelectionModel().getSelectedItems().isEmpty()) {
				menuList.getSelectionModel().deselectAll();
			}
			menuList.getSelectionModel().select(iComments, false);
		}
	}

	/**
	 * Injects the Comments specific message bundle.
	 * @param commentsMessages the commentsMessages to set
	 */
	@Inject
	public void setCommentsMessages(CommentsMessages commentsMessages) {
		this.commentsMessages = commentsMessages;
	}

	/**
	 * Injects the Comments specific styles bundle.
	 * @param commentsStyles the commentsStyles to set
	 */
	@Inject
	public void setCommentsStyles(CommentsStyles commentsStyles) {
		this.commentsStyles = commentsStyles;
	}

	/**
	 * Injects the {@link CommentsCenterWidget NR center widget}.
	 * @param centerWidget the centerWidget to set
	 */
	@Inject
	public void setCenterWidget(CommentsCenterWidget centerWidget) {
		this.centerWidget = centerWidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.isotrol.impe3.pms.gui.common.widget.IInitializableWidget#isInitialized()
	 */
	/**
	 * <br/>
	 */
	public boolean isInitialized() {
		return initialized;
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
	}
}
