package org.mbe.sat.assignment.parser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mbe.sat.api.parser.IFormulaParser;
import org.mbe.sat.core.model.formula.CnfFormula;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DimacsParser implements IFormulaParser<CnfFormula> {

    @Override
    public CnfFormula parse(List<String> formulaLines) {

        // TODO: Convert given lines of DIMACS file into instance of CnfFormula
        // You may ignore all other packages besides "org.mbe.sat.core.model.formula"
        return null;
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
