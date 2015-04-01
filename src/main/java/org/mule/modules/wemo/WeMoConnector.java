/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.modules.wemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.display.Summary;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.modules.wemo.strategy.ConnectorConnectionStrategy;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="we-mo", friendlyName="WeMo")
public class WeMoConnector
{

    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;

	protected transient Log logger = LogFactory.getLog(getClass());
   
   @MetaDataKeyRetriever
   public List<MetaDataKey> getMetaDataKeys() throws Exception {

       List<MetaDataKey> keys = new ArrayList<MetaDataKey>();
       Map<String, Object> map = getConnectionStrategy().getInsightSwitchFinder().getDiscoveredSwitches();
       if ((map == null) || (map.isEmpty())) {
       	logger.warn("No Belkin WeMo Insight switches found!");
       	throw new RuntimeException("No Belkin WeMo Insight switches found on network");
       }
       else {
           for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
       		String switchName = iter.next();
       		keys.add(new DefaultMetaDataKey(switchName, switchName));//"WeMoInsightSwitch"));
       		logger.info("Added " + switchName + " to MetaDataKeys");
       	}
       }

       return keys;
   }

   @MetaDataRetriever
   public MetaData getMetaData(MetaDataKey key) throws Exception {
   	//Here we describe the entity     	

	   	// RUPESH: This is what shows up as DataSense mapping fields in DataMapper
   		MetaDataModel switchModel =  new DefaultMetaDataBuilder().createPojo(WeMoMetadata.class).build();
   		return new DefaultMetaData(switchModel);
   		
//        DynamicObjectBuilder<?> dynamicObject = new DefaultMetaDataBuilder().createDynamicObject(key.getId());
//
//        dynamicObject.addSimpleField("switchName", DataType.STRING);
//        dynamicObject.addSimpleField("millisecondWaitBeforeOp", DataType.INTEGER);
//        dynamicObject.addPojoField("flashSettings", WeMoFlashSwitchMetadata.class);
//
//        DefaultMetaData defaultMetaData = new DefaultMetaData(dynamicObject.build());
//        return defaultMetaData;
   
   }


   /**
    * Discover processor
    * @return Some string
    */
   @Processor(friendlyName="Discover WeMo Insight Switches")
   @Summary ("Discover all WeMo devices on the network using UDP broadcast messages")
   public String getDiscoveredSwitches(){
      	Map<String, Object> map = getConnectionStrategy().getInsightSwitchFinder().getDiscoveredSwitches();
   	String switchList = "";
       try {
       	for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext(); ) {
       		String switchFriendlyName = iter.next();
       		logger.info("Found switch: " + switchFriendlyName);
       		if (!switchList.isEmpty()) switchList = switchList + ", " + switchFriendlyName; 
       		else switchList = switchFriendlyName;
       	}
       } catch (Exception e) {
			e.printStackTrace();
		}
       return switchList;
   }
   
   /**
    * Custom processor
    * @param sleepTime Time to wait before executing command
    * @return Some string
    */
   @Processor(friendlyName="Turn Switch ON")
   public String turnSwitchOn(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type, 
   		@Placement(group="Switch Settings") 
   	    @Default("0")
   	    @FriendlyName("Wait time before turning ON (in ms)") int sleepTime) {
   	
	   InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
	   String switchName = ""; 
       try {
    	   switchName = myInsightSwitch.getFriendlyName();
    	   Thread.sleep(sleepTime);
       	   myInsightSwitch.switchOn();
       } catch (Exception e) {
    	   e.printStackTrace();
       }
       
       return switchName + " has been turned ON";
   }

   /**
    * Custom processor
    *
    * 
    *
    * @param sleepTime Time to wait before executing command
    * @return Some string
    */
   @Processor(friendlyName="Turn Switch OFF")
   public String turnSwitchOff(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type, 
   		@Placement(group="Switch Settings") 
   	    @Default("0")
   	    @FriendlyName("Wait time before turning OFF (in ms)") int sleepTime) {

	   InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
	   String switchName = ""; 
       try {
    	    switchName = myInsightSwitch.getFriendlyName();
       		Thread.sleep(sleepTime);
       		myInsightSwitch.switchOff();
       } catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
       return switchName + " has been turned OFF";
   }

   
   /**
    *  processor that places an animal in the barn.
    *
    * 
    *
    * @param flashCount How many times to repeat flash
    * @param sleepTime Time to wait before executing command
    * @return returns processed message
    */
   @Processor(friendlyName="Flash Switch ON and OFF")
   public String flashSwitch(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type, 
   		@Placement(group="Flash Settings") 
   	    @Default("1")
   	    @FriendlyName("Count") int flashCount,
   	    @Placement(group="Flash Settings") 
   		@Default("1000")
   		@FriendlyName("Duration between flashes (in ms)") int sleepTime) 
   		{
       
	   InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
	   String switchName = ""; 
       try {
			switchName = myInsightSwitch.getFriendlyName();
			int i=0;
		   	while (i++ < flashCount) {
		   		myInsightSwitch.switchOn();
	            Thread.sleep(sleepTime);
	            myInsightSwitch.switchOff();
	            Thread.sleep(sleepTime);
		   	}
		} catch (Exception e) {
				e.printStackTrace();
		}
       	return switchName + " has been turned ON then OFF " + flashCount + " number of time(s)"; 
   }
   
   private String getNoSwitchFoundError(String switchName) {
   	if (getConnectionStrategy().getInsightSwitchFinder() != null){
			String error = null;
			if (getConnectionStrategy().getInsightSwitchFinder().getDiscoveredSwitches().size() == 0) {
				error = "Attempted to find " + switchName + ". However, no WeMo switches were discovered on this network";
			} else {
				error = "Attempted to find " + switchName + ". However, no such switch exists. Instead the following were discovered: " + getDiscoveredSwitches();
			}
			return (error);
		}
		return("Unable to connect to switches. insightSwitchFinder object is null");
	}

	private InsightSwitch getSwitchFromMetaData(String metaDataKey, Object metaDataObject) {
	   	logger.info("MetaDataKeyParam received: '" + metaDataKey + "'");
	   	logger.info("entityData received: '" + metaDataObject + "'");
	   	String mySwitchName = metaDataKey;
	   	InsightSwitch retSwitch = null;
	   	// if payload has switch name, use that. otherwise use the statically defined name which is in field 'metaDataKey'
	   	if ((metaDataObject != null) && (!(metaDataObject instanceof org.mule.transport.NullPayload))) { 
				// cleanup all junk chars
	   		mySwitchName = metaDataObject.toString().replace(System.getProperty("line.separator"), "").trim();
	
			if (mySwitchName.isEmpty())
			{
				logger.info("Dynamically passed name '" + mySwitchName + "' is invalid. Reverting to default name '" + metaDataKey);
				mySwitchName = metaDataKey;
			} else {
				logger.info("Over-riding static switch name '" + metaDataKey + "' with dynamically passed name '" + mySwitchName + "'");
			} 
		}
	   	logger.info("getSwitchFromMetaData: switch to connect to determined as:'" + mySwitchName + "'");
	   	// connect will check if switchname is cached, if not will reconnect
		try {
			retSwitch = getConnectionStrategy().connect(mySwitchName);
		} catch (Exception e1) {
			e1.printStackTrace();
			getNoSwitchFoundError(mySwitchName);
			throw new RuntimeException(e1);
		}

		return retSwitch;
	}

	/**
    * Custom processor that places an animal in the barn.
    *
    * 
    *
    * @return returns current switch state
    */

   @Processor(friendlyName="Get Switch ON/OFF/STANDBY State")
   public String getCurrentState(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type){ 
	   	InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
	   	org.mule.modules.wemo.PowerUsage.State state = null;
	   	try {
	   		PowerUsage usage = myInsightSwitch.getPowerUsage();
	   		if (usage != null) state = usage.getCurrentState();
	   		else logger.warn("getPowerUsage() returned Null");
	   		state = myInsightSwitch.getPowerUsage().getCurrentState();
	    } catch (Exception e) {
				e.printStackTrace();
		}
	   	return "Current state of WeMo Insight Switch is " + state.toString();
   }

   /**
    * Custom processor that places an animal in the barn.
    *
    * 
    *
    * @return returns current switch state
    */
   @Processor(friendlyName="Get State Transition Timestamp")
   public String getStateTransitionTimestamp(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type){
	    InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
   		int time=0;
   		try {
   			time = myInsightSwitch.getPowerUsage().getStateTransitionTimestamp();
   		} catch (Exception e) {
			e.printStackTrace();
   		}
   		return "Timestamp for State Transition: " + time;
   }
   
   /**
    * Custom processor that places an animal in the barn.
    *
    * 
    *
    * @return returns current switch state
    */    
   @Processor(friendlyName="Get time last ON for (seconds)")
   public String getLastOnForSeconds(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type){
	    InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
   		int time=0;
   		try {
   			time = myInsightSwitch.getPowerUsage().getLastOnSeconds();
   		} catch (Exception e) {
			e.printStackTrace();
		}
   		return "Switch was last on for " + time + " seconds";
   }

   /**
    * Custom processor that places an animal in the barn.
    *
    * 
    *
    * @return returns current switch state
    */
   @Processor(friendlyName="Get switch ON time today (seconds)")
   public String getOnTodaySeconds(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type){ 
	    InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
	    int time=0;
	    try {
	    	time = myInsightSwitch.getPowerUsage().getOnTodaySeconds();
	    } catch (Exception e) {
			e.printStackTrace();
		}
	    return "Switch was ON today for " + time + " seconds";
   }

   /**
    * Custom processor that places an animal in the barn.
    *
    * 
    *
    * @return returns current switch state
    */
   @Processor(friendlyName="Get current Power Usage (MilliWatts)")
   public String getCurrentMilliWatts(
   		@MetaDataKeyParam @Placement(group = "WeMo Insight Settings") @FriendlyName("Switch Name") String type){
	    InsightSwitch myInsightSwitch = getSwitchFromMetaData(type, null);
   		int time=0;
   		try {
   			time = myInsightSwitch.getPowerUsage().getCurrentMilliWatts();
   		} catch (Exception e) {
			e.printStackTrace();
		}
   		return "Device connected to switch has consumed " + time + " MilliWatts";
   }
  

    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

}