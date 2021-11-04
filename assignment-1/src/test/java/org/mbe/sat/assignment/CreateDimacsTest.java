/**
 * 
 */
package org.mbe.sat.assignment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.logicng.io.parsers.ParserException;

/**
 * @author Darwin Brambor
 *
 */
/**
 * @author Momo Johnson
 *
 */

class CreateDimacsTest {
//	private static final String bcsDiagramPath=new File("src/main/resources/Body_Comfort_System.xml").toURI().toString();
//	private static final String fridgeDiagramPath=new File("src/main/resources/refrigerator.xml").toURI().toString();
//	
//	private static FeatureModel fridgeFeatureModel;
//	private static FeatureModel bcsFeatureModel;
//	private static XMLToEMFParser xmlToEmfParser;
//	
	// for unit testing
	/**
	 * object under test
	 */
	private static CreateDimacs createDimacs;
	/**
	 * input parameter for the CreateDimacs constructor
	 */
	private static String fileName;
	/**
	 * input parameter for the CreateDimacs constructor
	 */
	private static String constraintFormula;
	/**
	 * input parameter for the CreateDimacs constructor ; needs to be parsed in the
	 * following ; input for extractFeatureTree-method
	 */
	private static String rawFeatureTree;
	/**
	 * input parameter for the CreateDimacs constructor ; output of the
	 * extractFeatureTree-method
	 */
	private static ArrayList<String> featureTree;

	// for output testing

	/**
	 * should-worth output of the writeDimacs-method
	 */
	private static final File patternBCS = new File("src/main/resources/pattern_bcs.txt");
	/**
	 * should-worth output of the writeDimacs-method
	 */
	private static final File patternFridge = new File("src/main/resources/pattern_fridge.txt");
	/**
	 * mapping between the literal identifiers and the literal names for the
	 * Body-Comfort-System example
	 */
	private static final File bcsMapFile = new File("src/main/resources/Body_Comfort_System.map");
	/**
	 * stores the final dimacs format for the Body-Comfort-System example
	 */
	private static final File bcsCnfFile = new File("src/main/resources/Body_Comfort_System.cnf");
	/**
	 * mapping between the literal identifiers and the literal names for the
	 * Refrigerator example
	 */
	private static final File fridgeMapFile = new File("src/main/resources/refrigerator.map");
	/**
	 * stores the final dimacs format for the Refrigerator example
	 */
	private static final File fridgeCnfFile = new File("src/main/resources/refrigerator.cnf");

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {

		// unit testing setup check
		fileName = "refrigerator";

		constraintFormula = "(~Temperature_Regulation_Control | Buttons) & (~Touch | Screen)";

		rawFeatureTree = "(, Refrigerator, ),  & , (, Screen,  => , (, LCD,  & , ~, LED, ),  | , (,"
				+ " ~, LCD,  & , LED, ), ),  & , (, Control,  => , Buttons,  | , Touch,  | , Voice, ),  & , (, Screen,  => , "
				+ "Refrigerator, ),  & , (, Temperature_Regulation_Control,  => , Refrigerator, ),  & , (, Control,  => , Refrigerator, ),  "
				+ "& , (, Water_Dispender,  => , Refrigerator, ),  & , (, Ice_Dispender,  => , Refrigerator, ),  & , (, LCD,  => , Screen, ),  "
				+ "& , (, LED,  => , Screen, ),  & , (, Buttons,  => , Control, ),  & , (, Touch,  => , Control, ),  & , (, Voice,  => , Control, ),  "
				+ "& , (, Refrigerator,  => , Control, )";

		featureTree = extractFeatureTree(rawFeatureTree);
		assertFalse(featureTree.isEmpty());
		assertFalse(featureTree.contains("["));
		assertFalse(featureTree.contains("]"));
		assertFalse(featureTree.contains(","));

		// output testing setup check

		File[] files = { patternBCS, patternFridge, bcsMapFile, bcsCnfFile, fridgeMapFile, fridgeCnfFile };

		for (int i = 0; i < files.length; i++) {
			assertTrue(files[i].exists());
		}

	}

	/**
	 * Test method for
	 * {@link org.mbe.sat.assignment.CreateDimacs#CreateDimacs(java.lang.String, java.lang.String, java.util.ArrayList)}
	 * / the constructor of the the CreateDimacs class. Goal of the test is to
	 * figure out which configuration of input parameters of the constructor leads
	 * to thrown exeptions
	 * 
	 */
	@Test
	void testCreateDimacs() {
		assertDoesNotThrow(() -> new CreateDimacs(null, constraintFormula, featureTree));
		assertDoesNotThrow(() -> new CreateDimacs(fileName, null, featureTree));
		assertThrows(NullPointerException.class, () -> new CreateDimacs(fileName, constraintFormula, null));
		assertThrows(NullPointerException.class, () -> new CreateDimacs(null, constraintFormula, null));
		assertThrows(NullPointerException.class, () -> new CreateDimacs(fileName, null, null));
		assertDoesNotThrow(() -> new CreateDimacs(null, null, featureTree));
		assertThrows(NullPointerException.class, () -> new CreateDimacs(null, constraintFormula, null));
		assertThrows(NullPointerException.class, () -> new CreateDimacs(null, null, null));
		assertDoesNotThrow(() -> new CreateDimacs(fileName, constraintFormula, featureTree));
	}

