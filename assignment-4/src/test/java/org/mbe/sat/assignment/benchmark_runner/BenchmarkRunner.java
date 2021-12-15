package org.mbe.sat.assignment.benchmark_runner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.mbe.assignment.sat.DpllSolver;
import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.assignment.DpSolver;
import org.mbe.sat.assignment.exceptions.EmptyChartInputException;
import org.mbe.sat.assignment.gui.BarChartFactory;
import org.mbe.sat.assignment.gui.IBarChartFactory;
import org.mbe.sat.assignment.gui.IBarChartGui;
import org.mbe.sat.assignment.gui.IInitDialogPanel;
import org.mbe.sat.assignment.gui.InitDialogPanel;
import org.mbe.sat.assignment.gui.InitDialogPanel.Difficulty;
import org.mbe.sat.assignment.gui.ProgressBarGui;
import org.mbe.sat.assignment.gui.UserCommunication;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import org.mbe.sat.core.problem.SatProblemFixtures;
import org.mbe.sat.core.problem.SatProblemJsonModel;
import org.mbe.sat.core.runner.TimedCnfSolvableRunner;
import org.mbe.sat.core.runner.TimedCnfSolvableRunner.TimedResult;
import org.mbe.sat.impl.BaseSolver;

/**
 * @author User Darwin Brambor
 *
 */


/**
 * runnable implementation to enable the benchmark to be interrupted
 */
class SolverRunnable implements Runnable {

	/**
	 * result of the solving process
	 */
	private TimedResult<Optional<Assignment>> timedResult;
	/**
	 * runner to measure the required time for the solving process
	 */
	private TimedCnfSolvableRunner runner;
	/**
	 * sat solver
	 */
	private ISolver<CnfFormula, Optional<Assignment>> solver;
	/**
	 * formula that shall be solved by the {@link #solver} to obtain a runtime measurement from the {@link #runner}
	 */
	private CnfFormula formula;
	/**
	 * control flag to state the validity of the {@link #runner} instance
	 */
	private boolean runnerValid = false;
	/**
	 * control flag to state the validity of the {@link #solver} instance
	 */
	private boolean solverValid = false;
	/**
	 * control flag to state the validity of the {@link #formula} instance
	 */
	private boolean formulaValid = false;
	/**
	 * control flag to reflect the status of the current execution
	 */
	private boolean finished = false;

	/**
	 *{@inheritDoc}
	 */
	@Override
	public void run() {
		if (validate()) {
			timedResult = runner.runTimed(solver, formula);
		}
		// this.finished=true;
	}

	/**
	 * method gives access to the finished or interrupted results of the solving process
	 * @return {@link #timedResult}
	 */
	public TimedResult<Optional<Assignment>> getTimedResult() {
		if (this.timedResult == null) {
			return new TimedResult<Optional<Assignment>>(Optional.empty(), 0);
		}
		return this.timedResult;
	}

	/**
	 * method sets the value of the attribute {@link #runner}
	 * @param runner
	 */
	public void setRunner(TimedCnfSolvableRunner runner) {
		if (runner != null) {
			this.runner = runner;
			this.runnerValid = true;
		}
	}

	/**
	 * method sets the value of the attribute {@link #solver}
	 * @param solver
	 */
	public void setSolver(ISolver<CnfFormula, Optional<Assignment>> solver) {
		if (solver != null) {
			this.solver = solver;
			this.solverValid = true;
		}
	}

	/**
	 * method sets the value of the attribute {@link #formula}
	 * @param formula
	 */
	public void setFormula(CnfFormula formula) {
		if (formula != null) {
			this.formula = formula;
			this.formulaValid = true;
		}
	}

	/**
	 * method evaluates all available validation flags before the execution begins
	 * @return true if all validation flags are set to true
	 */
	public boolean validate() {
		return (this.runnerValid && this.formulaValid && this.solverValid);
	}

	/**
	 * method gives feedback about the state of the current execution
	 * @return true if the current execution of {@link #run()} finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * method enabled terminating the solving process preemptively
	 * @param choice / if true nothing happens / if false the execution gets interrupted
	 */
	public void terminate(boolean choice) {
		this.solver.terminate(choice);
	}

}


