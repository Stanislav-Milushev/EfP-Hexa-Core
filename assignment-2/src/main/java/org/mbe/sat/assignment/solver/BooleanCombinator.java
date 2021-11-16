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
	 *  IntStream with a range from 0 to 2^(number of variables) - 1  
	 *  is created and mapped by calling the method bitSetToArray.
	 *  Afterwards the elements are accumulated into a new list
	 * 
	 * @author Alexander Kampen
	 * @return A list with all boolean combinations
	 */
	public List<boolean[]> getAllPossibleCombinations(){
		return IntStream.range(0, (int)Math.pow(2, this.numberOfVariables))
	            .mapToObj(i -> bitSetToArray(BitSet.valueOf(new long[] { i }), this.numberOfVariables))
	            .collect(Collectors.toList());
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
	
	
	public boolean[] getCombinationByBitIndex(int index) {
		return bitSetToArray(BitSet.valueOf(new long[] { index }), this.numberOfVariables);
	}
}