package org.mbe.sat.assignment.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author Darwin Brambor
 *
 */
public class BarChartGui extends ApplicationFrame implements IBarChartGui {

	// values

	/**
	 * {@link IBarChartFactory} instance to handle requests of this gui
	 */
	private IBarChartFactory factory;
	/**
	 * title of this gui
	 */
	private String title;
	/**
	 * level of difficulty of this gui
	 */
	private String[] names;
	/**
	 * values of categories / benchmark files ; for each element of {@link #values}
	 * one element of {@link #categories} is required
	 */
	private double[] values;
	/**
	 * names of categories / benchmark files ; for each element of {@link #values}
	 * one element of {@link #categories} is required
	 */
	private String[] categories;
	/**
	 * maximum number of bars shown in the bar chart at a time
	 */
	private static final int BARS_PER_PAGE = 8;
	/**
	 * pointer for the {@link #values} array and the {@link #categories} array to
	 * show the next / previous {@value #BARS_PER_PAGE} elements of the arrays as
	 * bar chart
	 */
	private int pointer;

	private int numComparedInstances;

	// components

	/**
	 * {@link JFreeChart} instance to display the next {@value #BARS_PER_PAGE}
	 * results
	 */
	private JFreeChart barChart;
	// private JFreeChart nextChart;

	/**
	 * container panel ({@link ChartPanel}) for the {@link #barChart} instance
	 */
	private ChartPanel chartPanel;
	/**
	 * content pane of this gui ({@link JFrame} extension)
	 */
	private JPanel container;
	/**
	 * container panel ({@link JPanel}) for the control elements
	 */
	private JPanel subcontainer;
	/**
	 * {@link JButton} for sending an exit request to the {@link #factory} instance
	 */
	private JButton exitButton;
	/**
	 * {@link JButton} for sending an export request to the {@link #factory}
	 * instance
	 */
	private JButton exportButton;
	/**
	 * {@link JButton} for displaying the next {@value #BARS_PER_PAGE} results in
	 * the {@link #barChart} (if available)
	 */
	private JButton nextButton;
	/**
	 * {@link JButton} for displaying the previous {@value #BARS_PER_PAGE} results
	 * in the {@link #barChart} (if available)
	 */
	private JButton prevButton;

	/**
	 * constructor ; receives
	 * {@link #factory},{@link #title},{@link #values},{@link #names},{@link #categories}
	 * and the {@link #pointer}
	 * 
	 * @param factory
	 * @param chartTitle
	 * @param values
	 * @param names
	 * @param categories
	 */
	public BarChartGui(IBarChartFactory factory, String chartTitle, double[] values, String[] names,
			String[] categories) {
		super("Statistics");

		this.factory = factory;
		this.title = chartTitle;
		this.values = values;
		this.names = names;
		this.categories = categories;

		this.pointer = 0;

		this.numComparedInstances = this.names.length;

		this.init();
	}

	/**
	 * initializes all gui elements and adds all required {@link ActionListener}s
	 * and {@link WindowListener}s
	 */
	private void init() {

		// init components

		this.barChart = ChartFactory.createBarChart3D(this.title, "benchmark-files", "t in miliseconds",
				createDataset(), PlotOrientation.VERTICAL, true, true, false);

		this.chartPanel = new ChartPanel(this.barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 650));

		this.exitButton = new JButton("EXIT");
		this.exportButton = new JButton("EXPORT");
		exportButton.setEnabled(false);
		this.nextButton = new JButton("NEXT ->");
		this.prevButton = new JButton("<- PREV");

		this.container = new JPanel();
		container.setLayout(new BorderLayout());

		this.subcontainer = new JPanel();
		this.subcontainer.add(prevButton);
		this.subcontainer.add(nextButton);

		this.subcontainer.add(this.exportButton);
		this.subcontainer.add(this.exitButton);

		container.add(chartPanel, BorderLayout.CENTER);
		container.add(subcontainer, BorderLayout.SOUTH);

		setContentPane(container);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

