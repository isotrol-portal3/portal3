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

package com.isotrol.impe3.gui.common.util;


import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.event.IComponentListeningStrategy;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.component.IDetailPanel;


/**
 * @author Manuel Ruiz
 * 
 */
public final class Buttons {

	/**
	 * Generic messages bundle.
	 */
	private GuiCommonMessages messages = null;

	/**
	 * Generic styles bundle.
	 */
	private GuiCommonStyles styles = null;

	/**
	 * Constructor provided with injectable dependencies.
	 * @param messages
	 * @param styles
	 */
	@Inject
	public Buttons(GuiCommonMessages messages, GuiCommonStyles styles) {
		super();
		this.messages = messages;
		this.styles = styles;
	}

	/**
	 * Añade un botón "Cerrar" en la cabecera del ContentPanel dado. Tras la pulsación de dicho boton, el ContentPanel
	 * dado será eliminado de su padre.
	 * 
	 * @param cp
	 */
	public void addCloseButton(final ContentPanel cp) {
		addCloseButton(cp, false);
	}

	/**
	 * Añade un botón "Cerrar" en la cabecera del ContentPanel dado
	 * 
	 * @param cp
	 * @param hide <ul> <li>si <code>true</code>, entonces el panel dado se ocultará;</li> <li>si <code>false</code>,
	 * entonces el panel dado será eliminado del contenedor padre.</li> </ul>
	 */
	public void addCloseButton(final ContentPanel cp, final boolean hide) {
		addCloseButton(cp, hide, null);
	}

	/**
	 * Adds a "Close" button to the ContentPanel header.<br/>
	 * 
	 * @param cp
	 * @param hide
	 * @param extraListener
	 */
	public void addCloseButton(final ContentPanel cp, final boolean hide,
		SelectionListener<IconButtonEvent> extraListener) {

		ToolButton closeButton = new ToolButton("x-tool-close");

		SelectionListener<IconButtonEvent> standardListener = new SelectionListener<IconButtonEvent>() {
			@Override
			public void componentSelected(IconButtonEvent ce) {
				if (hide) {
					cp.hide();
				} else {
					cp.removeFromParent();
				}
			}
		};
		closeButton.addSelectionListener(standardListener);

		if (extraListener != null) {
			closeButton.addSelectionListener(extraListener);
		}

		cp.getHeader().addTool(closeButton);
	}

	/**
	 * Adds a "Refresh" button to the header of the passed ContentPanel.<br/>
	 * 
	 * @param panel ContentPanel to create the "Refresh" button on.
	 * @param listener "Refresh" button "click" callback
	 */
	public void addRefreshButton(ContentPanel panel, SelectionListener<IconButtonEvent> listener) {
		ToolButton tbRefresh = createRefreshToolButton(listener);
		panel.getHeader().addTool(tbRefresh);
	}

	/*
	 * Toolbar-related buttons:
	 */
	/**
	 * Adds a "Refresh" button to the passed ToolBar.<br/>
	 * 
	 * @param toolbar
	 * @param listener
	 */
	public ToolButton addRefreshButton(ToolBar toolbar, SelectionListener<IconButtonEvent> listener) {
		ToolButton tbRefresh = createRefreshToolButton(listener);
		toolbar.add(tbRefresh);
		return tbRefresh;
	}

	/**
	 * Adds an "Add" button to the passed toolbar.<br/>
	 * 
	 * @param toolBar
	 */
	public Button addAddButton(ToolBar toolBar, SelectionListener<ButtonEvent> listener, String tooltip) {
		Button b = createAddButton(listener, tooltip);
		toolBar.add(b);
		return b;
	}

	/**
	 * Adds an "Add" button to the passed toolbar.<br/>
	 * @param toolBar
	 * @param listener
	 * @return the button
	 */
	public Button addAddButton(ToolBar toolBar, SelectionListener<ButtonEvent> listener) {
		return addAddButton(toolBar, listener, "");
	}

	/**
	 * Adds a "Help" button to the passed ToolBar.<br/>
	 * 
	 * @param toolbar
	 * @param url the url to open
	 */
	public ToolButton addHelpButton(ToolBar toolbar, final String url) {
		ToolButton tbHelp = createHelpToolButton(url);
		toolbar.add(tbHelp);
		return tbHelp;
	}

