/**
 * 
 */
package org.mbe.sat.assignment;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaFeatureModel.FeatureModel;


class EMFToCNFParserTest {

	XMLToEMFParser xmlToEMFParser = new XMLToEMFParser();
	ArrayList<FeatureModel> fMs = new ArrayList<FeatureModel>();
	
	@BeforeEach
	void setUp() throws Exception {
		 URL url = Main.class.getClassLoader().getResource("Body_Comfort_System.xml");
		 fMs.add(xmlToEMFParser.parse(url.toURI().toString()));
         
         URL url2 = Main.class.getClassLoader().getResource("FelixTestModel.xml");
         fMs.add(xmlToEMFParser.parse(url2.toURI().toString()));
         
         URL url3 = Main.class.getClassLoader().getResource("Flight.xml");
         fMs.add(xmlToEMFParser.parse(url3.toURI().toString()));
         
         URL url4 = Main.class.getClassLoader().getResource("refrigerator.xml");
         fMs.add(xmlToEMFParser.parse(url4.toURI().toString()));
         
	}

	@Test
	/*
	 * prueft ob die FeatureTrees leer sind*/
	void testFeatureTreeOutputisEmpty() {
		EMFToCNFParser emfToCNFParser = new EMFToCNFParser();
		Iterator<FeatureModel> i = fMs.iterator();
		
		while(i.hasNext()) {
			
			FeatureModel currentFM = i.next();
			ArrayList<String> featureTree = emfToCNFParser.parse(currentFM);
			Assertions.assertFalse(featureTree.isEmpty());
		}
		
			
	}
        
	@Test
	void testBodyComfortSystem() {
		EMFToCNFParser emfToCNFParser = new EMFToCNFParser();
			
		FeatureModel currentFM = fMs.get(0);
		ArrayList<String> featureTree = emfToCNFParser.parse(currentFM);
		String formel = "";
		for(String tree:featureTree) {
			
			formel += tree;
			
		}
		
		//pruefe die Alternativ-Gruppen
		Assertions.assertTrue(formel.contains("(Power_Window => (Manual_Power_Window & ~Automatic_Power_Window) | (~Manual_Power_Window & Automatic_Power_Window))"));
		//pruefe die Oder-Gruppen
		Assertions.assertTrue(formel.contains("Status_LED => LED_Alarm_System | LED_Finger_Protection | LED_Central_Locking_System"));
			
	}
	@Test
	void testFelixTestModel() {
		
		EMFToCNFParser emfToCNFParser = new EMFToCNFParser();
			
		FeatureModel currentFM = fMs.get(1);
		ArrayList<String> featureTree = emfToCNFParser.parse(currentFM);
		String formel = "";
		for(String tree:featureTree) {
				
			formel += tree;
					
		}
		//pruefe root-Feature (Hierarchie)
		Assertions.assertTrue(formel.contains("(Hardware => PC)"));
				
		//pruefe mandatory-Feature
		Assertions.assertTrue(formel.contains("(CPU => Cooler)"));
							
	}
	@Test
	void testFlight() {
		
		EMFToCNFParser emfToCNFParser = new EMFToCNFParser();
		
		FeatureModel currentFM = fMs.get(2);
		ArrayList<String> featureTree = emfToCNFParser.parse(currentFM);
		String formel = "";
		for(String tree:featureTree) {
			
			formel += tree;
			
		}
		//pruefe optional-Feature (Hierarchie)
		Assertions.assertTrue(formel.contains("(Hinflug => Inlandsflug)"));
			
		//pruefe mandatory-Feature
		Assertions.assertTrue(formel.contains("(Hinflug => Hinflug - Start)"));
			
		//pruefe groessere Alternativ-Gruppe
		Assertions.assertTrue(formel.contains("(Bezahlungsform => (PayPal & ~Kreditkarte & ~Bargeld) | (~PayPal & Kreditkarte & ~Bargeld) | (~PayPal & ~Kreditkarte & Bargeld))"));
					
	}
	
	@Test
	void testRefrigerator() {
		
		EMFToCNFParser emfToCNFParser = new EMFToCNFParser();
		
		FeatureModel currentFM = fMs.get(3);
		ArrayList<String> featureTree = emfToCNFParser.parse(currentFM);
		String formel = "";
		for(String tree:featureTree) {
			
			formel += tree;
			
		}
		//pruefe ob optional-Feature nicht mandatory 
		Assertions.assertFalse(formel.contains("(Refrigerator => Water_Dispender)"));
			
		//pruefe mandatory-Feature
		Assertions.assertTrue(formel.contains("(Refrigerator => Control)"));
			
		//pruefe Oder-Gruppe
		Assertions.assertTrue(formel.contains("Control => Buttons | Touch | Voice"));
			
		//pruefe ob Alternative-Gruppe nicht als Oder-Gruppe
		Assertions.assertFalse(formel.contains("(Screen => (LCD | LED))"));
			
	}
  
}