public class BenchmarkRunner {

	/**
	 * factor used later on to provide more accurate result values 
	 */
	private static final int FACTOR = 100;
	/**
	 * true if the user chose to specify a number of benchmark files according to their number of variables
	 */
	private static boolean panelSelectionActive;
	/**
	 * by default 0 / if the {@link #panelSelectionActive} flag is set to true, it stores the user defined lower bound instead
	 */
	private static int minVal;
	/**
	 * by default {@link Integer#MAX_VALUE} / if the {@link #panelSelectionActive} flag is set to true, it stores the user defined upper bound instead
	 */
	private static int maxVal;
	/**
	 * true if the user wants to include the specified selection / false if the user wants to exclude the specified selection
	 */
	private static boolean include;
	/**
	 * runs the {@link #solver}
	 */
	@SuppressWarnings("unused")
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

	public void setNumberOfRuns(int numberOfRuns) {
		this.numberOfRuns = numberOfRuns;
	}

	/**
	 * specifies the number of round executed to obtain an average execution time
	 */
	private int numberOfRuns;
	/**
	 * specifies the allowed timeout for one run
	 */
	private int timeout;

	private Difficulty difficulty;

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

	/**
	 * Sets the {@link #timeout} value of the benchmark
	 * 
	 * @param timeout
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * returns the user-selected difficulty of the benchmark
	 * 
	 * @return benchmark {@link Difficulty difficulty}
	 */
	public Difficulty getDifficulty() {
		return difficulty;
	}

	/**
	 * sets the new user-selected difficulty of the benchmark
	 * 
	 * @param new benchmark {@link Difficulty difficulty}
	 */
	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
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

		///////////////////////////////
		BenchmarkRunner runner = new BenchmarkRunner();

		// init-dialog
		IInitDialogPanel panel = new InitDialogPanel();

		// additionally : choose range of variables => therefore find out min / max
		// number of variables of each difficulty beforehand

		while (true) {
			int choice = JOptionPane.showConfirmDialog(null, panel, "CONFIGURE BENCHMARK",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			if (choice != JOptionPane.YES_OPTION) {
				System.exit(0);
			}

			StringBuilder errorMessageBuilder = new StringBuilder("ERROR : \n");

			if (panel.validateNumberOfRuns()) {
				if (panel.validateSelectedSolvers()) {
					if (panel.validateTimeout()) {

						// all values received properly -> pass values to current benchmark
						// configuration
						runner.setNumberOfRuns(panel.getNumberOfRuns());

						for (int i = 0; i < panel.getSelectedSolvers().size(); i++) {
							switch (panel.getSelectedSolvers().get(i)) {
							case IInitDialogPanel.BASE_SOLVER_STRING:
								runner.addSolver(i + 1 + " : Base-Solver", new BaseSolver());
								// runner.addSolver(i + 1 + ".2 : Base-Solver", new BaseSolver());
								break;
							case IInitDialogPanel.DP_SOLVER_STRING:
								runner.addSolver(i + 1 + " : DP-Solver", new DpSolver());
								// runner.addSolver(i + 1 + ".2 : DP-Solver", new DpSolver());
								break;
							case IInitDialogPanel.DPLL_SOLVER:
								runner.addSolver(i + 1 + ".1 : DPLL-Solver", new DpllSolver());
								// runner.addSolver(i+1+".2 : DPLL-Solver", new DpllSolver());

								break;
							case IInitDialogPanel.SOLVER_4_STRING:
								break;
							}
						}

						runner.setTimeout(panel.getTimeout());
						runner.setDifficulty(panel.getDifficulty());

						break;
					}
				}
			}

			if (!panel.validateNumberOfRuns()) {
				errorMessageBuilder.append("The provided number of runs must be larger than 0!");
				errorMessageBuilder.append("\n");
			}

			if (!panel.validateSelectedSolvers()) {
				errorMessageBuilder.append("At least one solver must be selected!");
				errorMessageBuilder.append("\n");
			}

			if (!panel.validateTimeout()) {
				errorMessageBuilder.append("The provided timeout value must be larger than 0 minutes!");
				errorMessageBuilder.append("\n");
			}

			UserCommunication.errorDialog("INVALID INPUT", errorMessageBuilder.toString());
		}

		//

		panelSelectionActive = panel.isSelectionActive();

		if (panelSelectionActive) {
			include = panel.isRangeIncluded();
			minVal = panel.getMinNumOfVariables();
			maxVal = panel.getMaxNumOfVariables();
		} else {
			include=true;
			minVal = 1;
			maxVal = Integer.MAX_VALUE;
		}

		//

		try {
			runner.runBenchmark();
		} catch (NullPointerException | EmptyChartInputException e) {

			e.printStackTrace();
		}

	}

