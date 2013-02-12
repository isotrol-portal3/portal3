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

package com.isotrol.impe3.samples.calculator;


import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ExtractQuery;
import com.isotrol.impe3.api.component.InjectRequest;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.VisualComponent;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;
import com.isotrol.impe3.api.component.html.Tag;


/**
 * Simple calculator component implementation.
 * @author Andres Rodriguez
 */
public class CalculatorComponent implements VisualComponent {
	public static final String ADD = "add";
	public static final String SUBSTRACT = "substract";

	/** Service. */
	private CalculatorService service;
	/** First argument. */
	private Integer a = null;
	/** Second argument. */
	private Integer b = null;
	/** Operation. */
	private String operation;
	/** Result argument. */
	private Integer result = null;

	public CalculatorComponent() {
	}

	/* Spring setters. */

	public void setService(CalculatorService service) {
		this.service = service;
	}

	/* IMPE injections. */

	@InjectRequest("a")
	public void setA(int a) {
		this.a = a;
	}

	@InjectRequest("b")
	public void setB(int b) {
		this.b = b;
	}

	@InjectRequest("op")
	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getBody() {
				Tag p = HTML.create(context).p();
				if (result != null) {
					p.p("Result: " + result);
				} else {
					p.p("Nothing to do");
				}
				p.p(context.getSamePageURI(null).toASCIIString());
				return p;
			}
		};
	}

	/* Output */

	public Integer getResult() {
		return result;
	}

	@ExtractQuery("previous")
	public String getPrevious() {
		return result != null ? result.toString() : "Nothing";
	}

	public ComponentResponse execute() {
		result = null;
		if (a != null && b != null && operation != null) {
			if (ADD.equals(operation)) {
				result = service.add(a, b);
			} else if (SUBSTRACT.equals(operation)) {
				result = service.substract(a, b);
			}
		}
		return ComponentResponse.OK;
	}

	public void edit() {
		result = null;

	}
}