	/**
	 * Just creates the "Add" button.<br/>
	 * @param listener the button listener. May be <code>null</code>
	 * @param tooltip the tooltip text. May be <code>null</code>
	 * @return
	 */
	public Button createAddButton(SelectionListener<ButtonEvent> listener, String tooltip) {
		String tip = tooltip;
		if (tip == null) {
			tip = messages.ttAdd();
		}
		return createGenericButton(messages.labelAdd(), tooltip, styles.iNew(), listener);
	}

	/**
	 * Creates a generic Button for the specified params.<br/> Does not add it to any container.
	 * 
	 * @param label text to display
	 * @param style tool style. May be <code>null</code>.
	 * @param listener tool selection listener. May be <code>null</code>.
	 * @return the created Button
	 */
	public Button createGenericButton(String label, String style, SelectionListener<ButtonEvent> listener) {
		return createGenericButton(label, null, style, listener);
	}

	/**
	 * Creates a generic Button for the specified params.<br/> Does not add it to any container.<br/>
	 * @param label text to display on the button
	 * @param tooltip tooltip shown when hovering the button. May be <code>null</code>
	 * @param style the button style. May be <code>null</code>
	 * @param listener button selection listener. May be <code>null</code>
	 * @return
	 */
	public Button createGenericButton(String label, String tooltip, String style,
		SelectionListener<ButtonEvent> listener) {
		Button b = new Button(label);
		if (!Util.emptyString(tooltip)) {
			b.setToolTip(tooltip);
		}
		if (style != null) {
			b.setIconStyle(style);
		}
		if (listener != null) {
			b.addSelectionListener(listener);
		}
		return b;
	}

	/**
	 * Adds a "Delete" button to the passed toolbar.<br/>
	 * 
	 * @param toolbar
	 * @param listener
	 */
	public Button addDeleteButton(ToolBar toolbar, SelectionListener<ButtonEvent> listener, String tooltip) {
		Button bDelete = createDeleteButton(listener, tooltip);
		toolbar.add(bDelete);
		return bDelete;
	}

	/**
	 * Adds a "Delete" button to the passed toolbar.<br/>
	 * 
	 * @param toolbar
	 * @param listener
	 */
	public Button addDeleteButton(ToolBar toolbar, SelectionListener<ButtonEvent> listener) {
		return addDeleteButton(toolbar, listener, "");
	}

	/**
	 * Creates a "Delete" button<br/>
	 * 
	 * @param listener
	 * @param tooltip
	 * @return
	 */
	public Button createDeleteButton(SelectionListener<ButtonEvent> listener, String tooltip) {
		String tip = tooltip;
		if (tip == null) {
			tip = messages.ttDelete();
		}
		return createGenericButton(messages.labelDelete(), tip, styles.iDelete(), listener);
	}

	/**
	 * Adds an "Accept" button to the passed toolbar.<br/>
	 * 
	 * @param toolbar
	 * @param listener
	 */
	public Button addAcceptButton(ToolBar toolbar, SelectionListener<ButtonEvent> listener) {
		return addGenericButton(messages.labelAccept(), styles.iAccept(), toolbar, listener);
	}

	/**
	 * Adds a "Reject" button to the passed toolbar.<br/>
	 * @param toolbar
	 * @param listener
	 * @return
	 */
	public Button addRejectButton(ToolBar toolbar, SelectionListener<ButtonEvent> listener) {
		return addGenericButton(messages.labelReject(), styles.iReject(), toolbar, listener);
	}

	/**
	 * Creates and adds a button to the passed toolbar.<br/>
	 * 
	 * @param text button label
	 * @param style icon style
	 * @param toolbar button container
	 * @param listener the button listener. May be <code>null</code> for subsequent configurations
	 * @return the added button.
	 */
	public Button addGenericButton(String text, String style, ToolBar toolbar, SelectionListener<ButtonEvent> listener) {
		return addGenericButton(text, null, style, toolbar, listener);
	}

	/**
	 * Creates and adds a button to the passed toolbar.<br/>
	 * @param text button label
	 * @param tooltip button tooltip. May be <code>null</code>
	 * @param style icon style
	 * @param toolbar button container
	 * @param listener the button listener. May be <code>null</code> for subsequent configurations
	 * @return
	 */
	public Button addGenericButton(String text, String tooltip, String style, ToolBar toolbar,
		SelectionListener<ButtonEvent> listener) {
		Button button = createGenericButton(text, tooltip, style, listener);
		toolbar.add(button);
		return button;
	}

	/**
	 * Adds a separator toolitem to the passed toolbar.<br/>
	 * 
	 * @param toolbar
	 */
	public void addSeparator(ToolBar toolbar) {
		toolbar.add(new SeparatorToolItem());
	}

