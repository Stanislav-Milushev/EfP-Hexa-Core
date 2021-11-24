package org.mbe.sat.assignment.gui;

import org.mbe.sat.assignment.exceptions.EmptyChartInputException;

import javax.swing.*;
import java.util.Arrays;

/**
 * @author Darwin Brambor
 *
 */
public class BarChartFactory implements IBarChartFactory {

	/**
	 * counter to guarantee only a specific number of guis is currently active (here
	 * : one)
	 */
	private int chartCount;
	/**
	 * flag to indicate availability of the values-array
	 */
	private boolean valuesProvided;
	/**
	 * flag to indicate availability of the names-array
	 */
	private boolean namesProvided;
	/**
	 * flag to indicate availability of the categories-array
	 */
	private boolean categoriesProvided;
	/**
	 * flag to indicate availability of the chart title
	 */
	private boolean chartTitleProvided;

	/**
	 * holds a long-value for each category; number corresponds to the number of
	 * categories
	 */
	private double[] values;
	/**
	 * holds the level of difficulty as first array element
	 */
	private String[] names;
	/**
	 * holds a String for each value / category; number corresponds to the number of
	 * values
	 */
	private String[] categories;
	/**
	 * title of the current bar chart
	 */
	private String chartTitle;
	/**
	 * instance(s) of all currently active IBarChartGui instances
	 */
	private IBarChartGui chartGui;

	/**
	 * constructor; initialization of all attributes
	 */
	public BarChartFactory() {
		this.chartCount = 0;
		this.valuesProvided = false;
		this.namesProvided = false;
		this.categoriesProvided = false;
		this.chartTitleProvided = false;
		this.values = null;
		this.names = null;
		this.categories = null;
		this.chartGui = null;
		this.chartTitle = null;
	}

	/**
	 * @param input (non empty / non blank / non null)
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 * 
	 *                                  validates input for the {@link #chartTitle}
	 *                                  and sets {@link #chartTitleProvided} and
	 *                                  {@link #chartTitle} if successful
	 */
	public void setChartTitle(String input) throws NullPointerException, EmptyChartInputException {
		if (input == null) {
			throw new NullPointerException("title-input is null");
		}

		if (input.isEmpty() || input.equalsIgnoreCase(" ")) {
			throw new EmptyChartInputException("title-input is empty");
		}

		this.chartTitle = input;
		this.chartTitleProvided = true;
	}

	/**
	 * @param input (non null / non empty)
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 * 
	 *                                  validates input for the {@link #values} and
	 *                                  sets {@link #valuesProvided} and
	 *                                  {@link #values} if successful
	 */
	public void setValues(double[] input) throws NullPointerException, EmptyChartInputException {
		if (input == null) {
			throw new NullPointerException("value-input is null");
		}

		if (input.length == 0) {
			throw new EmptyChartInputException("value-input is empty");
		}

		this.values = Arrays.copyOf(input, input.length);
		this.valuesProvided = true;
	}

	/**
	 * @param input (non null / non empty)
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 * 
	 *                                  validates input for the {@link #names} and
	 *                                  sets {@link #namesProvided} and
	 *                                  {@link #names} if successful
	 */
	public void setNames(String[] input) throws NullPointerException, EmptyChartInputException {
		if (input == null) {
			throw new NullPointerException("names-input is null");
		}

		if (input.length == 0) {
			throw new EmptyChartInputException("names-input is empty");
		}

		this.names = Arrays.copyOf(input, input.length);
		this.namesProvided = true;
	}

	/**
	 * @param input (non null / non empty)
	 * @throws NullPointerException
	 * @throws EmptyChartInputException
	 * 
	 *                                  validates input for the {@link #categories}
	 *                                  and sets {@link #categoriesProvided} and
	 *                                  {@link #categories} if successful
	 */
	public void setCategories(String[] input) throws NullPointerException, EmptyChartInputException {
		if (input == null) {
			throw new NullPointerException("categories-input is null");
		}

		if (input.length == 0) {
			throw new EmptyChartInputException("categories-input is empty");
		}

		this.categories = Arrays.copyOf(input, input.length);
		this.categoriesProvided = true;
	}

	/**
	 * {@inheritDoc}; redirects the export request to export handler method
	 * {@link #handleExportRequest()}
	 */
	@Override
	public void requestExport() {
		this.handleExportRequest();
	}

