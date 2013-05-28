/*L
 * Copyright The General Hospital Corporation d/b/a Massachusetts General Hospital
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/digital-model-repository/LICENSE.txt for details.
 */


package org.cvit.cabig.dmr.integration.queryprocessor;

import static org.junit.Assert.* ;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.examples.RecursiveElementNameAndTextQualifier;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import com.infotechsoft.cagrid.cql.SerializationException;
import com.infotechsoft.cagrid.cql.Utils;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.common.DataServiceI;

public abstract class CvitDataServiceTests {
    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;
    
    protected abstract DataServiceI getClient() ;
    
    protected void validateQuery(InputStream query, InputStream expectedResults) {
	String actualResults = executeQuery(query) ;
	XMLUnit.setIgnoreWhitespace(true) ;
	Diff resultsDiff ;
	try {
	    resultsDiff = new Diff(asString(expectedResults), actualResults) ;
	} catch (Exception e) {
	    throw new RuntimeException("Exception comparing results.", e) ;
	}
	resultsDiff.overrideElementQualifier(new RecursiveElementNameAndTextQualifier()) ;
	assertTrue(resultsDiff.toString(), resultsDiff.similar()) ;
    }
    
    protected void validateQueryUsingIdAttribute(InputStream query, InputStream expectedResults) {
	String actualResults = executeQuery(query) ;
	String xpath = "//ns1:Attribute[@name='id']/@value" ;
	Map<String, String> ns = new HashMap<String, String>() ;
	ns.put("ns1", "http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLResultSet") ;
	NamespaceContext ctx = new SimpleNamespaceContext(ns) ;
	XpathEngine engine = XMLUnit.newXpathEngine() ;
	engine.setNamespaceContext(ctx) ;
	try { 
	    NodeList controlList = engine.getMatchingNodes(xpath, XMLUnit.buildControlDocument(asString(expectedResults))) ;
	    Set<String> controlIds = new HashSet<String>() ;
	    for (int i = 0; i < controlList.getLength(); i++) {
		controlIds.add(controlList.item(i).getNodeValue()) ;
	    }
	    
	    NodeList testList = engine.getMatchingNodes(xpath, XMLUnit.buildControlDocument(actualResults)) ;
	    Set<String> testIds = new HashSet<String>() ;
	    for (int i = 0; i < testList.getLength(); i++) {
		testIds.add(testList.item(i).getNodeValue()) ;
	    }

	    assertTrue(controlIds.containsAll(testIds)) ;
	    assertTrue(testIds.containsAll(controlIds)) ;
	} catch (Exception e) {
	    throw new RuntimeException("Exception while comparing ID attributes.", e) ;
	}
    }
    
    private String executeQuery(InputStream query) {
	CQLQuery cql = parseQuery(query) ;
	CQLQueryResults cqlResults ;
	try {
	    cqlResults = getClient().query(cql) ;
	} catch (Exception e) {
	    throw new RuntimeException("Exception executing query.", e) ;
	}
	String actualResults ;
	try {
	    actualResults = Utils.cqlResults2String(cqlResults, getWsddStream()) ;
	} catch (SerializationException e) {
	    throw new RuntimeException("Exception serializing results.", e) ;
	}
	logger.debug("Actual Results: {}", actualResults) ;
	return actualResults ;
    }
    
    protected InputStream getWsddStream() {
	return CvitDataServiceTests.class.getResourceAsStream("server-config.wsdd") ;
    }
    
    protected CQLQuery parseQuery(InputStream query) {
	Reader reader = new InputStreamReader(query) ;
	try {
	    return (CQLQuery) gov.nih.nci.cagrid.common.Utils.deserializeObject(reader, CQLQuery.class) ;
	} catch (Exception e) {
	    throw new RuntimeException("Exception while parsing query.", e) ;
	} finally {
	    close(reader) ;
	}
	
    }
    
    private String asString(InputStream stream) {
	StringBuilder result = new StringBuilder() ;
	Reader reader = new InputStreamReader(stream) ;
	try {
	    char[] buff = new char[8192] ;
	    int read = 1 ;
	    while ((read = reader.read(buff)) > 0) {
		result.append(buff, 0, read) ;
	    }
	} catch (IOException e) {
	    throw new RuntimeException("Exception while converting stream to String.", e) ;
	} finally {
	    close(reader) ;
	}
	return result.toString() ;
    }
    
    protected void close(Closeable closeable) {
	try { closeable.close() ; } catch (Exception e) { }
    }
}
