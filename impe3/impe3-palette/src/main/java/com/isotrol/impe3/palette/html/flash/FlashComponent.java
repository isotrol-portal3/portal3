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

package com.isotrol.impe3.palette.html.flash;


import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileLoader;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLConstants;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragments;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.Script;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.component.html.Tag;


/**
 * HTML Flash Component.
 * @author Ivan Tejero.
 */
public class FlashComponent implements VisualComponent {

	private FlashModuleConfig config;

	private FlashConfig componentConfig;

	private FileLoader fileLoader;

	private FileData flashFileData;

	private FileData jsFileData;

	private static final String TYPE = "application/x-shockwave-flash";

	private static final String CODEBASE = "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0";

	private static final String QUALITY = "high";

	private static final String PLUGINSPAGE = "http://www.macromedia.com/go/getflashplayer";

	private static final String CLASSID = "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000";

	public void edit() {
		if (config.file() != null && componentConfig != null) {
			flashFileData = fileLoader.loadFromBundle(config.file().getId(), componentConfig.fileUrl());

			if (config.jsFileUrl() != null) {
				jsFileData = fileLoader.loadFromBundle(config.file().getId(), config.jsFileUrl());
			}
		}
	}

	public ComponentResponse execute() {
		edit();
		return ComponentResponse.OK;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				if (flashFileData == null) {
					return HTMLFragments.empty();
				}
				String flashUri = context.getURI(config.file(), componentConfig.fileUrl()).toASCIIString();
				final HTML html = HTML.create(context);
				final Tag div = html.div();
				final Tag objectParent;
				// Script tag
				if (jsFileData != null) {
					div.javascript().add(
						"AC_FL_RunContent('codebase','" + CODEBASE + "','width','" + componentConfig.width().toString()
							+ "','height','" + componentConfig.height().toString() + "','src','"
							+ flashUri.substring(0, flashUri.length() - 4) + "','quality','" + QUALITY
							+ "','pluginspage','" + PLUGINSPAGE + "','movie','"
							+ flashUri.substring(0, flashUri.length() - 4) + "');");
					objectParent = div.tag(HTMLConstants.NOSCRIPT);
				} else {
					objectParent = div;
				}
				Tag object = objectParent.object(componentConfig.height().toString(), componentConfig.width()
					.toString(), CODEBASE, CLASSID);
				object.p(componentConfig.alt());
				object.param("movie", flashUri);
				object.param("quality", "high");
				object.embed(componentConfig.height().toString(), componentConfig.width().toString(), TYPE,
					PLUGINSPAGE, QUALITY, flashUri);
				return html;
			}

			@Override
			public Iterable<Script> getHeaderScripts() {
				if (jsFileData != null) {
					return ImmutableList.of(new Script(context, context.getURI(config.file(), config.jsFileUrl())));
				} else {
					return ImmutableList.of();
				}
			}
		};
	}

	public void setConfig(FlashModuleConfig config) {
		this.config = config;
	}

	@Inject
	public void setComponentConfig(FlashConfig componentConfig) {
		this.componentConfig = componentConfig;
	}

	public void setFileLoader(FileLoader fileLoader) {
		this.fileLoader = fileLoader;
	}

	public void setFlashFile(FileData flashFile) {
		this.flashFileData = flashFile;
	}
}
