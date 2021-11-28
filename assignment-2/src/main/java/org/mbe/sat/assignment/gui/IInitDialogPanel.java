package org.mbe.sat.assignment.gui;

import java.util.List;
import java.util.Optional;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.gui.InitDialogPanel.Difficulty;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;

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
	public static final String SOLVER_3_STRING = "SOLVER_3";
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
	 *         {@value #BASE_SOLVER_STRING},{@value #DP_SOLVER_STRING},{@value #SOLVER_3_STRING}
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
}
