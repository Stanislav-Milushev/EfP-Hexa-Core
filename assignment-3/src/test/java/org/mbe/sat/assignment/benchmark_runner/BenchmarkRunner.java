package org.mbe.sat.assignment.benchmark_runner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.DpSolver;
import org.mbe.sat.assignment.exceptions.EmptyChartInputException;
import org.mbe.sat.assignment.gui.BarChartFactory;
import org.mbe.sat.assignment.gui.IBarChartFactory;
import org.mbe.sat.assignment.gui.IBarChartGui;
import org.mbe.sat.assignment.gui.ProgressBarGui;
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
	 * map, storing a description of a given {@link ISolver} instance with a String
	 * key
	 */
	private TreeMap<String, ISolver<CnfFormula, Optional<Assignment>>> solverMap;
	/**
	 * specifies the number of round executed to obtain an average execution time
	 */
	private static int NUMBER_OF_RUNS;

	/**
	 * constructor ; initializes
	 * {@link #runner},{@link #factory},{@link #resultList}
	 */
	public BenchmarkRunner() {
		runner = new TimedCnfSolvableRunner();
		factory = new BarChartFactory();
		resultList = new ArrayList<>();
		solverMap = new TreeMap<>();
	}

	/**
	 * adds a new item to {@link #solverMap} and returns false if the item
	 * previously existed and was replaced by the new version
	 * 
	 * @param solverName
	 * @param newSolver
	 * @return true if {@link #solverMap} did not contain any comparable value until
	 *         now
	 */
	public boolean addSolver(String solverName, ISolver<CnfFormula, Optional<Assignment>> newSolver) {
		return this.solverMap.put(solverName, newSolver) == null;
	}

	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Windows".equals(info.getName())) {
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

		NUMBER_OF_RUNS = 1000;

		BenchmarkRunner runner = new BenchmarkRunner();
