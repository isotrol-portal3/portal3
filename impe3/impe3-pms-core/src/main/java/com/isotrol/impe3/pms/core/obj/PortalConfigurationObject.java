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

package com.isotrol.impe3.pms.core.obj;


import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.io.ByteStreams.toByteArray;
import static com.isotrol.impe3.pms.core.obj.Builders.add;
import static com.isotrol.impe3.pms.core.obj.Builders.build;
import static com.isotrol.impe3.pms.core.obj.Builders.put;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.isotrol.impe3.api.AbstractIdentifiable;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.api.PortalConfiguration;
import com.isotrol.impe3.core.ImpeIAModel;
import com.isotrol.impe3.core.Loggers;
import com.isotrol.impe3.core.config.PortalConfigurationBuilder;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition;
import com.isotrol.impe3.core.config.PortalConfigurationDefinition.Item;
import com.isotrol.impe3.core.support.NamedSupport;
import com.isotrol.impe3.pbuf.BaseProtos.ConfigurationPB;
import com.isotrol.impe3.pbuf.BaseProtos.ConfigurationValuePB;
import com.isotrol.impe3.pbuf.BaseProtos.FileContentPB;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ChoiceItemDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationItemType;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateDTO;
import com.isotrol.impe3.pms.api.config.ConfigurationTemplateItemDTO;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.support.AbstractValueLoader;
import com.isotrol.impe3.pms.core.support.Mappers;
import com.isotrol.impe3.pms.model.CategoryEntity;
import com.isotrol.impe3.pms.model.ComponentDfn;
import com.isotrol.impe3.pms.model.ConfigurationEntity;
import com.isotrol.impe3.pms.model.ConfigurationValue;
import com.isotrol.impe3.pms.model.ContentTypeEntity;
import com.isotrol.impe3.pms.model.FileEntity;
import com.isotrol.impe3.pms.model.WithModuleDfn;


/**
 * Portal configuration domain object.
 * @author Enrique Diaz
 */
public final class PortalConfigurationObject extends AbstractIdentifiable {
	private static final String LOG_PREFIX = "Configuration [{}] type [{}]: ";
	private static final ImmutableMap<Class<?>, ConfigurationItemType> TYPES = ImmutableMap
		.<Class<?>, ConfigurationItemType> builder().put(String.class, ConfigurationItemType.STRING)
		.put(Integer.class, ConfigurationItemType.INTEGER).put(FileId.class, ConfigurationItemType.FILE)
		.put(ContentType.class, ConfigurationItemType.CONTENT_TYPE).put(Category.class, ConfigurationItemType.CATEGORY)
		.put(Boolean.class, ConfigurationItemType.BOOLEAN).build();

	private static Object[] logArgs(PortalConfigurationObject object, Object o) {
		return new Object[] {object.getStringId(), object.definition.getType().getName(), o};
	}

