/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.wemo.strategy;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.ConnectionException;
import org.mule.api.annotations.components.ConnectionManagement;
import org.mule.api.annotations.display.Password;
import org.mule.api.annotations.Connect;
import org.mule.api.annotations.ConnectionIdentifier;
import org.mule.api.annotations.Disconnect;
import org.mule.api.annotations.TestConnectivity;
import org.mule.api.annotations.ValidateConnection;
import org.mule.api.annotations.param.ConnectionKey;
import org.mule.modules.wemo.InsightSwitch;
import org.mule.modules.wemo.InsightSwitchFinder;

/**
 * Configuration type Strategy
 *
 * @author MuleSoft, Inc.
 */
//@Configuration(configElementName = "config-type", friendlyName = "Configuration type strategy")
@ConnectionManagement(friendlyName = "WeMo_Connector")
public class ConnectorConnectionStrategy
{
    /**
     * Configurable
     */
    //@Configurable
    //@Default("value")
    private InsightSwitchFinder insightSwitchFinder;
    protected transient Log logger = LogFactory.getLog(getClass());
    /**
     * Set strategy property
     *
     * @param myStrategyProperty my strategy property
     */
    public void setInsightSwitchFinder(InsightSwitchFinder switchFinder) {
        this.insightSwitchFinder = switchFinder;
    }

    /**
     * Get property
     */
    public InsightSwitchFinder getInsightSwitchFinder() {
/*        if (this.insightSwitchFinder == null){
           	connect(null);
           }*/
    	return this.insightSwitchFinder;
    }
    
    /**
     * Connect
     *
     * @param username A username
     * @param password A password
     * @throws ConnectionException
     */
    @Connect
    @TestConnectivity
    public void connect(@ConnectionKey String username, @Password String password)
        throws ConnectionException {
        
         // No connection pooling, will keep a singleton connection for UPnP Controlpoint
         
    	if (insightSwitchFinder == null) {
 	        try {
 	        	connect(null);
 	
 	        } catch (Exception e) {
 				e.printStackTrace();
 			}
    	}
    }
    
    public synchronized InsightSwitch connect(String switchFriendlyName) {
   		InsightSwitch retSwitch = null;
   		Set<String> cachedSwitchNames = null;
   		if (insightSwitchFinder != null) {
   			cachedSwitchNames = insightSwitchFinder.getDiscoveredSwitches().keySet();
   			logger.info("In connect method. Connection to " + switchFriendlyName + " requested. Current cached switches are: '" + cachedSwitchNames);
   	   		// if switch is already cached, return it
   	   		if (cachedSwitchNames.contains(switchFriendlyName)) {
   	   			retSwitch = (InsightSwitch) insightSwitchFinder.getDiscoveredSwitches().get(switchFriendlyName);
   	   			return retSwitch;
   	   		} else {
   	   			logger.info("No switches cached. Discover all switches");
   	   		}
   		}

        try {
    		insightSwitchFinder = new InsightSwitchFinder(new String[0]);
    		insightSwitchFinder.findSwitches();
    		if (insightSwitchFinder.getDiscoveredSwitches().size() == 0){
    			throw new IllegalStateException("No WeMo Insight switches found on network!");
    		}
    		// if this is the first ever call to connect, switchFriendlyName passed could be null. Log which switches were discovered instead
        	if (switchFriendlyName == null){
        		logger.warn("No switches specified in connect. These are the available switches: " + insightSwitchFinder.getDiscoveredSwitches());
        		return retSwitch; // will be null
        	}
        	
        	// Switch found.
        	logger.info("Connecting to: " + switchFriendlyName);
        	if (insightSwitchFinder != null) { 
        		retSwitch = insightSwitchFinder.getSwitch(switchFriendlyName);
        	}
        } catch (Exception e) {
			e.printStackTrace();
		}
       	return retSwitch;
       }    
    /**
     * Disconnect
     */
    @Disconnect
    public void disconnect() {
        
         // CODE FOR CLOSING A CONNECTION GOES IN HERE
         
    	if (insightSwitchFinder != null){
 			try {
 				insightSwitchFinder.close();
 			} catch (Exception e) {
 				// 
 				e.printStackTrace();
 			}
 			insightSwitchFinder = null;
    	}

    }
    
    /* Are we connected
    */
    @ValidateConnection
   public boolean isConnected() {
       return false;
   }

   /**
    * Are we connected
    * @return String
    */
   @ConnectionIdentifier
   public String connectionId() {
       return "001";
   }
   

}