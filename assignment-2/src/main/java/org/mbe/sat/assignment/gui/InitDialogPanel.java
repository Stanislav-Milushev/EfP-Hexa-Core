package org.mbe.sat.assignment.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import org.mbe.sat.assignment.solver.BaseSolver;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Dimension;

/**
 * @author User Darwin Brambor
 *
 */
@SuppressWarnings("serial")
public class InitDialogPanel extends JPanel implements IInitDialogPanel {
	/**
	 * timeout label
	 */
	private JLabel timeoutValueLabel;
	/**
	 * run value label
	 */
	private JLabel runValueLabel;
	/**
	 * can be changed by the user to specify the timeout for the current benchmark
	 */
	private JSlider timeoutSlider;
	/**
	 * can be changed by the user to specify the timeout for the current benchmark
	 */
	private JSlider runSlider;
	/**
	 * enables the user to select from all available {@link Difficulty difficulties}
	 */
	private JComboBox<Object> difficultyCombobox;
	/**
	 * enables the user to include the {@link BaseSolver base-solver} in the current
	 * benchmark if selected
	 */
	private JCheckBox baseCheckbox;
	/**
	 * enables the user to include the DP-solver in the current benchmark if
	 * selected
	 */
	private JCheckBox dpCheckbox;
	/**
	 * placeholder
	 */
	private JCheckBox dpllCheckbox;
	/**
	 * placeholder
	 */
	private JCheckBox solver4Checkbox;

	/**
	 * {@link Difficulty difficulty} selected by the user
	 */
	private Difficulty diff;
	/**
	 * boolean value corresponds to the current selection state of
	 * {@link #baseCheckbox}
	 */
	private boolean baseSolverSelected;
	/**
	 * boolean value corresponds to the current selection state of
	 * {@link #dpCheckbox}
	 */
	private boolean dpSolverSelected;
	/**
	 * boolean value corresponds to the current selection state of
	 * {@link #dpllCheckbox}
	 */
	private boolean solver3Selected;
	/**
	 * boolean value corresponds to the current selection state of
	 * {@link #solver4Checkbox}
	 */
	private boolean solver4Selected;
	/**
	 * number of runs selected by the user using {@link #runSlider}
	 */
	private int numberOfRuns;
	/**
	 * timeout selected by the user using {@link #timeoutSlider}
	 */
	private int timeout;

	public enum Difficulty {
		TRIVIAL, EASY, MEDIUM, HARD, INSANE
	}

	/**
	 * Initializes all relevant values
	 */
	private void initValues() {
		this.diff = Difficulty.TRIVIAL;
		this.baseSolverSelected = this.baseCheckbox.isSelected();
		this.dpSolverSelected = this.baseCheckbox.isSelected();
		this.solver3Selected = this.dpllCheckbox.isSelected();
		this.solver4Selected = this.solver4Checkbox.isSelected();
		this.numberOfRuns = this.runSlider.getValue();
		this.timeout = this.timeoutSlider.getValue();
	}

