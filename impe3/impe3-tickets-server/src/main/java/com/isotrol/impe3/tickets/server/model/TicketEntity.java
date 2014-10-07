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
package com.isotrol.impe3.tickets.server.model;


import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import com.google.common.collect.Maps;
import com.isotrol.impe3.hib.model.Lengths;
import com.isotrol.impe3.hib.model.VersionedEntity;
import com.isotrol.impe3.tickets.domain.Ticket;


/**
 * Entity that represents a ticket subject.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "IMPE3_TICKET")
public class TicketEntity extends VersionedEntity {
	/** Ticket subject. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "TCKT_SUBJ_ID", nullable = false)
	private SubjectEntity subject;

	/** Creation timestamp. */
	@Column(name = "TCKT_CREATED", nullable = false)
	private long creation = System.currentTimeMillis();

	/** Expiration timestamp. */
	@Column(name = "TCKT_EXPIRATION", nullable = false)
	private long expiration;

	/** Number of ticket uses. */
	@Column(name = "TCKT_USES", nullable = false)
	private int uses;

	/** Number of available uses. */
	@Column(name = "TCKT_AVAILABLE", nullable = false)
	private int available;

	/** Ticket payload. */
	@ElementCollection
	@JoinTable(name = "IMPE3_TICKET_PAYLOAD", joinColumns = @JoinColumn(name = "TCKT_ID", nullable = false))
	@MapKeyColumn(name = "TCKT_NAME", length = Lengths.NAME)
	@Column(name = "TCKT_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> payload;

	/** Default constructor. */
	public TicketEntity() {
	}

	/**
	 * Returns the subject.
	 * @return The subject.
	 */
	public SubjectEntity getSubject() {
		return subject;
	}

	/**
	 * Sets the subject.
	 * @param name The subject.
	 */
	public void setSubject(SubjectEntity name) {
		this.subject = name;
	}

	/**
	 * Returns the creation timestamp.
	 * @return The creation timestamp.
	 */
	public long getCreation() {
		return creation;
	}

	/**
	 * Sets the creation timestamp.
	 * @param creation The creation timestamp.
	 */
	public void setCreation(long creation) {
		this.creation = creation;
	}

	/**
	 * Returns the expiration timestamp.
	 * @return The expiration timestamp.
	 */
	public long getExpiration() {
		return expiration;
	}

	/**
	 * Sets the expiration timestamp.
	 * @param expiration The expiration timestamp.
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	/**
	 * Returns the number of times the ticket can be consumed.
	 * @return The number of times the ticket can be consumed.
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * Sets the number of times the ticket can be consumed.
	 * @param uses The number of uses.
	 */
	public void setUses(int uses) {
		this.uses = uses;
		this.available = uses;
	}

	/**
	 * Consumes the ticket.
	 * @return The consumed ticket.
	 */
	public Ticket consume() {
		if (available <= 0) {
			return Ticket.consumed(getId());
		} else {
			available--;
			if (expiration < System.currentTimeMillis()) {
				return Ticket.expired(getId());
			}
		}
		return Ticket.valid(getId(), getPayload());
	}

	/**
	 * Returns the ticket payload.
	 */
	public Map<String, String> getPayload() {
		if (payload == null) {
			payload = Maps.newHashMap();
		}
		return payload;
	}

}
