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


import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.i18n.GuiCommonMessages;
import com.isotrol.impe3.gui.common.i18n.GuiCommonStyles;
import com.isotrol.impe3.gui.common.util.component.ILazyComponent;


/**
 * Utility class that is able to:<ul> <li>display an animated info popup that fades out some time after it is
 * displayed;</li> <li>configure components for lazy initialization;</li> <li>check empty strings;</li> <li>fire Change
 * events for passed components;</li> <li>compute the base for the web application</li> <li>compute the screen
 * width</li> <li>mask and unmask the viewport with a passed message</li> </ul>
 * 
 * @author Andrei Cojocaru
 * 
 */
public final class Util {

	/**
	 * Component that will be masked when using method mask.<br/>
	 */
	private El viewport = new El(RootPanel.get().getElement());

	/**
	 * base URL for the Application Context.<br/>
	 */
	private static String appContextBaseUrl = null;

	private static final String TEMPLATE_INFO_WINDOW = "<div class='ext-mb-icon ext-mb-info'></div>"
		+ "<div class='ext-mb-content' style='position: relative;'>"
		+ "<span class='ext-mb-text'>${MESSAGE}</span></div>";

	private static final String PATTERN_MESSAGE = "\\$\\{MESSAGE\\}";

	/**
	 * Template for icons created with {@link AbstractImagePrototype#getHTML()}<br/>
	 */
	private static final String TEMPLATE_ICON = "<div style='width: 15px; height: 15px'>${IMG}</div>";

	/**
	 * Pattern to be replaced in {@link #TEMPLATE_ICON} by the return value of {@link AbstractImagePrototype#getHTML()}
	 * <br/>
	 */
	private static final String PATTERN_IMG = "${IMG}";

	/**
	 * Time in millis during which the {@link #infoBox} will be visible before sliding out.<br/>
	 */
	private static final int SLIDE_OUT_MILLIS = 3000;

	private static Window infoBox = null;

	private static Timer tSlideAlertBox = new Timer() {
		@Override
		public void run() {
			// animation: slide out
			if (infoBox != null && infoBox.isVisible()) { // may be already
				// closed..
				infoBox.el().slideOut(Direction.UP, FxConfig.NONE);
			}
		}
	};

	/*
	 * Injected deps
	 */

	/**
	 * Validates non empty values for the bound fields.<br/>
	 */
	private NonEmptyStringValidator nonEmptyStringValidator = null;

	/**
	 * Validates when fired.<br/>
	 */
	private ValidatorListener validatorListener = null;

	/**
	 * Generic messages bundle.<br/>
	 */
	private GuiCommonMessages messages = null;

	/**
	 * GuiCommon styles bundle.<br/>
	 */
	private GuiCommonStyles guiCommonStyles = null;

	/**
	 * Default constructor.
	 */
	public Util() {
	}

	/**
	 * Displays the "alert" box.<br/> Cannot be called from within the design panel (central panel must be visible).
	 * 
	 * @param title
	 * @param message
	 */
	private void info(String title, String message) {
		if (infoBox != null && infoBox.isAttached()) {
			infoBox.removeFromParent();
		}
		infoBox = new Window();
		infoBox.setLayoutOnChange(true);
		infoBox.setClosable(false);
		infoBox.setDraggable(false);
		infoBox.addStyleName("x-window-dlg");
		infoBox.setAutoHide(true);

		// display current text into a box similar to MessageBox.info():

		String sHtml = TEMPLATE_INFO_WINDOW.replaceAll(PATTERN_MESSAGE, message);
		Html html = new Html(sHtml);
		infoBox.add(html);

		infoBox.setTitle(title);
		infoBox.show();

		RootPanel pRoot = RootPanel.get();
		Element rootElement = pRoot.getElement();

		// align to right-top corner of main tabPanel:
		// El tpEl = new El(rootElement).child("div.x-tab-panel-body");
		El tpEl = new El(rootElement).child("#" + Constants.CENTER_PANEL_ID);
		infoBox.alignTo(tpEl.dom, "tr-tr", new int[] {-10, 10});

		// animation: fade in
		infoBox.el().fadeIn(FxConfig.NONE);

		// schedule animation: slide out
		tSlideAlertBox.schedule(SLIDE_OUT_MILLIS);
	}

