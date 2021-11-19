package org.mbe.sat.core;

import org.mbe.sat.api.ICombinationProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.NoSuchElementException;

/**
 * {@inheritDoc}
 */
public class SequentialCombinationProvider implements ICombinationProvider {

    /**
     * The size of the boolean arrays that are returned.
     */
    private final int size;
    /**
     * Number of all combinations that can be created with boolean arrays of the given {@link #size}.
     */
    private final BigDecimal numberOfCombinations;
    /**
     * Counter the number of combinations created and returned.
     */
    private BigInteger counter = BigInteger.ZERO;

    /**
     * Creates a new instance of {@link SequentialCombinationProvider} with the given size for the boolean array
     * combinations that are returned.
     *
     * @param size the size of returned boolean array combinations
     */
    public SequentialCombinationProvider(int size) {
        this.size = size;
        this.numberOfCombinations = BigDecimal.valueOf(Math.pow(2, size));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return counter.compareTo(numberOfCombinations.toBigInteger()) < 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean[] next() {

        // When calling next, if no more combinations can be provided, an NoSuchElementException is thrown
        if (!hasNext()) {
            throw new NoSuchElementException("No more combinations can be provided");
        }

        // Prepend '0' zeros to the start of the binary string until the size is reached
        StringBuilder binaryStringBuilder = new StringBuilder(counter.toString(2));
        while (binaryStringBuilder.length() < size) {
            binaryStringBuilder.insert(0, "0");
        }

        // Convert the string array into an character array so we can convert each '0' or '1' into a boolean value
        char[] binaryCharacterArray = binaryStringBuilder.toString().toCharArray();
        boolean[] booleanArray = new boolean[size];
        for (int j = 0; j < binaryCharacterArray.length; j++) {
            booleanArray[j] = binaryCharacterArray[j] == '0';
        }

        // Increase the counter so we get the next array on the next call
        counter = counter.add(BigInteger.ONE);

        return booleanArray;
    }
}
