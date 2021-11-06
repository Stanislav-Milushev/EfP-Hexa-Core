package org.mbe.sat.assignment.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
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

public class BarChartGui extends ApplicationFrame implements IBarChartGui{

    //values

    private IBarChartFactory factory;
    private String title;
    private String[] names;
    private double[] values;
    private String[] categories;

    //components

    private JFreeChart barChart;
    private ChartPanel chartPanel;
    private JPanel container;
    private JPanel subcontainer;
    private JButton exitButton;
    private JButton exportButton;

    public BarChartGui(IBarChartFactory factory, String chartTitle, double[] values, String[] names, String[] categories){
        super("Bar-Chart");

        this.factory=factory;
        this.title=chartTitle;
        this.values=values;
        this.names=names;
        this.categories=categories;

        this.init();
    }

    private void init(){

        //init components

        this.barChart= ChartFactory.createBarChart(this.title, "Benchmark", "Time",
                createDataset(),PlotOrientation.VERTICAL,
                true, true, false);

        this.chartPanel=new ChartPanel(this.barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 500));

        this.exitButton=new JButton("EXIT");
        this.exportButton=new JButton("EXPORT");

        this.container=new JPanel();
        container.setLayout(new BorderLayout());

        this.subcontainer=new JPanel();
        this.subcontainer.add(this.exportButton);
        this.subcontainer.add(this.exitButton);

        container.add(chartPanel,BorderLayout.CENTER);
        container.add(subcontainer,BorderLayout.SOUTH);

        setContentPane(container);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);

        //Add Listener

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

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                requestExit();
            }
        });
    }

    private CategoryDataset createDataset(){
        final DefaultCategoryDataset dataset=new DefaultCategoryDataset();

        /*
        reference :
        dataset.addValue(1.0, fiat, speed);
        dataset.addValue(3.0, fiat, userrating);
        dataset.addValue(5.0, fiat, millage);
        dataset.addValue(5.0, fiat, safety);

        dataset.addValue(5.0, audi, speed);
        dataset.addValue(6.0, audi, userrating);
        dataset.addValue(10.0, audi, millage);
        dataset.addValue(4.0, audi, safety);

        dataset.addValue(4.0, ford, speed);
        dataset.addValue(2.0, ford, userrating);
        dataset.addValue(3.0, ford, millage);
        dataset.addValue(6.0, ford, safety);

        dataset.addValue(4.0, bmw, speed);
        dataset.addValue(2.0, bmw, userrating);
        dataset.addValue(3.0, bmw, millage);
        dataset.addValue(6.0, bmw, safety);
         */

        for(int i=0; i<this.categories.length; i++){
            for(int j=0; j<this.names.length; j++){
                dataset.addValue(this.values[j],this.names[j],this.categories[i]);
            }
        }

        return dataset;
    }

    @Override
    public void requestExport() {
        factory.requestExport();
    }

    @Override
    public void requestExit() {
        factory.requestExit();
    }

    public void close(){
        this.dispose();
    }
}
