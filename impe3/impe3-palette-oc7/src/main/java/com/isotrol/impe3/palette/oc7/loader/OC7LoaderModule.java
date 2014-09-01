package com.isotrol.impe3.palette.oc7.loader;

import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.SpringSimple;


/**
 * OpenCMS 7 loader components.
 */
@Name("Cargador de contenidos para OC7")
@SpringSimple
public interface OC7LoaderModule extends Module {
	
	/** Single content by path. */
	@Name("Uno por ruta")
	PathComponent oneByPath();
	
	/** Many contents by path. */
	@Name("Varios por ruta")
	ManyPathsComponent manyByPath();
}