	/**
	 * calls {@link #org.mbe.sat.assignment.gui.BarChartFactory.showGui() showGui}
	 * ({@link IBarChartFactory}) after the benchmark setup was successful was
	 * successful ({@link #setup() setup})
	 * 
	 * @throws NullPointerException     (error in {@link #setup() setup})
	 * @throws EmptyChartInputException (error in {@link #setup() setup})
	 */
	public void runBenchmark() throws NullPointerException, EmptyChartInputException {
		this.setup();
		this.factory.showGui();
	}

	/**
	 * performs setup steps
	 * 
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 */
	public void setup() throws NullPointerException, EmptyChartInputException {
		String benchmarkType = null;

		switch (this.difficulty) {
		case TRIVIAL:
			if (include) {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getTrivial().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() >= minVal
								&& s1.getFormula().getVariables().size() <= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "TRIVIAL";
			} else {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getTrivial().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() <= minVal
								|| s1.getFormula().getVariables().size() >= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "TRIVIAL";
			}
			break;

		case EASY:
			if (include) {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getEasy().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() >= minVal
								&& s1.getFormula().getVariables().size() <= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "EASY";
			} else {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getEasy().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() <= minVal
								|| s1.getFormula().getVariables().size() >= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "EASY";
			}
			break;

		case MEDIUM:

			if (include) {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getMedium().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() >= minVal
								&& s1.getFormula().getVariables().size() <= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "MEDIUM";
			} else {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getMedium().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() <= minVal
								|| s1.getFormula().getVariables().size() >= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "MEDIUM";
			}

			break;

		case HARD:

			if (include) {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getHard().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() >= minVal
								&& s1.getFormula().getVariables().size() <= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "HARD";
			} else {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getHard().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() <= minVal
								|| s1.getFormula().getVariables().size() >= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "HARD";
			}

			break;

		case INSANE:

			if (include) {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getInsane().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() >= minVal
								&& s1.getFormula().getVariables().size() <= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "INSANE";
			} else {
				// get/sort required benchmark files
				this.jsonModels = SatProblemFixtures.getInsane().stream().sorted(
						(s1, s2) -> s1.getFormula().getVariables().size() - s2.getFormula().getVariables().size())
						.filter((s1) -> (s1.getFormula().getVariables().size() <= minVal
								|| s1.getFormula().getVariables().size() >= maxVal))
						.collect(Collectors.toList());
				benchmarkType = "INSANE";
			}

			break;
		}

		//////////////////////////////
		if (jsonModels.size() == 0) {
			UserCommunication.errorDialog("ERROR : EMPTY RANGE",
					"Error : the user specified range of variables does not include any benchmark files!");
			System.exit(0);
		}
		ProgressBarGui progressBarGui = new ProgressBarGui(jsonModels.size(), solverMap.size(), numberOfRuns);
		//////////////////////////////
		int modelCounter = 1;

		// measure runtime of each retrieved json-model / contained cnf-formula
		for (Iterator<SatProblemJsonModel> jsonModelsIterator = jsonModels.iterator(); jsonModelsIterator.hasNext();) {
			SatProblemJsonModel jsonModel = (SatProblemJsonModel) jsonModelsIterator.next();
			CnfFormula formula = jsonModel.getFormula();

			progressBarGui.setNewBenchmarkValue(modelCounter++);
			int solverCounter = 1;
			boolean timeoutDue = false;

			for (Iterator<String> solverIterator = this.solverMap.keySet().iterator(); solverIterator.hasNext();) {
				timeoutDue = false;
				progressBarGui.setNewSolverValue(solverCounter++);

				String solverName = (String) solverIterator.next();
				ISolver<CnfFormula, Optional<Assignment>> currentSolver = this.solverMap.get(solverName);
				ArrayList<TimedResult<Optional<Assignment>>> intermediateResultList = new ArrayList<>();

				int runCounter = 0;
				progressBarGui.setNewRunValue(runCounter);

				SolverRunnable runnable = null;
				Thread thread = null;

				boolean skipFirst = true;

				// capture runtimes for given number of rounds
				for (int i = 0; i < (numberOfRuns + 1); i++) {
					TimedResult<Optional<Assignment>> timedResult = null;
					TimedCnfSolvableRunner timedRunner = new TimedCnfSolvableRunner();

					// setup runnable thread
					currentSolver.terminate(false);
					runnable = new SolverRunnable();
					CnfFormula newForm = new CnfFormula(formula);
					// copy constructor
					runnable.setFormula(newForm);
					runnable.setSolver(currentSolver);
					runnable.setRunner(timedRunner);
					// runnable.setRunner(this.runner);
					thread = new Thread(runnable);

					thread.start();
					long timestamp = System.currentTimeMillis();

					while (true) {
						if ((System.currentTimeMillis() - timestamp) > (timeout * 60 * 1000)) {
							timeoutDue = true;
							break;
						}

						if (!thread.isAlive()) {
							System.out.println("Thread not alive");
							break;
						}
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
					}

					runnable.terminate(true);
					try {
						thread.join();
					} catch (InterruptedException e) {

						e.printStackTrace();
					}

					timedResult = runnable.getTimedResult();

					if (timeoutDue) {
						timedResult = new TimedResult<Optional<Assignment>>(timedResult.getResult(), 0);
						intermediateResultList.add(timedResult);
						runCounter = 0;
					} else if (skipFirst) {
						skipFirst = false;
					} else {
						intermediateResultList.add(timedResult);
					}

					progressBarGui.setNewRunValue(++runCounter);

					if (timeoutDue) {
						currentSolver.terminate(false);
						break;
					}
				}

				long sumDuration = 0;

				for (Iterator<TimedResult<Optional<Assignment>>> resultListIterator = intermediateResultList
						.iterator(); resultListIterator.hasNext();) {
					TimedResult<Optional<Assignment>> result = resultListIterator.next();
					sumDuration += result.getDurationInMilliseconds();
				}

				// calculate average runtime
				long finalDuration = ((sumDuration * BenchmarkRunner.FACTOR) / (numberOfRuns));
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
			builder.append("Filename : ");
			builder.append(model.getFileName());
			builder.append("\n");
			builder.append(" Number of variables : ");
			builder.append(model.getFormula().getVariables().size());
			builder.append("\n");
			builder.append(" Solvable : ");
			builder.append(model.isSatisfiable());
			// builder.append(" | \n");
			categories[counter++] = builder.toString();
		}

		counter = 0;
		for (Iterator<TimedResult<Optional<Assignment>>> resultIterator = this.resultList.iterator(); resultIterator
				.hasNext();) {
			TimedResult<Optional<Assignment>> result = resultIterator.next();
			values[counter++] = ((double) result.getDurationInMilliseconds() / (double) BenchmarkRunner.FACTOR);
		}

		try {
			// pass parameters to BarChartFactory
			this.factory.setChartTitle(benchmarkType + " Benchmark : " + numberOfRuns + " runs");
			this.factory.setNames(names);
			this.factory.setCategories(categories);
			this.factory.setValues(values);
		} catch (NullPointerException | EmptyChartInputException e) {
			e.printStackTrace();
		}

		progressBarGui.close();
	}
}