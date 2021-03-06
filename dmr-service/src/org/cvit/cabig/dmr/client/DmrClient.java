/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */

package org.cvit.cabig.dmr.client;

import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.AxisClient;
import org.apache.axis.client.Stub;
import org.apache.axis.configuration.FileProvider;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI.MalformedURIException;

import org.oasis.wsrf.properties.GetResourcePropertyResponse;

import org.globus.gsi.GlobusCredential;

import org.cvit.cabig.dmr.stubs.DmrPortType;
import org.cvit.cabig.dmr.stubs.service.DmrServiceAddressingLocator;
import org.cvit.cabig.dmr.common.DmrI;
import gov.nih.nci.cagrid.introduce.security.client.ServiceSecurityClient;

/**
 * This class is autogenerated, DO NOT EDIT GENERATED GRID SERVICE ACCESS METHODS.
 *
 * This client is generated automatically by Introduce to provide a clean unwrapped API to the
 * service.
 *
 * On construction the class instance will contact the remote service and retrieve it's security
 * metadata description which it will use to configure the Stub specifically for each method call.
 * 
 * @created by Introduce Toolkit version 1.2
 */
public class DmrClient extends DmrClientBase implements DmrI {	

	public DmrClient(String url) throws MalformedURIException, RemoteException {
		this(url,null);	
	}

	public DmrClient(String url, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(url,proxy);
	}
	
	public DmrClient(EndpointReferenceType epr) throws MalformedURIException, RemoteException {
	   	this(epr,null);
	}
	
	public DmrClient(EndpointReferenceType epr, GlobusCredential proxy) throws MalformedURIException, RemoteException {
	   	super(epr,proxy);
	}

	public static void usage(){
		System.out.println(DmrClient.class.getName() + " -url <service url>");
	}
	
