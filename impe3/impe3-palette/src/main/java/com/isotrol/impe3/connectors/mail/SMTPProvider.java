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

package com.isotrol.impe3.connectors.mail;


import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;

import com.google.common.base.Preconditions;


/**
 * SMTP Server-based mail sender provider.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class SMTPProvider implements MailSenderProvider {
	/** Server configuration. */
	private final MailServerConfig config;

	public SMTPProvider(final MailServerConfig config) {
		this.config = Preconditions.checkNotNull(config);
	}

	/**
	 * Creates and returns a new mail sender.
	 * @return A new mail sender.
	 */
	public JavaMailSender get() {
		final JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(config.host());
		// Port
		final Integer port = config.port();
		if (port != null) {
			sender.setPort(port);
		}
		// Username
		final String username = config.username();
		if (StringUtils.hasText(username)) {
			sender.setUsername(username);
		}
		// Password
		final String password = config.password();
		if (StringUtils.hasText(password)) {
			sender.setPassword(password);
		}
		// Protocol
		final String protocol = config.protocol();
		if (StringUtils.hasText(protocol)) {
			sender.setProtocol(protocol);
		}
		// Done
		return sender;
	}
}
