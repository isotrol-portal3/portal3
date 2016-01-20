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

/**
 * 
 */
package com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry;


import java.util.List;

import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.inject.Inject;
import com.isotrol.impe3.gui.common.util.Constants;
import com.isotrol.impe3.pms.api.mreg.AbstractValidModuleDTO;
import com.isotrol.impe3.pms.api.mreg.ModuleDependencyDTO;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractModuleModelData;
import com.isotrol.impe3.pms.gui.client.data.impl.AbstractValidModuleModelData;
import com.isotrol.impe3.pms.gui.client.renderer.InstantiableCellRenderer;


/**
 * Common logic used for valid modules: Connectos and Components. It basically consists in the detail panel computing
 * logic: the same template is used for both types of Modules.
 * 
 * @author Andrei Cojocaru
 * 
 * @param <M> ModelData type parameter
 * @param <D> DTO type parameter
 */
public abstract class AValidModuleView<M extends AbstractValidModuleModelData<D>, D extends AbstractValidModuleDTO>
	extends AModuleDTOView<M, D> {

	// a template for the template!
	/**
	 * Template for the details of a valid Module.<br/>
	 */
	private static final String TEMPLATE = "<b>${LABEL_ID}: </b>${ID}<br/>"
		+ "<b>${LABEL_COPYRIGHT}: </b>${COPYRIGHT}<br/>" + "<b>${LABEL_DESCRIPTION}: </b>${DESCRIPTION}<br/>"
		+ "<b>${LABEL_VERSION}: </b>${VERSION}<br/>" + "<p><b>${LABEL_DEPENDENCES}: </b><ul>${DEPENDENCES}</ul></p>"
		+ "<p><b>${LABEL_MODULES}: </b><ul>${MODULES}</ul></p>";

	/**
	 * Pattern for module copyright label (for localization purposes).<br/>
	 */
	private static final String PATTERN_LABEL_COPYRIGHT = "\\$\\{LABEL_COPYRIGHT\\}";
	/**
	 * Pattern for module copyright<br/>
	 */
	private static final String PATTERN_COPYRIGHT = "\\$\\{COPYRIGHT\\}";
	/**
	 * Pattern for module description label.<br/>
	 */
	private static final String PATTERN_LABEL_DESCRIPTION = "\\$\\{LABEL_DESCRIPTION\\}";
	/**
	 * Pattern for module description.<br/>
	 */
	private static final String PATTERN_DESCRIPTION = "\\$\\{DESCRIPTION\\}";
	/**
	 * Pattern for module dependences label.<br/>
	 */
	private static final String PATTERN_LABEL_DEPENDENCES = "\\$\\{LABEL_DEPENDENCES\\}";
	/**
	 * Pattern form module dependences.<br/>
	 */
	private static final String PATTERN_DEPENDENCES = "\\$\\{DEPENDENCES\\}";
	/**
	 * Pattern for exported modules title.<br/>
	 */
	private static final String PATTERN_LABEL_MODULES = "\\$\\{LABEL_MODULES\\}";
	/**
	 * Pattern for exported modules.<br/>
	 */
	private static final String PATTERN_MODULES = "\\$\\{MODULES\\}";
	/**
	 * Pattern for module id title (class).<br/>
	 */
	private static final String PATTERN_LABEL_ID = "\\$\\{LABEL_ID\\}";
	/**
	 * Pattern for module id (class).<br/>
	 */
	private static final String PATTERN_ID = "\\$\\{ID\\}";
	/**
	 * Pattern for module version title.<br/>
	 */
	private static final String PATTERN_LABEL_VERSION = "\\$\\{LABEL_VERSION\\}";
	/**
	 * Pattern for module version.<br/>
	 */
	private static final String PATTERN_VERSION = "\\$\\{VERSION\\}";

	/**
	 * Pattern for generating HTML for one dependence.<br/>
	 */
	private static final String DEPENDENCE_TEMPLATE = "<li>${DEPENDENCE_DESCRIPTION}<span style='margin-right:15px'>(${DEPENDENCE_TYPE})</span>"
		+ "${DEPENDENCE_REQUIRED}"
		+ "<img alt='${SATISFIABLE}' src='img/${SATISFIED_ICON}' title='${SATISFIED_TITLE}' />" + "</li>";
	/**
	 * Pattern for dependence description.<br/>
	 */
	private static final String PATTERN_DEPENDENCE_DESCRIPTION = "\\$\\{DEPENDENCE_DESCRIPTION\\}";
	/**
	 * Pattern for dependence type.<br/>
	 */
	private static final String PATTERN_DEPENDENCE_TYPE = "\\$\\{DEPENDENCE_TYPE\\}";
	/**
	 * Pattern for dependence "required" state.<br/>
	 */
	private static final String PATTERN_DEPENDENCE_REQUIRED = "\\$\\{DEPENDENCE_REQUIRED\\}";
	/**
	 * Pattern for dependence "satisfiable" state.<br/>
	 */
	private static final String PATTERN_SATISFIABLE = "\\$\\{SATISFIABLE\\}";
	/**
	 * Pattern for "satisfied" state icon.<br/>
	 */
	private static final String PATTERN_SATISFIED_ICON = "\\$\\{SATISFIED_ICON\\}";
	/**
	 * Pattern for "satisfied" state icon title.<br/>
	 */
	private static final String PATTERN_SATISFIED_TITLE = "\\$\\{SATISFIED_TITLE\\}";

	/**
	 * Template for rendering the piece of HTML which displays that "dependence is required".<br/>
	 */
	private static final String TEMPLATE_DEPENDENCE_REQUIRED = "<img style='margin-right:15px' title='${TITLE_DEPENDENCE_REQUIRED}' src='img/required_icon.gif'/>";
	private static final String PATTERN_TITLE_DEPENDENCE_REQUIRED = "\\$\\{TITLE_DEPENDENCE_REQUIRED\\}";

	/**
	 * Template for displaying the piece of HTML corrseponding to the details of an exported module.<br/>
	 */
	private static final String MODULE_TEMPLATE = "<li>${MODULE_DESC} (${MODULE_NAME})</li>";
	private static final String PATTERN_MODULE_DESC = "\\$\\{MODULE_DESC\\}";
	private static final String PATTERN_MODULE_NAME = "\\$\\{MODULE_NAME\\}";

	/**
	 * Cell renderer for "instantiable" grid column.<br/>
	 */
	private InstantiableCellRenderer instantiableCellRenderer = null;

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.pms.gui.client.widget.systemmanagement.moduleregistry.AModuleDTOView
	 * #addSpecificColumnConfig(java.util.List)
	 */
	@Override
	protected void addSpecificColumnConfig(List<ColumnConfig> configs) {
		super.addSpecificColumnConfig(configs);
		// instantiable:
		addColumnInstantiable(configs);
	}

	/**
	 * adds the Instantiable column.<br/>
	 * @param configs
	 */
	private void addColumnInstantiable(List<ColumnConfig> configs) {
		ColumnConfig config = new ColumnConfig();
		config.setId(AbstractModuleModelData.PROPERTY_INSTANTIABLE);
		config.setHeaderText(getPmsMessages().columnHeaderInstantiable());
		config.setWidth(Constants.COLUMN_ICON_WIDTH);
		config.setRenderer(getInstantiableCellRenderer());
		configs.add(config);
	}

	/**
	 * Returns the text to show in exported modules title on the module detail
	 * @return the text to show in exported modules title on the module detail
	 */
	protected String getLabelModulesMessage() {
		return getPmsMessages().labelExportedModules();
	}

	/**
	 * @return the instantiableCellRenderer
	 */
	protected InstantiableCellRenderer getInstantiableCellRenderer() {
		return instantiableCellRenderer;
	}

	/**
	 * Injects the cell renderer for "instantiable" cell.
	 * @param instantiableCellRenderer the instantiableCellRenderer to set
	 */
	@Inject
	public void setInstantiableCellRenderer(InstantiableCellRenderer instantiableCellRenderer) {
		this.instantiableCellRenderer = instantiableCellRenderer;
	}

	@Override
	protected String getXDetailTemplate(M model) {
		String template = "";

		D dto = model.getDTO();
		// compute dependences replacement:
		String dependences = getDependences(dto);
		// compute modules replacement:
		String modules = getModules(dto);

		template = getTemplate().replaceAll(getPatternLabelCopyright(), getPmsMessages().labelCopyright()).replaceAll(
			getPatternCopyright(), dto.getCopyright()).replaceAll(getPatternLabelDescription(),
			getPmsMessages().labelDescription()).replaceAll(getPatternDescription(), dto.getDescription()).replaceAll(
			getPatternLabelDependences(), getPmsMessages().labelDependences()).replaceAll(getPatternDependences(),
			dependences).replaceAll(getPatternLabelModules(), getLabelModulesMessage()).replaceAll(getPatternModules(),
			modules).replaceAll(getPatternLabelId(), getPmsMessages().labelId())
			.replaceAll(getPatternId(), dto.getId()).replaceAll(getPatternLabelVersion(),
				getPmsMessages().labelVersion()).replaceAll(getPatternVersion(), dto.getVersion());

		return template;
	}

	/**
	 * Computes the piece of HTML of the module detail, that corresponds to exported module for the passed DTO.<br/>
	 * @param dto
	 * @return
	 */
	protected abstract String getModules(D dto);

	/**
	 * Compute the piece of HTML that corresponds to the dependences of the passed DTO.<br/>
	 * @param dto
	 * @return
	 */
	private final String getDependences(D dto) {
		String dependences = "";
		String isDependenceRequired = getTemplateDependenceRequired().replaceAll(getPatternTitleDependenceRequired(),
			getPmsMessages().titleRequiredDependence());
		for (ModuleDependencyDTO depDto : dto.getDependencies()) {
			String dependenceRequired = "";
			if (depDto.isRequired()) {
				dependenceRequired = isDependenceRequired;
			}
			String satisfiedIcon = null;
			String satisfiedTitle = null;
			if (depDto.isSatisfiable()) {
				satisfiedIcon = Constants.OK_IMAGE;
				satisfiedTitle = getPmsMessages().titleSatisfiedDependence();
			} else {
				satisfiedIcon = Constants.ERROR_IMAGE;
				satisfiedTitle = getPmsMessages().titleUnsatisfiedDependence();
			}
			String dependence = getDependenceTemplate().replaceAll(getPatternDependenceDescription(),
				depDto.getDescription()).replaceAll(getPatternDependenceType(), depDto.getType()).replaceAll(
				getPatternDependenceRequired(), dependenceRequired).replaceAll(getPatternSatisfiable(),
				getPmsMessages().titleSatisfiable()).replaceAll(getPatternSatisfiedIcon(), satisfiedIcon).replaceAll(
				getPatternSatisfiedTitle(), satisfiedTitle);
			dependences = dependences.concat(dependence);
		}
		return dependences;
	}

	/**
	 * Retruns the string template used to obtain the final template.<br/>
	 * @return
	 */
	protected static final String getTemplate() {
		return TEMPLATE;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_LABEL_COPYRIGHT}
	 */
	protected static final String getPatternLabelCopyright() {
		return PATTERN_LABEL_COPYRIGHT;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_COPYRIGHT}
	 */
	protected static final String getPatternCopyright() {
		return PATTERN_COPYRIGHT;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_LABEL_DESCRIPTION}
	 */
	protected static final String getPatternLabelDescription() {
		return PATTERN_LABEL_DESCRIPTION;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_DESCRIPTION}
	 */
	protected static final String getPatternDescription() {
		return PATTERN_DESCRIPTION;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_LABEL_DEPENDENCES}
	 */
	protected static final String getPatternLabelDependences() {
		return PATTERN_LABEL_DEPENDENCES;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_DEPENDENCES}
	 */
	protected static final String getPatternDependences() {
		return PATTERN_DEPENDENCES;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_LABEL_MODULES}
	 */
	protected static final String getPatternLabelModules() {
		return PATTERN_LABEL_MODULES;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_MODULES}
	 */
	protected static final String getPatternModules() {
		return PATTERN_MODULES;
	}

	/**
	 * <br/>
	 * @return the {@link #DEPENDENCE_TEMPLATE}
	 */
	protected static final String getDependenceTemplate() {
		return DEPENDENCE_TEMPLATE;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_DEPENDENCE_DESCRIPTION}
	 */
	protected static final String getPatternDependenceDescription() {
		return PATTERN_DEPENDENCE_DESCRIPTION;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_DEPENDENCE_TYPE}
	 */
	protected static final String getPatternDependenceType() {
		return PATTERN_DEPENDENCE_TYPE;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_DEPENDENCE_REQUIRED}
	 */
	protected static final String getPatternDependenceRequired() {
		return PATTERN_DEPENDENCE_REQUIRED;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_SATISFIABLE}
	 */
	protected static final String getPatternSatisfiable() {
		return PATTERN_SATISFIABLE;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_SATISFIED_ICON}
	 */
	protected static final String getPatternSatisfiedIcon() {
		return PATTERN_SATISFIED_ICON;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_SATISFIED_TITLE}
	 */
	protected static final String getPatternSatisfiedTitle() {
		return PATTERN_SATISFIED_TITLE;
	}

	/**
	 * <br/>
	 * @return the {@link #TEMPLATE_DEPENDENCE_REQUIRED}
	 */
	protected static final String getTemplateDependenceRequired() {
		return TEMPLATE_DEPENDENCE_REQUIRED;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_TITLE_DEPENDENCE_REQUIRED}
	 */
	protected static final String getPatternTitleDependenceRequired() {
		return PATTERN_TITLE_DEPENDENCE_REQUIRED;
	}

	/**
	 * <br/>
	 * @return the {@link #MODULE_TEMPLATE}
	 */
	protected static final String getModuleTemplate() {
		return MODULE_TEMPLATE;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_MODULE_NAME}
	 */
	protected static final String getPatternModuleName() {
		return PATTERN_MODULE_NAME;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_MODULE_DESC}
	 */
	protected static final String getPatternModuleDesc() {
		return PATTERN_MODULE_DESC;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_LABEL_ID}
	 */
	protected static final String getPatternLabelId() {
		return PATTERN_LABEL_ID;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_ID}
	 */
	protected static final String getPatternId() {
		return PATTERN_ID;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_LABEL_VERSION}
	 */
	protected static final String getPatternLabelVersion() {
		return PATTERN_LABEL_VERSION;
	}

	/**
	 * <br/>
	 * @return the {@link #PATTERN_VERSION}
	 */
	protected static final String getPatternVersion() {
		return PATTERN_VERSION;
	}
}
