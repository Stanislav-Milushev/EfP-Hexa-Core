package org.mbe.sat.assignment.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

/**
 * @author User Darwin Brambor
 *
 */
public class ProgressBarGui extends JFrame {

	private JPanel contentPane;
	/**
	 * displays the value of the current execution run/round
	 */
	private JProgressBar roundProgressBar;
	/**
	 * displays the value of the current {@link ISolver solver-instance}
	 */
	private JProgressBar solverProgressBar;
	/**
	 * displays the value of the current benchmark file
	 */
	private JProgressBar benchmarkProgressBar;
	/**
	 * displays the numeric value of the {@link #roundProgressBar}
	 */
	private JLabel roundProgressLabel;
	/**
	 * displays the numeric value of the {@link #solverProgressBar}
	 */
	private JLabel solverProgressLabel;
	/**
	 * displays the numeric value of the {@link #benchmarkProgressBar}
	 */
	private JLabel benchmarkProgressLabel;

	/**
	 * max value for {@link #benchmarkProgressBar}
	 */
	private int maxNumBenchmarks;
	/**
	 * max value for {@link #solverProgressBar}
	 */
	private int maxNumSolvers;
	/**
	 * max value for {@link #roundProgressBar}
	 */
	private int maxNumRuns;

//	/**
//	 * Launch the application.
//	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ProgressBarGui frame = new ProgressBarGui(0,0,0);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public ProgressBarGui(int maxNumBenchmarks, int maxNumSolvers, int maxNumRuns) {

		this.maxNumBenchmarks = maxNumBenchmarks;
		this.maxNumSolvers = maxNumSolvers;
		this.maxNumRuns = maxNumRuns;

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 600, 180);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(6, 0, 0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_2 = new JLabel("round progress...");
		panel.add(lblNewLabel_2);

		roundProgressLabel = new JLabel("...%");
		panel.add(roundProgressLabel);

		roundProgressBar = new JProgressBar(0, this.maxNumRuns);
		contentPane.add(roundProgressBar);

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_1 = new JLabel("solver progress ...");
		panel_1.add(lblNewLabel_1);

		solverProgressLabel = new JLabel("...%");
		panel_1.add(solverProgressLabel);

		solverProgressBar = new JProgressBar(0, this.maxNumSolvers);
		contentPane.add(solverProgressBar);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel = new JLabel("processing benchmark files ...");
		panel_2.add(lblNewLabel);

		benchmarkProgressLabel = new JLabel("...%");
		panel_2.add(benchmarkProgressLabel);

		benchmarkProgressBar = new JProgressBar(0, this.maxNumBenchmarks);
		contentPane.add(benchmarkProgressBar);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * @param newValue becomes the new value of {@link #roundProgressBar}
	 */
	public void setNewRunValue(int newValue) {
		this.roundProgressBar.setValue(newValue);
		this.roundProgressLabel.setText((int) (this.roundProgressBar.getPercentComplete() * 100) + "%");
	}

	/**
	 * @param newValue becomes the new value of {@link #benchmarkProgressBar}
	 */
	public void setNewBenchmarkValue(int newValue) {
		this.benchmarkProgressBar.setValue(newValue);
		this.benchmarkProgressLabel.setText((int) (this.benchmarkProgressBar.getPercentComplete() * 100) + "%");
	}

	/**
	 * @param newValue becomes the new value of {@link #solverProgressBar}
	 */
	public void setNewSolverValue(int newValue) {
		this.solverProgressBar.setValue(newValue);
		this.solverProgressLabel.setText((int) (this.solverProgressBar.getPercentComplete() * 100) + "%");
	}

	/**
	 * closes the gui
	 */
	public void close() {
		this.dispose();
	}
}
