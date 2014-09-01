package com.isotrol.impe3.palette.oc7.loader;

import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Single content by path configuration.
 */
public interface PathConfig extends Configuration {
	/** OpenCMS path. */
	@Name("Ruta del contenido en OpenCMS")
	@Description("Selecciona como contenido a mostrar un contenido de OpenCMS de tipo Texto (ej: '/sites/default/general/ayuda').")
	String path();
}