	private static final Function<Item, ConfigurationTemplateItemDTO> item(final Context1 ctx,
		final PortalConfigurationObject object) {
		return new Function<Item, ConfigurationTemplateItemDTO>() {
			public ConfigurationTemplateItemDTO apply(Item from) {
				final ConfigurationTemplateItemDTO dto = new ConfigurationTemplateItemDTO();
				dto.setKey(from.getParameter());
				Mappers.named2described(from, dto, ctx.getLocale());
				dto.setRequired(from.isRequired());
				dto.setType(getType(from));
				dto.setStringStyle(from.getStringStyle());
				final Object defaultValue = from.getDefaultValue();
				final Object value = (object == null) ? null : object.values.get(from.getParameter());
				if (value == null && defaultValue != null) {
					switch (dto.getType()) {
						case CHOICE:
							dto.setChoice(((Enum<?>) defaultValue).name());
							break;
						case STRING:
							dto.setString((String) defaultValue);
							break;
						case INTEGER:
							dto.setInteger((Integer) defaultValue);
							break;
						case BOOLEAN:
							dto.setBoolean((Boolean) defaultValue);
							break;
						default:
							break;
					}
				} else if (value != null) {
					try {
						switch (dto.getType()) {
							case CHOICE:
								dto.setChoice(((Enum<?>) value).name());
								break;
							case STRING:
								dto.setString((String) value);
								break;
							case INTEGER:
								dto.setInteger((Integer) value);
								break;
							case FILE:
								final FileId fid = (FileId) value;
								final UploadedFileDTO uf = new UploadedFileDTO();
								uf.setId(fid.getStringId());
								uf.setName(fid.getName());
								dto.setFile(uf);
								dto.setBundle(from.isBundle());
								break;
							case CONTENT_TYPE:
								dto.setContentType(checkNotNull(ctx.getContentTypes().get((UUID) value)).toSelDTO());
								break;
							case CATEGORY:
								dto.setCategory(checkNotNull(ctx.getCategories().get((UUID) value)).toSelDTO());
								break;
							case BOOLEAN:
								dto.setBoolean((Boolean) value);
								break;
							default:
								break;
						}
					} catch (RuntimeException e) {
						Loggers.pms().error(LOG_PREFIX + "Unable to load item", logArgs(object, from.getParameter()));
						throw e;
					}
				}
				if (from.isEnum()) {
					Map<Enum<?>, NamedSupport> choices = from.getChoices();
					List<ChoiceItemDTO> list = Lists.newArrayListWithCapacity(choices.size());
					for (Entry<Enum<?>, NamedSupport> choice : choices.entrySet()) {
						ChoiceItemDTO c = new ChoiceItemDTO();
						c.setKey(choice.getKey().name());
						Mappers.named2described(choice.getValue(), c, ctx.getLocale());
						list.add(c);
					}
					dto.setChoices(list);
				}
				return dto;
			}
		};
	}

	private static ConfigurationItemType getType(Item item) {
		if (item.isEnum()) {
			return ConfigurationItemType.CHOICE;
		}
		return TYPES.get(item.getType());
	}

	/** Object loader. */
	private static final Loader LOADER = new Loader();

	/**
	 * Creates a new configuration object.
	 * @param definition Configuration definition.
	 * @param dfn Configuration entity.
	 * @return A configuration object or {@code null} if any of the arguments is {@code null}.
	 */
	static PortalConfigurationObject of(PortalConfigurationDefinition<?> definition, WithModuleDfn dfn) {
		if (definition == null || dfn == null) {
			return null;
		}
		
		// Si es componente, leemos la configuracion por portal
		if (ComponentDfn.class.isAssignableFrom(dfn.getClass())) {
						
//			return ((ComponentDfn) dfn).getComponent().getPortal().getCurrent().getPortalConfiguration();
		} 
		
		return null;
	}

	private static ConfigurationTemplateDTO create(PortalConfigurationDefinition<?> definition, Context1 ctx,
		PortalConfigurationObject object) {
		final ConfigurationTemplateDTO dto = new ConfigurationTemplateDTO();
		dto.setKey(definition.getType().getName());
		if (definition.hasContentTypes()) {
			dto.setContentTypes(ctx.getContentTypes().map2sel());
		}
		if (definition.hasCategories()) {
			dto.setCategories(ctx.getCategories().map2tree());
		}
		final Locale locale = ctx.getLocale();
		Mappers.named2described(definition, dto, locale);
		dto.setItems(Mappers.list(definition.getParameters().values(), item(ctx, object)));
		return dto;
	}

	static ConfigurationTemplateDTO template(PortalConfigurationDefinition<?> definition, Context1 ctx) {
		final ConfigurationTemplateDTO dto = create(definition, ctx, null);
		return dto;
	}

	/** Configuration definition. */
	private final PortalConfigurationDefinition<?> definition;
	/** Configuration values. */
	private final ImmutableMap<String, Object> values;
	/** Used content types. */
	private final ImmutableSet<UUID> usedContentTypes;
	/** Used categories. */
	private final ImmutableSet<UUID> usedCategories;
	/** Exported files. */
	private final ImmutableMap<UUID, PortalFileObject> files;
	/** Errors: Missing required parameters or incorrect values. */
	private final ImmutableSet<String> errors;
	/** Extra parameters. */
	private final ImmutableSet<String> extra;

