package com.microsoft.azuretools.utils;

import java.io.File;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.microsoft.azuretools.azurecommons.helpers.NotNull;
import com.microsoft.azuretools.azurecommons.util.GetHashMac;
import com.microsoft.azuretools.azurecommons.xmlhandling.DataOperations;

public class TelemetryUtils {
	private static final String EXPR_LISTENER = "/web-app/listener[listener-class/text()='com.microsoft.applicationinsights.web.internal.ApplicationInsightsServletContextListener']";
	private static final String LISTENER_TAG = "listener";
	private static final String LISTENER_CLASS_TAG = "listener-class";
	private static final String LISTENER_CLASS = "com.microsoft.applicationinsights.web.internal.ApplicationInsightsServletContextListener";
	
	
    @NotNull
    public static String getMachieId(String dataFile, String prefVal, String instId) {
        String ret = "";
        if (new File(dataFile).exists()) {
            String prefValue = DataOperations.getProperty(dataFile, prefVal);
            if (prefValue != null && prefValue.equalsIgnoreCase("false")) {
                return ret;
            }
            ret = DataOperations.getProperty(dataFile, instId);
            if (ret == null || ret.isEmpty() || !GetHashMac.IsValidHashMacFormat(ret)) {
                ret = GetHashMac.GetHashMac();
            }
        } else {
            ret = GetHashMac.GetHashMac();
        }

        return ret;
    }
    
    /**
     * This method add a servlet context listener in web.xml.
     * This is a work round for issue https://github.com/Microsoft/azure-tools-for-java/issues/2122
     * @throws XPathExpressionException 
     */
    public static void setAIServletContextListener(Document webXMLDoc) throws XPathExpressionException {
        if (webXMLDoc == null) {
            return;
        }
		final XPath xpath = XPathFactory.newInstance().newXPath();
		final Element eleListenerMapping = (Element) xpath.evaluate(EXPR_LISTENER, webXMLDoc, XPathConstants.NODE);
		if (eleListenerMapping == null) {
			final Element listenerMapping = webXMLDoc.createElement(LISTENER_TAG);
			final Element listenerName = webXMLDoc.createElement(LISTENER_CLASS_TAG);
			listenerName.setTextContent(LISTENER_CLASS);
			listenerMapping.appendChild(listenerName);

			final NodeList existingListenerNodeList = webXMLDoc.getElementsByTagName(LISTENER_TAG);
			final Node existingListenerNode = existingListenerNodeList != null
					&& existingListenerNodeList.getLength() > 0 ? existingListenerNodeList.item(0) : null;

			webXMLDoc.getDocumentElement().insertBefore(listenerMapping, existingListenerNode);
		}
    }
   
    /**
     * This method remove a servlet context listener in web.xml
     * This is a work round for issue https://github.com/Microsoft/azure-tools-for-java/issues/2122
     * @param webXMLDoc
     * @throws XPathExpressionException
     */
	public static void removeAIServletContextListener(Document webXMLDoc) throws XPathExpressionException {
		if (webXMLDoc == null) {
			return;
		}
		final XPath xpath = XPathFactory.newInstance().newXPath();
		final String exprListener = EXPR_LISTENER;
		final Element eleListener = (Element) xpath.evaluate(exprListener, webXMLDoc, XPathConstants.NODE);
		if (eleListener != null) {
			eleListener.getParentNode().removeChild(eleListener);
		}
	}

    

}