	/**
	 * {@inheritDoc}; redirects the exit request to exit handler method
	 * {@link #handleExitRequest()}
	 */
	@Override
	public void requestExit() {
		this.handleExitRequest();
	}

	/**
	 * {@inheritDoc}; redirects the show request to request handler method
	 * {@link #handleShowRequest()}
	 */
	@Override
	public void showGui() {
		this.handleShowRequest();
	}

	/**
	 * handler method for {@link #showGui()}; performs multiple validation steps and
	 * creates {@link IBarChartGui} instance : -> check if {@link #namesProvided},
	 * {@link #categoriesProvided}, {@link #valuesProvided} and
	 * {@link #chartTitleProvided} are all true -> check if one value per category
	 * is present -> check if no other gui instance is currently running
	 */
	private void handleShowRequest() {
		if (this.namesProvided && this.categoriesProvided && this.valuesProvided && this.chartTitleProvided) {

//            if(!(this.values.length==this.names.length)){
//                UserCommunication.errorDialog("INPUT-ERROR : ","The values- and names-input must have the same size!");
//                return;
//            }

//			if (!(this.values.length == this.categories.length)) {
//				UserCommunication.errorDialog("INPUT-ERROR : ", "The values- and names-input must have the same size!");
//				return;
//			}

			if (this.values.length != (this.categories.length * this.names.length)) {
				UserCommunication.errorDialog("INPUT-ERROR : ",
						"The number of values must be the product of the category- and the names-length!");
			}

			if (this.chartCount > 0) {
				UserCommunication.errorDialog("DUPLICATE CHART ERROR : ", "A chart is already running");
				return;
			}

			this.chartGui = new BarChartGui(this, this.chartTitle, this.values, this.names, this.categories);
			this.chartCount++;

		} else {
			StringBuilder builder = new StringBuilder();

			if (!this.chartTitleProvided) {
				builder.append("chart title");
				builder.append("\n");
			}
			if (!this.namesProvided) {
				builder.append("chart names");
				builder.append("\n");
			}
			if (!this.valuesProvided) {
				builder.append("chart values");
				builder.append("\n");
			}
			if (!this.categoriesProvided) {
				builder.append("chart categories");
				builder.append("\n");
			}
			UserCommunication.errorDialog("ERROR : MISSING PARAMETERS",
					"Currently missing parameters : " + builder.toString());
		}
	}

	/**
	 * handler method for {@link #requestExport()} method (not yet implemented)
	 */
	private void handleExportRequest() {
		UserCommunication.confirmAction("UNDER CONSTRUCTION", "UNDER CONSTRUCTION");
	}

	/**
	 * handler method for {@link #requestExit()} method
	 */
	private void handleExitRequest() {
		if (UserCommunication.confirmAction("CONFIRM EXIT", "Do you want to exit the bar-chart?")) {
			closeGui();
		}
	}

	/**
	 * handler method for {@link #closeGui()} method; all data belonging to the
	 * current gui gets deleted
	 */
	private void closeGui() {
		this.chartGui.close();
		this.chartGui = null;
		this.chartTitle = null;
		this.chartTitleProvided = false;
		this.values = null;
		this.valuesProvided = false;
		this.names = null;
		this.namesProvided = false;
		this.categories = null;
		this.categoriesProvided = false;
		this.chartCount--;
	}

	/**
	 * usage : create BarChartFactory-Object -> pass title/value/name/category
	 * parameters -> factory.showGui()
	 */
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

		BarChartFactory factory = new BarChartFactory();

		String chartTitle = "Benchmark-Comparison";
		// String[] names={"Trivial","Easy","Medium","No Pain. No Gain"};
		String[] names = { "Runtime" };
		String[] categories = { "Trivial", "Easy", "Medium", "No Pain. No Gain" };
		double[] values = { 10.0, 42.0, 69.69, 420.0 };

		try {
			factory.setChartTitle(chartTitle);
		} catch (EmptyChartInputException e) {
			e.printStackTrace();
		}

		try {
			factory.setNames(names);
		} catch (EmptyChartInputException e) {
			e.printStackTrace();
		}

		try {
			factory.setCategories(categories);
		} catch (EmptyChartInputException e) {
			e.printStackTrace();
		}

		try {
			factory.setValues(values);
		} catch (EmptyChartInputException e) {
			e.printStackTrace();
		}

		factory.showGui();
	}
}
