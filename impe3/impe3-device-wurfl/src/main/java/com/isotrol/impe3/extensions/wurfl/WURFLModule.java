package com.isotrol.impe3.extensions.wurfl;

import com.isotrol.impe3.api.DeviceCapabilitiesProvider;
import com.isotrol.impe3.api.metadata.Bundle;
import com.isotrol.impe3.api.metadata.Copyright;
import com.isotrol.impe3.api.metadata.Description;
import com.isotrol.impe3.api.metadata.Name;
import com.isotrol.impe3.api.metadata.Version;
import com.isotrol.impe3.api.modules.Module;

/**
 * Simple WURFL module for default configuration
 * 
 * @author Emilio Escobar Reyero
 */
@Bundle
@Name("name")
@Description("desc")
@Copyright("Isotrol, SA. (GPL3)")
@Version("1.0")
public interface WURFLModule extends Module {

	@Name("provider")
	DeviceCapabilitiesProvider provider();

}
