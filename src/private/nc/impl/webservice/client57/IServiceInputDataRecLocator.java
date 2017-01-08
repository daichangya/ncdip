/**
 * IServiceInputDataRecLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package nc.impl.webservice.client57;

public class IServiceInputDataRecLocator extends org.apache.axis.client.Service implements nc.impl.webservice.client57.IServiceInputDataRec {

    public IServiceInputDataRecLocator() {
    }


    public IServiceInputDataRecLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public IServiceInputDataRecLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for IServiceInputDataRecSOAP11port_http
    private java.lang.String IServiceInputDataRecSOAP11port_http_address = "http://127.0.0.1/uapws/service/IServiceInputDataRec";

    public java.lang.String getIServiceInputDataRecSOAP11port_httpAddress() {
        return IServiceInputDataRecSOAP11port_http_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String IServiceInputDataRecSOAP11port_httpWSDDServiceName = "IServiceInputDataRecSOAP11port_http";

    public java.lang.String getIServiceInputDataRecSOAP11port_httpWSDDServiceName() {
        return IServiceInputDataRecSOAP11port_httpWSDDServiceName;
    }

    public void setIServiceInputDataRecSOAP11port_httpWSDDServiceName(java.lang.String name) {
        IServiceInputDataRecSOAP11port_httpWSDDServiceName = name;
    }

    public nc.impl.webservice.client57.IServiceInputDataRecPortType getIServiceInputDataRecSOAP11port_http() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(IServiceInputDataRecSOAP11port_http_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getIServiceInputDataRecSOAP11port_http(endpoint);
    }

    public nc.impl.webservice.client57.IServiceInputDataRecPortType getIServiceInputDataRecSOAP11port_http(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            nc.impl.webservice.client57.IServiceInputDataRecSOAP11BindingStub _stub = new nc.impl.webservice.client57.IServiceInputDataRecSOAP11BindingStub(portAddress, this);
            _stub.setPortName(getIServiceInputDataRecSOAP11port_httpWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setIServiceInputDataRecSOAP11port_httpEndpointAddress(java.lang.String address) {
        IServiceInputDataRecSOAP11port_http_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (nc.impl.webservice.client57.IServiceInputDataRecPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                nc.impl.webservice.client57.IServiceInputDataRecSOAP11BindingStub _stub = new nc.impl.webservice.client57.IServiceInputDataRecSOAP11BindingStub(new java.net.URL(IServiceInputDataRecSOAP11port_http_address), this);
                _stub.setPortName(getIServiceInputDataRecSOAP11port_httpWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("IServiceInputDataRecSOAP11port_http".equals(inputPortName)) {
            return getIServiceInputDataRecSOAP11port_http();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://pub.dip.itf.nc/IServiceInputDataRec", "IServiceInputDataRec");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://pub.dip.itf.nc/IServiceInputDataRec", "IServiceInputDataRecSOAP11port_http"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("IServiceInputDataRecSOAP11port_http".equals(portName)) {
            setIServiceInputDataRecSOAP11port_httpEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
