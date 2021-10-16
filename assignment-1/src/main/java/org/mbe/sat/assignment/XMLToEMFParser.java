package org.mbe.sat.assignment;

import metaFeatureModel.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Parses XML files of FeatureIDE models into an instance of the corresponding meta model.
 */
public class XMLToEMFParser {

    private static final Group ALT_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private static final Group AND_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();
    private static final Group OR_GROUP_IDENTIFIER = MetaFeatureModelFactory.eINSTANCE.createGroup();

    public XMLToEMFParser() {
        // Example of how to instantiate elements of the Meta model
        // Initializing Groups and an empty FeatureModel from src-gen[main]
//        final Group altGroup;
        // MetaFeatureModelFactory.eINSTANCE.createGroup() creates Groups from FeatureModel
        ALT_GROUP_IDENTIFIER.setGroupType(GroupType.ALT);
        AND_GROUP_IDENTIFIER.setGroupType(GroupType.AND);
        OR_GROUP_IDENTIFIER.setGroupType(GroupType.OR);
    }

    /**
     * TODO: Parse the XML generated from the featureModel (without CrossTree) into featureModel
     * Note: Every Feature of the FeatureModel should get a number, starting by 1 e.g. feature.setNumber(1)
     */
    public FeatureModel parse(String uri) throws ParserConfigurationException, IOException, SAXException {

        FeatureModel featureModel = MetaFeatureModelFactory.eINSTANCE.createFeatureModel();

        // Parse XML document into  meta model
        // Initializing an Document for parsing the XML with Java Dom Parser
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document xmlDocument = builder.parse(uri);

        // TODO: Solution goes here
        // Use MetaFeatureModelFactory.eInstance... to create Features etc.


        return featureModel;

    }

}