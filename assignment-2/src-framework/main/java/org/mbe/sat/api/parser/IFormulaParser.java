package org.mbe.sat.api.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Used to parse formulas given as string into a data structure representation.
 *
 * @param <T> the type of the returned formula
 */
public interface IFormulaParser<T> {

    /**
     * Parses the given strings into a formula. The given list of strings represents the lines of a file that defines
     * the formula.
     *
     * @param formulaLines the lines that make the formula
     * @return the formula that is represented by the given lines
     */
    T parse(List<String> formulaLines);

    /**
     * Parses the given file by reading its content as lines and using {@link #parse(List)} to parse them.
     *
     * @param formulaFile the DIMACS file
     * @param charset     the charset that is used to read the lines
     * @return the formula that is represented by the given DIMACS file
     *
     * @see #parse(List)
     */
    T parse(File formulaFile, Charset charset) throws IOException;

    /**
     * Parses the file given as input stream by reading its content as lines and using {@link #parse(List)} to parse
     * them.
     *
     * @param inputStream the input stream providing the content of the DIMACS file
     * @param charset     the charset that is used to read the lines
     * @return the formula that is represented by the given DIMACS file
     *
     * @see #parse(List)
     */
    T parse(InputStream inputStream, Charset charset) throws IOException;

    /**
     * Parses the file given as input stream by reading its content as lines and using {@link #parse(List)} to parse
     * them.
     *
     * @param inputStream the input stream providing the content of the DIMACS file
     * @return the formula that is represented by the given DIMACS file
     *
     * @see #parse(List)
     */
    T parse(InputStream inputStream) throws IOException;
}
