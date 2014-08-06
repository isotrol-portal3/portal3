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

import java.util.Date;

import com.isotrol.impe3.dto.AbstractVersionedLongId;

/**
 * DTO for questions and answers
 * @author Emilio Escobar Reyero
 */
public class QaADTO extends AbstractVersionedLongId {
	/**serial uid */
	private static final long serialVersionUID = -1447442053335013374L;
	
	/** Order. */
	private int order;
	/** Rate value. */
	private double rate;
	/** True if validated question. */
	private boolean valid;
	/** Question. */
	private String question;
	/** Answer. */
	private String answer;
	/** Insertion date. */
	private Date date;
	/** Member id. */
	private String memberId;
	/** Interview id. */
	private String interview;
	
	/** Constructor. */
	public QaADTO() {
	}
	
	/** 
	 * Returns the order.
	 * @return The order. 
	 */
	public int getOrder() {
		return order;
	}
	
	/**
	 * Sets the order.
	 * @param order The order.
	 */
	public void setOrder(int order) {
		this.order = order;
	}
	
	/**
	 * Returns the rate.
	 * @return The rate.
	 */
	public double getRate() {
		return rate;
	}
	
	/**
	 * Sets the rate.
	 * @param rate The rate.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	/**
	 * Returns whether the question is valid.
	 * @return True if the question is valid.
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Sets whether the question is valid.
	 * @param deleted True if the question is valid.
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * Returns the question.
	 * @return The question.
	 */
	public String getQuestion() {
		return question;
	}
	
	/**
	 * Sets the question.
	 * @param question The question.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	
	/**
	 * Returns the answer.
	 * @return The answer.
	 */
	public String getAnswer() {
		return answer;
	}
	
	/**
	 * Sets the answer.
	 * @param answer The answer.
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	/**
	 * Returns the date.
	 * @return The date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the date.
	 * @param date The date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the member id.
	 * @return The member id.
	 */
	public String getMemberId() {
		return memberId;
	}
	
	/**
	 * Sets the member id.
	 * @param memberId The member id.
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	/**
	 * Returns the interview id.
	 * @return The interview id.
	 */
	public String getInterview() {
		return interview;
	}
	
	/**
	 * Sets the interview id.
	 * @param interview The interview id.
	 */
	public void setInterview(String interview) {
		this.interview = interview;
	}
	
}
