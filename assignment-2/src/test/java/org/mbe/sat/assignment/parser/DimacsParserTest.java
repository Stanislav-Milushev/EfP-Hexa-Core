/**
 * 
 */
package org.mbe.sat.assignment.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Or;
import org.mbe.sat.core.model.formula.Variable;
import org.mbe.sat.core.problem.SatProblemFixtures;

/**
 * @author Stan
 *
 */
class DimacsParserTest {

	/**
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

//	@Test
//	void test() throws IOException {
////		DimacsParser dp = new DimacsParser();
////		File cnffile = new File(
////				"src-framework/testFixtures/resources/org/mbe/sat/core/problem/" + "aim-50-1_6-yes1-4.cnf");
////		CnfFormula formula = dp.parse(cnffile, StandardCharsets.UTF_8);
//
//	}

	@Test
	void emptyCnfInputTest() throws IOException {
		DimacsParser dp = new DimacsParser();
		File cnfFile = null;
		// File cnfFile = new
		// File("src-framework/testFixtures/resources/org/mbe/sat/core/problem/" +
		// "aim-50-1_6-yes1-4.cnf");
		// CnfFormula formula = dp.parse(cnfFile, StandardCharsets.UTF_8);
		assertThrows(NullPointerException.class, () -> dp.parse(cnfFile, StandardCharsets.UTF_8));
	}
	
	@Test
	void invalidFileTest() throws IOException {
		DimacsParser dp = new DimacsParser();
		File cnfFile = new File("");
		boolean fileExists=cnfFile.exists();
		assertThrows(IOException.class, () -> dp.parse(cnfFile, StandardCharsets.UTF_8));
	}

	@ParameterizedTest
	@MethodSource("dimacsFormulaMappingTestAllProvider")
	void dimacsFormulaMappingTest(String filename) throws IOException {
		DimacsParser dp = new DimacsParser();

		//		File cnfFile = new File(
//				"src-framework/testFixtures/resources/org/mbe/sat/core/problem/" + "aim-50-1_6-yes1-4.cnf");
		
		File cnfFile=new File(("src-framework/testFixtures/resources/org/mbe/sat/core/problem/"+filename));

		//		boolean exists=cnfFile.exists();
		
		if(cnfFile.getName().contains("tutorial")) {
			return;
		}
		
		CnfFormula formula = dp.parse(cnfFile, StandardCharsets.UTF_8);
		assertTrue(mapDimacsFormula(cnfFile, formula));
	}
	
	private static Stream<String> dimacsFormulaMappingTestAllProvider(){
		return SatProblemFixtures.getAll().stream().map(model->model.getFileName());
	}
	
	
	private static boolean mapDimacsFormula(File cnfFile, CnfFormula formula) throws IOException {
		boolean result = true;

		Set<Or<Atom>> clauses = formula.getClauses();
		List<String> extracted = extract(cnfFile);

		// for every clause : iterate over every extracted line and check if all
		// literals of the clause are contained within the extracted line. If all lines
		// have been checked without success : test fails

		for (Iterator<Or<Atom>> clauseIterator = clauses.iterator(); clauseIterator.hasNext();) {
			Or<Atom> clause = clauseIterator.next();
			Set<Literal> literals = clause.getLiterals();

			boolean intermediateResult = true;

			for (Iterator<String> extractedIterator = extracted.iterator(); extractedIterator.hasNext();) {
				String line = (String) extractedIterator.next();

				for (Iterator<Literal> literalsIterator = literals.iterator(); literalsIterator.hasNext();) {
					String literalString = literalsIterator.next().toString();

					if (!line.contains(literalString)) {
						intermediateResult = false;
						break;
					} else {
						intermediateResult = true;
					}
				}
				
				if(intermediateResult) {
					String match="line : "+line+" | clause : "+clause;
					break;
				}
			}

			// if intermediate result is false at this point : no line has been found
			// containing all literals of the current clause => therefore false is returned
			if(!intermediateResult) {
				result=false;
				break;
			}
		}

		return result;
	}

	private static List<String> extract(File cnfFile) throws IOException {
		return Files.lines(cnfFile.toPath()).map(s -> s.replace(" 0", "")).filter(s -> !s.contains("c"))
				.filter(s -> !s.contains("p")).map(s -> s.trim()).collect(Collectors.toList());
	}
}