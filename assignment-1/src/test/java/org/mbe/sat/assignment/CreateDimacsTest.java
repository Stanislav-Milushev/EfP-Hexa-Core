/**
 * 
 */
package org.mbe.sat.assignment;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author User
 *
 */
class CreateDimacsTest {

	//BASIC TESTING
	
	
	/*
	 * EXTENDED TESTING : 
	 * 
	 * BCS : Automatic_Power_Window;1 Body_Comfort_System;2 Door_System;3
	 * Exterior_Mirror;4 Human_Machine_Interface;5 LED_Alarm_System;6
	 * LED_Central_Locking_System;7 LED_Finger_Protection;8 Manual_Power_Window;9
	 * Power_Window;10 Security;11 Status_LED;12
	 * 
	 * Kühlschrank : Buttons;1 Control;2 Ice_Dispender;3 LCD;4 LED;5 Refrigerator;6
	 * Screen;7 Temperature_Regulation_Control;8 Touch;9 Voice;10 Water_Dispender;11
	 * 
	 * CPU : Air_coolingSystem;1 CPU;2 Cooler;3 Drivers;4 GPU;5 GPUDriver;6 HDD;7
	 * Hardware;8 Linux;9 MacOS;10 Mainboard;11 Modular;12 Non_Modular;13 OS;14
	 * PC;15 PowerAdapter;16 RAM;17 SSD;18 Software;19 Storage;20
	 * Water_coolingSystem;21 Windows;22
	 */

	
	
	private static CreateDimacs createDimacs;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	
	
	}

	/**
	 * Test method for {@link org.mbe.sat.assignment.CreateDimacs#CreateDimacs(java.lang.String, java.lang.String, java.util.ArrayList)}.
	 */
	@Test
	void testCreateDimacs() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.mbe.sat.assignment.CreateDimacs#WriteDimacs()}.
	 */
	@Test
	void testWriteDimacs() {
		fail("Not yet implemented");
	}

}
