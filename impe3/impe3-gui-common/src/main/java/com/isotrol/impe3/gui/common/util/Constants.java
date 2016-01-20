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

/**
 * Utility class that contains common constants used in several apps with GWT.
 * 
 * @author Andrei Cojocaru
 * 
 */
public final class Constants {

	/**
	 * Private constructor = not instantiable from outside.<br/>
	 */
	private Constants() {
	}
	
	/**
	 * Column width in pixels for icon cells.<br/>
	 */
	public static final int COLUMN_ICON_WIDTH = 25;
	
	/**
	 * A simple space constant.<br/>
	 */
	public static final String SPACE = " ";
	
	/**
	 * Empty string.<br/>
	 */
	public static final String EMPTY_STRING = "";

	/** ok image */
	public static final String OK_IMAGE = "ok.gif";
	/** error image */
	public static final String ERROR_IMAGE = "ko.gif";
	
	/**
	 * Label width in detail panels w/ form layout.<br/>
	 */
	public static final int LABEL_WIDTH = 150;

	/**
	 * Field width in detail panels w/ form layout.<br/>
	 */
	public static final int FIELD_WIDTH = 310;
	
	/**
	 * PmsSettings key in the Registry. It's the only key that should be declared
	 * as constant here. All other keys are defined in the PmsSettings files.<br/>
	 */
	public static final String SETTINGS_KEY = "settings";

	/**
	 * Width in pixels for grid columns that contain UUIDs.<br/>
	 */
	public static final int COLUMN_UUID_WIDTH = 210;
	
	/*
	 * Universal definitions for ModelData property descriptors:
	 */
	/**
	 * Universal definition for <b>ID</b> property.<br/>
	 */
	public static final String PROPERTY_ID = "id";

	/**
	 * Universal definition for <b>inherited</b> property.<br/>
	 */
	public static final String PROPERTY_INHERITED = "inherited";

	/**
	 * Universal definition for <b>module</b> property.<br/>
	 */
	public static final String PROPERTY_MODULE = "module";

	/**
	 * Universal definition for the <b>state</b> property.<br/>
	 */
	public static final String PROPERTY_STATE = "state";

	/**
	 * Universal definition for the <b>key</b> property.<br/>
	 */
	public static final String PROPERTY_KEY = "key";

	/**
	 * Universal definition for the <b>name</b> property.<br/>
	 */
	public static final String PROPERTY_NAME = "name";

	/**
	 * Universal definition for the <b>description</b> property.<br/>
	 */
	public static final String PROPERTY_DESCRIPTION = "description";

	/**
	 * Universal definition for the <b>uri</b> property.<br/>
	 */
	public static final String PROPERTY_URI = "uri";
	
	public static final String PROPERTY_VALIDITY ="validity";
	
	
	public static final String PROPERTY_HERENCY ="herency";
	
	
	
	
	
	

	/**
	 * Universal definition for the <b>routable</b> property.<br/>
	 */
	public static final String PROPERTY_ROUTABLE = "routable";

	/**
	 * Universal definition for the <b>visible</b> property.<br/>
	 */
	public static final String PROPERTY_VISIBLE = "visible";

	/**
	 * Universal definition for the <b>creation date</b> property.<br/>
	 */
	public static final String PROPERTY_CREATED = "created";

	/**
	 * Universal definition for the <b>current</b> property.<br/>
	 */
	public static final String PROPERTY_CURRENT = "current";

	/**
	 * Universal definition for the <b>bean</b> property.<br/>
	 */
	public static final String PROPERTY_BEAN = "bean";
	
	/**
	 * Universal definition for the <b>correctness</b> property.<br/>
	 */
	public static final String PROPERTY_CORRECTNESS = "correctness";

	/** value for widths and heights */
	public static final String HUNDRED_PERCENT = "100%";

	/**
	 * 85%<br/>
	 */
	public static final String EIGHTY_FIVE_PERCENT = "85%";
	
	/**
	 * Float value 85%<br/>
	 */
	public static final float EIGHTY_FIVE_PERCENT_FLOAT = .85f;

	/**
	 * Float value 75%
	 */
	public static final float SEVENTY_FIVE_PERCENT_FLOAT = .75f;
	
	/**
	 * 65%<br/>
	 */
	public static final String SIXTY_FIVE_PERCENT = "65%";

	/**
	 * 50%<br/>
	 */
	public static final String FIFTY_PERCENT = "50%";

	/**
	 * 0.5 value.<br/>
	 */
	public static final float FIFTY_PERCENT_FLOAT = .5f;

	/**
	 * Double quotation literal (symbol: ")<br/>
	 */
	public static final String DOUBLE_QUOTATION = "\"";
	
	/**
	 * HTML line break<br/>
	 */
	public static final String HTML_LINE_BREAK = "<br/>";
	
	/**
	 * Standard width for small windows.<br/>
	 */
	public static final int SMALL_WINDOW_WIDTH = 400;
	
	/**
	 * Recommended width for windows with a layout configured 
	 * width {@link FormSupport#getStandardLayout()}<br/>
	 */
	public static final int FORM_WINDOW_WIDTH = 530;

	/**
	 * Width in pixels for the search field.<br/>
	 */
	public static final int SEARCH_FIELD_WIDTH = 150;

	/**
	 * Recommended width for readonly form layout containers.<br/>
	 */
	public static final float READONLY_FORM_WIDTH = 500;

	/**
	 * Recommended margin for border layouts.<br/>
	 */
	public static final int BORDER_LAYOUT_MARGIN = 5;
	
	/**
	 * The Center Panel id, it's used to show the info succes panel {@link Util#info(String)} 
	 */
	public static final String CENTER_PANEL_ID = "center-panel";

}
