package org.mbe.sat.assignment;

import metaFeatureModel.FeatureModel;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {

    public static void main(String[]args){

        /**
         * Load your XML-FeatureModel File into an Instance of an EMF-Metamodel
         * Your XML-File should be part of the resources folder
         */

        try {
            URL url = Main.class.getClassLoader().getResource("refrigerator.xml");
            XMLToEMFParser xmlToEMFParser = new XMLToEMFParser();
            FeatureModel featureModel = xmlToEMFParser.parse(url.toURI().toString());

            EMFToCNFParser emfToCNFParser = new EMFToCNFParser();
            String cnfTree = emfToCNFParser.parse(featureModel);

            CrossTreeParser crossTreeParser = new CrossTreeParser(url.toURI().toString(), featureModel);

            CreateDimacs createDimacs = new CreateDimacs(featureModel.getRoot(), cnfTree, crossTreeParser.getCnfCrossTree(), url.toURI().toString());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
