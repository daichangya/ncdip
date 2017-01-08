package nc.impl.webservice.client57;

import java.rmi.RemoteException;

public class IServiceInputDataRecPortTypeProxy implements nc.impl.webservice.client57.IServiceInputDataRecPortType {
  private String _endpoint = null;
  private nc.impl.webservice.client57.IServiceInputDataRecPortType iServiceInputDataRecPortType = null;
  
  public IServiceInputDataRecPortTypeProxy() {
    _initIServiceInputDataRecPortTypeProxy();
  }
  
  private void _initIServiceInputDataRecPortTypeProxy() {
    try {
      iServiceInputDataRecPortType = (new nc.impl.webservice.client57.IServiceInputDataRecLocator()).getIServiceInputDataRecSOAP11port_http();
      if (iServiceInputDataRecPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iServiceInputDataRecPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iServiceInputDataRecPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iServiceInputDataRecPortType != null)
      ((javax.xml.rpc.Stub)iServiceInputDataRecPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public nc.impl.webservice.client57.IServiceInputDataRecPortType getIServiceInputDataRecPortType() {
    if (iServiceInputDataRecPortType == null)
      _initIServiceInputDataRecPortTypeProxy();
    return iServiceInputDataRecPortType;
  }

public String callData(String prop) throws RemoteException {
    if (iServiceInputDataRecPortType == null)
    	_initIServiceInputDataRecPortTypeProxy();
      return iServiceInputDataRecPortType.callData(prop);
    }
  
  
}