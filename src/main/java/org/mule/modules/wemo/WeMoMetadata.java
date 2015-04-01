package org.mule.modules.wemo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WeMoMetadata {
	private String switchName=null;
    protected transient Log logger = LogFactory.getLog(getClass());

	public String getSwitchName() {
		return switchName;
	}

	public void setSwitchName(String switchName) {
		this.switchName = switchName;
	}
}
