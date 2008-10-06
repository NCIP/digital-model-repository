<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery">
 <ns1:Target name="org.cvit.cabig.dmr.domain.Paper">
  <ns1:Association name="org.cvit.cabig.dmr.domain.Entry" roleName="entries">
   <ns1:Group logicRelation="AND">
    <ns1:Association name="org.cvit.cabig.dmr.domain.Image" roleName="data">
     <ns1:Attribute name="title" predicate="LIKE" value="%"/>
    </ns1:Association>
    <ns1:Attribute name="id" predicate="LIKE" value="%"/>
   </ns1:Group>
  </ns1:Association>
 </ns1:Target>
</ns1:CQLQuery>
