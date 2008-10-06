<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery">
 <ns1:Target name="org.cvit.cabig.dmr.domain.Organization">
  <ns1:Association name="org.cvit.cabig.dmr.domain.Person" roleName="members">
   <ns1:Attribute name="firstName" predicate="LIKE" value="%"/>
  </ns1:Association>
 </ns1:Target>
</ns1:CQLQuery>