	/**
	 * Masks the viewport with a default "Loading..." message.<br/>
	 */
	public void mask() {
		mask(messages.msgLoading());
	}

	/**
	 * Masks the viewport and shows the passed message.<br/>
	 * @param message
	 * @return
	 */
	public void mask(String message) {
		viewport.mask(message, guiCommonStyles.maskWaiting());
	}

	/**
	 * Unmasks the viewport.<br/>
	 */
	public void unmask() {
		viewport.unmask();
	}

	/**
	 * Displays an "info box" with defalt title as defined at {@link Labels#WINDOW_HEADER_SUCCESS}<br/>
	 * 
	 * @param message
	 * @see #info(String, String)
	 */
	public final void info(String message) {
		info(messages.headerOkWindow(), message);
	}

	/**
	 * Shows a confirm dialog with default header.<br/>
	 * @param message
	 * @param listener
	 */
	public void confirm(String message, Listener<MessageBoxEvent> listener) {
		MessageBox.confirm(messages.headerConfirmWindow(), message, listener).setModal(true);
	}

	/**
	 * Displays an alert as a modal dialog. The alert dialog has the word "Error" for header, and displays the passed
	 * message in its body.<br/>
	 * 
	 * @param message
	 */
	public final void error(String message) {
		error(message, null);
	}

	/**
	 * Displays a GXT alert box titled "Error", and binds it to the passed callback. The alert popup is modal, and its
	 * title is internationalized.
	 * 
	 * @param message will be displayed as the alert box content.
	 * @param callback may be <code>null</code>
	 */
	public void error(String message, Listener<MessageBoxEvent> callback) {
		MessageBox.alert(messages.headerErrorWindow(), message, callback).setModal(true);
	}

	/**
	 * Configures the passed text field to fail validation when having empty string values.<br/> Additionally,
	 * configures the passed field to be validated when rendered.
	 * 
	 * @param field
	 */
	public void configTextFieldForNonEmptyValues(TextField<String> field) {
		field.setAllowBlank(false);
		field.setAutoValidate(true);
		field.setValidator(nonEmptyStringValidator);
		field.addListener(Events.Render, validatorListener);
		field.addListener(Events.OnKeyUp, validatorListener);
		field.addListener(Events.Change, validatorListener);
	}

	/**
	 * @return the screen width
	 */
	public static native int getScreenWidth() /*-{
		return $wnd.screen.width;
	}-*/;

	/**
	 * @return the application context url
	 */
	public static String getBaseApplicationContext() {
		if (appContextBaseUrl == null) {
			int sizeModuleName = GWT.getModuleName().length();
			int sizeModuleBaseUrl = GWT.getModuleBaseURL().length();
			int sizeContext = sizeModuleBaseUrl - sizeModuleName;
			appContextBaseUrl = GWT.getModuleBaseURL().substring(0, sizeContext - 1);
		}
		return appContextBaseUrl;
	}

	/**
	 * Makes the passed component fire a <code>Change</code> event.<br/>
	 * @param component
	 */
	public static boolean fireChangeEvent(Component component) {
		// using the text field editor
		BaseEvent be = new BaseEvent(component);
		return component.fireEvent(Events.Change, be);
	}

	/**
	 * <br/>
	 * @param s
	 * @return <code>true</code>, if the passed String is <code>null</code>, or empty after cleaning leading & trailing
	 * spaces;<code>false</code> otherwise.
	 */
	public static boolean emptyString(String s) {
		return s == null || s.matches("(\\s)*");
	}

	/**
	 * Configures the passed lazy component to be initialized on Render.<br/>
	 * @param <C>
	 * @param c
	 */
	public static <C extends Component & ILazyComponent> void configLazyComponent(final C c) {
		c.addListener(Events.Render, new Listener<ComponentEvent>() {
			/**
			 * (non-Javadoc)
			 * @see com.extjs.gxt.ui.client.event.Listener#handleEvent(com.extjs.gxt.ui.client.event.BaseEvent)
			 */
			public void handleEvent(ComponentEvent be) {
				GWT.log(c.getClass().getName() + ": lazy init", null);
				c.lazyInit();
			}
		});
	}

