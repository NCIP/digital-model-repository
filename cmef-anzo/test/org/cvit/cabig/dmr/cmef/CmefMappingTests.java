package org.cvit.cabig.dmr.cmef;

import java.util.Set ;

import org.cvit.cabig.dmr.cmef.domain.ComputationalModel ;
import org.junit.Before ;
import org.junit.Test ;

import com.infotechsoft.rdf.Statement ;
import com.infotechsoft.rdf.basic.BasicRdfFactory ;
import com.infotechsoft.rdf.mapping.MappingException ;
import com.infotechsoft.rdf.mapping.ObjectConverter ;
import com.infotechsoft.rdf.mapping.RdfMapper ;


public class CmefMappingTests {
    private RdfMapper mapper ;
    
    @Before
    public void init() {
	mapper = new ObjectConverter(CmefMappings.getMappers(), new BasicRdfFactory()) ;
    }
    
    @Test
    public void canConvertComputationalModel() throws MappingException {
	ComputationalModel model = new ComputationalModel() ;
	model.setId("urn:lsid:org.cvit.dmr.cmef:1") ;
	model.setTitle("My Model") ;
	model.setDescription("This is my model.") ;
	model.setComment("a test model..") ;
	model.setCommandLine("model.exe <param1>") ;
	model.setVersion("1") ;
	
	Set<Statement> stmts = mapper.toRdf(model) ;
	System.out.println(stmts) ;
    }
}
