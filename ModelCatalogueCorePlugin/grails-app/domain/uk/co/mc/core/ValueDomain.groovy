package uk.co.mc.core

import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/*
* subjects, isbn, rating
*
* ValueDomain subjects with EnumeratedType enumerations['history', 'science', 'politics'],
* ValueDomain isbn with regex "^\d{9}[\d|X]$" and DataType String,
* ValueDomain rating with regex "\b[0-5]\b" and DataType Integer,
* ValueDomain content with regex "^[:;,\-@0-9a-zA-Zâéè'.\s]{1,2000000}$"  and DataType String,
*
*  *  * <xs:complexType name="PatientModelA">
<xs:sequence>
<xs:element name="name" type="xs:string"/>
<xs:element name="treatment" type="treatment"/>
</xs:sequence>
 </xs:complexType>


 *
 *
 *
 * !!!!!!!!VALUE DOMAINS Need to be related to at least one conceptual domain.....we need to build this into the
 * constraints
 *
 *
*/

class ValueDomain extends CatalogueElement  {

	MeasurementUnit unitOfMeasure
	String regexDef
	DataType dataType
	
    static constraints = {
		description nullable:true, maxSize: 2000
		unitOfMeasure nullable:true, maxSize: 255
		regexDef nullable:true, maxSize: 500, validator: { val,obj ->
            if(!val){return true}
            try{
                Pattern.compile(val)
            }catch(PatternSyntaxException e){
                return ['wontCompile', e.message]
            }
            return true
        }
    }


    static transients = ['includedIn']


    List/*<DataElement>*/ getIncludedIn() {
        getIncomingRelationsByType(RelationshipType.inclusionType)
    }

    Relationship addToIncludedIn(ConceptualDomain conceptualDomain) {
        createLinkFrom(conceptualDomain, RelationshipType.inclusionType)
    }

    void removeFromIncludedIn(ConceptualDomain conceptualDomain) {
        removeLinkFrom(conceptualDomain, RelationshipType.inclusionType)
    }


}
