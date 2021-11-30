package org.mbe.sat.assignment.benchmark_runner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.exceptions.EmptyChartInputException;
import org.mbe.sat.assignment.gui.BarChartFactory;
import org.mbe.sat.assignment.gui.IBarChartGui;
import org.mbe.sat.assignment.solver.BaseSolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.problem.SatProblemFixtures;
import org.mbe.sat.core.problem.SatProblemJsonModel;
import org.mbe.sat.core.runner.TimedCnfSolvableRunner;
import org.mbe.sat.core.runner.TimedCnfSolvableRunner.TimedResult;

/**
 * @author User Darwin Brambor
 *
 */
public class BenchmarkRunner {

	/**
	 * runs the {@link #solver}
	 */
	private TimedCnfSolvableRunner runner;
	/**
	 * wrapper class for providing a {@link IBarChartGui} instance to display the
	 * results produced by {@link #solver}/{@link #runner}
	 */
	private BarChartFactory factory;
	/**
	 * list of all benchmark files as {@link SatProblemJsonModel}s
	 */
	private List<SatProblemJsonModel> jsonModels;
	/**
	 * results produced by {@link #solver}/{@link #runner}
	 */
	private List<TimedResult<Optional<Assignment>>> resultList;
	/**
	 * instance of {@link ISolver} to find a solution for each element of
	 * {@link #jsonModels}
	 */
	private ISolver<CnfFormula, Optional<Assignment>> solver;

