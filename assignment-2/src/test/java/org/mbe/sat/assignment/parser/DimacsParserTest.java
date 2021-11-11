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
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Variable;

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

	@Test
	void test() throws IOException {
		DimacsParser dp = new DimacsParser();
		File cnffile = new File("src-framework/testFixtures/resources/org/mbe/sat/core/problem/" + "aim-50-1_6-yes1-4.cnf");
		CnfFormula test = dp.parse(cnffile, StandardCharsets.UTF_8);
		System.out.println(test);

		
		

}}