//		runner.addSolver("1 : BASE SOLVER", new BaseSolver());
		runner.addSolver("1 : DP-SOLVER", new DpSolver());
		runner.addSolver("2 : DP-SOLVER", new DpSolver());
		runner.addSolver("3 : DP-SOLVER", new DpSolver());
		runner.addSolver("4 : DP-SOLVER", new DpSolver());

		try {
//			runner.runTrivial();
			runner.runEasy();
//			runner.runMedium();
//			runner.runHard();
//			runner.runInsane();
		} catch (NullPointerException | EmptyChartInputException e) {
			e.printStackTrace();
		}

	}

	/**
	 * performs setup for the {@link #runTrivial()} method by measuring the runtime
	 * for all given {@link ISolver} instances ({@link #solverMap}) for a defined
	 * number of repetitions (defined by {@link #NUMBER_OF_RUNS} :
	 * {@value #NUMBER_OF_RUNS}) to calculate the average runtime of each given
	 * benchmark file for the difficulty "TRIVIAL". Finally all generated values are
	 * passed to the {@link IBarChartFactory} instance ({@link #factory})
	 * 
	 * @throws NullPointerException     (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 * @throws EmptyChartInputException (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 */
	public void setupTrivial() throws NullPointerException, EmptyChartInputException {
		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getTrivial().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

		//////////////////////////////
		ProgressBarGui progressBarGui = new ProgressBarGui(jsonModels.size(), solverMap.size(), NUMBER_OF_RUNS);
		//////////////////////////////
		int modelCounter = 0;

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();

			progressBarGui.setNewBenchmarkValue(modelCounter++);
			int solverCounter = 0;

			for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
				String solverName = (String) solverIterator.next();
				ISolver<CnfFormula, Optional<Assignment>> currentSolver = this.solverMap.get(solverName);
				ArrayList<TimedResult<Optional<Assignment>>> intermediateResultList = new ArrayList<>();

				progressBarGui.setNewSolverValue(solverCounter++);
				int runCounter = 0;

				// capture runtimes for given number of rounds
				for (int i = 0; i < NUMBER_OF_RUNS; i++) {
					TimedResult<Optional<Assignment>> timedResult = runner.runTimed(currentSolver, formula);
					intermediateResultList.add(timedResult);

					progressBarGui.setNewRoundValue(runCounter++);
				}

				long sumDuration = 0;

				for (Iterator<TimedResult<Optional<Assignment>>> resultListIterator = intermediateResultList
						.iterator(); resultListIterator.hasNext();) {
					TimedResult<Optional<Assignment>> result = resultListIterator.next();
					sumDuration += result.getDurationInMilliseconds();
				}

				// calculate average runtime
				long finalDuration = ((sumDuration * 1000000) / (NUMBER_OF_RUNS));
				// double finalDuration = ((double)sumDuration / (double)NUMBER_OF_ROUNDS);

				TimedResult<Optional<Assignment>> finalResult = new TimedResult<Optional<Assignment>>(
						intermediateResultList.get(0).getResult(), finalDuration);
				this.resultList.add(finalResult);
			}
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = new String[this.solverMap.size()];
		String[] categories = new String[this.jsonModels.size()];

		int counter = 0;
		for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
			String solverName = (String) solverIterator.next();
			names[counter++] = solverName;
		}

		counter = 0;
		for (Iterator<SatProblemJsonModel> iterator = jsonModels.iterator(); iterator.hasNext();) {
			SatProblemJsonModel model = (SatProblemJsonModel) iterator.next();
			StringBuilder builder = new StringBuilder();
			builder.append(model.getFileName());
			builder.append(" | ");
			builder.append("VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());
			categories[counter++] = builder.toString();
		}

		counter = 0;
		for (Iterator<TimedResult<Optional<Assignment>>> resultIterator = this.resultList.iterator(); resultIterator
				.hasNext();) {
			TimedResult<Optional<Assignment>> result = resultIterator.next();
			values[counter++] = ((double) result.getDurationInMilliseconds() / (double) 1000000);
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Trivial Benchmark : " + NUMBER_OF_RUNS + " runs");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);

		progressBarGui.close();
	}

	/**
	 * calls {@link #org.mbe.sat.assignment.gui.BarChartFactory.showGui()}
	 * ({@link IBarChartFactory}) after trivial setup was successful
	 * ({@link #setupTrivial()})
	 * 
	 * @throws NullPointerException     (error in {@link #setupTrivial()})
	 * @throws EmptyChartInputException (error in {@link #setupTrivial()})
	 */
	public void runTrivial() throws NullPointerException, EmptyChartInputException {
		this.setupTrivial();
		this.factory.showGui();
	}

	/**
	 * performs setup for the {@link #runEasy()} method by measuring the runtime for
	 * all given {@link ISolver} instances ({@link #solverMap}) for a defined number
	 * of repetitions (defined by {@link #NUMBER_OF_RUNS} :
	 * {@value #NUMBER_OF_RUNS}) to calculate the average runtime of each given
	 * benchmark file for the difficulty "EASY". Finally all generated values are
	 * passed to the {@link IBarChartFactory} instance ({@link #factory})
	 * 
	 * @throws NullPointerException     (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 * @throws EmptyChartInputException (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 */
	public void setupEasy() throws NullPointerException, EmptyChartInputException {
		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getEasy().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

//////////////////////////////
		ProgressBarGui progressBarGui = new ProgressBarGui(jsonModels.size(), solverMap.size(), NUMBER_OF_RUNS);
//////////////////////////////
		int modelCounter = 0;

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();

			progressBarGui.setNewBenchmarkValue(modelCounter++);
			int solverCounter = 0;

			for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
				String solverName = (String) solverIterator.next();
				ISolver<CnfFormula, Optional<Assignment>> currentSolver = this.solverMap.get(solverName);
				ArrayList<TimedResult<Optional<Assignment>>> intermediateResultList = new ArrayList<>();

				progressBarGui.setNewSolverValue(solverCounter++);
				int runCounter = 0;

				// capture runtimes for given number of rounds
				for (int i = 0; i < NUMBER_OF_RUNS; i++) {
					TimedResult<Optional<Assignment>> timedResult = runner.runTimed(currentSolver, formula);
					intermediateResultList.add(timedResult);

					progressBarGui.setNewRoundValue(runCounter++);
				}

				long sumDuration = 0;

				for (Iterator<TimedResult<Optional<Assignment>>> resultListIterator = intermediateResultList
						.iterator(); resultListIterator.hasNext();) {
					TimedResult<Optional<Assignment>> result = resultListIterator.next();
					sumDuration += result.getDurationInMilliseconds();
				}

				// calculate average runtime
				long finalDuration = ((sumDuration * 1000000) / (NUMBER_OF_RUNS));
				// double finalDuration = ((double)sumDuration / (double)NUMBER_OF_ROUNDS);

				TimedResult<Optional<Assignment>> finalResult = new TimedResult<Optional<Assignment>>(
						intermediateResultList.get(0).getResult(), finalDuration);
				this.resultList.add(finalResult);
			}
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = new String[this.solverMap.size()];
		String[] categories = new String[this.jsonModels.size()];

		int counter = 0;
		for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
			String solverName = (String) solverIterator.next();
			names[counter++] = solverName;
		}

		counter = 0;
		for (Iterator<SatProblemJsonModel> iterator = jsonModels.iterator(); iterator.hasNext();) {
			SatProblemJsonModel model = (SatProblemJsonModel) iterator.next();
			StringBuilder builder = new StringBuilder();
			builder.append(model.getFileName());
			builder.append(" | ");
			builder.append("VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());
			categories[counter++] = builder.toString();
		}

		counter = 0;
		for (Iterator<TimedResult<Optional<Assignment>>> resultIterator = this.resultList.iterator(); resultIterator
				.hasNext();) {
			TimedResult<Optional<Assignment>> result = resultIterator.next();
			values[counter++] = ((double) result.getDurationInMilliseconds() / (double) 1000000);
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Easy Benchmark : " + NUMBER_OF_RUNS + " runs");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);

		progressBarGui.close();
	}

	/**
	 * calls {@link #org.mbe.sat.assignment.gui.BarChartFactory.showGui()}
	 * ({@link IBarChartFactory}) after easy setup was successful
	 * ({@link #setupEasy()})
	 * 
	 * @throws NullPointerException     (error in {@link #setupEasy()})
	 * @throws EmptyChartInputException (error in {@link #setupEasy()})
	 */
	public void runEasy() throws NullPointerException, EmptyChartInputException {
		this.setupEasy();
		this.factory.showGui();
	}

	/**
	 * performs setup for the {@link #runTrivial()} method by measuring the runtime
	 * for all given {@link ISolver} instances ({@link #solverMap}) for a defined
	 * number of repetitions (defined by {@link #NUMBER_OF_RUNS} :
	 * {@value #NUMBER_OF_RUNS}) to calculate the average runtime of each given
	 * benchmark file for the difficulty "TRIVIAL". Finally all generated values are
	 * passed to the {@link IBarChartFactory} instance ({@link #factory})
	 * 
	 * @throws NullPointerException     (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 * @throws EmptyChartInputException (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 */
	public void setupMedium() throws NullPointerException, EmptyChartInputException {
		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getMedium().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

//////////////////////////////
		ProgressBarGui progressBarGui = new ProgressBarGui(jsonModels.size(), solverMap.size(), NUMBER_OF_RUNS);
//////////////////////////////
		int modelCounter = 0;

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();

			progressBarGui.setNewBenchmarkValue(modelCounter++);
			int solverCounter = 0;

			for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
				String solverName = (String) solverIterator.next();
				ISolver<CnfFormula, Optional<Assignment>> currentSolver = this.solverMap.get(solverName);
				ArrayList<TimedResult<Optional<Assignment>>> intermediateResultList = new ArrayList<>();

				progressBarGui.setNewSolverValue(solverCounter++);
				int runCounter = 0;

				// capture runtimes for given number of rounds
				for (int i = 0; i < NUMBER_OF_RUNS; i++) {
					TimedResult<Optional<Assignment>> timedResult = runner.runTimed(currentSolver, formula);
					intermediateResultList.add(timedResult);

					progressBarGui.setNewRoundValue(runCounter++);
				}

				long sumDuration = 0;

				for (Iterator<TimedResult<Optional<Assignment>>> resultListIterator = intermediateResultList
						.iterator(); resultListIterator.hasNext();) {
					TimedResult<Optional<Assignment>> result = resultListIterator.next();
					sumDuration += result.getDurationInMilliseconds();
				}

				// calculate average runtime
				long finalDuration = ((sumDuration * 1000000) / (NUMBER_OF_RUNS));
				// double finalDuration = ((double)sumDuration / (double)NUMBER_OF_ROUNDS);

				TimedResult<Optional<Assignment>> finalResult = new TimedResult<Optional<Assignment>>(
						intermediateResultList.get(0).getResult(), finalDuration);
				this.resultList.add(finalResult);
			}
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = new String[this.solverMap.size()];
		String[] categories = new String[this.jsonModels.size()];

		int counter = 0;
		for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
			String solverName = (String) solverIterator.next();
			names[counter++] = solverName;
		}

		counter = 0;
		for (Iterator<SatProblemJsonModel> iterator = jsonModels.iterator(); iterator.hasNext();) {
			SatProblemJsonModel model = (SatProblemJsonModel) iterator.next();
			StringBuilder builder = new StringBuilder();
			builder.append(model.getFileName());
			builder.append(" | ");
			builder.append("VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());
			categories[counter++] = builder.toString();
		}

		counter = 0;
		for (Iterator<TimedResult<Optional<Assignment>>> resultIterator = this.resultList.iterator(); resultIterator
				.hasNext();) {
			TimedResult<Optional<Assignment>> result = resultIterator.next();
			values[counter++] = ((double) result.getDurationInMilliseconds() / (double) 1000000);
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Medium Benchmark : " + NUMBER_OF_RUNS + " runs");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);

		progressBarGui.close();
	}

	/**
	 * calls {@link #org.mbe.sat.assignment.gui.BarChartFactory.showGui()}
	 * ({@link IBarChartFactory}) after medium setup was successful
	 * ({@link #setupMedium()})
	 * 
	 * @throws NullPointerException     (error in {@link #setupMedium()})
	 * @throws EmptyChartInputException (error in {@link #setupMedium()})
	 */
	public void runMedium() throws NullPointerException, EmptyChartInputException {
		this.setupMedium();
		this.factory.showGui();
	}

	/**
	 * performs setup for the {@link #runHard()} method by measuring the runtime for
	 * all given {@link ISolver} instances ({@link #solverMap}) for a defined number
	 * of repetitions (defined by {@link #NUMBER_OF_RUNS} :
	 * {@value #NUMBER_OF_RUNS}) to calculate the average runtime of each given
	 * benchmark file for the difficulty "HARD". Finally all generated values are
	 * passed to the {@link IBarChartFactory} instance ({@link #factory})
	 * 
	 * @throws NullPointerException     (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 * @throws EmptyChartInputException (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 */
	public void setupHard() throws NullPointerException, EmptyChartInputException {
		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getHard().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

//////////////////////////////
		ProgressBarGui progressBarGui = new ProgressBarGui(jsonModels.size(), solverMap.size(), NUMBER_OF_RUNS);
//////////////////////////////
		int modelCounter = 0;

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();

			progressBarGui.setNewBenchmarkValue(modelCounter++);
			int solverCounter = 0;

			for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
				String solverName = (String) solverIterator.next();
				ISolver<CnfFormula, Optional<Assignment>> currentSolver = this.solverMap.get(solverName);
				ArrayList<TimedResult<Optional<Assignment>>> intermediateResultList = new ArrayList<>();

				progressBarGui.setNewSolverValue(solverCounter++);
				int runCounter = 0;

				// capture runtimes for given number of rounds
				for (int i = 0; i < NUMBER_OF_RUNS; i++) {
					TimedResult<Optional<Assignment>> timedResult = runner.runTimed(currentSolver, formula);
					intermediateResultList.add(timedResult);

					progressBarGui.setNewRoundValue(runCounter++);
				}

				long sumDuration = 0;

				for (Iterator<TimedResult<Optional<Assignment>>> resultListIterator = intermediateResultList
						.iterator(); resultListIterator.hasNext();) {
					TimedResult<Optional<Assignment>> result = resultListIterator.next();
					sumDuration += result.getDurationInMilliseconds();
				}

				// calculate average runtime
				long finalDuration = ((sumDuration * 1000000) / (NUMBER_OF_RUNS));
				// double finalDuration = ((double)sumDuration / (double)NUMBER_OF_ROUNDS);

				TimedResult<Optional<Assignment>> finalResult = new TimedResult<Optional<Assignment>>(
						intermediateResultList.get(0).getResult(), finalDuration);
				this.resultList.add(finalResult);
			}
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = new String[this.solverMap.size()];
		String[] categories = new String[this.jsonModels.size()];

		int counter = 0;
		for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
			String solverName = (String) solverIterator.next();
			names[counter++] = solverName;
		}

		counter = 0;
		for (Iterator<SatProblemJsonModel> iterator = jsonModels.iterator(); iterator.hasNext();) {
			SatProblemJsonModel model = (SatProblemJsonModel) iterator.next();
			StringBuilder builder = new StringBuilder();
			builder.append(model.getFileName());
			builder.append(" | ");
			builder.append("VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());
			categories[counter++] = builder.toString();
		}

		counter = 0;
		for (Iterator<TimedResult<Optional<Assignment>>> resultIterator = this.resultList.iterator(); resultIterator
				.hasNext();) {
			TimedResult<Optional<Assignment>> result = resultIterator.next();
			values[counter++] = ((double) result.getDurationInMilliseconds() / (double) 1000000);
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Hard Benchmark : " + NUMBER_OF_RUNS + " runs");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);

		progressBarGui.close();
	}

	/**
	 * calls {@link #org.mbe.sat.assignment.gui.BarChartFactory.showGui()}
	 * ({@link IBarChartFactory}) after hard setup was successful
	 * ({@link #setupHard()})
	 * 
	 * @throws NullPointerException     (error in {@link #setupHard()})
	 * @throws EmptyChartInputException (error in {@link #setupHard()})
	 */
	public void runHard() throws NullPointerException, EmptyChartInputException {
		this.setupHard();
		this.factory.showGui();
	}

	/**
	 * performs setup for the {@link #runInsane()} method by measuring the runtime
	 * for all given {@link ISolver} instances ({@link #solverMap}) for a defined
	 * number of repetitions (defined by {@link #NUMBER_OF_RUNS} :
	 * {@value #NUMBER_OF_RUNS}) to calculate the average runtime of each given
	 * benchmark file for the difficulty "INSANE". Finally all generated values are
	 * passed to the {@link IBarChartFactory} instance ({@link #factory})
	 * 
	 * @throws NullPointerException     (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 * @throws EmptyChartInputException (if not all required parameters are passed
	 *                                  to {@link #factory} properly)
	 */
	public void setupInsane() throws NullPointerException, EmptyChartInputException {
		// get/sort required benchmark files
		this.jsonModels = SatProblemFixtures.getInsane().stream()
				.sorted((s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
				.collect(Collectors.toList());

//////////////////////////////
		ProgressBarGui progressBarGui = new ProgressBarGui(jsonModels.size(), solverMap.size(), NUMBER_OF_RUNS);
//////////////////////////////
		int modelCounter = 0;

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();

			progressBarGui.setNewBenchmarkValue(modelCounter++);
			int solverCounter = 0;

			for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
				String solverName = (String) solverIterator.next();
				ISolver<CnfFormula, Optional<Assignment>> currentSolver = this.solverMap.get(solverName);
				ArrayList<TimedResult<Optional<Assignment>>> intermediateResultList = new ArrayList<>();

				progressBarGui.setNewSolverValue(solverCounter++);
				int runCounter = 0;

				// capture runtimes for given number of rounds
				for (int i = 0; i < NUMBER_OF_RUNS; i++) {
					TimedResult<Optional<Assignment>> timedResult = runner.runTimed(currentSolver, formula);
					intermediateResultList.add(timedResult);

					progressBarGui.setNewRoundValue(runCounter++);
				}

				long sumDuration = 0;

				for (Iterator<TimedResult<Optional<Assignment>>> resultListIterator = intermediateResultList
						.iterator(); resultListIterator.hasNext();) {
					TimedResult<Optional<Assignment>> result = resultListIterator.next();
					sumDuration += result.getDurationInMilliseconds();
				}

				// calculate average runtime
				long finalDuration = ((sumDuration * 1000000) / (NUMBER_OF_RUNS));
				// double finalDuration = ((double)sumDuration / (double)NUMBER_OF_ROUNDS);

				TimedResult<Optional<Assignment>> finalResult = new TimedResult<Optional<Assignment>>(
						intermediateResultList.get(0).getResult(), finalDuration);
				this.resultList.add(finalResult);
			}
		}

		// prepare parameters for BarChartFactory
		double[] values = new double[this.resultList.size()];
		String[] names = new String[this.solverMap.size()];
		String[] categories = new String[this.jsonModels.size()];

		int counter = 0;
		for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
			String solverName = (String) solverIterator.next();
			names[counter++] = solverName;
		}

		counter = 0;
		for (Iterator<SatProblemJsonModel> iterator = jsonModels.iterator(); iterator.hasNext();) {
			SatProblemJsonModel model = (SatProblemJsonModel) iterator.next();
			StringBuilder builder = new StringBuilder();
			builder.append(model.getFileName());
			builder.append(" | ");
			builder.append("VARIABLES : ");
			builder.append(model.getFormula().getVariables().size());
			categories[counter++] = builder.toString();
		}

		counter = 0;
		for (Iterator<TimedResult<Optional<Assignment>>> resultIterator = this.resultList.iterator(); resultIterator
				.hasNext();) {
			TimedResult<Optional<Assignment>> result = resultIterator.next();
			values[counter++] = ((double) result.getDurationInMilliseconds() / (double) 1000000);
		}

		// pass parameters to BarChartFactory
		this.factory.setChartTitle("Insane Benchmark : " + NUMBER_OF_RUNS + " runs");
		this.factory.setNames(names);
		this.factory.setCategories(categories);
		this.factory.setValues(values);

		progressBarGui.close();
	}

	/**
	 * calls {@link #org.mbe.sat.assignment.gui.BarChartFactory.showGui()}
	 * ({@link IBarChartFactory}) after insane setup was successful
	 * ({@link #setupInsane()})
	 * 
	 * @throws NullPointerException     (error in {@link #setupInsane()})
	 * @throws EmptyChartInputException (error in {@link #setupInsane()})
	 */
	public void runInsane() throws NullPointerException, EmptyChartInputException {
		this.setupInsane();
		this.factory.showGui();
	}
}