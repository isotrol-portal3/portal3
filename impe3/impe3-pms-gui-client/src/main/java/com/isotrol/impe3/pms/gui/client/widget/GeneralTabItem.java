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

package com.isotrol.impe3.pms.gui.client.widget;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class instances must be inserted in the Users central tab panel.
 * 
 * @author Manuel Ruiz
 *
 */
public class GeneralTabItem extends TabItem {

	/**
	 * distancia entre los bordes del TabItem y el {@link #content} dado
	 */
	private static final int MARGINS = 10;
	
	/**
	 * ID del contenido. Sirve para controlar la inserción
	 * de pestañas que no pueden repetirse en el panel central.
	 */
	private String tabItemId = null;

	/**
	 * Widget mostrado en este TabItem
	 */
	private Widget content = null;
	
	/**
	 * Toma valor por defecto: tabItemId = title
	 * @param title
	 * @param content
	 */
	public GeneralTabItem(String title, Widget content) {
		this(title,content,title);
	}
	
	/**
	 * @param title titulo que aparecerá en la pestaña
	 * @param widget Widget-contenido
	 * @param tabItemId descriptor de la pestaña. Utilizado para verificar la unicidad de
	 * algunas instancias en el TabPanel central.
	 */
	public GeneralTabItem(String title,Widget widget,String tabItemId) {
		super(title);
		this.tabItemId = tabItemId;
		this.content = widget;
		
		initComponents();
	}
	
	/**
	 * inicializa los componentes gráficos
	 */
	private void initComponents() {

		setLayout(new BorderLayout());
		BorderLayoutData layoutData = new BorderLayoutData(LayoutRegion.CENTER);
		layoutData.setMargins(new Margins(MARGINS));
		add(content, layoutData);		
	}

	public String getTabItemId() {
		return tabItemId;
	}
	
	public Widget getContent() {
		return content;
	}
}
