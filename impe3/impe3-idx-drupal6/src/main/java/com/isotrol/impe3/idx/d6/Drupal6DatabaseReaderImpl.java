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
package com.isotrol.impe3.idx.d6;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.idx.LocalMappingsService;
import com.isotrol.impe3.idx.d6.Drupal6Content.Drupal6ContentBuilder;


/**
 * Drupal6 Database Reader Implementation
 * @author Alejandro Espinosa
 * @author Javier Ráez
 */
public class Drupal6DatabaseReaderImpl extends NamedParameterJdbcDaoSupport implements Drupal6DatabaseReader {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String ID = "id";
	
	private static final String rutaThumbnails = "imagefield_thumbs";

	private String selectContent;
	private String selectFieldsNoTable;
	private String selectValuesType1;
	private String selectValuesType2;
	private String selectValuesFile;
	private String selectValuesNoTable1;
	private String selectValuesNoTable2;
	private String selectValuesVocabulary;
	private String selectValuesParent;
	private String selectTaxName;
	private String selectLinkType;
	private String selectUploadFiles;

	private LocalMappingsService mappingsService;

	/**
	 * @see org.springframework.dao.support.DaoSupport#initDao()
	 */
	@Override
	protected void initDao() throws Exception {
		this.logger.info("Init Drupal 6 database reader.");

		selectContent = "select NID, VID, TYPE, LANGUAGE, TITLE, UID, STATUS,"
			+ "CREATED, CHANGED, COMMENT, PROMOTE, MODERATE, STICKY, TNID, TRANSLATE from node where NID = :id";

		selectFieldsNoTable = "SELECT f.field_name, f.type, f.multiple, f.module FROM content_node_field_instance i, "
			+ "content_node_field f where i.type_name= :tipo and i.field_name=f.field_name and f.active='1' and locked='0'"
			+ " and f.db_storage='0'";

		selectValuesType1 = "SELECT * FROM content_type_";

		selectValuesType2 = " where nid= :id";

		selectValuesFile = "SELECT filepath from files where fid= :fid";

		selectValuesNoTable1 = "SELECT * FROM content_";

		selectValuesNoTable2 = " where nid=:nid";

		selectValuesVocabulary = "select tid from term_node where nid= :id";

		selectValuesParent = "select parent from term_hierarchy where tid= :tid";

		selectTaxName = "select name from term_data where tid= :tid";

		// FIXME
		/*
		 * El campo field_texto_enlace_value, depende del nombre que se le de a el campo en drupal. 
		 * Si se cambiara el nombre del campo seria necesario cambiar este valor
		 */
		selectLinkType = "SELECT nid, field_nodo_nid, field_texto_enlace_value " + 
				"FROM content_type_enlace_contenido where nid= :id";

		selectUploadFiles = "select fid,weight,description from upload where nid= :id";

		super.initDao();
	}

	private static final ParameterizedRowMapper<Drupal6ContentBuilder> CONTENT_MAPPER = 
		new ParameterizedRowMapper<Drupal6ContentBuilder>() {

		public Drupal6ContentBuilder mapRow(ResultSet rs, int rowNum) throws SQLException {
			Drupal6ContentBuilder builder = Drupal6Content.builder();

			// NID,TYPE,STATUS,CREATED,CHANGED,LANGUAGE,TITLE,UID, , COMMENT, PROMOTE, MODERATE, STICKY, TNID,
			// TRANSLATE,VID

			int i = 1;
			builder.setNid(rs.getInt(i++));
			builder.setVid(rs.getInt(i++));
			builder.setType(rs.getString(i++));
			builder.setLanguage(rs.getString(i++));
			builder.setTitle(rs.getString(i++));
			builder.setUid(rs.getInt(i++));
			builder.setStatus(rs.getInt(i++));
			builder.setCreated(new Date(rs.getLong(i++)));
			builder.setChanged(new Date(rs.getLong(i++)));
			builder.setComment(rs.getInt(i++));
			builder.setPromote(rs.getInt(i++));
			builder.setModerate(rs.getInt(i++));
			builder.setSticky(rs.getInt(i++));
			builder.setTnid(rs.getInt(i++));
			builder.setTranslate(rs.getInt(i++));

			return builder;
		}
	};

	static class TextFieldRowMapper implements org.springframework.jdbc.core.RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Map<String, String> contenido = new HashMap<String, String>();

			int i = 1;
			contenido.put(rs.getString(i++), rs.getString(i++));

