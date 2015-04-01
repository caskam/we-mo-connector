
package org.mule.modules.wemo.config;

import javax.annotation.Generated;
import org.mule.config.MuleManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


/**
 * Registers bean definitions parsers for handling elements in <code>http://www.mulesoft.org/schema/mule/we-mo</code>.
 * 
 */
@Generated(value = "Mule DevKit Version 3.6.0", date = "2015-03-31T11:50:36-07:00", comments = "Build UNNAMED.2363.ef5c8a7")
public class WeMoNamespaceHandler
    extends NamespaceHandlerSupport
{

    private static Logger logger = LoggerFactory.getLogger(WeMoNamespaceHandler.class);

    private void handleException(String beanName, String beanScope, NoClassDefFoundError noClassDefFoundError) {
        String muleVersion = "";
        try {
            muleVersion = MuleManifest.getProductVersion();
        } catch (Exception _x) {
            logger.error("Problem while reading mule version");
        }
        logger.error(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [we-mo] is not supported in mule ")+ muleVersion));
        throw new FatalBeanException(((((("Cannot launch the mule app, the  "+ beanScope)+" [")+ beanName)+"] within the connector [we-mo] is not supported in mule ")+ muleVersion), noClassDefFoundError);
    }

    /**
     * Invoked by the {@link DefaultBeanDefinitionDocumentReader} after construction but before any custom elements are parsed. 
     * @see NamespaceHandlerSupport#registerBeanDefinitionParser(String, BeanDefinitionParser)
     * 
     */
    public void init() {
        try {
            this.registerBeanDefinitionParser("config", new WeMoConnectorConnectorConnectionStrategyConfigDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("config", "@Config", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-discovered-switches", new GetDiscoveredSwitchesDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-discovered-switches", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("turn-switch-on", new TurnSwitchOnDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("turn-switch-on", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("turn-switch-off", new TurnSwitchOffDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("turn-switch-off", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("flash-switch", new FlashSwitchDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("flash-switch", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-current-state", new GetCurrentStateDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-current-state", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-state-transition-timestamp", new GetStateTransitionTimestampDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-state-transition-timestamp", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-last-on-for-seconds", new GetLastOnForSecondsDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-last-on-for-seconds", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-on-today-seconds", new GetOnTodaySecondsDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-on-today-seconds", "@Processor", ex);
        }
        try {
            this.registerBeanDefinitionParser("get-current-milli-watts", new GetCurrentMilliWattsDefinitionParser());
        } catch (NoClassDefFoundError ex) {
            handleException("get-current-milli-watts", "@Processor", ex);
        }
    }

}
