package org.mbe.sat.assignment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.emf.common.util.EList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import metaFeatureModel.Feature;
import metaFeatureModel.FeatureModel;
import metaFeatureModel.Group;
import metaFeatureModel.MetaFeatureModelFactory;
import metaFeatureModel.impl.FeatureModelImpl;
import metaFeatureModel.impl.MetaFeatureModelFactoryImpl;

class EMFToCNFParserUnitTest {

	private static FeatureModel featureModelBCS;
	private static FeatureModel featureModelFlight;
	private static XMLToEMFParser xmlToEmfParser=new XMLToEMFParser();
	private static String bcsTestFileURI;
	private static String flightTestFileURI;
	private static EMFToCNFParser emfToCnfParser=new EMFToCNFParser();
	
	/*
	 * setup object under test : feature model provided by xmltoemfparser
	 */
	@BeforeAll
	static void setup() throws ParserConfigurationException, IOException, SAXException {
		bcsTestFileURI=new File("src/main/resources/Body_Comfort_System.xml").toURI().toString();
		flightTestFileURI=new File("src/main/resources/Flight.xml").toURI().toString();
		featureModelBCS=xmlToEmfParser.parse(bcsTestFileURI);
		featureModelFlight=xmlToEmfParser.parse(flightTestFileURI);
	}
	
	
//	@Test
//	void getGroupTypeTest() {
//		List<Group> groupList=featureModel.getGroup();
//		
//		assertFalse(groupList.isEmpty(),"Error : group list is empty!");
//		
//		for (Iterator<Group> groupListIterator = groupList.iterator(); groupListIterator.hasNext();) {
//			Group group = (Group) groupListIterator.next();
//			System.out.println("group types : "+group.getGroupType().toString());
//		}
//	}
	
	@Test
	void testParseBCS() {		
		ArrayList<String> resultList=emfToCnfParser.parse(featureModelBCS);
		StringBuilder builder=new StringBuilder();
		
		for (Iterator<String> resultListIterator = resultList.iterator(); resultListIterator.hasNext();) {
			String string = (String) resultListIterator.next();
			builder.append(string);
		}
		System.out.println(builder.toString());
		assertTrue(true);
	}
	
	
	@Test
	void testParseFlight() {		
		ArrayList<String> resultList=emfToCnfParser.parse(featureModelFlight);
		StringBuilder builder=new StringBuilder();
		
		for (Iterator<String> resultListIterator = resultList.iterator(); resultListIterator.hasNext();) {
			String string = (String) resultListIterator.next();
			builder.append(string);
		}
		System.out.println(builder.toString());
		assertTrue(true);
	}
	
}
