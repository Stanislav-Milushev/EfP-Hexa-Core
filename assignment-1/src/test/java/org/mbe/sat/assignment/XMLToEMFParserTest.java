package org.mbe.sat.assignment;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaFeatureModel.Feature;
import metaFeatureModel.FeatureModel;
import metaFeatureModel.Group;
import metaFeatureModel.GroupType;
import metaFeatureModel.MetaFeatureModelFactory;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import metaFeatureModel.FeatureModel;

class XMLToEMFParserTest {

	XMLToEMFParser xmlToEMFParser = new XMLToEMFParser();
	ArrayList<FeatureModel> fMs = new ArrayList<FeatureModel>();
	ArrayList<FeatureModel> validFms = new ArrayList<FeatureModel>();

	@BeforeEach
	void setUp() throws Exception {
		// Test data generieren
		URL url4 = Main.class.getClassLoader().getResource("refrigerator.xml");
		fMs.add(xmlToEMFParser.parse(url4.toURI().toString()));

		URL url3 = Main.class.getClassLoader().getResource("Flight.xml");
		fMs.add(xmlToEMFParser.parse(url3.toURI().toString()));

		URL url2 = Main.class.getClassLoader().getResource("FelixTestModel.xml");
		fMs.add(xmlToEMFParser.parse(url2.toURI().toString()));

		URL url = Main.class.getClassLoader().getResource("Body_Comfort_System.xml");
		fMs.add(xmlToEMFParser.parse(url.toURI().toString()));

		// Body_Comfort_System FeatureModel

		// Feature LEDAlarmSystem = this.genFeature("LED_Alarm_System", null)

		// Refrigerator FeatureModel

		// Build Screen Node
		Feature LCDFeature = this.genFeature("LCD", GroupType.ALT);
		Feature LEDFeature = this.genFeature("LED", GroupType.ALT);
		Feature ScreenFeature = this.genFeature("Screen", GroupType.AND);

		ScreenFeature.getChildren().add(LCDFeature);
		ScreenFeature.getChildren().add(LEDFeature);

		// Build Temperature_Regulation_Control Node
		Feature TemperatureRegulationControlFeature = this.genFeature("Temperature_Regulation_Control", GroupType.AND);

		// Build Control Node
		Feature ButtonFeature = this.genFeature("Buttons", GroupType.OR);
		Feature TouchFeature = this.genFeature("Touch", GroupType.OR);
		Feature VoiceFeature = this.genFeature("Voice", GroupType.OR);
		Feature ControlFeature = this.genFeature("Control", GroupType.AND, true);

		ControlFeature.getChildren().add(ButtonFeature);
		ControlFeature.getChildren().add(TouchFeature);
		ControlFeature.getChildren().add(VoiceFeature);

		// Build Water_Dispender Node
		Feature WaterDispenderFeature = this.genFeature("Water_Dispender", GroupType.AND);

		// Build Ice_Dispender Node
		Feature IceDispenderFeature = this.genFeature("Ice_Dispender", GroupType.AND);

		Feature Root = MetaFeatureModelFactory.eINSTANCE.createFeature();
		Root.setGroup(null);
		Root.setName("Refrigerator");

		Root.getChildren().add(ScreenFeature);
		Root.getChildren().add(TemperatureRegulationControlFeature);
		Root.getChildren().add(ControlFeature);
		Root.getChildren().add(WaterDispenderFeature);
		Root.getChildren().add(IceDispenderFeature);

		FeatureModel refrigeratorFeatureModel = MetaFeatureModelFactory.eINSTANCE.createFeatureModel();
		refrigeratorFeatureModel.setRoot(Root);

		validFms.add(refrigeratorFeatureModel);

	}

	@Test
	void groupCountTest() {
		// Test auf anzahl der gruppen
		FeatureModel bcmf = fMs.get(0);
		assertEquals(3, bcmf.getGroup().size());
	}

	@Test
	void FeatureCountTest() {
		// test auf anzahl der Features

		FeatureModel bodyComfortSystemFeatureModel = fMs.get(0);
		assertEquals(10, childrensizeR(bodyComfortSystemFeatureModel.getRoot(),
				bodyComfortSystemFeatureModel.getRoot().getChildren().size()));

	}

