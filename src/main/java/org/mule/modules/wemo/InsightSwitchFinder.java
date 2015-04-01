package org.mule.modules.wemo;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InsightSwitchFinder implements AutoCloseable {

    private static final String BELKIN_INSIGHT_DEVICE_TYPE = "urn:Belkin:device:insight:1";

    private final ControlPoint controlPoint;

    private final List<String> friendlyNames;
    
    private final Map<String, Object> discoveredSwitches;
    
    protected transient Log logger = LogFactory.getLog(getClass());

    public InsightSwitchFinder(String... friendlyNames) {
//        if (friendlyNames.length == 0) {
//            //throw new IllegalArgumentException("Specify at least 1 friendly name to search for");
//        	friendlyNames[0] = "WeMo Insight"; // use a random name to allow a discovery even if no switch name specified
//        }

        this.friendlyNames = Collections.unmodifiableList(Arrays.asList(friendlyNames));
        this.discoveredSwitches = Collections.synchronizedMap(new HashMap<String, Object>());

        this.controlPoint = new ControlPoint();
        try{
	        if (!controlPoint.start()) {
	            throw new RuntimeException("Unable to start UPnP ControlPoint");
        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    public boolean findSwitches() throws InterruptedException {
        //return findSwitches(Long.MAX_VALUE, TimeUnit.DAYS);
    	return findSwitches(10, TimeUnit.SECONDS);
    }

    public boolean findSwitches(long timeout, TimeUnit unit) throws InterruptedException {
        logger.info("Find Switches invoked");
        int numberSwitches = friendlyNames.size();
        if (numberSwitches > 0)
        	logger.info("Attempting to find " + numberSwitches + " switch: " + friendlyNames);
        else
        	logger.info("No switch names specified. Attempting to find upto 5 switches on network");
        // if zero switches, use 1 instead
    	// final CountDownLatch countDownLatch = new CountDownLatch(numberSwitches==0?1:numberSwitches);
    	final CountDownLatch countDownLatch = new CountDownLatch(numberSwitches==0?5:numberSwitches);


        controlPoint.addDeviceChangeListener(new DeviceChangeListener() {
            @Override
            public void deviceAdded(Device device) {
            	String deviceType = device.getDeviceType(); 
                if (!device.getDeviceType().equals(BELKIN_INSIGHT_DEVICE_TYPE)) {
                	logger.info("Found and ignoring non-Belkin device: " + deviceType);
                	return;
                } else {
                	logger.info("Found Belkin WeMo Insight: " + deviceType);
                }

                InsightSwitch insightSwitch = new InsightSwitch(device);

                try {
                	String discoveredSwitchName = insightSwitch.getFriendlyName();
                	discoveredSwitches.put(discoveredSwitchName, insightSwitch);
                	// Check if user input friendlyName matches this device
                    if (friendlyNames.contains(discoveredSwitchName)) {
                        countDownLatch.countDown();
                    }
                } catch (InsightSwitchOperationException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void deviceRemoved(Device device) {

            }
        });
        try {
            controlPoint.search();
        } catch (Throwable e) {
            //String msg = e.getMessage();
        	e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return countDownLatch.await(timeout, unit);
    }

    public InsightSwitch getSwitch(String friendlyName) throws InsightSwitchNotFoundException {
        if (discoveredSwitches.containsKey(friendlyName)) { 
        	logger.info("Found cached switch object: " + friendlyName + " in existing InsightSwitchFinder. Re-using");
        	return (InsightSwitch) discoveredSwitches.get(friendlyName);
        }
    	Device device = controlPoint.getDevice(friendlyName);

        if (device == null) {
            logger.warn("Device named <" + friendlyName + "> not found. Make sure Mule is running on the same network as the switch");
        	//throw new InsightSwitchNotFoundException("Device named <" + friendlyName + "> not found. Make sure you passed the name to the constructor and called findSwitches()");
        }

        return new InsightSwitch(device);
    }

    @Override
    public void close() throws Exception {
        controlPoint.stop();
    }
    
    public Map<String, Object> getDiscoveredSwitches(){
    	if (discoveredSwitches.isEmpty()) {
    		logger.warn("Empty list for discovered switches. Attempting to find switches");
    		try {
				this.findSwitches();
				Thread.sleep(100);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	return discoveredSwitches;
    }
}
