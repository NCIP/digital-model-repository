package org.cvit.cabig.dmr.common;

import javax.xml.namespace.QName;


public interface DmrConstants {
	public static final String SERVICE_NS = "http://dmr.cabig.cvit.org/Dmr";
	public static final QName RESOURCE_KEY = new QName(SERVICE_NS, "DmrKey");
	public static final QName RESOURCE_PROPERTY_SET = new QName(SERVICE_NS, "DmrResourceProperties");

	//Service level metadata (exposed as resouce properties)
	public static final QName DOMAINMODEL = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.dataservice", "DomainModel");
	public static final QName SERVICEMETADATA = new QName("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata", "ServiceMetadata");
	
}
