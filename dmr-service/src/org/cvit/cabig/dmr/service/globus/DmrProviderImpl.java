package org.cvit.cabig.dmr.service.globus;

import org.cvit.cabig.dmr.domain.DataClassification;
import org.cvit.cabig.dmr.domain.Reference;
import org.cvit.cabig.dmr.service.DmrImpl;

import java.rmi.RemoteException;

/** 
 * DO NOT EDIT:  This class is autogenerated!
 *
 * This class implements each method in the portType of the service.  Each method call represented
 * in the port type will be then mapped into the unwrapped implementation which the user provides
 * in the DmrImpl class.  This class handles the boxing and unboxing of each method call
 * so that it can be correclty mapped in the unboxed interface that the developer has designed and 
 * has implemented.  Authorization callbacks are automatically made for each method based
 * on each methods authorization requirements.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DmrProviderImpl{
	
	DmrImpl impl;
	
	public DmrProviderImpl() throws RemoteException {
		impl = new DmrImpl();
	}
	

    public org.cvit.cabig.dmr.stubs.AddEntryResponse addEntry(org.cvit.cabig.dmr.stubs.AddEntryRequest params) throws RemoteException {
    org.cvit.cabig.dmr.stubs.AddEntryResponse boxedResult = new org.cvit.cabig.dmr.stubs.AddEntryResponse();
    boxedResult.setEntry(impl.addEntry(params.getNewEntry().getEntry(),params.getFundingOrganization().getOrganization()));
    return boxedResult;
  }

    public org.cvit.cabig.dmr.stubs.AddDataToEntryResponse addDataToEntry(org.cvit.cabig.dmr.stubs.AddDataToEntryRequest params) throws RemoteException {
    org.cvit.cabig.dmr.stubs.AddDataToEntryResponse boxedResult = new org.cvit.cabig.dmr.stubs.AddDataToEntryResponse();
    DataClassification param = params.getData().getDataClassification() ;
	if (param == null) {
		param = params.getData().getAlgorithm() ;
	}
	if (param == null) {
		param = params.getData().getExperimentalData() ;
	}
	if (param == null) {
		param = params.getData().getImage() ;
	}
	if (param == null) {
		param = params.getData().getMovie() ;
	}
	if (param == null) {
		param = params.getData().getParameters() ;
	}
	if (param == null) {
		param = params.getData().getSourceCode() ;
	}
	boxedResult.setDataClassification(impl.addDataToEntry(param,params.getSourceEntry().getEntry()));
	return boxedResult;
  }

    public org.cvit.cabig.dmr.stubs.AddReferenceToEntryResponse addReferenceToEntry(org.cvit.cabig.dmr.stubs.AddReferenceToEntryRequest params) throws RemoteException {
    org.cvit.cabig.dmr.stubs.AddReferenceToEntryResponse boxedResult = new org.cvit.cabig.dmr.stubs.AddReferenceToEntryResponse();
    Reference param = params.getReference().getReference() ;
    if (param == null) {
    	param = params.getReference().getBook() ;
    }
	if (param == null) {
	    param = params.getReference().getPaper() ;	
	}
	if (param == null) {
		param = params.getReference().getReview() ;
	}
    boxedResult.setReference(impl.addReferenceToEntry(param,params.getSourceEntry().getEntry()));
    return boxedResult;
  }

    public org.cvit.cabig.dmr.stubs.UpdateEntryResponse updateEntry(org.cvit.cabig.dmr.stubs.UpdateEntryRequest params) throws RemoteException {
    org.cvit.cabig.dmr.stubs.UpdateEntryResponse boxedResult = new org.cvit.cabig.dmr.stubs.UpdateEntryResponse();
    impl.updateEntry(params.getEntry().getEntry());
    return boxedResult;
  }

    public org.cvit.cabig.dmr.stubs.UpdateReferenceResponse updateReference(org.cvit.cabig.dmr.stubs.UpdateReferenceRequest params) throws RemoteException {
    org.cvit.cabig.dmr.stubs.UpdateReferenceResponse boxedResult = new org.cvit.cabig.dmr.stubs.UpdateReferenceResponse();
    Reference param = params.getReference().getReference() ;
    if (param == null) {
    	param = params.getReference().getBook() ;
    }
	if (param == null) {
	    param = params.getReference().getPaper() ;	
	}
	if (param == null) {
		param = params.getReference().getReview() ;
	}
    impl.updateReference(param);
    return boxedResult;
  }

    public org.cvit.cabig.dmr.stubs.UpdateDataResponse updateData(org.cvit.cabig.dmr.stubs.UpdateDataRequest params) throws RemoteException {
    org.cvit.cabig.dmr.stubs.UpdateDataResponse boxedResult = new org.cvit.cabig.dmr.stubs.UpdateDataResponse();
    DataClassification param = params.getData().getDataClassification() ;
	if (param == null) {
		param = params.getData().getAlgorithm() ;
	}
	if (param == null) {
		param = params.getData().getExperimentalData() ;
	}
	if (param == null) {
		param = params.getData().getImage() ;
	}
	if (param == null) {
		param = params.getData().getMovie() ;
	}
	if (param == null) {
		param = params.getData().getParameters() ;
	}
	if (param == null) {
		param = params.getData().getSourceCode() ;
	}
	impl.updateData(param);
	return boxedResult;
  }

}
