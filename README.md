# WeMo Anypoint Connector

This connector is an example of an Internet of Things (IoT) device that can be connected to the rest of the enterprise and controlled from MuleESB. The device in this case is a Belkin WeMo Insight Switch (http://www.belkin.com/us/p/P-F7C029/). You can purchase this device at your local electronics store (e.g. BestBuy, Apple Store, Amazon, etc). The WeMo switch allows you to remotely turn ON/OFF any device connected to its socket. The switch uses WiFi to connect to your network. Belkin provides mobile apps that let you turn the switch on and off. This connector lets you do the same from MuleESB. It also allows you to get usage stats (e.g. power consumed, how long it has been on, etc) as well. Combining this with the orchestration and enterprise connectivity features of Mule (e.g. Salesforce, SAP, Oracle, etc), you get an enterprise grade version of IFTTT (If This Then That).

This connector uses the underlying UPnP (Universal Plug And Play) protocol to discover and talk to Belkin WeMo Insight Switches. It uses the open source UPnP library from Cybergarage: http://www.cybergarage.org/do/view/Main/CyberLinkForJava

The Mule application has to be running on the same network as the Belkin WeMo switch to be able to communicate with the switch. For instance, if you configured your WeMo device network settings to use 'wifi_guest', then your laptop running Mule would need to be on the same wifi. Mule can talk to any number of switches in the same network and it can dynamically discover the switches on the network. 

# Sample Projects   
See the demo folder for a few examples. 
1. Use the 'findingwemo' application to discover all Belkin WeMo Insight switches on your network. Once you get the 'friendly names' of these switches, you can direct commands to it.
2. The 'email-wemo' application allows you to send an email to your WeMo switch asking it to turn ON or OFF
3. The 'wemo-raml-api' application allows you to use the API Console to invoke all the operations that are available on any Belkin Wemo device in the same network, via a REST API.

# Connector Configuration
Drag-drop the WeMo connector into your flow.
UPnP does not support authentication so communication is unsecure by design. However, the username/password field in the connector configuratin cannot be empty. So give it some junk values (e.g. test/test)
Next to 'Switch Name', select the refresh icon to query your network (takes about 10 seconds) and discover all switches in the network
You can use the discovered name as-is or replace it with a MEL expression (e.g. payload.switchName)

## Example IoT integration usages: 

* Automatically flash lights to celebrate a Salesforce deal closing
* Turn an appliance OFF if it has been ON for over a pre-set limit or has drawn more power than it should
* Open a ticket in a ticketing system if power usage goes above a certain limit, then send an email to the technician who should have the device checked out in person
* Turn one or more devices ON/OFF when you get an email, SMS, tweet, poke, etc
* Use the WeMo in combination with any number of systems with REST API's, SOAP API's, Java API's
* Trasform and map data in multiple formats (JSON, XML, CSV) when interfacing the WeMo with multiple systems with their own data formats
* Use your favorite language (Python, Ruby, Java, JavaScript, Groovy) to write custom code snippets and transformers to supplement OOTB rules involving the WeMo
* Secure access to your otherwise unsecure WeMo device (one of the flaws with the underlying UPnP protocol) if you plan to allow control over the internet via HTTP


# Mule supported versions
Mule 3.5.x
Mule 3.6.x

# Belkin WeMo supported products
Belkin WeMo Insight Switch (Part: F7C029fc)



# Installation 
For beta connectors you can download the source code and build it with devkit to find it available on your local repository. Then you can add it to Studio.

For released connectors you can download them from the update site in Studio. 
Open MuleStudio, go to Help → Install New Software and select Anypoint Connectors Update Site where you’ll find all avaliable connectors.

#Usage
For information about usage our documentation at http://github.com/mulesoft/we-mo.

# Reporting Issues

We use GitHub:Issues for tracking issues with this connector. You can report new issues at this link http://github.com/mulesoft/we-mo/issues.