	@Test
	void childrenNameTest() {

		FeatureModel testfeature = fMs.get(0);
		FeatureModel validfeature = validFms.get(0);
		assertEquals(testfeature.getRoot().getName(), validfeature.getRoot().getName());
		// testfeaturenames input ungleich null
		childrenNameTestNullR(testfeature.getRoot(), validfeature.getRoot(), 5);
		// nametest vergleich mit dummy data
		childrenNameTestR(testfeature.getRoot(), validfeature.getRoot(), 5);

	}

	@Test
	void childrenGTypeTest() {
		// Typtest vergleich mit dummy data
		FeatureModel testfeature = fMs.get(0);
		FeatureModel validfeature = validFms.get(0);
		assertEquals(testfeature.getRoot().getName(), validfeature.getRoot().getName());
		childrenGTypeTestR(testfeature.getRoot(), validfeature.getRoot(), 5);

	}

	@Test
	void childrenManTest() {
		// Mandatorytest vergleich mit dummy data
		FeatureModel testfeature = fMs.get(0);
		FeatureModel validfeature = validFms.get(0);
		assertEquals(testfeature.getRoot().getName(), validfeature.getRoot().getName());
		childrenManTestR(testfeature.getRoot(), validfeature.getRoot(), 5);
	}


	private void childrenNameTestNullR(Feature testfeature, Feature validfeature2, int x) {

		int anz = 0;
		anz = anz + x;

		for (int i = 0; i < x; i++) {
			childrenNameTestR(testfeature.getChildren().get(i), validfeature2.getChildren().get(i),
					validfeature2.getChildren().get(i).getChildren().size());
			assertNotNull(testfeature.getChildren().get(i).getName());
			;
		}

	}

	private static int childrensizeR(Feature feature, int x) {
		int anz = 0;
		anz = anz + x;

		for (int i = 0; i < x; i++) {
			anz = anz + childrensizeR(feature.getChildren().get(i), feature.getChildren().get(i).getChildren().size());
		}

		return anz;
	}

	private void childrenNameTestR(Feature testfeature, Feature validfeature2, int x) {

		int anz = 0;
		anz = anz + x;

		for (int i = 0; i < x; i++) {
			childrenNameTestR(testfeature.getChildren().get(i), validfeature2.getChildren().get(i),
					validfeature2.getChildren().get(i).getChildren().size());
			assertEquals(testfeature.getChildren().get(i).getName(), validfeature2.getChildren().get(i).getName());
		}

	}

	private void childrenGTypeTestR(Feature testfeature, Feature validfeature2, int x) {

		int anz = 0;
		anz = anz + x;

		for (int i = 0; i < x; i++) {
			childrenNameTestR(testfeature.getChildren().get(i), validfeature2.getChildren().get(i),
					validfeature2.getChildren().get(i).getChildren().size());
			assertEquals(testfeature.getChildren().get(i).getGroup().getGroupType(),
					validfeature2.getChildren().get(i).getGroup().getGroupType());
		}

	}

	private void childrenManTestR(Feature testfeature, Feature validfeature2, int x) {

		int anz = 0;
		anz = anz + x;
		for (int i = 0; i < x; i++) {
			childrenNameTestR(testfeature.getChildren().get(i), validfeature2.getChildren().get(i),
					validfeature2.getChildren().get(i).getChildren().size());
			assertEquals(testfeature.getChildren().get(i).isMandatory(),
					validfeature2.getChildren().get(i).isMandatory());
		}

	}

	Feature genFeature(String featureName, GroupType groupType) {
		return this.genFeature(featureName, groupType, false);
	}

	Feature genFeature(String featureName, GroupType groupType, boolean mandatory) {
		Feature feature = MetaFeatureModelFactory.eINSTANCE.createFeature();
		feature.setName(featureName);
		feature.setMandatory(mandatory);
		Group group = MetaFeatureModelFactory.eINSTANCE.createGroup();
		group.setGroupType(groupType);

		FeatureModel featureModel = MetaFeatureModelFactory.eINSTANCE.createFeatureModel();
		featureModel.getGroup().add(group);
		group.getFeature().add(feature);

		return feature;
	}

}
