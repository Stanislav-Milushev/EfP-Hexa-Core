package org.mbe.sat.assignment.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JPanel;

import org.mbe.sat.api.solver.ISolver;
import org.mbe.sat.core.model.Assignment;
import org.mbe.sat.core.model.formula.CnfFormula;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import org.mbe.sat.assignment.gui.InitDialogPanel.Difficulty;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Dimension;

public class InitDialogPanel extends JPanel implements IInitDialogPanel {
	private JLabel timeoutValueLabel;
	private JLabel runValueLabel;
	private JSlider timeoutSlider;
	private JSlider runSlider;
	private JComboBox difficultyCombobox;
	private JCheckBox baseCheckbox;
	private JCheckBox dpCheckbox;
	private JCheckBox solver3Checkbox;
	private JCheckBox solver4Checkbox;

	private Difficulty diff;
	private boolean baseSolverSelected;
	private boolean dpSolverSelected;
	private boolean solver3Selected;
	private boolean solver4Selected;
	private int numberOfRuns;
	private int timeout;
	
	public enum Difficulty {
		TRIVIAL, EASY, MEDIUM, HARD, INSANE
	}

	private void initValues() {
		this.diff = Difficulty.TRIVIAL;
		this.baseSolverSelected = this.baseCheckbox.isSelected();
		this.dpSolverSelected = this.baseCheckbox.isSelected();
		this.solver3Selected = this.solver3Checkbox.isSelected();
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

		difficultyCombobox = new JComboBox();
		difficultyCombobox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				diff = (Difficulty) difficultyCombobox.getSelectedItem();
			}
		});
		difficultyCombobox.setModel(new DefaultComboBoxModel(Difficulty.values()));
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

		solver3Checkbox = new JCheckBox("Solver 3");
		solver3Checkbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				solver3Selected = solver3Checkbox.isSelected();
			}
		});
		solver3Checkbox.setEnabled(false);
		panel_2.add(solver3Checkbox);
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
		runSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				runValueLabel.setText(runSlider.getValue() + " runs");
				numberOfRuns = runSlider.getValue();
			}
		});
		runSlider.setValue(5);
		runSlider.setMinorTickSpacing(1);
		runSlider.setMajorTickSpacing(10);
		runSlider.setSnapToTicks(true);
		runSlider.setPaintTicks(true);
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
		timeoutSlider.setValue(5);
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

	@Override
	public Difficulty getDifficulty() {
		return this.diff;
	}

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
			resultList.add(SOLVER_3_STRING);
		}

		if (this.solver4Selected) {
			resultList.add(SOLVER_4_STRING);
		}

		return resultList;
	}

	@Override
	public int getNumberOfRuns() {
		return this.numberOfRuns;
	}

	@Override
	public boolean validateSelectedSolvers() {
		if(this.getSelectedSolvers().size()==0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean validateNumberOfRuns() {
		if(this.numberOfRuns==0) {
			return false;
		}
		return true;
	}

	@Override
	public boolean validateTimeout() {
		if(this.timeout==0) {
			return false;
		}
		return true;
	}

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