	/**
	 * Adds an "Edit"<br/>
	 * 
	 * @param toolbar
	 * @param edit
	 * @return
	 */
	public Button addEditButton(ToolBar toolbar, SelectionListener<ButtonEvent> lEdit) {
		return addEditButton(toolbar, lEdit, null);
	}

	/**
	 * Adds an "Edit"<br/>
	 * @param toolbar
	 * @param lEdit
	 * @param tooltip
	 * @return the added button
	 */
	public Button addEditButton(ToolBar toolbar, SelectionListener<ButtonEvent> lEdit, String tooltip) {
		Button bEdit = createEditButton(lEdit, tooltip);
		toolbar.add(bEdit);
		return bEdit;
	}

	/**
	 * Creates an "Edit"<br/>
	 * @param lEdit
	 * @param tooltip
	 * @return
	 */
	public Button createEditButton(SelectionListener<ButtonEvent> lEdit, String tooltip) {
		return createGenericButton(messages.labelEdit(), tooltip, styles.iEdit(), lEdit);
	}

	/**
	 * Creates a "Refresh" tool button
	 * @param listener
	 * @return
	 */
	private ToolButton createRefreshToolButton(SelectionListener<IconButtonEvent> listener) {
		ToolButton tbRefresh = new ToolButton(styles.gxtToolRefresh());
		if (listener != null) {
			tbRefresh.addSelectionListener(listener);
		}
		tbRefresh.setToolTip(messages.ttRefresh());
		return tbRefresh;
	}

	/**
	 * Creates a "Help" tool button
	 * @param listener
	 * @return the creted ToolButton
	 */
	public ToolButton createHelpToolButton(final String url) {
		ToolButton tbHelp = new ToolButton(styles.gxtToolHelp());
		if (url != null) {
			SelectionListener<IconButtonEvent> listener = new SelectionListener<IconButtonEvent>() {
				@Override
				public void componentSelected(IconButtonEvent ce) {
					com.google.gwt.user.client.Window.open(url, "help", "");
				}
			};
			tbHelp.addSelectionListener(listener);
		}
		tbHelp.setToolTip(messages.ttHelp());
		return tbHelp;
	}

	/**
	 * Creates a Cancel button.<br/>
	 * @param listener may be <code>null</code>
	 * @return the created button.
	 */
	public Button createCancelButton(SelectionListener<ButtonEvent> listener) {
		return createGenericButton(messages.labelCancel(), null, listener);
	}

