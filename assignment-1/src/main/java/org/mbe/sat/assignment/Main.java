package org.mbe.sat.assignment;

import metaFeatureModel.FeatureModel;

import org.logicng.io.parsers.ParserException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class Main {

    public static void main(String[]args){

        /**
         * Load your XML-FeatureModel File into an Instance of an EMF-Metamodel
         * Your XML-File should be part of the resources folder
         */

        try {
            URL url = Main.class.getClassLoader().getResource("Body_Comfort_System.xml");

            XMLToEMFParser xmlToEMFParser = new XMLToEMFParser();
            FeatureModel featureModel = xmlToEMFParser.parse(url.toURI().toString());

            EMFToCNFParser emfToCNFParser = new EMFToCNFParser();
            ArrayList<String> featureTree = emfToCNFParser.parse(featureModel);

            /**
             * Already implemented!!!
             * crossTreeParser.getCnfCrossTree(); return an ArrayList<String> with the CNFCrossTree
             **/
            CrossTreeParser crossTreeParser = new CrossTreeParser(url.toURI().toString(), featureModel);

            //TODO: Create the class 'createDimacs' with should create a Dimacs File in the resources folder from the crossTreeCNF and the normal CNFTree
                  
                try {
                	CreateDimacs dimacscreator = new CreateDimacs("src/main/resources/test123",crossTreeParser.getCnfCrossTree());
             		dimacscreator.WriteDimacs();
             		} catch (ParserException e) {
            			// TODO Auto-generated catch block
             		}
            

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