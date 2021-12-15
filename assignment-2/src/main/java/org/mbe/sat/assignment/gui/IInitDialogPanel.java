package org.mbe.sat.assignment.gui;

import java.util.List;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.gui.InitDialogPanel.Difficulty;

/**
 * @author User Darwin Brambor
 *
 */
public interface IInitDialogPanel {

	/**
	 * returned if the user chooses the base-solver option
	 */
	public static final String BASE_SOLVER_STRING = "BASE_SOLVER";
	/**
	 * returned if the user chooses the dp-solver option
	 */
	public static final String DP_SOLVER_STRING = "DP_SOLVER";
	/**
	 * placeholder
	 */
	public static final String DPLL_SOLVER = "DPLL_SOLVER";
	/**
	 * placeholder
	 */
	public static final String SOLVER_4_STRING = "SOLVER_4";

	/**
	 * @return {@link Difficulty} selected by the user
	 */
	public Difficulty getDifficulty();

	/**
	 * @return user selection of available {@link ISolver}s represented as
	 *         {@link String}; possible values are
	 *         {@value #BASE_SOLVER_STRING},{@value #DP_SOLVER_STRING},{@value #DPLL_SOLVER}
	 *         and {@value #SOLVER_4_STRING}
	 */
	public List<String> getSelectedSolvers();

	/**
	 * @return number of runs selected by the user
	 */
	public int getNumberOfRuns();

	/**
	 * @return timeout value selected by the user
	 */
	public int getTimeout();

	/**
	 * @return true if validation was successful (here : if at least one
	 *         {@link ISolver} was selected by the user, returns false otherwise)
	 */
	public boolean validateSelectedSolvers();

	/**
	 * @return true if validation was successful (here : if the selected number of
	 *         runs is larger than 0, returns false otherwise)
	 */
	public boolean validateNumberOfRuns();

	/**
	 * @return true if validation was successful (here : if the selected timeout
	 *         duration is larger than 0, returns false otherwise)
	 */
	public boolean validateTimeout();

	/**
	 * method returns minimal number of variables a benchmark file is allowed to
	 * have in the following
	 * 
	 * @return minimal number of variables selected by the user
	 */
	public int getMinNumOfVariables();

	/**
	 * method returns minimal number of variables a benchmark file is allowed to
	 * have in the following
	 * 
	 * @return minimal number of variables selected by the user
	 */
	public int getMaxNumOfVariables();

	/**
	 * method returns the users choice to only consider benchmark files with an user
	 * defined number of variables in the following
	 * 
	 * @return true if the user wants to specify a range of variables in the
	 *         benchmark file more specificly
	 */
	public boolean isSelectionActive();

	/**
	 * method returns the users choice to include or exclude all of the benchmark
	 * files with a specific number of variables of the selected range
	 * 
	 * @return true if the selected range shall be included into the test run /
	 *         false if the remaining range without the specified one shall be
	 *         included in the following
	 */
	public boolean isRangeIncluded();

}
