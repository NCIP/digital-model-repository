<ns1:CQLQuery xmlns:ns1="http://CQL.caBIG/1/gov.nih.nci.cagrid.CQLQuery">
 <ns1:Target name="org.cvit.cabig.dmr.domain.Movie">
  <ns1:Association name="org.cvit.cabig.dmr.domain.Entry" roleName="entry">
   <ns1:Group logicRelation="AND">
    <ns1:Association name="org.cvit.cabig.dmr.domain.Organization" roleName="fundingOrganization">
     <ns1:Group logicRelation="AND">
      <ns1:Association name="org.cvit.cabig.dmr.domain.Person" roleName="members">
       <ns1:Group logicRelation="AND">
        <ns1:Association name="org.cvit.cabig.dmr.domain.Entry" roleName="entries">
         <ns1:Group logicRelation="AND">
          <ns1:Association name="org.cvit.cabig.dmr.domain.Book" roleName="references">
           <ns1:Attribute name="title" predicate="LIKE" value="%"/>
          </ns1:Association>
          <ns1:Attribute name="id" predicate="LIKE" value="%"/>
         </ns1:Group>
        </ns1:Association>
        <ns1:Attribute name="firstName" predicate="LIKE" value="%"/>
       </ns1:Group>
      </ns1:Association>
      <ns1:Attribute name="name" predicate="LIKE" value="%"/>
     </ns1:Group>
    </ns1:Association>
    <ns1:Attribute name="title" predicate="LIKE" value="%"/>
   </ns1:Group>
  </ns1:Association>
 </ns1:Target>
</ns1:CQLQuery>
