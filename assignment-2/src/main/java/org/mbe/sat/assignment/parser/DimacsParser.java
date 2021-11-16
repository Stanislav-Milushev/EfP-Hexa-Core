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
	public CnfFormula parse(List<String> formulaLines) throws IOException {
		Set<Or<Atom>> op = new HashSet<Or<Atom>>();
		boolean readmode = false;
		for (String string : formulaLines) {
			if (readmode && !string.startsWith("c ") && !string.isEmpty() && string.matches(".*\\d.*")) {
				String newstring = string.trim();
				String[] splited = newstring.split("\\s+");
				Set variablen = new HashSet<Formula>();
				for (Object slitedvar : splited) {
					if (Integer.parseInt(slitedvar.toString()) != 0) {
						boolean minustest = !slitedvar.toString().contains("-");
						String t = slitedvar.toString().replace("-", "");
						Variable newVar = new Variable(t);
						Literal newLit = new Literal(newVar, minustest);
						variablen.add(newLit);
						// System.out.println(ob);
					}

				}

				Or<Atom> tempvar = new Or<Atom>(variablen);
				op.add(tempvar);
			}

			if (string.contains("p cnf ") && !string.startsWith("c ")) {
				readmode = true;

			}
		}
		CnfFormula finalcnf = new CnfFormula(op);
		System.out.println(finalcnf.getVariables());
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

	@Override
	public CnfFormula parse(File formulaFile) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
