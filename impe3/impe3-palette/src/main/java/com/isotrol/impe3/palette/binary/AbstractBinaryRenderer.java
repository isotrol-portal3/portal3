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

package com.isotrol.impe3.palette.binary;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;
import com.isotrol.impe3.api.component.BinaryRenderer;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.palette.Loggers;


/**
 * Base class for binary renderers. This class IS NOT THREAd SAFE.
 * @author Andres Rodriguez
 */
public abstract class AbstractBinaryRenderer implements BinaryRenderer {
	/** Render context. */
	private final RenderContext context;
	/** File name. */
	private final String fileName;
	/** Whether to stream output. */
	private final boolean stream;
	/** Rendered output. */
	private byte[] output = null;

	protected AbstractBinaryRenderer(RenderContext context, ModuleConfig moduleConfig, ComponentConfig componentConfig) {
		this.context = checkNotNull(context, "Render context");
		// File name
		if (componentConfig != null && componentConfig.overrideModule()) {
			this.fileName = componentConfig.fileName();
		} else if (moduleConfig != null) {
			this.fileName = moduleConfig.fileName();
		} else {
			this.fileName = null;
		}
		// Stream
		if (moduleConfig != null) {
			this.stream = moduleConfig.streamingOutput();
		} else {
			this.stream = false;
		}
	}

	protected final RenderContext getContext() {
		return context;
	}

	/**
	 * @see com.isotrol.impe3.api.component.BinaryRenderer#getFileName()
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @see com.isotrol.impe3.api.component.BinaryRenderer#getLength()
	 */
	public Integer getLength() {
		if (output != null) {
			return output.length;
		}
		return null;
	}

	/**
	 * @see com.isotrol.impe3.api.component.BinaryRenderer#prepare()
	 */
	public final void prepare() {
		doPrepare();
		if (stream) {
			final ByteArrayOutputStream bos = new ByteArrayOutputStream(4096);
			try {
				doWrite(bos);
			} catch (Exception e) {
				Loggers.palette().error("Unable to pre-render binary output", e);
				throw new RuntimeException(e);
			}
			output = bos.toByteArray();
		}
	}

	/**
	 * Perform preparation operations EXCEPT rendering.
	 */
	protected void doPrepare() {
	}

	/**
	 * @see com.isotrol.impe3.api.component.BinaryRenderer#write(java.io.OutputStream)
	 */
	public final void write(OutputStream os) throws IOException {
		if (stream) {
			if (output != null) {
				ByteStreams.copy(new ByteArrayInputStream(output), os);
			}
		} else {
			doWrite(os);
		}
	}

	/**
	 * Performs actual writing.
	 * @param os Output stream.
	 * @throws IOException If an error occurs.
	 */
	protected abstract void doWrite(OutputStream os) throws IOException;

}
