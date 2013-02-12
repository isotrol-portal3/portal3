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

package com.isotrol.impe3.web.pms.upload;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.core.FileManager;


/**
 * Controller for PMS File Uploads.
 * @author Andres Rodriguez
 */
public class UploadController implements Controller {
	private final FileManager fileManager;

	/**
	 * Constructor with required not null file manager param
	 * @param fileManager file manager
	 */
	public UploadController(final FileManager fileManager) {
		this.fileManager = Preconditions.checkNotNull(fileManager);
	}

	private static String clean(String path, char separator) {
		int i = path.lastIndexOf(separator);
		if (i < 0) {
			return path;
		}
		if ((++i) >= path.length()) {
			return null;
		}
		return path.substring(i);
	}

	private ModelAndView error(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return null;
	}

	/**
	 * Spring mvc implementation for upload contents.
	 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile("uploadedFile");
		if (multipartFile == null) {
			return error(response);
		}
		String name = multipartFile.getOriginalFilename();
		name = clean(name, '/');
		name = clean(name, '\\');
		if (name == null || name.length() == 0) {
			return error(response);
		}
		final UploadedFileDTO dto;
		try {
			dto = fileManager.upload(name, multipartFile.getBytes());
		} catch (Exception e) {
			return error(response);
		}
		final JSONObject o = new JSONObject().put("id", dto.getId()).put("name", dto.getName());
		response.setContentType("text/html");
		response.getWriter().write(o.toString());
		return null;
	}
}
