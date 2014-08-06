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
package com.isotrol.impe3.oi.model;


import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.isotrol.impe3.es.common.model.LongIdEntity;
import com.isotrol.impe3.es.common.model.VersionedLongIdEntity;
import com.isotrol.impe3.hib.model.Lengths;


/**
 * Entity that represents an interview question and answer.
 * @author Emilio Escobar Reyero
 */
@Entity
@Table(name = "OI_QAA")
public class QaAEntity extends VersionedLongIdEntity {

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "QAAS_INTE_ID", nullable = false)
	private InterviewEntity interview;
	
	/** Order value. */
	@Column(name = "QAAS_ORDER", nullable = false)
	private Integer order;

	/** Actual rate value. */
	@Column(name = "QAAS_RATE", nullable = false)
	private Double rate;

	/** Question. */
	@Column(name = "QAAS_QUESTION", length = Lengths.DESCRIPTION, nullable = false)
	private String question;

	/** Question. */
	@Column(name = "QAAS_ANSWER", length = Lengths.DESCRIPTION, nullable = true)
	private String answer;

	/** Whether the question is valid. */
	@Column(name = "QAAS_VALID", nullable = false)
	private boolean valid = true;

	/** Whether the question is deleted. */
	@Column(name = "QAAS_DELETED", nullable = false)
	private boolean deleted = false;

	/** Timestamp. */
	@Column(name = "QAAS_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar date;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "QAAS_MMBR_ID", nullable = false)
	private OIMemberEntity member;

	/** constructor. */
	public QaAEntity() {
		super();
	}
	
	/**
	 * Returns the order.
	 * @return The order.
	 */
	public int getOrder() {
		return order == null ? 1024 : order;
	}
	
	/**
	 * Sets the order.
	 * @param order The order.
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	/**
	 * Returns the rate value.
	 * @return The rate value.
	 */
	public double getRate() {
		return rate == null ? 0 : rate;
	}
	
	/**
	 * Sets the rate value.
	 * @param rate The rate value.
	 */
	public void setRate(Double rate) {
		this.rate = rate;
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
	 * Returns whether the qaa is valid.
	 * @return True if the qaa is valid.
	 */
	public boolean isValid() {
		return valid;
	}
	
	/**
	 * Sets whether the qaa is valid.
	 * @param deleted True if the qaa is valid.
	 */
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	
	/**
	 * Returns whether the qaa is deleted.
	 * @return True if the qaa is deleted.
	 */
	public Boolean isDeleted() {
		return deleted;
	}
	
	/**
	 * Sets whether the qaa is deleted.
	 * @param deleted True if the qaa is deleted.
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}
	
	/**
	 * Returns the insertion date.
	 * @return The insertion date.
	 */
	public Calendar getDate() {
		return date;
	}
	
	/**
	 * Sets the insertion date.
	 * @param date The insertion date.
	 */
	public void setDate(Calendar date) {
		this.date = date;
	}
	
	/**
	 * Returns the question maker member.
	 * @return The question maker member.
	 */
	public OIMemberEntity getMember() {
		return member;
	}
	
	/**
	 * Sets the question maker member.
	 * @param member The question maker member.
	 */
	public void setMember(OIMemberEntity member) {
		this.member = member;
	}
	
	/**
	 * Returns the interview.
	 * @return The interview.
	 */
	public InterviewEntity getInterview() {
		return interview;
	}
	
	/**
	 * Sets the interview.
	 * @param interview The interview.
	 */
	public void setInterview(InterviewEntity interview) {
		this.interview = interview;
	}
	
	@Override
	protected Class<? extends LongIdEntity> getEntityType() {
		return QaAEntity.class;
	}
}