	/**
	 * Cleans the Spanish special alphabetical characters.<br/>
	 * @param string
	 * @return the ASCII corresponding string.
	 */
	public static String cleanString(String string) {
		return string.replace('\u00C1', 'A') // Á
			.replace('\u00E1', 'a') // á
			.replace('\u00C9', 'E') // É
			.replace('\u00E9', 'e') // é
			.replace('\u00CD', 'I') // Í
			.replace('\u00D3', 'O') // Ó
			.replace('\u00F3', 'o') // ó
			.replace('\u00DA', 'U') // Ú
			.replace('\u00FA', 'u') // ú
			.replace('\u00DC', 'U') // Ü
			.replace('\u00FC', 'u'); // ü
	}

	/**
	 * <br/>
	 * @param tooltip
	 * @return
	 */
	public Html createInvalidIcon(String tooltip) {
		AbstractImagePrototype prototype = GXT.IMAGES.field_invalid();

		Html html = new Html(TEMPLATE_ICON.replace(PATTERN_IMG, prototype.getHTML()));

		ToolTipConfig ttc = new ToolTipConfig(tooltip);
		html.setToolTip(ttc);
		html.getToolTip().addStyleName(guiCommonStyles.gxtInvalidTip());

		return html;
	}

	/**
	 * Load the passed url in browser
	 * @param url the url to load in browser
	 */
	public native void openDocumentHref(String url) /*-{
		$doc.location.href = url;
	}-*/;

	// /**
	// * Name property in JSON file upload result.<br/>
	// */
	// private static final String JSON_PROPERTY_NAME = "nombre";
	//	
	// /**
	// * Path property in JSON file upload result.<br/>
	// */
	// private static final String JSON_PROPERTY_PATH = "ruta";
	//	
	// /**
	// * Mime type property in JSON file upload result.<br/>
	// */
	// private static final String JSON_PROPERTY_MIMETYPE = "mimetype";
	//	
	// /**
	// * Media type property in JSON file upload result.<br/>
	// */
	// private static final String JSON_PROPERTY_MEDIA_TYPE = "tipo";

	// public static RecursoFicheroDTO parseUploadedFileResult(String result) throws JSONException {
	// JSONObject fileJson = (JSONObject) JSONParser.parse(result).isObject();
	//		
	// // create DTO
	// RecursoFicheroDTO fileDto = new RecursoFicheroDTO();
	// fileDto.setMimetype(fileJson.get(JSON_PROPERTY_MIMETYPE).isString().stringValue());
	// fileDto.setNombre(fileJson.get(JSON_PROPERTY_NAME).isString().stringValue());
	// fileDto.setRuta(fileJson.get(JSON_PROPERTY_PATH).isString().stringValue());
	// JSONValue mediaType = fileJson.get(JSON_PROPERTY_MEDIA_TYPE);
	// if (mediaType != null) {
	// fileDto.setTipo(TipoRecurso.valueOf(mediaType.isString().stringValue()));
	// }
	//		
	// return fileDto;
	// }

	/*
	 * Injector methods
	 */
	/**
	 * Injects the generic messages bundle.
	 * @param messages
	 */
	@Inject
	public void setMessages(GuiCommonMessages messages) {
		this.messages = messages;
	}

	/**
	 * @param nonEmptyStringValidator the nonEmptyStringValidator to set
	 */
	@Inject
	public void setNonEmptyStringValidator(NonEmptyStringValidator nonEmptyStringValidator) {
		this.nonEmptyStringValidator = nonEmptyStringValidator;
	}

	/**
	 * @param validatorListener the validatorListener to set
	 */
	@Inject
	public void setValidatorListener(ValidatorListener validatorListener) {
		this.validatorListener = validatorListener;
	}

	/**
	 * Injects the styles bundle.<br/>
	 * @param guiCommonStyles the styles bundle.
	 */
	@Inject
	public void setGuiCommonStyles(GuiCommonStyles guiCommonStyles) {
		this.guiCommonStyles = guiCommonStyles;
	}

}
