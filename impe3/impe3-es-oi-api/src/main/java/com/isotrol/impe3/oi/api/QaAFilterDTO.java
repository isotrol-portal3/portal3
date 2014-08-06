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
package com.isotrol.impe3.oi.api;

import java.io.Serializable;

/**
 * Interview questions filter DTO. 
 * @author Emilio Escobar Reyero
 */
public class QaAFilterDTO implements Serializable {
	/** serial uid.*/
	private static final long serialVersionUID = -457252191201582518L;
	
	/** Interview id. */
	private String id;
	/** Interview state. */
	private Boolean valid;
	/** Member id. */
	private String member;
	/** Internal member id. */
	private Long memberId;
	/** answered. */
	private Boolean answered;

	/** Constructor. */
	public QaAFilterDTO() {
	}
	
	/**
	 * Returns the interview id.
	 * @return The interview id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the interview id.
	 * @param id The interview id.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Sets the interview id.
	 * @param id The interview id.
	 * @return The fluid builder.
	 */
	public QaAFilterDTO putId(String id) {
		this.id = id;
		return this;
	}
	
	/**
	 * If {@code null} wants all, true means only validated.
	 * @return The status.
	 */
	public Boolean isValid() {
		return valid;
	}
	
	/**
	 * If {@code null} wants all, true means only validated.
	 * @param valid the status.
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * If {@code null} wants all, true means only validated.
	 * @param valid The status.
	 * @return The fluid builder.
	 */
	public QaAFilterDTO putValid(Boolean valid) {
		this.valid = valid;
		return this;
	}
	
	/**
	 * Returns the member id.
	 * @return The member id.
	 */
	public String getMember() {
		return member;
	}
	
	/**
	 * Sets the member id.
	 * @param member The member id.
	 */
	public void setMember(String member) {
		this.member = member;
	}
	
	/**
	 * Sets the member id.
	 * @param member The member id.
	 * @return The fluid builder.
	 */
	public QaAFilterDTO putMember(String member) {
		this.member = member;
		return this;
	}	
	
	/**
	 * Returns internal member id.
	 * @return The internal member id.
	 */
	public Long getMemberId() {
		return memberId;
	}
	
	/**
	 * Sets the internal member id.
	 * @param memberId The internal member id.
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	/**
	 * True if answer is not {@code null}
	 * @return The status.
	 */
	public Boolean isAnswered() {
		return answered;
	}

	/**
	 * Sets answer status.
	 * @param answered The status.
	 */
	public void setAnswered(Boolean answered) {
		this.answered = answered;
	}
	
	/**
	 * Sets answer status.
	 * @param answered The status.
	 * @return The fluid builder.
	 */
	public QaAFilterDTO putAnswered(Boolean answered) {
		this.answered = answered;
		return this;
	}

}