	/**
	 * Constructor.
	 * @param definition Definition.
	 * @param entity Configuration entity.
	 */
	private PortalConfigurationObject(PortalConfigurationDefinition<?> definition, ConfigurationEntity entity) {
		super(entity.getId());
		ImmutableMap.Builder<String, Object> values = null;
		ImmutableMap.Builder<UUID, PortalFileObject> files = null;
		ImmutableSet.Builder<UUID> usedContentTypes = null;
		ImmutableSet.Builder<UUID> usedCategories = null;
		ImmutableSet.Builder<String> extra = null;
		ImmutableSet.Builder<String> errors = null;
		final ImmutableMap<String, Item> parameters = definition.getParameters();
		for (Entry<String, ConfigurationValue> ecv : entity.getValues().entrySet()) {
			final String name = ecv.getKey();
			final Item item = parameters.get(name);
			if (item == null) {
				extra = add(extra, name);
				continue;
			}
			final Class<?> type = item.getType();
			Object value = null;
			final ConfigurationValue cv = ecv.getValue();
			if (item.isEnum()) {
				try {
					value = item.fromString(cv.getStringValue());
				} catch (IllegalArgumentException iae) {
					if (errors == null) {
						errors = ImmutableSet.builder();
					}
					errors.add(name);
				}

			} else if (String.class == type) {
				value = cv.getStringValue();
			} else if (Integer.class == type) {
				value = cv.getIntegerValue();
			} else if (Boolean.class == type) {
				value = cv.getBooleanValue();
			} else if (FileId.class == type) {
				final FileEntity fe = cv.getFileValue();
				if (fe != null) {
					final UUID id = fe.getId();
					final FileId fid = FileId.of(id, fe.getName());
					value = fid;
					files = put(files, id, new PortalFileObject(fid, item));
				}
			} else if (Category.class == type) {
				final CategoryEntity ce = cv.getCategoryValue();
				if (ce != null) {
					final UUID id = ce.getId();
					value = id;
					usedCategories = add(usedCategories, id);
				}
			} else if (ContentType.class == type) {
				final ContentTypeEntity ce = cv.getContentTypeValue();
				if (ce != null) {
					final UUID id = ce.getId();
					value = id;
					usedContentTypes = add(usedContentTypes, id);
				}
			}
			if (value != null) {
				values = put(values, name, value);
			}
		}
		this.definition = definition;
		this.values = build(values);
		this.files = build(files);
		this.usedContentTypes = build(usedContentTypes);
		this.usedCategories = build(usedCategories);
		Set<String> missing = Sets.difference(definition.getMBPParameters().keySet(), this.values.keySet());
		if (errors == null) {
			this.errors = ImmutableSet.copyOf(missing);
		} else {
			this.errors = errors.addAll(missing).build();
		}
		this.extra = build(extra);
	}

	public boolean isError() {
		return !errors.isEmpty();
	}

	public boolean isWarning() {
		return isError() || !extra.isEmpty();
	}

	private Object[] logArgs(Object o) {
		return new Object[] {getStringId(), definition.getType().getName(), o};
	}

	public void log(Logger logger) {
		if (!extra.isEmpty() && logger.isWarnEnabled()) {
			logger.warn(LOG_PREFIX + "Unused saved parameters: ", logArgs(extra));
		}
		if (!errors.isEmpty() && logger.isErrorEnabled()) {
			logger.warn(LOG_PREFIX + "Missing required parameters: ", logArgs(errors));
		}
	}