	/**
	 * Creates a button to cancel the actions in a detail panel and close the window.<br/> When selected, the button
	 * prompts for confirm only if the detailPanel is dirty.<br/> The created button is not added automatically to any
	 * container (caller must do that).
	 * 
	 * @param detailPanel
	 * @return
	 */
	public Button createCancelButtonForDetailWindows(final IDetailPanel detailPanel, final Window w) {
		SelectionListener<ButtonEvent> lCancel = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent be) {
				if (detailPanel.isDirty()) {
					// if dirty, ask for confirm:
					Listener<MessageBoxEvent> wListener = new Listener<MessageBoxEvent>() {
						public void handleEvent(MessageBoxEvent we) {
							Button clicked = we.getButtonClicked();
							if (clicked != null && clicked.getItemId().equals(Dialog.YES)) {
								w.hide();
							}
						}
					};
					MessageBox.confirm(messages.headerConfirmWindow(), messages.msgConfirmClose(), wListener).setModal(
						true);
				} else { // close without confirm:
					w.hide();
				}
			}
		};
		return createCancelButton(lCancel);
	}

	/**
	 * @param <W>
	 * @param wDetail
	 * @return
	 */
	public <W extends Window & IDetailPanel> Button createCancelButtonForDetailPanels(W wDetail) {
		return createCancelButtonForDetailWindows(wDetail, wDetail);
	}

	/**
	 * Creates a "Save" button for the passed detail panel.<br/> The button is configured to be initially enabled only
	 * if the passed detail panel has valid values. Its <code>enabled</code> state may change according to changes in
	 * the components of the passed changeSourceComponents param: on each <code>Change</code> event capture, the
	 * <code>valid</code> and <code>dirty</code> states are checked on the passed detail panel and the created button
	 * state will be enabled only if both are <code>true</code>.<br/> Since the <code>valid</code> state is checked,
	 * notice that every component <b>MUST</b> be initialized with its corresponding data.<br/>
	 * 
	 * The created button is not automatically added to any container.<br/>
	 * 
	 * @param detailPanel
	 * @param listener
	 * @return created Button
	 */
	public <C extends Component & IDetailPanel> Button createSaveButtonForDetailPanels(final C detailPanel,
		SelectionListener<ButtonEvent> listener, List<Component> changeSourceComponents,
		IComponentListeningStrategy context) {

		final Button bAccept = createAcceptButton(listener, null);

		Listener<BaseEvent> lEnableSave = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent fe) {
				maybeEnableButton(bAccept, detailPanel);
			}
		};
		detailPanel.addListener(Events.Render, lEnableSave);
		for (Component component : changeSourceComponents) {
			context.configListeners(component, lEnableSave);
		}

		return bAccept;
	}
	
	/**
	 * Creates a "Apply" button for the passed detail panel.<br/> The button is configured to be initially enabled only
	 * if the passed detail panel has valid values. Its <code>enabled</code> state may change according to changes in
	 * the components of the passed changeSourceComponents param: on each <code>Change</code> event capture, the
	 * <code>valid</code> and <code>dirty</code> states are checked on the passed detail panel and the created button
	 * state will be enabled only if both are <code>true</code>.<br/> Since the <code>valid</code> state is checked,
	 * notice that every component <b>MUST</b> be initialized with its corresponding data.<br/>
	 * 
	 * The apply button is not automatically added to any container.<br/>
	 * 
	 * @param detailPanel
	 * @param listener
	 * @return created Button
	 */
	public <C extends Component & IDetailPanel> Button createApplyButtonForDetailPanels(final C detailPanel,
		SelectionListener<ButtonEvent> listener, List<Component> changeSourceComponents,
		IComponentListeningStrategy context) {

		final Button bApply = createApplyButton(listener, null);

		Listener<BaseEvent> lEnableSave = new Listener<BaseEvent>() {
			public void handleEvent(BaseEvent fe) {
				maybeEnableButton(bApply, detailPanel);
			}
		};
		detailPanel.addListener(Events.Render, lEnableSave);
		for (Component component : changeSourceComponents) {
			context.configListeners(component, lEnableSave);
		}

		return bApply;
	}

	/**
	 * Enables or disables the passed button, according to the <code>dirty</code> and <code>valid</code> states of the
	 * passed detail panel.<br/>
	 * 
	 * @param button the "Accept" button
	 * @param detailPanel the detail panel.
	 */
	private void maybeEnableButton(Button button, IDetailPanel detailPanel) {
		if (detailPanel.isValid() && (!detailPanel.isEdition() || detailPanel.isDirty())) {
			button.enable();
		} else {
			button.disable();
		}
	}

	/**
	 * Creates a standard "Accept" button<br/>
	 * @param listener
	 * @return
	 */
	public Button createAcceptButton(SelectionListener<ButtonEvent> listener) {
		return createAcceptButton(listener, null);
	}

	/**
	 * Creates a standard "Accept" button.<br/>
	 * 
	 * @param listener
	 * @return the created button
	 */
	public Button createAcceptButton(SelectionListener<ButtonEvent> listener, String tooltip) {
		String tip = tooltip;
		if (Util.emptyString(tip)) {
			tip = messages.ttAccept();
		}
		return createGenericButton(messages.labelAccept(), tip, styles.iAccept(), listener);
	}
	
	/**
	 * Creates a standard "Apply" button.<br/>
	 * 
	 * @param listener
	 * @return the created button
	 */
	public Button createApplyButton(SelectionListener<ButtonEvent> listener, String tooltip) {
		String tip = tooltip;
		if (Util.emptyString(tip)) {
			tip = messages.ttApply();
		}
		return createGenericButton(messages.labelApply(), tip, styles.iAccept(), listener);
	}

	/**
	 * Creates a "Save" button.<br/>
	 * 
	 * @param listener
	 * @return the created button
	 */
	public Button createSaveButton(SelectionListener<ButtonEvent> listener) {
		Button button = new Button(messages.labelSave());
		button.setIconStyle(styles.iSave());
		button.addSelectionListener(listener);
		return button;
	}

	/**
	 * Creates a icon "Save and close" button.<br/>
	 * 
	 * @param listener
	 * @return the created button
	 */
	public Button createSaveAndCloseButton(SelectionListener<ButtonEvent> listener) {
		Button button = new Button(messages.labelSaveQuit());
		button.setIconStyle(styles.iSaveQuit());
		button.addSelectionListener(listener);
		return button;
	}

	/*
	 * Icon buttons:
	 */

	/**
	 * Creates and adds an "Edit" button to the passed LayoutContainer <br/>
	 * 
	 * @param lc button container
	 * @param listener button listener
	 * @return the created button.
	 */
	public IconButton addEditionIconButton(LayoutContainer lc, SelectionListener<IconButtonEvent> listener) {
		return addGenericIconButton(messages.labelEdit(), styles.iEdit(), lc, listener);
	}

	/**
	 * Adds a button with trash icon to the passed container.<br/>
	 * 
	 * @param lc container for newly added button.
	 * @param listener button listener
	 * @return configured button.
	 */
	public IconButton addTrashIconButton(LayoutContainer lc, SelectionListener<IconButtonEvent> listener) {
		return addGenericIconButton(messages.labelDelete(), styles.iTrash(), lc, listener);
	}

	/**
	 * Creates and adds a generic icon button. <br/>
	 * 
	 * @param title button title (tooltip)
	 * @param style button style name
	 * @param lc button container
	 * @param listener button listener
	 * @return the configured button
	 */
	public IconButton addGenericIconButton(String title, String style, LayoutContainer lc,
		SelectionListener<IconButtonEvent> listener) {
		IconButton button = createGenericIconButton(title, style, listener);
		lc.add(button);
		return button;
	}

	/**
	 * Creates a generic icon button. Does not add it to any container.<br/>
	 * @param tooltip tooltip displayed when hovering the button
	 * @param style icon style of the button
	 * @param listener button selection listener. May be <code>null</code>
	 * @return the created button
	 */
	public IconButton createGenericIconButton(String tooltip, String style, SelectionListener<IconButtonEvent> listener) {
		IconButton button = new IconButton(style, listener);
		button = new IconButton(style);
		if (listener != null) {
			button.addSelectionListener(listener);
		}
		button.addStyleName(styles.marginRight4px());
		button.setToolTip(new ToolTipConfig(tooltip));

		return button;
	}

	/**
	 * Creates a "Search" icon button.<br/>
	 * @param listener
	 * @return
	 */
	public IconButton createSearchIconButton(SelectionListener<IconButtonEvent> listener) {
		return createGenericIconButton(messages.ttSearch(), styles.iSearch(), listener);
	}

	/**
	 * Creates a "Search" button<br/>
	 * @param listener
	 * @return
	 */
	public Button createSearchButton(SelectionListener<ButtonEvent> listener) {
		return createGenericButton(messages.labelSearch(), messages.ttSearch(), styles.iSearch(), listener);
	}

	/*
	 * Added methods that are not needed in GRM:
	 */

	/**
	 * Adds a "preview" button to the passed ToolBar.<br/>
	 * 
	 * @param toolBar
	 * @param listener
	 */
	public ToolButton addPreviewButton(ToolBar toolBar, SelectionListener<IconButtonEvent> listener) {
		ToolButton tbPreview = createPreviewToolButton(listener);
		toolBar.add(tbPreview);
		return tbPreview;
	}

	/**
	 * Creates a "Preview" button
	 * @param listener
	 * @return
	 */
	private ToolButton createPreviewToolButton(SelectionListener<IconButtonEvent> listener) {
		ToolButton tbRefresh = new ToolButton(styles.iPreview(), listener);
		tbRefresh.setToolTip(messages.ttPreviewOffline());
		return tbRefresh;
	}

	/**
	 * Closes the highest z-index window.<br/>
	 */
	public void closeActiveWindow() {
		Window active = WindowManager.get().getActive();
		if (active != null) {
			active.hide();
		}
	}
}

/**
 * Listens for <code>Render</code> events, once per component that issued the event.<br/> When listened as many times as
 * indicated in {@link RenderListener#times} field, checks the detail panel for the <code>valid</code> state and enables
 * or disables the {@link RenderListener#button} according to its value.
 * 
 * @author Andrei Cojocaru
 * 
 */
class RenderListener implements Listener<ComponentEvent> {

	private int times = 0;
	private IDetailPanel detailPanel = null;
	private Button button = null;

	public RenderListener(IDetailPanel detailPanel, Button bAccept, int times) {
		this.detailPanel = detailPanel;
		this.button = bAccept;
		this.times = times;
	}

	public void handleEvent(ComponentEvent be) {
		if (be.getType() == Events.Render) {
			times--;
			Component c = be.getComponent();
			c.removeListener(Events.Render, this);
		}
		if (times == 0) {
			button.setEnabled(detailPanel.isValid() && !detailPanel.isEdition());
		}
	}
}
