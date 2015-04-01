
package org.mule.modules.wemo.adapters;

import javax.annotation.Generated;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.devkit.ProcessAdapter;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.devkit.ProcessTemplate;
import org.mule.api.processor.MessageProcessor;
import org.mule.api.routing.filter.Filter;
import org.mule.modules.wemo.WeMoConnector;
import org.mule.security.oauth.callback.ProcessCallback;


/**
 * A <code>WeMoConnectorProcessAdapter</code> is a wrapper around {@link WeMoConnector } that enables custom processing strategies.
 * 
 */
@Generated(value = "Mule DevKit Version 3.6.0", date = "2015-03-31T11:50:36-07:00", comments = "Build UNNAMED.2363.ef5c8a7")
public class WeMoConnectorProcessAdapter
    extends WeMoConnectorLifecycleAdapter
    implements ProcessAdapter<WeMoConnectorCapabilitiesAdapter>
{


    public<P >ProcessTemplate<P, WeMoConnectorCapabilitiesAdapter> getProcessTemplate() {
        final WeMoConnectorCapabilitiesAdapter object = this;
        return new ProcessTemplate<P,WeMoConnectorCapabilitiesAdapter>() {


            @Override
            public P execute(ProcessCallback<P, WeMoConnectorCapabilitiesAdapter> processCallback, MessageProcessor messageProcessor, MuleEvent event)
                throws Exception
            {
                return processCallback.process(object);
            }

            @Override
            public P execute(ProcessCallback<P, WeMoConnectorCapabilitiesAdapter> processCallback, Filter filter, MuleMessage message)
                throws Exception
            {
                return processCallback.process(object);
            }

        }
        ;
    }

}
