package org.mbe.sat.assignment.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;
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
@SuppressWarnings("serial")
public class BarChartGui extends ApplicationFrame implements IBarChartGui {

	// values

	private static final double TIMEOUT_VALUE = 0.00001;
	/**
	 * maximum number of bars shown in the bar chart at a time
	 */
	private static final int BARS_PER_PAGE = 4;
	/**
	 * space between two categories
	 */
	private static final double DEFAULT_CATEGORY_MARGIN = 0.2;
	/**
	 * space between two items
	 */
	private static final double DEFAULT_ITEM_MARGIN = 0.1;
	/**
	 * default font type
	 */
	private static final int DEFAULT_FONT_TYPE = Font.PLAIN;
	/**
	 * default font size for the y-axis label
	 */
	private static final int DEFAULT_RANGE_LABEL_FONT_SIZE = 20;
	/**
	 * default font size for the y-axis value-labels
	 */
	private static final int DEFAULT_RANGE_TICK_LABEL_FONT_SIZE = 15;
	/**
	 * flag to tell if URLs shall be shown in the diagram or not
	 */
	private static final boolean DEFAULT_URL = false;
	/**
	 * flag to tell if tooltips shall be shown in the diagram or not
	 */
	private static final boolean DEFAULT_TOOLTIP = true;
	/**
	 * flag to tell if the legend shall be shown in the diagram or not
	 */
	private static final boolean DEFAULT_LEGEND = true;
	/**
	 * default plot orientation
	 */
	private static final PlotOrientation DEFAULT_PLOT_ORIENTATION = PlotOrientation.VERTICAL;
	/**
	 * default fixed chart panel height
	 */
	private static final int DEFAULT_CHART_PANEL_HEIGHT = 650;
	/**
	 * default fixed chart panel width
	 */
	private static final int DEFAULT_CHART_PANEL_WIDTH = 1400;
	/**
	 * number of lines to show an potentially multiline category name
	 */
	private static final int DEFAULT_LINES_PER_CATEGORY = 2;
	/**
	 * default axis label font size
	 */
	private static final int DEFAULT_AXIS_LABEL_FONT_SIZE = 25;
	/**
	 * default font
	 */
	private static final String DEFAULT_FONT_VALUE = "Arial";
	/**
	 * description of the category axis
	 */
	private static final String CATEGORY_AXIS_TEXT = "benchmark-files";
	/**
	 * description of the value axis
	 */
	private static final String VALUE_AXIS_TEXT = "t in miliseconds";
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
	 * pointer for the {@link #values} array and the {@link #categories} array to
	 * show the next / previous {@value #BARS_PER_PAGE} elements of the arrays as
	 * bar chart
	 */
	private int pointer;
	/**
	 * number of compared instances for each {@link #categories category}
	 */
	@SuppressWarnings("unused")
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

		this.barChart = ChartFactory.createBarChart3D(this.title, BarChartGui.CATEGORY_AXIS_TEXT,
				BarChartGui.VALUE_AXIS_TEXT, createDataset(), BarChartGui.DEFAULT_PLOT_ORIENTATION,
				BarChartGui.DEFAULT_LEGEND, BarChartGui.DEFAULT_TOOLTIP, BarChartGui.DEFAULT_URL);

		chartPanel = new ChartPanel(this.barChart);
		chartPanel.setPreferredSize(
				new java.awt.Dimension(BarChartGui.DEFAULT_CHART_PANEL_WIDTH, BarChartGui.DEFAULT_CHART_PANEL_HEIGHT));

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

				if (pointer < 0) {
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

		customizeChart();

		// finish
		this.setResizable(false);
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}

	/**
	 * customize the {@link #barChart} to fulfill certain criteria (e.g. logarithmic
	 * y-axis)
	 */
	private void customizeChart() {
		// customizations

		// decrease space between bars of one category
		((BarRenderer) this.barChart.getCategoryPlot().getRenderer()).setItemMargin(BarChartGui.DEFAULT_ITEM_MARGIN);

		// set logarithmic value axis
		LogAxis logAxis = new LogAxis();
		logAxis.setLabel(VALUE_AXIS_TEXT);
		logAxis.setSmallestValue(BarChartGui.TIMEOUT_VALUE);
		this.barChart.getCategoryPlot().setRangeAxis(logAxis);
		//this.barChart.getCategoryPlot().getRangeAxis().setNegativeArrowVisible(true);
		
		this.barChart.setAntiAlias(true);
//		this.barChart.getCategoryPlot().getDomainAxis()
//				.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6));
		this.barChart.getCategoryPlot().getDomainAxis().setCategoryMargin(BarChartGui.DEFAULT_CATEGORY_MARGIN);

		// category axis text customization
		// this.barChart.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		this.barChart.getCategoryPlot().getDomainAxis()
				.setMaximumCategoryLabelLines(BarChartGui.DEFAULT_LINES_PER_CATEGORY);
		this.barChart.getCategoryPlot().getRangeAxis().setTickLabelFont(new Font(BarChartGui.DEFAULT_FONT_VALUE,
				BarChartGui.DEFAULT_FONT_TYPE, BarChartGui.DEFAULT_RANGE_TICK_LABEL_FONT_SIZE));
		this.barChart.getCategoryPlot().getRangeAxis().setLabelFont(new Font(BarChartGui.DEFAULT_FONT_VALUE,
				BarChartGui.DEFAULT_FONT_TYPE, BarChartGui.DEFAULT_RANGE_LABEL_FONT_SIZE));

		for (int i = 0; i < this.barChart.getCategoryPlot().getLegendItems().getItemCount(); i++) {
			this.barChart.getCategoryPlot().getRenderer().setSeriesItemLabelGenerator(i,
					new StandardCategoryItemLabelGenerator());
			this.barChart.getCategoryPlot().getRenderer().setSeriesItemLabelsVisible(i, true);
			this.barChart.getCategoryPlot().getRenderer()
					.setBaseItemLabelFont(new Font(BarChartGui.DEFAULT_FONT_VALUE, Font.ROMAN_BASELINE, 12));

		}

//		this.barChart.getCategoryPlot().getRenderer().setBasePositiveItemLabelPosition(
//				new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_LEFT));

		this.barChart.getCategoryPlot().getRenderer().setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_LEFT, TextAnchor.BOTTOM_LEFT, (-Math.PI / 4)));

		// set font of heading
		this.barChart.getTitle().setFont(new Font(BarChartGui.DEFAULT_FONT_VALUE, BarChartGui.DEFAULT_FONT_TYPE,
				BarChartGui.DEFAULT_AXIS_LABEL_FONT_SIZE));

		// chartPanel customization
		chartPanel.setMaximumDrawHeight(BarChartGui.DEFAULT_CHART_PANEL_HEIGHT);
		chartPanel.setMaximumDrawWidth(BarChartGui.DEFAULT_CHART_PANEL_WIDTH);
	}

	/**
	 * updates the current {@link #barChart} instance to display the changed
	 * (next/previous) elements according to the current {@link #pointer} value
	 */
	private void updateChart() {
		barChart = ChartFactory.createBarChart3D(this.title, BarChartGui.CATEGORY_AXIS_TEXT,
				BarChartGui.VALUE_AXIS_TEXT, createDataset(), BarChartGui.DEFAULT_PLOT_ORIENTATION,
				BarChartGui.DEFAULT_LEGEND, BarChartGui.DEFAULT_TOOLTIP, BarChartGui.DEFAULT_URL);
		customizeChart();
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

		int valueIndex = this.pointer * this.names.length;

		for (int i = 0; i < BARS_PER_PAGE; i++) {
			for (int j = 0; j < names.length; j++) {
				if (valueIndex >= values.length) {
					break;
				}
				
				if(values[valueIndex]==0) {
					dataset.addValue(BarChartGui.TIMEOUT_VALUE, names[j], categories[this.pointer+i]);
				}else {
					dataset.addValue(values[valueIndex], names[j], categories[this.pointer + i]);
				}
				valueIndex++;
				
				//dataset.addValue(values[valueIndex++], names[j], categories[this.pointer + i]);
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
