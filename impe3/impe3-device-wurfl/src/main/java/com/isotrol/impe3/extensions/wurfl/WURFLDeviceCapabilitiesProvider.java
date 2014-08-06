package com.isotrol.impe3.extensions.wurfl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.HttpHeaders;

import net.sourceforge.wurfl.core.WURFLHolder;
import net.sourceforge.wurfl.core.request.DefaultWURFLRequest;
import net.sourceforge.wurfl.core.request.WURFLRequest;
import net.sourceforge.wurfl.core.request.normalizer.UserAgentNormalizer;

import org.apache.commons.lang.StringUtils;

import com.isotrol.impe3.api.DeviceCapabilities;
import com.isotrol.impe3.api.DeviceCapabilitiesProvider;

/**
 * Default WURFL device capabilities provider implementation. 
 * @author Emilio Escobar Reyero
 */
public class WURFLDeviceCapabilitiesProvider implements DeviceCapabilitiesProvider {

	private WURFLHolder holder;
	private UserAgentNormalizer userAgentNormalizer;
	
	/**
	 * @see com.isotrol.impe3.api.DeviceCapabilitiesProvider#getDeviceCapabilities(javax.ws.rs.core.HttpHeaders)
	 */
	public DeviceCapabilities getDeviceCapabilities(HttpHeaders headers) {
		final String userAgent = getUserAgent(headers);
		final String userAgentProfile = getUAProfile(headers);
		final WURFLRequest agent = new DefaultWURFLRequest(userAgent, userAgentProfile);
		
		final net.sourceforge.wurfl.core.Device solvedDevice = holder
				.getWURFLManager().getDeviceForRequest(agent);

		if (isEmpty(solvedDevice.getCapability("mobile_browser"))){
			return null;
		}
		
		@SuppressWarnings("unchecked")
		final Map<String, String> solvedCapabilities = solvedDevice.getCapabilities();
		
		if (solvedCapabilities == null) {
			return null;
		}

		return new WURFLDeviceCapabilities(solvedCapabilities);
	}
	
	private boolean isEmpty(String capability) {
		return capability == null || capability.trim().length() == 0;
	}
	
	private String getUserAgent(HttpHeaders headers) {
		final String userAgent;

		if (getHeader("UA", headers) != null) {
			userAgent = StringUtils.trimToEmpty(getHeader("UA", headers));
		} else if (getHeader("X-Skyfire-Version", headers) != null) {
			userAgent = "Generic_Skyfire_Browser";
		} else if (getHeader("x-device-user-agent", headers) != null) {
			userAgent = StringUtils.trimToEmpty(getHeader("x-device-user-agent", headers));
		} else {
			userAgent = StringUtils.trimToEmpty(getHeader("User-Agent", headers));
		}

		return userAgent.length() == 0 ? "" : userAgentNormalizer.normalize(userAgent);
	}

	private String getUAProfile(HttpHeaders headers) {
		String headerName = null;
		String uaProfile = null;

		if (getHeader("x-wap-profile", headers) != null) {
			headerName = "x-wap-profile";
		} else if (getHeader("Profile", headers) != null) {
			headerName = "Profile";
		} else if (getHeader("wap-profile", headers) != null) {
			headerName = "wap-profile";
		} else if (getHeader("Opt", headers) != null) {
			headerName = namespaceProfileHeader(getHeader("Opt", headers));
		}

		if (headerName != null && headerName.trim().length() > 0) {
			uaProfile = getHeader(headerName, headers);
		}

		// Strip out quotes from uaProfile
		if (uaProfile != null && uaProfile.trim().length() > 0) {
			STRIP_QUOTE_PATTERN.matcher(uaProfile).replaceAll("");
		}

		return uaProfile;
	}

	private String namespaceProfileHeader(String header) {
		String namespaceNumber = nameSpaceNumber(header);
		return namespaceNumber != null ? (namespaceNumber + "-Profile") : null;
	}

	private static final Pattern STRIP_QUOTE_PATTERN = Pattern.compile("\"");
	private static final Pattern NAMESPACE_NUMBER_PATTERN = Pattern
			.compile("ns=(\\d*)");

	private static String nameSpaceNumber(String header) {
		final Matcher matcher = NAMESPACE_NUMBER_PATTERN.matcher(header);
		return matcher.matches() ? matcher.group(1) : null;
	}

	private String getHeader(String key, HttpHeaders headers) {
		final List<String> h = headers.getRequestHeader(key);

		if (h == null || h.isEmpty()) {
			return null;
		}
		
		return h.get(0);
	}

	public void setHolder(WURFLHolder holder) {
		this.holder = holder;
	}
	public void setUserAgentNormalizer(UserAgentNormalizer userAgentNormalizer) {
		this.userAgentNormalizer = userAgentNormalizer;
	}
}