	public static void main(String [] args){
	    System.out.println("Running the Grid Service Client");
		try{
		if(!(args.length < 2)){
			if(args[0].equals("-url")){
			  DmrClient client = new DmrClient(args[1]);
			  // place client calls here if you want to use this main as a
			  // test....
			} else {
				usage();
				System.exit(1);
			}
		} else {
			usage();
			System.exit(1);
		}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

  public org.cvit.cabig.dmr.domain.Entry addEntry(org.cvit.cabig.dmr.domain.Entry newEntry,org.cvit.cabig.dmr.domain.Organization fundingOrganization) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"addEntry");
    org.cvit.cabig.dmr.stubs.AddEntryRequest params = new org.cvit.cabig.dmr.stubs.AddEntryRequest();
    org.cvit.cabig.dmr.stubs.AddEntryRequestNewEntry newEntryContainer = new org.cvit.cabig.dmr.stubs.AddEntryRequestNewEntry();
    newEntryContainer.setEntry(newEntry);
    params.setNewEntry(newEntryContainer);
    org.cvit.cabig.dmr.stubs.AddEntryRequestFundingOrganization fundingOrganizationContainer = new org.cvit.cabig.dmr.stubs.AddEntryRequestFundingOrganization();
    fundingOrganizationContainer.setOrganization(fundingOrganization);
    params.setFundingOrganization(fundingOrganizationContainer);
    org.cvit.cabig.dmr.stubs.AddEntryResponse boxedResult = portType.addEntry(params);
    return boxedResult.getEntry();
    }
  }

  public org.cvit.cabig.dmr.domain.DataClassification addDataToEntry(org.cvit.cabig.dmr.domain.DataClassification data,org.cvit.cabig.dmr.domain.Entry sourceEntry) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"addDataToEntry");
    org.cvit.cabig.dmr.stubs.AddDataToEntryRequest params = new org.cvit.cabig.dmr.stubs.AddDataToEntryRequest();
    org.cvit.cabig.dmr.stubs.AddDataToEntryRequestData dataContainer = new org.cvit.cabig.dmr.stubs.AddDataToEntryRequestData();
    dataContainer.setDataClassification(data);
    params.setData(dataContainer);
    org.cvit.cabig.dmr.stubs.AddDataToEntryRequestSourceEntry sourceEntryContainer = new org.cvit.cabig.dmr.stubs.AddDataToEntryRequestSourceEntry();
    sourceEntryContainer.setEntry(sourceEntry);
    params.setSourceEntry(sourceEntryContainer);
    org.cvit.cabig.dmr.stubs.AddDataToEntryResponse boxedResult = portType.addDataToEntry(params);
    return boxedResult.getDataClassification();
    }
  }

  public org.cvit.cabig.dmr.domain.Reference addReferenceToEntry(org.cvit.cabig.dmr.domain.Reference reference,org.cvit.cabig.dmr.domain.Entry sourceEntry) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"addReferenceToEntry");
    org.cvit.cabig.dmr.stubs.AddReferenceToEntryRequest params = new org.cvit.cabig.dmr.stubs.AddReferenceToEntryRequest();
    org.cvit.cabig.dmr.stubs.AddReferenceToEntryRequestReference referenceContainer = new org.cvit.cabig.dmr.stubs.AddReferenceToEntryRequestReference();
    referenceContainer.setReference(reference);
    params.setReference(referenceContainer);
    org.cvit.cabig.dmr.stubs.AddReferenceToEntryRequestSourceEntry sourceEntryContainer = new org.cvit.cabig.dmr.stubs.AddReferenceToEntryRequestSourceEntry();
    sourceEntryContainer.setEntry(sourceEntry);
    params.setSourceEntry(sourceEntryContainer);
    org.cvit.cabig.dmr.stubs.AddReferenceToEntryResponse boxedResult = portType.addReferenceToEntry(params);
    return boxedResult.getReference();
    }
  }

  public void updateEntry(org.cvit.cabig.dmr.domain.Entry entry) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"updateEntry");
    org.cvit.cabig.dmr.stubs.UpdateEntryRequest params = new org.cvit.cabig.dmr.stubs.UpdateEntryRequest();
    org.cvit.cabig.dmr.stubs.UpdateEntryRequestEntry entryContainer = new org.cvit.cabig.dmr.stubs.UpdateEntryRequestEntry();
    entryContainer.setEntry(entry);
    params.setEntry(entryContainer);
    org.cvit.cabig.dmr.stubs.UpdateEntryResponse boxedResult = portType.updateEntry(params);
    }
  }

  public void updateReference(org.cvit.cabig.dmr.domain.Reference reference) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"updateReference");
    org.cvit.cabig.dmr.stubs.UpdateReferenceRequest params = new org.cvit.cabig.dmr.stubs.UpdateReferenceRequest();
    org.cvit.cabig.dmr.stubs.UpdateReferenceRequestReference referenceContainer = new org.cvit.cabig.dmr.stubs.UpdateReferenceRequestReference();
    referenceContainer.setReference(reference);
    params.setReference(referenceContainer);
    org.cvit.cabig.dmr.stubs.UpdateReferenceResponse boxedResult = portType.updateReference(params);
    }
  }

  public void updateData(org.cvit.cabig.dmr.domain.DataClassification data) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"updateData");
    org.cvit.cabig.dmr.stubs.UpdateDataRequest params = new org.cvit.cabig.dmr.stubs.UpdateDataRequest();
    org.cvit.cabig.dmr.stubs.UpdateDataRequestData dataContainer = new org.cvit.cabig.dmr.stubs.UpdateDataRequestData();
    dataContainer.setDataClassification(data);
    params.setData(dataContainer);
    org.cvit.cabig.dmr.stubs.UpdateDataResponse boxedResult = portType.updateData(params);
    }
  }

  public org.oasis.wsrf.properties.GetMultipleResourcePropertiesResponse getMultipleResourceProperties(org.oasis.wsrf.properties.GetMultipleResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getMultipleResourceProperties");
    return portType.getMultipleResourceProperties(params);
    }
  }

  public org.oasis.wsrf.properties.GetResourcePropertyResponse getResourceProperty(javax.xml.namespace.QName params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"getResourceProperty");
    return portType.getResourceProperty(params);
    }
  }

  public org.oasis.wsrf.properties.QueryResourcePropertiesResponse queryResourceProperties(org.oasis.wsrf.properties.QueryResourceProperties_Element params) throws RemoteException {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"queryResourceProperties");
    return portType.queryResourceProperties(params);
    }
  }

  public gov.nih.nci.cagrid.cqlresultset.CQLQueryResults query(gov.nih.nci.cagrid.cqlquery.CQLQuery cqlQuery) throws RemoteException, gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType, gov.nih.nci.cagrid.data.faults.MalformedQueryExceptionType {
    synchronized(portTypeMutex){
      configureStubSecurity((Stub)portType,"query");
    gov.nih.nci.cagrid.data.QueryRequest params = new gov.nih.nci.cagrid.data.QueryRequest();
    gov.nih.nci.cagrid.data.QueryRequestCqlQuery cqlQueryContainer = new gov.nih.nci.cagrid.data.QueryRequestCqlQuery();
    cqlQueryContainer.setCQLQuery(cqlQuery);
    params.setCqlQuery(cqlQueryContainer);
    gov.nih.nci.cagrid.data.QueryResponse boxedResult = portType.query(params);
    return boxedResult.getCQLQueryResultCollection();
    }
  }

}