	/**
	 * constructor ; initializes
	 * {@link #runner},{@link #factory},{@link #resultList}
	 */
	public BenchmarkRunner() {
		runner = new TimedCnfSolvableRunner();
		factory = new BarChartFactory();
		resultList = new ArrayList<>();
	}

	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look and feel.
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			} catch (InstantiationException ex) {
				ex.printStackTrace();
			} catch (IllegalAccessException ex) {
				ex.printStackTrace();
			} catch (UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
			}
		}

		BenchmarkRunner runner = new BenchmarkRunner();
		try {
//			 runner.runTrivial(new BaseSolver());
			runner.runEasy(new BaseSolver());
			// runner.runMedium(new BaseSolver());
			// runner.runHard(new BaseSolver());
			// runner.runInsane(new BaseSolver());
		} catch (NullPointerException e) {
			
			e.printStackTrace();
		} catch (EmptyChartInputException e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * method to prepare the execution of the trivial benchmark
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	private void setupTrivial(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {

		// set current solver
		this.solver = solver;

		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getTrivial().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();
			TimedResult<Optional<Assignment>> timedResult = runner.runTimed(this.solver, formula);
			this.resultList.add(timedResult);
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = { "Trivial" };
		String[] categories = new String[this.resultList.size()];

		for (int i = 0; i < this.resultList.size(); i++) {
			SatProblemJsonModel model = this.jsonModels.get(i);
			TimedResult<Optional<Assignment>> result = this.resultList.get(i);
			StringBuilder builder = new StringBuilder();

			// builder.append("t:");
			builder.append(result.getDurationAsString());
			builder.append(" | FILENAME : ");
			builder.append(model.getFileName());
			builder.append(" | VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());

			categories[i] = builder.toString();
			values[i] = result.getDurationInMilliseconds();
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Trivial Benchmark");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);
	}

	/**
	 * method to prepare the execution of the easy benchmark
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	private void setupEasy(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {

		// set current solver
		this.solver = solver;

		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getEasy().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();
			TimedResult<Optional<Assignment>> timedResult = runner.runTimed(this.solver, formula);
			this.resultList.add(timedResult);
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = { "Easy" };
		String[] categories = new String[this.resultList.size()];

		for (int i = 0; i < this.resultList.size(); i++) {
			SatProblemJsonModel model = this.jsonModels.get(i);
			TimedResult<Optional<Assignment>> result = this.resultList.get(i);
			StringBuilder builder = new StringBuilder();

			// builder.append("t:");
			builder.append(result.getDurationAsString());
			builder.append(" | FILENAME : ");
			builder.append(model.getFileName());
			builder.append(" | VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());

			categories[i] = builder.toString();
			values[i] = result.getDurationInMilliseconds();
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Easy Benchmark");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);
	}

	/**
	 *
	 * method to prepare the execution of the medium benchmark
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	private void setupMedium(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {

		// set current solver
		this.solver = solver;

		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getMedium().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();
			TimedResult<Optional<Assignment>> timedResult = runner.runTimed(this.solver, formula);
			this.resultList.add(timedResult);
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = { "Medium" };
		String[] categories = new String[this.resultList.size()];

		for (int i = 0; i < this.resultList.size(); i++) {
			SatProblemJsonModel model = this.jsonModels.get(i);
			TimedResult<Optional<Assignment>> result = this.resultList.get(i);
			StringBuilder builder = new StringBuilder();

			// builder.append("t:");
			builder.append(result.getDurationAsString());
			builder.append(" | FILENAME : ");
			builder.append(model.getFileName());
			builder.append(" | VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());

			categories[i] = builder.toString();
			values[i] = result.getDurationInMilliseconds();
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Medium Benchmark");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);
	}

	/**
	 * method to prepare the execution of the hard benchmark
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	private void setupHard(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {

		// set current solver
		this.solver = solver;

		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getHard().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();
			TimedResult<Optional<Assignment>> timedResult = runner.runTimed(this.solver, formula);
			this.resultList.add(timedResult);
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = { "Hard" };
		String[] categories = new String[this.resultList.size()];

		for (int i = 0; i < this.resultList.size(); i++) {
			SatProblemJsonModel model = this.jsonModels.get(i);
			TimedResult<Optional<Assignment>> result = this.resultList.get(i);
			StringBuilder builder = new StringBuilder();

			// builder.append("t:");
			builder.append(result.getDurationAsString());
			builder.append(" | FILENAME : ");
			builder.append(model.getFileName());
			builder.append(" | VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());

			categories[i] = builder.toString();
			values[i] = result.getDurationInMilliseconds();
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Hard Benchmark");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);
	}

	/**
	 * 
	 * method to prepare the execution of the insane benchmark
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	private void setupInsane(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {

		// set current solver
		this.solver = solver;

		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getInsane().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();
			TimedResult<Optional<Assignment>> timedResult = runner.runTimed(this.solver, formula);
			this.resultList.add(timedResult);
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = { "Insane" };
		String[] categories = new String[this.resultList.size()];

		for (int i = 0; i < this.resultList.size(); i++) {
			SatProblemJsonModel model = this.jsonModels.get(i);
			TimedResult<Optional<Assignment>> result = this.resultList.get(i);
			StringBuilder builder = new StringBuilder();

			// builder.append("t:");
			builder.append(result.getDurationAsString());
			builder.append(" | FILENAME : ");
			builder.append(model.getFileName());
			builder.append(" | VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());

			categories[i] = builder.toString();
			values[i] = result.getDurationInMilliseconds();
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Insane Benchmark");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);
	}

	/**
	 * method to run the trivial benchmark ; requires execution of
	 * {@link #setupTrivial(ISolver)} method beforehand
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	public void runTrivial(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {
		this.setupTrivial(solver);
		this.factory.showGui();
	}

	/**
	 * method to run the easy benchmark ; requires execution of
	 * {@link #setupEasy(ISolver)} method beforehand
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	public void runEasy(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {
		this.setupEasy(solver);
		this.factory.showGui();
	}

	/**
	 * method to run the medium benchmark ; requires execution of
	 * {@link #setupMedium(ISolver)} method beforehand
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	public void runMedium(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {
		this.setupMedium(solver);
		this.factory.showGui();
	}

	/**
	 * method to run the hard benchmark ; requires execution of
	 * {@link #setupHard(ISolver)} method beforehand
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	public void runHard(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {
		this.setupHard(solver);
		this.factory.showGui();
	}

	/**
	 * method to run the insane benchmark ; requires execution of
	 * {@link #setupInsane(ISolver)} method beforehand
	 * 
	 * @param solver
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	public void runInsane(ISolver<CnfFormula, Optional<Assignment>> solver)
			throws NullPointerException, EmptyChartInputException {
		this.setupInsane(solver);
		this.factory.showGui();
	}

}
