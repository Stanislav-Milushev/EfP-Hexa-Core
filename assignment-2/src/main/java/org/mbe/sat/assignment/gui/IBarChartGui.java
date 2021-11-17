package org.mbe.sat.assignment.gui;

/**
 * @author Darwin Brambor
 *
 */
public interface IBarChartGui {

	/**
	 * method to delegate the export request of the current diagram displayed by the
	 * {@link IBarChartGui} instance into a specific format forward to the
	 * {@link IBarChartFactory} instance
	 */
	public void requestExport();

	/**
	 * method to delegate the exit request of the {@link IBarChartGui} instance
	 * forward to the {@link IBarChartFactory} instance
	 */
	public void requestExit();

	/**
	 * method to enable the {@link IBarChartFactory} instance to close the current
	 * {@link IBarChartGui} instance
	 */
	void close();
}
