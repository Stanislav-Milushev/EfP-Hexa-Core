package org.mbe.sat.assignment.gui;

import java.util.List;
import java.util.Optional;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.gui.InitDialogPanel.Difficulty;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;

public interface IInitDialogPanel {

	public static final String BASE_SOLVER_STRING="BASE_SOLVER";
	public static final String DP_SOLVER_STRING="DP_SOLVER";
	public static final String SOLVER_3_STRING="SOLVER_3";
	public static final String SOLVER_4_STRING="SOLVER_4";
	
	public Difficulty getDifficulty();
	
	public List<String> getSelectedSolvers();
	
	public int getNumberOfRuns();
	
	public int getTimeout();
	
	public boolean validateSelectedSolvers();
	
	public boolean validateNumberOfRuns();
	
	public boolean validateTimeout();
}
