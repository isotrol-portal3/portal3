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
package com.isotrol.impe3.web20.client.counter;


import java.util.List;
import java.util.UUID;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.component.Action;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ExtractAction;
import com.isotrol.impe3.api.component.Inject;
import com.isotrol.impe3.api.component.InjectLocal;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.Renderer;
import com.isotrol.impe3.api.component.html.HTML;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragments;
import com.isotrol.impe3.api.component.html.HTMLRenderer;
import com.isotrol.impe3.api.component.html.SkeletalHTMLRenderer;


/**
 * Exporter action component.
 * @author Emilio Escobar Reyero
 */
public class CounterComponent implements Component {
	/** Local context resource id param name. */
	public static final String COUNTERRESOURCEID = "web20_counter_resourceId";
	/** Local context community id param name. */
	public static final String COUNTERCOMMUNITYID = "web20_counter_communityId";
	/** Local context aggregations param name. */
	public static final String COUNTERAGGREGATIONS = "web20_counter_aggregations";
	/** Local context counter type param name. */
	public static final String COUNTERCOUNTERTYPE = "web20_counter_counterType";
	/** Local context member id param name. */
	public static final String COUNTERMEMBERID = "web20_counter_memberId";
	/** Local context origin param name. */
	public static final String COUNTERORIGIN = "web20_counter_origin";

	private CounterConfig config;
	private CounterConfig moduleConfig;

	/** Resource uuid. */
	private String idr;
	/** Community uuid. */
	private String idc;
	/** Aggregations names. */
	private List<String> cgr;
	/** Counter type. */
	private String ct;
	/** Member uuid. */
	private String sm;
	/** Source origin. */
	private String so;

	/** Default constructor. */
	public CounterComponent() {
	}

	/**
	 * Everytimes reponse OK.
	 * @see com.isotrol.impe3.api.component.Component#execute()
	 */
	public ComponentResponse execute() {
		return ComponentResponse.OK;
	}

	/**
	 * @see com.isotrol.impe3.api.component.EditModeComponent#edit()
	 */
	public void edit() {
	}

	/**
	 * Generates img likes uri for action.
	 * @param context
	 * @return Returns action uri.
	 */
	String getURI(final RenderContext context) {
		if (idr == null) {
			return null;
		}

		if (idc == null) {
			idc = new UUID(0L, 0L).toString();
		}

		if (cgr == null) {
			cgr = Lists.newArrayList();
		}

		if (ct == null) {
			if (config != null && config.counterType() != null) {
				ct = config.counterType();
			} else {
				ct = moduleConfig.counterType();
			}
		}

		Multimap<String, Object> parameters = ArrayListMultimap.create();
		parameters.put("idr", idr);
		parameters.put("idc", idc);
		parameters.putAll("cgr", cgr);
		parameters.put("ct", ct);
		if (sm != null) {
			parameters.put("sm", sm);
		}
		if (so != null) {
			parameters.put("so", so);
		}

		return context.getActionURI("counter", parameters).toASCIIString();
	}

	@Renderer
	public HTMLRenderer html(final RenderContext context) {
		return new SkeletalHTMLRenderer() {
			@Override
			public HTMLFragment getFooter() {
				String src = getURI(context);
				if (src == null) {
					return HTMLFragments.empty();
				}
				return HTML.create(context).img(src, "");
			}
		};
	}

	@InjectLocal(COUNTERRESOURCEID)
	public void setIdr(String idr) {
		this.idr = idr;
	}

	@InjectLocal(COUNTERCOMMUNITYID)
	public void setIdc(String idc) {
		this.idc = idc;
	}

	@InjectLocal(COUNTERAGGREGATIONS)
	public void setCgr(List<String> cgr) {
		this.cgr = cgr;
	}

	@InjectLocal(COUNTERCOUNTERTYPE)
	public void setCt(String ct) {
		this.ct = ct;
	}

	@InjectLocal(COUNTERMEMBERID)
	public void setSm(String sm) {
		this.sm = sm;
	}

	@InjectLocal(COUNTERORIGIN)
	public void setSo(String so) {
		this.so = so;
	}

	/** Returns counter action. */
	@ExtractAction
	public Action getAccion() {
		return Action.of("counter");
	}

	@Inject
	public void setConfig(CounterConfig config) {
		this.config = config;
	}

	public void setModuleConfig(CounterConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}
}