	/**
	 * Test method for {@link org.mbe.sat.assignment.CreateDimacs#WriteDimacs()}.
	 * Goal of the test is to run several configurations of input parameters of the
	 * constructor which do not throw any exceptions during the initialization but
	 * may throw exception during the file access of the writeDimacs method. The
	 * test is separated into a setup -> test -> output-check - structure.
	 * 
	 * @throws ParserException
	 */
	@Test
	void testWriteDimacs() throws ParserException {

		//setup
		assertDoesNotThrow(() -> new CreateDimacs(fileName, constraintFormula, featureTree));
		createDimacs = new CreateDimacs(fileName, constraintFormula, featureTree);

		//test
		assertDoesNotThrow(() -> createDimacs.WriteDimacs());

		//output-check
		assertTrue(patternComparison(bcsCnfFile, patternBCS));

		//========================================================================
		
		//setup
		assertDoesNotThrow(() -> new CreateDimacs(null, constraintFormula, featureTree));
		createDimacs = new CreateDimacs(null, constraintFormula, featureTree);

		//test
		assertDoesNotThrow(() -> createDimacs.WriteDimacs());
		
		//output-check
		assertTrue(patternComparison(bcsCnfFile, patternBCS));
		
		//========================================================================

		//setup
		assertDoesNotThrow(() -> assertDoesNotThrow(() -> new CreateDimacs(fileName, null, featureTree)));
		createDimacs = assertDoesNotThrow(() -> new CreateDimacs(fileName, null, featureTree));

		//test
		assertDoesNotThrow(() -> createDimacs.WriteDimacs());
		
		//output-check
		assertTrue(patternComparison(bcsCnfFile, patternBCS));
		
		//========================================================================
		
		//setup
		assertDoesNotThrow(() -> assertDoesNotThrow(() -> new CreateDimacs(null, null, featureTree)));
		createDimacs = assertDoesNotThrow(() -> new CreateDimacs(null, null, featureTree));

		//test
		assertDoesNotThrow(() -> createDimacs.WriteDimacs());
		
		//output-check
		assertTrue(patternComparison(bcsCnfFile, patternBCS));
		
		//========================================================================
		
	}

	/**
	 * method for comparing the content of two files considering equality
	 * (respectively different file format allowed), in this case the comparison of
	 * the output cnf-file produced by the writeDimacs-method and a pattern file as
	 * should-worth; requires the getFileLines-method
	 * 
	 * @param outputFile
	 * @param patternFile
	 * @return boolean (true if file contents are equal)
	 */
	private static boolean patternComparison(File outputFile, File patternFile) {
		if (outputFile == null || patternFile == null) {
			return false;
		}

		if (!outputFile.exists() || !patternFile.exists()) {
			return false;
		}

		try {
			ArrayList<String> outputList = getFileLines(outputFile);
			ArrayList<String> patternList = getFileLines(patternFile);

			if (outputList.containsAll(patternList) && patternList.containsAll(outputList)) {
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * method for extracting all non-empty lines from a file with a given filename
	 * 
	 * @param filename
	 * @return ArrayList of Strings / includes all non-empty lines of a
	 *         .txt/.cnf/.map file (in this case)
	 * @throws IOException
	 */
	private static ArrayList<String> getFileLines(String filename) throws IOException {
		ArrayList<String> lines = (ArrayList<String>) Files.lines(new File(filename).toPath()).map(str -> str.trim())
				.filter(str -> !str.isEmpty()).collect(Collectors.toList());
		return lines;
	}

	/**
	 * method for extracting all non-empty lines from a given file
	 * 
	 * @param file / .txt/.cnf/.map file (in this case) that should be read
	 * @return ArrayList of Strings / includes all non-empty lines of a
	 *         .txt/.cnf/.map file (in this case)
	 * @throws IOException
	 */
	private static ArrayList<String> getFileLines(File file) throws IOException {
		ArrayList<String> lines = (ArrayList<String>) Files.lines(file.toPath()).map(str -> str.trim())
				.filter(str -> !str.isEmpty()).collect(Collectors.toList());
		return lines;
	}

	/**
	 * method for converting a given comma-separated CNF-formula in form of a string
	 * into an ArrayList of type string including single elements of the CNF as
	 * elements (example : "=>" "|" "&")
	 * 
	 * @param rawFeatureTree
	 * @return ArrayList of Strings / includes single elements of the CNF as
	 *         elements (example : "=>" "|" "&")
	 */
	private static ArrayList<String> extractFeatureTree(String rawFeatureTree) {
		ArrayList<String> finalFeatureTree = new ArrayList<>();
		String[] rawFeatureArray = rawFeatureTree.split(",");
		finalFeatureTree.addAll(Arrays.asList(rawFeatureArray));
		return finalFeatureTree;
	}
}
