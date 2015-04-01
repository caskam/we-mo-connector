
package org.mule.modules.wemo.connectivity;

import javax.annotation.Generated;
import org.mule.api.ConnectionException;
import org.mule.devkit.shade.connection.management.ConnectionManagementConnectionAdapter;
import org.mule.modules.wemo.strategy.ConnectorConnectionStrategy;

@Generated(value = "Mule DevKit Version 3.6.0", date = "2015-03-31T11:50:36-07:00", comments = "Build UNNAMED.2363.ef5c8a7")
public class ConnectorConnectionStrategyWeMoConnectorAdapter
    extends ConnectorConnectionStrategy
    implements ConnectionManagementConnectionAdapter<ConnectorConnectionStrategy, ConnectionManagementConfigWeMoConnectorConnectionKey>
{


    @Override
    public void connect(ConnectionManagementConfigWeMoConnectorConnectionKey connectionKey)
        throws ConnectionException
    {
        super.connect(connectionKey.getUsername(), connectionKey.getPassword());
    }

    @Override
    public void disconnect() {
        super.disconnect();
    }

    @Override
    public String connectionId() {
        return super.connectionId();
    }

    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public ConnectorConnectionStrategy getStrategy() {
        return this;
    }

}
