package org.mbe.sat.assignment.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.JToggleButton;
import java.awt.Insets;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

//TODO : add validation for max value
//TODO : add validation for min value

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
	 * stores value retrieved by an {@link IInitDialogPanel} instance using the
	 * {@link IInitDialogPanel#isRangeIncluded()} method
	 */
	protected boolean include;
	/**
	 * timeout selected by the user using {@link #timeoutSlider}
	 */
	private int timeout;
	/**
	 * optional gui element to decrement the selected number of runs
	 */
	private JButton decrementRuns;
	/**
	 * optional gui element to increment the selected number of runs
	 */
	private JButton incrementRuns;
	/**
	 * optional gui element to decrement the selected timeout value
	 */
	private JButton decrementTimeout;
	/**
	 * optional gui element to increment the selected timeout value
	 */
	private JButton incrementTimeout;
	/**
	 * gui element to specify the lower bound of the user defined range
	 */
	private JSpinner minSpinner;
	/**
	 * gui element to specify the upper bound of the user defined range
	 */
	private JSpinner maxSpinner;
	/**
	 * enabled/disables all gui elements required for specifying an user defined
	 * selection of the variable range by calling {@link #enableRange()} and
	 * {@link #disableRange()}
	 */
	private JCheckBox selectionCheckbox;
	/**
	 * gui element to specify whether the user selected range shall be included or
	 * excluded
	 */
	private JButton includeExcludeButton;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton timeoutEnabledRadioButton;
	private JRadioButton timeoutDisabledRadioButton;
	private JSlider fileSlider;
	protected int numberOfFiles;

	/**
	 * all possible difficulty levels
	 */
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
		this.include = true;
		this.numberOfFiles=this.fileSlider.getValue();
	}

	/**
	 * Create the panel.
	 */
	public InitDialogPanel() {
		super();
		setPreferredSize(new Dimension(435, 504));
		setLayout(new GridLayout(5, 0, 0, 0));

		JPanel panel_1 = new JPanel();
		add(panel_1);

		JPanel panel_9 = new JPanel();
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_2 = new JLabel("Benchmark Configuration");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panel_13 = new JPanel();

		JPanel panel_14 = new JPanel();
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(gl_panel_9.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_9.createSequentialGroup().addGroup(gl_panel_9.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_14, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(panel_13, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
						.addComponent(lblNewLabel_2, Alignment.LEADING)).addContainerGap()));
		gl_panel_9.setVerticalGroup(gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup().addComponent(lblNewLabel_2).addGap(18)
						.addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_14, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE).addContainerGap()));
		panel_14.setLayout(new GridLayout(0, 2, 0, 0));

		timeoutEnabledRadioButton = new JRadioButton("Timeout enabled");
		timeoutEnabledRadioButton.setSelected(true);
		buttonGroup.add(timeoutEnabledRadioButton);
		panel_14.add(timeoutEnabledRadioButton);

		timeoutDisabledRadioButton = new JRadioButton("Timeout disabled");
		buttonGroup.add(timeoutDisabledRadioButton);
		panel_14.add(timeoutDisabledRadioButton);
		panel_13.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel = new JLabel("Difficulty");
		panel_13.add(lblNewLabel);

		difficultyCombobox = new JComboBox<Object>();
		panel_13.add(difficultyCombobox);
		difficultyCombobox.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				diff = (Difficulty) difficultyCombobox.getSelectedItem();
			}
		});
		difficultyCombobox.setModel(new DefaultComboBoxModel<Object>(Difficulty.values()));
		difficultyCombobox.setSelectedIndex(0);
		panel_9.setLayout(gl_panel_9);
		panel_1.add(panel_9);

		JPanel panel_7 = new JPanel();
		panel_1.add(panel_7);
		panel_7.setLayout(new GridLayout(0, 1, 0, 0));

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"RANGE CONFIG", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_7.add(panel_10);
		panel_10.setLayout(new GridLayout(3, 1, 0, 0));

		JPanel panel_8 = new JPanel();
		panel_10.add(panel_8);
		panel_8.setLayout(new GridLayout(0, 2, 0, 0));

		selectionCheckbox = new JCheckBox("Select range");
		selectionCheckbox.setMargin(new Insets(2, 0, 2, 2));
		panel_8.add(selectionCheckbox);
		selectionCheckbox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (selectionCheckbox.isSelected()) {
					enableRange();
				} else {
					disableRange();
				}
			}
		});
		selectionCheckbox.setHorizontalAlignment(SwingConstants.LEFT);

		includeExcludeButton = new JButton("INCLUDE");
		includeExcludeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (includeExcludeButton.getText().equalsIgnoreCase("INCLUDE")) {
					includeExcludeButton.setText("EXCLUDE");
					include = false;
				} else if (includeExcludeButton.getText().equalsIgnoreCase("EXCLUDE")) {
					includeExcludeButton.setText("INCLUDE");
					include = true;
				}
			}
		});
		panel_8.add(includeExcludeButton);

		JPanel panel_11 = new JPanel();
		panel_10.add(panel_11);
		panel_11.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_3 = new JLabel("Min. num. variables");
		panel_11.add(lblNewLabel_3);

		minSpinner = new JSpinner();
		panel_11.add(minSpinner);
		minSpinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
		minSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if ((int) minSpinner.getValue() > (int) maxSpinner.getValue()) {
					maxSpinner.setValue(minSpinner.getValue());
				}
			}
		});
		minSpinner.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

		JPanel panel_12 = new JPanel();
		panel_10.add(panel_12);
		panel_12.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblNewLabel_4 = new JLabel("Max. num. variables");
		panel_12.add(lblNewLabel_4);

		maxSpinner = new JSpinner();
		panel_12.add(maxSpinner);
		maxSpinner.setFont(new Font("Tahoma", Font.PLAIN, 14));
		maxSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if ((int) minSpinner.getValue() > (int) maxSpinner.getValue()) {
					minSpinner.setValue(maxSpinner.getValue());
				}
			}
		});
		maxSpinner.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"SOLVER CONFIG", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
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
		panel_3.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"RUN CONFIG", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(panel_3);
		panel_3.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panel = new JPanel();
		panel_3.add(panel);
		panel.setLayout(new GridLayout(0, 3, 0, 0));

		JLabel lblNewLabel_1 = new JLabel("Number of runs : ");
		panel.add(lblNewLabel_1);

		runValueLabel = new JLabel("New label");
		panel.add(runValueLabel);

		runSlider = new JSlider();
		runSlider.setPaintTicks(true);
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
		runSlider.setPaintLabels(true);
		panel_3.add(runSlider);

		JPanel panel_15 = new JPanel();
		panel_15.setBorder(
				new TitledBorder(null, "BENCHMARK FILE CONFIG", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_15);
		panel_15.setLayout(new GridLayout(2, 1, 0, 0));

		JPanel panel_16 = new JPanel();
		panel_15.add(panel_16);
		panel_16.setLayout(new GridLayout(0, 3, 0, 0));

		JLabel lblNewLabel_1_2 = new JLabel("Max. num. of files :");
		panel_16.add(lblNewLabel_1_2);

		JLabel fileValueLabel = new JLabel("5 files");
		panel_16.add(fileValueLabel);

		JPanel panel_6_1 = new JPanel();
		panel_16.add(panel_6_1);
		panel_6_1.setLayout(new GridLayout(0, 2, 0, 0));

		JButton decrementFiles = new JButton("<");
		decrementFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSlider.setValue(fileSlider.getValue() == fileSlider.getMinimum() ? fileSlider.getMinimum()
						: (fileSlider.getValue() - 1));
			}
		});
		panel_6_1.add(decrementFiles);

		JButton incrementFiles = new JButton(">");
		incrementFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileSlider.setValue(fileSlider.getValue() == fileSlider.getMaximum() ? fileSlider.getMaximum()
						: (fileSlider.getValue() + 1));
			}
		});
		panel_6_1.add(incrementFiles);

		fileSlider = new JSlider();
		fileSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fileValueLabel.setText(fileSlider.getValue() + " files");
				numberOfFiles = fileSlider.getValue();
			}
		});
		fileSlider.setValue(0);
		fileSlider.setSnapToTicks(true);
		fileSlider.setPaintTicks(true);
		fileSlider.setPaintLabels(true);
		fileSlider.setMinorTickSpacing(1);
		fileSlider.setMajorTickSpacing(10);
		panel_15.add(fileSlider);

		JPanel panel_3_1 = new JPanel();
		panel_3_1.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"TIMEOUT CONFIG", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		add(panel_3_1);
		panel_3_1.setLayout(new GridLayout(2, 0, 0, 0));

		JPanel panel_4 = new JPanel();
		panel_3_1.add(panel_4);
		panel_4.setLayout(new GridLayout(0, 3, 0, 0));

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

		JPanel panel_6 = new JPanel();
		panel.add(panel_6);
		panel_6.setLayout(new GridLayout(0, 2, 0, 0));

		decrementRuns = new JButton("<");
		decrementRuns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runSlider.setValue(runSlider.getValue() == runSlider.getMinimum() ? runSlider.getMinimum()
						: (runSlider.getValue() - 1));
			}
		});
		panel_6.add(decrementRuns);

		incrementRuns = new JButton(">");
		incrementRuns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				runSlider.setValue(runSlider.getValue() == runSlider.getMaximum() ? runSlider.getMaximum()
						: (runSlider.getValue() + 1));
			}
		});
		panel_6.add(incrementRuns);
		timeoutValueLabel.setText(timeoutSlider.getValue() + " minutes");

		JPanel panel_5 = new JPanel();
		panel_4.add(panel_5);
		panel_5.setLayout(new GridLayout(0, 2, 0, 0));

		decrementTimeout = new JButton("<");
		decrementTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeoutSlider
						.setValue(timeoutSlider.getValue() == timeoutSlider.getMinimum() ? timeoutSlider.getMinimum()
								: (timeoutSlider.getValue() - 1));
			}
		});
		panel_5.add(decrementTimeout);

		incrementTimeout = new JButton(">");
		incrementTimeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				timeoutSlider
						.setValue(timeoutSlider.getValue() == timeoutSlider.getMaximum() ? timeoutSlider.getMaximum()
								: (timeoutSlider.getValue() + 1));
			}
		});
		panel_5.add(incrementTimeout);

		if (selectionCheckbox.isSelected()) {
			enableRange();
		} else {
			disableRange();
		}

		this.initValues();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMinNumOfVariables() throws NullPointerException {
		return (int) minSpinner.getValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxNumOfVariables() throws NullPointerException {
		return (int) maxSpinner.getValue();
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

	/**
	 * enables all gui elements needed to specify an user selected number of
	 * benchmark files according to their number of variables
	 */
	private void enableRange() {
		minSpinner.setEnabled(true);
		maxSpinner.setEnabled(true);
		includeExcludeButton.setEnabled(true);
	}

	/**
	 * disables all gui elements needed to specify an user selected number of
	 * benchmark files according to their number of variables
	 */
	private void disableRange() {
		minSpinner.setEnabled(false);
		maxSpinner.setEnabled(false);
		includeExcludeButton.setEnabled(false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSelectionActive() {
		return this.selectionCheckbox.isSelected();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isRangeIncluded() {
		return this.include;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTimeoutEnabled() {
		return this.timeoutEnabledRadioButton.isSelected();
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public int getMaxNumOfFiles() {
		return this.numberOfFiles;
	}
}
