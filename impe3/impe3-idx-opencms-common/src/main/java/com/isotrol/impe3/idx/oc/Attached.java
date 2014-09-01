package com.isotrol.impe3.idx.oc;


public class Attached {

	public static final String FILE_EXTENSION_PDF = ".pdf";
	public static final String FILE_EXTENSION_XLS = ".xls";
	public static final String FILE_EXTENSION_DOC = ".doc";

	private String id;
	private String path;
	private FileTypes type;

	/**
	 * @return the id
	 */
	public final String getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the path
	 */
	public final String getPath() {
		return this.path;
	}

	/**
	 * @param path the path to set
	 */
	public final void setPath(String path) {

		parsePath(path);

		this.path = path;

	}

	private void parsePath(String path) {

		if (path != null) {

			if (path.toLowerCase().endsWith(FILE_EXTENSION_DOC)) {
				this.type = FileTypes.DOC;
			} else if (path.toLowerCase().endsWith(FILE_EXTENSION_PDF)) {
				this.type = FileTypes.PDF;
			} else if (path.toLowerCase().endsWith(FILE_EXTENSION_XLS)) {
				this.type = FileTypes.XLS;
			}

		}

	}

	/**
	 * @return the type
	 */
	public final FileTypes getType() {
		return this.type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(FileTypes type) {
		this.type = type;
	}

	/**
	 * Devuelve TRUE si el recurso es un PDF.
	 * @return boolean
	 */
	public final boolean isPDF() {

		if (this.type == FileTypes.PDF) {
			return true;
		}

		return false;
	}

	/**
	 * Devuelve TRUE si el recurso es un DOC.
	 * @return boolean
	 */
	public final boolean isDOC() {

		if (this.type == FileTypes.DOC) {
			return true;
		}

		return false;
	}

	/**
	 * Devuelve TRUE si el recurso es un XLS.
	 * @return boolean
	 */
	public final boolean isXLS() {

		if (this.type == FileTypes.XLS) {
			return true;
		}

		return false;
	}

	/**
	 * Clase interna para almacenar todos los datos de una categoria, no solo su id, para poder construir los grupos de
	 * rutas.
	 */
	/*
	class Category {
		private String categoryId;
		private String categoryPath;
		private String categoryName;
		private String mappedUUID;

		public String getCategoryId() {
			return categoryId;
		}

		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}

		public String getCategoryPath() {
			return categoryPath;
		}

		public void setCategoryPath(String categoryPath) {
			this.categoryPath = categoryPath;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public String getMappedUUID() {
			return mappedUUID;
		}

		public void setMappedUUID(String mappedUUID) {
			this.mappedUUID = mappedUUID;
		}

		public String toString() {
			return categoryPath;
		}

	}*/

}
