## What Next?

The next step is to use an implementation of this API to control and monitor your Insight switch(es). You can build your own implementation of this API from the RAML. To see what free RAML code generators are available, check out http://raml.org/projects.html. 

### MuleSoft Implementation
I have implemented this API using the open source Mule ESB. Mule allows you to auto-generate a scaffolding project from this RAML. Then you may use the WeMo connector to talk to the actual switch. Based on intelligent rules, this application can leverage other connectors to talk to any number of systems. These could be enterprise systems (Salesforce, SAP, Email servers, Databases, Messaging Servers, etc), Social media (LinkedIn, Facebook, Twitter, etc) and other services (Zendesk, Box, BigData, SMS, Paypal, Zuora, etc). The full list of connectors are available here:
   http://www.mulesoft.org/connectors
   
### How is this different from IFTTT.com Rules
IFTTT does exactly what the name says - If This Then That. It is great for simple rules, and for personal use (e.g. email, SMS, dropbox). However, 'channels' do not exist for many Enterprise level systems. Similarly, complex rules and controlling multiple devices are not currently possible with IFTTT. 

### Example usages: 

* Automatically flash lights to celebrate a Salesforce deal closing
* Turn an appliance OFF if it has been ON for over a pre-set limit or has drawn more power than it should
* Open a ticket in a ticketing system if power usage goes above a certain limit, then send an email to the technician who should have the device checked out in person
* Turn one or more devices ON/OFF when you get an email, SMS, tweet, poke, etc
* Use the WeMo in combination with any number of systems with REST API's, SOAP API's, Java API's
* Trasform and map data in multiple formats (JSON, XML, CSV) when interfacing the WeMo with multiple systems with their own data formats
* Use your favorite language (Python, Ruby, Java, JavaScript, Groovy) to write custom code snippets and transformers to supplement OOTB rules involving the WeMo
* Secure access to your otherwise unsecure WeMo device (one of the flaws with the underlying UPnP protocol) if you plan to allow control over the internet via HTTP
* Other use cases for the WeMo and MuleESB: **Use your imagination here!**