package org.mbe.sat.assignment.gui;

import javax.swing.*;

/**
 * @author User Darwin Brambor
 *
 */
public class UserCommunication {

	/**
	 * general method to display an {@link JOptionPane} error dialog with a specific
	 * title and textual content to inform the user about an occured error
	 * 
	 * @param title   : title of the error dialog ; "ERROR-DIALOG" if not provided
	 * @param message : message of the error dialog ; "Something went wrong!" if not
	 *                provided
	 */
	public static void errorDialog(String title, String message) {
		// check input

		if (title == null || title.isEmpty() || title.equalsIgnoreCase(" ")) {
			title = "ERROR-DIALOG";
		}

		if (message == null || message.isEmpty() || message.equalsIgnoreCase(" ")) {
			message = "Something went wrong!";
		}

		// show dialog

		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * general method to display an {@link JOptionPane} confirm dialog with a
	 * specific title and textual content to request confirmation by the user
	 * 
	 * @param title   : title of the confirm dialog ; "CONFIRM-DIALOG : " if not
	 *                provided
	 * @param message : message of the confirm dialog ; "Please confirm!" if not
	 *                provided
	 * @return true if user confirms by pressing yes/YES/JA/ja/OK/ok ; false
	 *         otherwise
	 */
	public static boolean confirmAction(String title, String message) {
		boolean result = false;

		// check input

		if (title == null || title.isEmpty() || title.equalsIgnoreCase(" ")) {
			title = "CONFIRM-DIALOG : ";
		}

		if (message == null || message.isEmpty() || message.equalsIgnoreCase(" ")) {
			message = "Please confirm!";
		}

		int choice = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (choice == JOptionPane.YES_OPTION) {
			result = true;
		}

		return result;
	}

}
