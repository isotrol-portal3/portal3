package com.isotrol.impe3.palette.oc7.loader;


import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.Optional;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;


/**
 * Single content by path configuration.
 * @author Juan Manuel Valverde Ramirez
 */
public interface ManyPathsConfig extends Configuration {
	/** First OpenCMS content path. */
	@Name("Ruta del contenido 1 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	String path1();

	/** 2nd OpenCMS content path. */
	@Name("Ruta del contenido 2 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path2();

	/** 3rd OpenCMS content path. */
	@Name("Ruta del contenido 3 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path3();

	/** 4th OpenCMS content path. */
	@Name("Ruta del contenido 4 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path4();

	/** 5th OpenCMS content path. */
	@Name("Ruta del contenido 5 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path5();

	/** 6th OpenCMS content path. */
	@Name("Ruta del contenido 6 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path6();

	/** 7th OpenCMS content path. */
	@Name("Ruta del contenido 7 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path7();

	/** 8th OpenCMS content path. */
	@Name("Ruta del contenido 8 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path8();

	/** 9th OpenCMS content path. */
	@Name("Ruta del contenido 9 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path9();

	/** 10th OpenCMS content path. */
	@Name("Ruta del contenido 10 en OpenCMS")
	@Description(Constants.DESCRIPTION)
	@Optional
	String path10();
}