			return contenido;
		}
	}

	/**
	 * 
	 * @see com.isotrol.impe3.idx.d6.Drupal6DatabaseReader#createBuilder(java.lang.String)
	 */
	@Transactional
	public Drupal6ContentBuilder createBuilder(String id) {
		Drupal6ContentBuilder builder = (Drupal6ContentBuilder) getNamedParameterJdbcTemplate().queryForObject(
			selectContent, ImmutableMap.of(ID, id), CONTENT_MAPPER);

		readContentFields(builder);

		return builder;
	}

	@Transactional
	public void readContentFields(Drupal6ContentBuilder builder) {
		final Map<String, Object> mapa = new HashMap<String, Object>();
		List<Map<String, Object>> fieldText;
		List fieldTypeText;
		mapa.put("id", builder.getNid());

		Element xmlContenido = new Element(builder.getType());
		xmlContenido.addAttribute(new Attribute(ID, String.valueOf(builder.getNid())));

		// Primero tratamos los campos que se encuentran en la tabla correspondiente al tipo
		Map<String, Object> mapaTipo = new HashMap<String, Object>();
		fieldTypeText = (List) getNamedParameterJdbcTemplate().queryForList(
			selectValuesType1 + builder.getType().trim() + selectValuesType2, mapa);
		if (builder.getTitle() != null) {
			Element xmlElement = new Element("title");
			xmlElement.addAttribute(new Attribute("type", "texto"));
			xmlElement.appendChild(builder.getTitle());
			xmlContenido.appendChild(xmlElement);
			builder.addField("title", builder.getTitle());
		}
		Iterator fieldIterator = fieldTypeText.iterator();
		while (fieldIterator.hasNext()) {
			LinkedHashMap field = (LinkedHashMap) fieldIterator.next();
			Set c = field.entrySet();
			Iterator campos = c.iterator();
			while (campos.hasNext()) {
				Object campoBaseObject = campos.next();
				if (campoBaseObject != null) {
					String campo = campoBaseObject.toString();
					int puntoCorte = campo.indexOf('=');
					String nombreCampo = campo.substring(0, puntoCorte);
					String valorCampo = campo.substring(puntoCorte + 1);
					if (nombreCampo.contains("_value") && valorCampo != null && !valorCampo.equals("null")) {
						// Guardamos como una propiedad
						nombreCampo = trataWysiwyg(nombreCampo);
						Element xmlElement = new Element(nombreCampo);
						xmlElement.addAttribute(new Attribute("type", "texto"));
						xmlElement.appendChild(valorCampo);
						xmlContenido.appendChild(xmlElement);
						builder.addField(nombreCampo, valorCampo);
					} else if (nombreCampo.contains("_fid") && valorCampo != null) {
						// Sacamos el valor de la tabla filefield y añadimos la propiedad
						Map<String, Object> mapaFichero = new HashMap<String, Object>();
						mapaFichero.put("fid", valorCampo);
						fieldTypeText = (List) getNamedParameterJdbcTemplate().queryForList(selectValuesFile,
							mapaFichero);
						Iterator fileIterator = fieldTypeText.iterator();
						while (fileIterator.hasNext()) {
							Object campoBaseObject3 = fileIterator.next();
							if (campoBaseObject3 != null) {
								String campoBase = campoBaseObject3.toString();

								Element xmlElement = new Element(nombreCampo);
								if (campoBase.startsWith("{filepath=")) {
									campoBase = campoBase.substring(10, campoBase.length() - 1);
								}
								xmlElement.appendChild(campoBase);
								xmlElement.addAttribute(new Attribute("type", "fichero"));
								if (campoBase.lastIndexOf("/") != -1) {
									String miniatura = campoBase.substring(0, campoBase.lastIndexOf("/")) + '/'
										+ rutaThumbnails + '/' + campoBase.substring(campoBase.lastIndexOf("/") + 1);
									xmlElement.addAttribute(new Attribute("miniatura", miniatura));
								}

								builder.addField(nombreCampo, valorCampo);
								String[] atributos = field.get(nombreCampo.split("_fid")[0] + "_data").toString()
									.split("\"");
								for (int i = 1; (i + 2) < atributos.length; i = i + 4) {
									xmlElement.addAttribute(new Attribute(atributos[i], atributos[i + 2]));
									builder.addField(nombreCampo + "_" + atributos[i], atributos[i + 2]);
								}
								xmlContenido.appendChild(xmlElement);
							}
						}
					}
				}
			}
		}

		// Ahora tratamos los campos correspondientes a los campos con tabla propia
		mapaTipo.put("tipo", builder.getType());
		fieldText = (List<Map<String, Object>>) getNamedParameterJdbcTemplate().queryForList(selectFieldsNoTable,
			mapaTipo);
		Iterator a1 = fieldText.iterator();
		while (a1.hasNext()) {
			Map<String, Object> field = (Map<String, Object>) a1.next();
			Map<String, Object> mapaCampo = new HashMap<String, Object>();
			mapaCampo.put("nid", builder.getNid());
			fieldTypeText = (List) getNamedParameterJdbcTemplate().queryForList(
				selectValuesNoTable1 + field.get("field_name") + selectValuesNoTable2, mapaCampo);
			Iterator fieldIteratorNoTabla = fieldTypeText.iterator();
			while (fieldIteratorNoTabla.hasNext()) {
				Map fieldNoTabla = (Map) fieldIteratorNoTabla.next();

				Set c = fieldNoTabla.entrySet();
				Iterator campos = c.iterator();
				while (campos.hasNext()) {
					Object campoBaseObject = campos.next();
					if (campoBaseObject != null) {
						String campo = campoBaseObject.toString();
						int puntoCorte = campo.indexOf('=');
						String nombreCampo = campo.substring(0, puntoCorte);
						String valorCampo = campo.substring(puntoCorte + 1);
						String valor_fecha = trataWysiwyg(valorCampo);
						if (nombreCampo.contains("_value") && valorCampo != null && !valorCampo.equals("null")) {
							String fecha = valorCampo;
							if (fecha != null && fecha.length() == 19
								&& "-".equals(Character.toString(fecha.charAt(4)))
								&& "-".equals(Character.toString(fecha.charAt(7)))
								&& "T".equals(Character.toString(fecha.charAt(10)))
								&& ":".equals(Character.toString(fecha.charAt(13)))
								&& ":".equals(Character.toString(fecha.charAt(16)))) {
								DateFormat formatter;
								Date date;
								try {
									formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
									date = (Date) formatter.parse(fecha);
									valor_fecha = String.valueOf(date.getTime());
								} catch (Exception e) {

								}
							}

							// Guardamos como una propiedad
							Element xmlElement = new Element(nombreCampo);
							xmlElement.appendChild(valor_fecha);
							xmlElement.addAttribute(new Attribute("type", "texto"));
							xmlContenido.appendChild(xmlElement);
							builder.addField(nombreCampo, valor_fecha);
						} else if (nombreCampo.contains("_fid") && valorCampo != null) {
							// Sacamos el valor de la tabla filefield y aÃ±adimos la propiedad
							Map<String, Object> mapaFichero = new HashMap<String, Object>();
							mapaFichero.put("fid", valorCampo);
							fieldTypeText = (List) getNamedParameterJdbcTemplate().queryForList(selectValuesFile,
								mapaFichero);

							Iterator fileIteratorNoTabla2 = fieldTypeText.iterator();
							while (fileIteratorNoTabla2.hasNext()) {
								Object campoBaseObject2 = fileIteratorNoTabla2.next();
								if (campoBaseObject2 != null) {
									String campoBase = campoBaseObject2.toString();

									Element xmlElement = new Element(nombreCampo);
									if (campoBase.startsWith("{filepath=")) {
										campoBase = campoBase.substring(10, campoBase.length() - 1);
									}
									xmlElement.appendChild(campoBase);
									xmlElement.addAttribute(new Attribute("type", "fichero"));
									if (campoBase.lastIndexOf("/") != -1) {
										String miniatura = campoBase.substring(0, campoBase.lastIndexOf("/")) + '/'
											+ rutaThumbnails + '/'
											+ campoBase.substring(campoBase.lastIndexOf("/") + 1);
										xmlElement.addAttribute(new Attribute("miniatura", miniatura));
									}

									builder.addField(nombreCampo, campoBase);
									String[] atributos = fieldNoTabla.get(nombreCampo.split("_fid")[0] + "_data")
										.toString().split("\"");
									for (int i = 1; (i + 2) < atributos.length; i = i + 4) {
										xmlElement.addAttribute(new Attribute(atributos[i], atributos[i + 2]));
										builder.addField(nombreCampo + "_" + atributos[i], atributos[i + 2]);
									}
									xmlContenido.appendChild(xmlElement);
								}
							}
						} else if (nombreCampo.contains("_nid") && valorCampo != null) {
							Element xmlElement = new Element(nombreCampo);
							String enlace = valorCampo;
							xmlElement.addAttribute(new Attribute("type", "enlace"));

							Map<String, Object> mapaFichero = new HashMap<String, Object>();
							mapaFichero.put(ID, valorCampo);
							fieldTypeText = (List) getNamedParameterJdbcTemplate().queryForList(selectLinkType,
								mapaFichero);
							Iterator fileIteratorNoTabla2 = fieldTypeText.iterator();
							while (fileIteratorNoTabla2.hasNext()) {
								Map<String,Object> campoBaseObject2 = (Map<String,Object>) fileIteratorNoTabla2.next();
								if (campoBaseObject2 != null) {
									Object nid = campoBaseObject2.get("field_nodo_nid");
									Object texto = campoBaseObject2.get("field_texto_enlace_value");

									if (nid != null) {
										xmlElement.addAttribute(new Attribute(ID, nid.toString()));

										Drupal6ContentBuilder d6b = (Drupal6ContentBuilder) getNamedParameterJdbcTemplate()
											.queryForObject(selectContent, ImmutableMap.of(ID, nid.toString()),
												CONTENT_MAPPER);
										String linkType = d6b.getType();

										UUID uuid = mappingsService.getContentType(linkType);
										if (uuid != null) {
											xmlElement.addAttribute(new Attribute("contentTypeUuid", uuid.toString()));
										}
									}
									if (texto != null) {
										enlace = texto.toString();
									}
								}
							}
							xmlElement.appendChild(enlace);
							xmlContenido.appendChild(xmlElement);
							builder.addField(nombreCampo, valorCampo);
						}
					}
				}
			}
		}
		// Comprobamos si tiene archivos adjuntos
		List uploadsFiles = (List) getNamedParameterJdbcTemplate().queryForList(selectUploadFiles, mapa);
		if (!uploadsFiles.isEmpty()) {
			Element xmlElement = new Element("adjuntos");

			Iterator uploadIterator = uploadsFiles.iterator();
			while (uploadIterator.hasNext()) {
				Object uploadFile = uploadIterator.next();
				if (uploadFile != null) {
					String uploadFileString = uploadFile.toString();
					if (uploadFileString.startsWith("{fid=")) {
						int weightStart = uploadFileString.indexOf("weight=");
						int descriptionStart = uploadFileString.indexOf("description=");
						String fid = uploadFileString.substring(5, weightStart - 2);
						String weight = uploadFileString.substring(weightStart + 7, descriptionStart - 2);
						String description = uploadFileString.substring(descriptionStart + 12, uploadFileString
							.length() - 1);
						Element xmlUploadFile = new Element("adjunto");
						xmlUploadFile.addAttribute(new Attribute("peso", weight));
						xmlUploadFile.addAttribute(new Attribute("descripcion", description));

						Map<String, Object> mapaFichero = new HashMap<String, Object>();
						mapaFichero.put("fid", fid);
						fieldTypeText = (List) getNamedParameterJdbcTemplate().queryForList(selectValuesFile,
							mapaFichero);
						Iterator fileIterator = fieldTypeText.iterator();
						while (fileIterator.hasNext()) {
							Object campoBaseObject3 = fileIterator.next();
							if (campoBaseObject3 != null) {
								String campoBase = campoBaseObject3.toString();
								if (campoBase.startsWith("{filepath=")) {
									campoBase = campoBase.substring(10, campoBase.length() - 1);
									xmlUploadFile.addAttribute(new Attribute("ruta", campoBase));
									if (campoBase.lastIndexOf("/") != -1) {
										String miniatura = campoBase.substring(0, campoBase.lastIndexOf("/")) + '/'
											+ rutaThumbnails + '/'
											+ campoBase.substring(campoBase.lastIndexOf("/") + 1);
										xmlUploadFile.addAttribute(new Attribute("miniatura", miniatura));
									}
								}
							}
						}
						xmlElement.appendChild(xmlUploadFile);
					}
				}
			}

			xmlContenido.appendChild(xmlElement);
		}

		/* Fin tratamiento fichero adjuntos */

		XML xml = new XML(new Document(xmlContenido));
		builder.setXml(xml);
	}

	/**
	 * (non-Javadoc)
	 * @see com.isotrol.impe3.idx.d6.Drupal6DatabaseReader#readContentCategories(java.lang.String)
	 */
	@Transactional
	public List<String> readContentCategories(String id) {

		List vocabularyList;
		List<String> result = new ArrayList<String>();
		String categoryName = "";
		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put(ID, id);
		vocabularyList = (List) getNamedParameterJdbcTemplate().queryForList(selectValuesVocabulary, fileMap);
		Iterator vocabularyIterator = vocabularyList.iterator();
		while (vocabularyIterator.hasNext()) {
			Object campo = vocabularyIterator.next();
			if (campo != null) {
				campo = campo.toString().substring(5, campo.toString().length() - 1);
				categoryName = getTaxName(campo.toString());

				String parentTax = parentSearch(campo.toString());
				while (!parentTax.equals("0")) {
					categoryName = getTaxName(parentTax) + "/" + categoryName;
					parentTax = parentSearch(parentTax);
				}
				result.add(categoryName);
			}
		}
		return result;
	}

	private String parentSearch(String clue) {
		String result = "";
		List parentList;

		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("tid", clue);
		parentList = (List) getNamedParameterJdbcTemplate().queryForList(selectValuesParent, fileMap);
		Iterator vocabularyIterator = parentList.iterator();

		while (vocabularyIterator.hasNext()) {
			Object campo = vocabularyIterator.next();
			if (campo != null) {
				result = campo.toString().substring(8, campo.toString().length() - 1);
			}
		}

		return result;
	}

	private String getTaxName(String tax) {
		String result = "";

		List taxList;

		Map<String, Object> fileMap = new HashMap<String, Object>();
		fileMap.put("tid", tax);
		taxList = (List) getNamedParameterJdbcTemplate().queryForList(selectTaxName, fileMap);
		Iterator vocabularyIterator = taxList.iterator();

		while (vocabularyIterator.hasNext()) {
			Object campo = vocabularyIterator.next();
			if (campo != null) {
				result = campo.toString().substring(6, campo.toString().length() - 1);
			}
		}

		return result;
	}

	private String trataWysiwyg(String texto) {
		if (texto.contains("/?q=node/")) {
			int ultimaPosicion = 0;
			ultimaPosicion = texto.indexOf("/?q=node/", ultimaPosicion) + 9;
			// -1 + 9 = 8 -> no encontrado
			while (ultimaPosicion != 8) {
				int finId = texto.indexOf('"', ultimaPosicion);
				String id = texto.substring(ultimaPosicion, finId);
				Map<String, String> campos = getIdFields(id);
				if (campos != null && campos.size() > 0) {
					String ident = "";
					String tipo = "";
					String enlace = "";
					if (campos.get("tipo") != null) {
						tipo = " tipo=\"" + campos.get("tipo") + "\" ";
					}
					if (campos.get("id") != null) {
						ident = " id=\"" + campos.get(ID) + "\" ";
					}
					if (campos.get("enlace") != null) {
						enlace = " enlace=\"" + campos.get("enlace") + "\" ";
					}
					texto = texto.substring(0, finId + 1) + ident + tipo + enlace + texto.substring(finId + 1);
					// ultimaPosicion=ultimaPosicion+ident.length()+tipo.length()+enlace.length();
				}
				ultimaPosicion = texto.indexOf("/?q=node/", ultimaPosicion) + 9;
			}
		}
		return texto;
		// href="/drupal-6.10/?q=node/24"&gt
	}

	private Map<String, String> getIdFields(String id) {
		Map<String, String> campos = new HashMap<String, String>();

		String idDestino = "";
		String tipoDestino = "";
		String enlaceDestino = "";

		Map<String, Object> mapaFichero = new HashMap<String, Object>();
		mapaFichero.put(ID, id);
		List fieldTypeText;
		fieldTypeText = (List) getNamedParameterJdbcTemplate().queryForList(selectLinkType, mapaFichero);
		// href="/drupal-6.10/?q=node/24"&gt
		Iterator fileIteratorNoTabla2 = fieldTypeText.iterator();
		while (fileIteratorNoTabla2.hasNext()) {
			LinkedHashMap campoBaseObject2 = (LinkedHashMap) fileIteratorNoTabla2.next();
			if (campoBaseObject2 != null) {
				Object nid = campoBaseObject2.get("field_nodo_nid");
				Object texto = campoBaseObject2.get("field_texto_enlace_value");

				if (nid != null) {
					idDestino = nid.toString();
					campos.put(ID, idDestino);

					Drupal6ContentBuilder d6b = (Drupal6ContentBuilder) getNamedParameterJdbcTemplate().queryForObject(
						selectContent, ImmutableMap.of(ID, nid.toString()), CONTENT_MAPPER);
					String linkType = d6b.getType();
					campos.put("tipo", linkType);

					UUID uuid = mappingsService.getContentType(linkType);
					if (uuid != null) {
						tipoDestino = uuid.toString();
						campos.put("tipo", tipoDestino);
					}

				}
				if (texto != null) {
					enlaceDestino = texto.toString();
					campos.put("enlace", enlaceDestino);
				}
			}
		}
		return campos;
	}

	public void setMappingsService(LocalMappingsService mappingsService) {
		this.mappingsService = mappingsService;
	}
}
