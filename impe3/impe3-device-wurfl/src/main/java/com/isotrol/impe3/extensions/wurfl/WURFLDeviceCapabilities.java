package com.isotrol.impe3.extensions.wurfl;

import java.util.Map;

import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.api.DeviceCapabilities;

public class WURFLDeviceCapabilities extends ForwardingMap<String, String>
		implements DeviceCapabilities {

	private final String RESOLUTION_WIDTH = "resolution_width";
	private final String XHTML_SUPPORT_LEVEL = "xhtml_support_level";
	private final String HTML_WEB_4_0 = "html_web_4_0";
	private final String FULL_FLASH_SUPPORT = "full_flash_support";
	private final String MAX_IMAGE_WIDTH = "max_image_width";
	private final String VIEWPORT_WIDTH = "viewport_width";
	private final String VIEWPORT_DEVICE_WITH_TOKEN = "device_width_token";
	private final String VIEWPORT_WIDTH_EQUALS_RESOLUTION_WIDTH = "width_equals_resolution_width";
	private final String VIEWPORT_WIDTH_EQUALS_MAX_IMAGE_WIDTH = "width_equals_max_image_width";
	
	
	private final Map<String, String> caps;
	
	private final int width;
	private final boolean flash;
	private final boolean html4;
	private final boolean xhtml;

	public WURFLDeviceCapabilities(Map<String, String> original) {
		caps = original == null ? ImmutableMap.<String, String> of()
				: ImmutableMap.copyOf(original);
		
		width = guessWidth(caps);
		flash = guessFlash(caps);
		html4 = guessHtml4(caps);
		xhtml = guessXhtml(caps);
	}

	private int guessWidth(Map<String, String> map) {
		final String viewport = map.get(VIEWPORT_WIDTH);
		
		final String w;
		
		if (viewport == null) {
			w = map.get(RESOLUTION_WIDTH);
		} else if (viewport.trim().length() == 0) {
			w = map.get(RESOLUTION_WIDTH);
		} else if (VIEWPORT_DEVICE_WITH_TOKEN.equals(viewport)) {
			w = map.get(RESOLUTION_WIDTH);
		} else if (VIEWPORT_WIDTH_EQUALS_RESOLUTION_WIDTH.equals(viewport)) {
			w = map.get(RESOLUTION_WIDTH);
		} else if (VIEWPORT_WIDTH_EQUALS_MAX_IMAGE_WIDTH.equals(viewport)) {
			w = map.get(MAX_IMAGE_WIDTH);
		} else {
			w = map.get(RESOLUTION_WIDTH);
		}
		
		try {
			return Integer.parseInt(w);	
		} catch (Exception e) {
			return 1024;
		}
	}
	private boolean guessFlash(Map<String, String> map) {
		final String f = map.get(FULL_FLASH_SUPPORT);
		return f != null && "true".equals(f.trim());
	}
	private boolean guessHtml4(Map<String, String> map) {
		final String h = map.get(HTML_WEB_4_0);
		return h != null && "true".equals(h.trim());
	}
	private boolean guessXhtml(Map<String, String> map) {
		int level = -1;

		try {
			level = Integer.parseInt(map.get(XHTML_SUPPORT_LEVEL));
		} catch(Exception e) {
		}
		
		
		return level >= 0;
	}
	
	public int getWidth() {
		return width;
	}

	public boolean isFlashSupported() {
		return flash;
	}

	public boolean isHTML4Supported() {
		return html4;
	}

	public boolean isXHTMLSupported() {
		return xhtml;
	}

	@Override
	protected Map<String, String> delegate() {
		return caps;
	}

}
