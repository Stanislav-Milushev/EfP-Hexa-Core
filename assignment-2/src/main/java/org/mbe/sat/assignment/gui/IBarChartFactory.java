package org.mbe.sat.assignment.gui;

/**
 * @author Darwin Brambor
 *
 */
public interface IBarChartFactory {

	/**
	 * export the current results displayed by the corresponding
	 * {@link IBarChartGui} instance into a specific format (not yet implemented)
	 */
	public void requestExport();

	/**
	 * exit the current program exectuion
	 */
	public void requestExit();

	/**
	 * after all required data has been passed to the {@link IBarChartFactory} :
	 * create/show {@link IBarChartGui}
	 */
	public void showGui();

}