	/**
	 * Create the panel.
	 */
	public InitDialogPanel() {
		super();
		setPreferredSize(new Dimension(400, 300));
		setLayout(new GridLayout(4, 0, 0, 0));

		JPanel panel_1 = new JPanel();
		add(panel_1);

		JLabel lblNewLabel = new JLabel("Difficulty");

		difficultyCombobox = new JComboBox<Object>();
		difficultyCombobox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				diff = (Difficulty) difficultyCombobox.getSelectedItem();
			}
		});
		difficultyCombobox.setModel(new DefaultComboBoxModel<Object>(Difficulty.values()));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addComponent(lblNewLabel).addGap(18)
						.addComponent(difficultyCombobox, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(49, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup().addGap(30).addComponent(lblNewLabel))
						.addGroup(gl_panel_1.createSequentialGroup().addGap(26).addComponent(difficultyCombobox,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(27, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);

		JPanel panel_2 = new JPanel();
		add(panel_2);

		baseCheckbox = new JCheckBox("Base-Solver");
		baseCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				baseSolverSelected = baseCheckbox.isSelected();
			}
		});
		baseCheckbox.setSelected(true);

		dpCheckbox = new JCheckBox("Dp-Solver");
		dpCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				dpSolverSelected = dpCheckbox.isSelected();
			}
		});
		dpCheckbox.setSelected(true);
		panel_2.setLayout(new GridLayout(2, 2, 0, 0));
		panel_2.add(baseCheckbox);

		dpllCheckbox = new JCheckBox("DPLL-Solver");
		dpllCheckbox.setSelected(true);
		dpllCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				solver3Selected = dpllCheckbox.isSelected();
			}
		});
		dpllCheckbox.setEnabled(true);
		panel_2.add(dpllCheckbox);
		panel_2.add(dpCheckbox);

		solver4Checkbox = new JCheckBox("Solver 4");
		solver4Checkbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				solver4Selected = solver4Checkbox.isSelected();
			}
		});
		solver4Checkbox.setEnabled(false);
		panel_2.add(solver4Checkbox);

		JPanel panel_3 = new JPanel();
		add(panel_3);
		panel_3.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panel = new JPanel();
		panel_3.add(panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_1 = new JLabel("Number of runs : ");
		panel.add(lblNewLabel_1);

		runValueLabel = new JLabel("New label");
		panel.add(runValueLabel);

		runSlider = new JSlider();
		runSlider.setPaintTicks(true);
		runSlider.setMaximum(1000);
		runSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				runValueLabel.setText(runSlider.getValue() + " runs");
				numberOfRuns = runSlider.getValue();
			}
		});
		runSlider.setValue(10);
		runSlider.setMinorTickSpacing(1);
		runSlider.setMajorTickSpacing(100);
		runSlider.setSnapToTicks(true);
		runSlider.setPaintLabels(true);
		panel_3.add(runSlider);

		JPanel panel_3_1 = new JPanel();
		add(panel_3_1);
		panel_3_1.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panel_4 = new JPanel();
		panel_3_1.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_1_1 = new JLabel("Timeout in minutes : ");
		panel_4.add(lblNewLabel_1_1);

		timeoutValueLabel = new JLabel("New label");
		panel_4.add(timeoutValueLabel);

		timeoutSlider = new JSlider();
		timeoutSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				timeoutValueLabel.setText(timeoutSlider.getValue() + " minutes");
				timeout = timeoutSlider.getValue();
			}
		});
		timeoutSlider.setValue(15);
		timeoutSlider.setSnapToTicks(true);
		timeoutSlider.setPaintTicks(true);
		timeoutSlider.setPaintLabels(true);
		timeoutSlider.setMinorTickSpacing(1);
		timeoutSlider.setMaximum(60);
		timeoutSlider.setMajorTickSpacing(5);
		panel_3_1.add(timeoutSlider);

		runValueLabel.setText(runSlider.getValue() + " runs");
		timeoutValueLabel.setText(timeoutSlider.getValue() + " minutes");

		this.initValues();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Difficulty getDifficulty() {
		return this.diff;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getSelectedSolvers() {
		List<String> resultList = new ArrayList<>();

		if (this.baseSolverSelected) {
			resultList.add(BASE_SOLVER_STRING);
		}

		if (this.dpSolverSelected) {
			resultList.add(DP_SOLVER_STRING);
		}

		if (this.solver3Selected) {
			resultList.add(DPLL_SOLVER);
		}

		if (this.solver4Selected) {
			resultList.add(SOLVER_4_STRING);
		}

		return resultList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumberOfRuns() {
		return this.numberOfRuns;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateSelectedSolvers() {
		if (this.getSelectedSolvers().size() == 0) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateNumberOfRuns() {
		if (this.numberOfRuns == 0) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean validateTimeout() {
		if (this.timeout == 0) {
			return false;
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTimeout() {
		return this.timeout;
	}

//	public static void main(String[] args) {
//		InitDialogPanel panel = new InitDialogPanel();
//		JOptionPane.showConfirmDialog(null, panel, "CONFIGURE BENCHMARK", JOptionPane.YES_NO_CANCEL_OPTION,
//				JOptionPane.INFORMATION_MESSAGE);
//		
//		System.out.println("");
//	}
}