	PortalConfiguration get(ImpeIAModel model) {
		checkState(!isError(), "Invalid configuration state");
		final PortalConfigurationBuilder<?> cb = definition.builder();
		final ImmutableMap<String, Item> parameters = definition.getParameters();
		for (Entry<String, Object> ecv : values.entrySet()) {
			final String name = ecv.getKey();
			final Class<?> type = parameters.get(name).getType();
			final Object value = ecv.getValue();
			try {
				if (Category.class == type) {
					cb.set(name, checkNotNull(model.getCategories().get((UUID) value)));
				} else if (ContentType.class == type) {
					cb.set(name, checkNotNull(model.getContentTypes().get((UUID) value)));
				} else {
					cb.set(name, value);
				}
			} catch (RuntimeException e) {
				Loggers.pms().error(LOG_PREFIX + "Error setting parameter [{}]", logArgs(name));
				throw e;
			}
		}
		try {
			return cb.get();
		} catch (RuntimeException e) {
			Loggers.pms().error(LOG_PREFIX + "Unable to build configuration", logArgs(null));
			throw e;
		}
	}

	/**
	 * Returns whether a content type is used by this configuration.
	 * @param id Content type id.
	 * @return True if the content type is used by this configuration.
	 */
	public boolean isContentTypeUsed(UUID id) {
		return usedContentTypes.contains(id);
	}

	/**
	 * Returns whether a category is used by this configuration.
	 * @param id Category id.
	 * @return True if the category is used by this configuration.
	 */
	public boolean isCategoryUsed(UUID id) {
		return usedCategories.contains(id);
	}

	public ImmutableMap<UUID, PortalFileObject> getFiles() {
		return files;
	}

	public ConfigurationTemplateDTO toTemplateDTO(Context1 ctx) {
		return create(definition, ctx, this);
	}

	/**
	 * Transforms the object to a protocol buffer message.
	 * @param fileManager Required in order to recover file content.
	 * @return The PB message.
	 */
	final ConfigurationPB toPB(FileManager fileManager) {
		ConfigurationPB.Builder b = ConfigurationPB.newBuilder();

		final ImmutableMap<String, Item> parameters = definition.getParameters();

		for (Entry<String, Item> entry : parameters.entrySet()) {
			final Item item = entry.getValue();
			if (item != null) {
				final String name = entry.getKey();
				b.addConfigurationValues(configurationValuePB(name, item, fileManager));
			}
		}

		return b.build();
	}

	private ConfigurationValuePB configurationValuePB(String name, Item item, FileManager fileManager) {
		ConfigurationValuePB.Builder b = ConfigurationValuePB.newBuilder();
		b.setName(name);

		final Class<?> type = item.getType();

		if (item.isEnum()) {
			final Enum<?> value = (Enum<?>) values.get(name);
			if (value != null) {
				b.setCtString(value.name());
			}
		} else if (String.class == type) {
			final String value = (String) values.get(name);
			if (value != null) {
				b.setCtString(value);
			}
		} else if (Integer.class == type) {
			final Integer value = (Integer) values.get(name);
			if (value != null) {
				b.setCtInteger(value);
			}
		} else if (Boolean.class == type) {
			final Boolean value = (Boolean) values.get(name);
			if (value != null) {
				b.setCtBoolean(value);
			}
		} else if (FileId.class == type) {
			if (fileManager != null) {
				final FileId value = (FileId) values.get(name);
				if (value != null) {
					try {
						FileContentPB.Builder fileBuilder = FileContentPB.newBuilder();
						FileData data = fileManager.getFile(value.getStringId());

						fileBuilder.setName(data.getName());

						fileBuilder.setContent(ByteString.copyFrom(toByteArray(data.getData())));

						b.setCtFile(fileBuilder);
					} catch (PMSException e) {} catch (IOException e) {}
				}
			}
		} else if (Category.class == type) {
			final UUID value = (UUID) values.get(name);
			if (value != null) {
				b.setCtString(value.toString());
			}
		} else if (ContentType.class == type) {
			final UUID value = (UUID) values.get(name);
			if (value != null) {
				b.setCtString(value.toString());
			}
		}

		return b.build();
	}

	private static final class Loader extends
		AbstractValueLoader<ConfigurationEntity, PortalConfigurationObject, PortalConfigurationDefinition<?>> {
		Loader() {
			super("Configuration");
		}

		@Override
		protected PortalConfigurationObject load(ConfigurationEntity entity, PortalConfigurationDefinition<?> definition) {
			return new PortalConfigurationObject(definition, entity);
		}
	}

}
