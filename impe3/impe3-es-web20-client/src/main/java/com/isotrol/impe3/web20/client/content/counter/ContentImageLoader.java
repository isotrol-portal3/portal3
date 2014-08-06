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
package com.isotrol.impe3.web20.client.content.counter;


import org.springframework.beans.factory.InitializingBean;

import com.google.common.io.ByteStreams;


/**
 * Content counter image loader.
 * @author Andres Rodriguez
 */
public final class ContentImageLoader implements InitializingBean {
	/** Image bytes. */
	private byte[] image;

	/** Default constructor. */
	public ContentImageLoader() {
	}

	public void afterPropertiesSet() throws Exception {
		image = ByteStreams.toByteArray(getClass().getResourceAsStream("counter.png"));
	}

	/**
	 * Returns the loaded image.
	 */
	public byte[] getImage() {
		return image;
	}
}