		// Add Listener

		this.exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				requestExit();
			}
		});

		this.exportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				requestExport();
			}
		});

		this.prevButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pointer -= BARS_PER_PAGE;
				if (pointer <= 0) {
					pointer = 0;
				}
				updateChart();
			}
		});

		this.nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pointer += BARS_PER_PAGE;
				if (pointer >= categories.length) {
					pointer -= BARS_PER_PAGE;
				}
				updateChart();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				requestExit();
			}
		});

//		CategoryPlot plot=this.barChart.getCategoryPlot();
//		BarRenderer renderer=(BarRenderer)plot.getRenderer();
//		renderer.setItemMargin(0.1);

		this.setResizable(false);

		// customizations

		// decrease space between bars of one category
		((BarRenderer) this.barChart.getCategoryPlot().getRenderer()).setItemMargin(0.0);

		// set font of heading
		this.barChart.getTitle().setFont(new Font("Arial", Font.PLAIN, 25));
	}

	/**
	 * updates the current {@link #barChart} instance to display the changed
	 * (next/previous) elements according to the current {@link #pointer} value
	 */
	private void updateChart() {
		barChart = ChartFactory.createBarChart3D(this.title, "benchmark-files", "t in miliseconds", createDataset(),
				PlotOrientation.VERTICAL, true, true, false);

		// customizations

		// decrease space between bars of one category
		((BarRenderer) this.barChart.getCategoryPlot().getRenderer()).setItemMargin(0.0);

		// set font of heading
		this.barChart.getTitle().setFont(new Font("Arial", Font.PLAIN, 25));

		chartPanel.setChart(barChart);
	}

	/**
	 * provides the new input for the next {@link #barChart} instance
	 * 
	 * @return CategoryDataset : input for the new {@link #barChart} instance to
	 *         display the changed (next/previous) elements according to the current
	 *         {@link #pointer} value
	 */
	private CategoryDataset createDataset() {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		/*
		 * reference : dataset.addValue(1.0, fiat, speed); dataset.addValue(3.0, fiat,
		 * userrating); dataset.addValue(5.0, fiat, millage); dataset.addValue(5.0,
		 * fiat, safety);
		 * 
		 * dataset.addValue(5.0, audi, speed); dataset.addValue(6.0, audi, userrating);
		 * dataset.addValue(10.0, audi, millage); dataset.addValue(4.0, audi, safety);
		 * 
		 * dataset.addValue(4.0, ford, speed); dataset.addValue(2.0, ford, userrating);
		 * dataset.addValue(3.0, ford, millage); dataset.addValue(6.0, ford, safety);
		 * 
		 * dataset.addValue(4.0, bmw, speed); dataset.addValue(2.0, bmw, userrating);
		 * dataset.addValue(3.0, bmw, millage); dataset.addValue(6.0, bmw, safety);
		 */

//        for(int i=0; i<this.categories.length; i++){
//            for(int j=0; j<this.names.length; j++){
//                dataset.addValue(this.values[i],this.names[j],this.categories[i]);
//            }
//        }

//		for (int i = 0; i < BARS_PER_PAGE; i++) {
//			if ((this.pointer + i) >= this.values.length) {
//				// this.pointer=0;
//				break;
//			}
//			dataset.addValue(this.values[this.pointer + i], this.names[0], this.categories[this.pointer + i]);
//		}

		int index = 0;
		int nameCounter = 0;

		for (int i = 0; i < BARS_PER_PAGE; i++) {
			if ((this.pointer + i) >= this.categories.length) {
				break;
			}

			for (int j = 0; j < this.numComparedInstances; j++) {
				dataset.addValue(this.values[index++], this.names[nameCounter++], this.categories[i + this.pointer]);
				nameCounter %= this.names.length;
			}
		}

		return dataset;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestExport() {
		factory.requestExport();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestExit() {
		factory.requestExit();
	}

	/**
	 * {@inheritDoc}
	 */
	public void close() {
		this.dispose();
	}

}
