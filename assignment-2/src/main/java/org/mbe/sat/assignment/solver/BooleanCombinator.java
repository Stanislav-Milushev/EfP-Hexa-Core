package org.mbe.sat.assignment.solver;

import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BooleanCombinator {
	private int numberOfVariables;
	
	BooleanCombinator(int numberOfVariables){
		this.numberOfVariables = numberOfVariables;
	}
	
	/**
	 * An array of Boolean values is created where all 
	 * values are false at the beginning. For the index values 
	 * corresponding to one bit in the bitset the value is set to true
	 * 
	 * @author Alexander Kampen
	 * @param BitSet with bit value
	 * @param Size of Boolean array
	 * @return Array of Boolean values whose true values correspond to the bit value
	 */
	boolean[] bitSetToArray(BitSet bs, int width) {
        boolean[] result = new boolean[width]; 
        bs.stream().forEach(i -> result[i] = true);
        return result;
	}
	
	
	/**
	 * An array of Boolean values is created, where all 
	 * true values correspond to the index value 
	 * 
	 * @author Alexander Kampen
	 * @param Index
	 * @return Array of Boolean values whose true values correspond to the index value
	 */
	public boolean[] getCombinationByBitIndex(int index) {
		return bitSetToArray(BitSet.valueOf(new long[] { index }), this.numberOfVariables);
	}
}