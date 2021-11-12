package org.mbe.sat.assignment.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mbe.sat.api.parser.IFormulaParser;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.And;
import org.mbe.sat.core.model.formula.Atom;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.model.formula.Formula;
import org.mbe.sat.core.model.formula.Literal;
import org.mbe.sat.core.model.formula.Or;
import org.mbe.sat.core.model.formula.Tristate;
import org.mbe.sat.core.model.formula.Variable;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DimacsParser implements IFormulaParser<CnfFormula> {

	@Override
	public CnfFormula parse(List<String> formulaLines) {
		Set<Or<Atom>> op = new HashSet<Or<Atom>>();

		/*
		 * Set kek1 = new HashSet<Formula>();
		 * 
		 * Variable xb = new Variable("b"); Literal x1b = new Literal(x, true);
		 * 
		 * kek1.add(x1b); kek1.add(xb);
		 * 
		 * Or<Atom> kekw1 = new Or<Atom>(kek1);
		 * 
		 * op.add(kekw1);
		 */
		// TODO: Convert given lines of DIMACS file into instance of CnfFormula
		// You may ignore all other packages besides "org.mbe.sat.core.model.formula"
		boolean readmode = false;
		for (String string : formulaLines) {
			if (readmode) {

				String newstring = string.trim();

				String[] splited = newstring.split("\\s+");

				Set variablen = new HashSet<Formula>();
				for (Object ob : splited) {
					if (Integer.parseInt(ob.toString())!= 0) {
						boolean minustest = !ob.toString().contains("-");
						String t = ob.toString().replace("-", "");
						Variable x = new Variable(t);
						Literal x1 = new Literal(x, minustest);
						variablen.add(x1);
						//System.out.println(ob);
					}

				}

				Or<Atom> kekw = new Or<Atom>(variablen);
				op.add(kekw);
			}

			if (string.contains("p cnf ")) {
				readmode = true;

			}
		}

		/*
		 * for (Or<Atom> or : op) { System.out.println(or); }
		 */

		CnfFormula finalcnf = new CnfFormula(op);
		System.out.println(finalcnf.getClauses());
		return finalcnf;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CnfFormula parse(File formulaFile, Charset charset) throws IOException {
		return parse(FileUtils.readLines(formulaFile, StandardCharsets.UTF_8));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CnfFormula parse(InputStream inputStream, Charset charset) throws IOException {
		return parse(IOUtils.readLines(inputStream, charset));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CnfFormula parse(InputStream inputStream) throws IOException {
		return parse(inputStream, StandardCharsets.UTF_8);
	}

}
