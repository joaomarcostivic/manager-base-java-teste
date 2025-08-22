package com.tivic.manager.util;

import java.io.*;

public class HardDiskSerialNumber {

	/**
	 * @author Edgard Hufelande
	 * @param string
	 *            Partition
	 * @return string result
	 */
	public static String getSerial(String Partition) {
		String os = System.getProperty("os.name");
		String result;

		try {
			if (os.startsWith("Windows")) {
				result = getSerialWindows(Partition);
			} else if (os.startsWith("Linux")) {
				result = getSerialLinux(Partition);
			} else {
				result = "";
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println(e);
			return null;
		}
	}

	/**
	 * @author Edgard Hufelande
	 * @param string
	 *            Partition
	 * @return string Serial
	 */
	public static String getSerialWindows(String Partition) throws IOException {

		String Serial = "";

		try {

			Process p = Runtime.getRuntime().exec(
					"cmd /c" + " vol " + Partition + ":");
			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line;

			while ((line = input.readLine()) != null) {
				Serial += line;
			}

			input.close();
			String[] serial = Serial.split(" ");

			Serial = serial[12];

		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println(e);
			return null;
		}

		return Serial.trim();

	}

	/**
	 * @author Edgard Hufelande
	 * @param string
	 *            Partition
	 * @return string Serial
	 */
	public static String getSerialLinux(String Partition) throws IOException {

		String result = "";
//		String[] args = {"sudo", "-S", "lshw", "-class", "disk | grep serial" };
		String[] args = {"bash", "-c", "echo 12352 |", "sudo", "-S", "hdparm", "-I", "/dev/sda1 | grep serial" };

		ProcessBuilder run = new ProcessBuilder(args);
		run.redirectErrorStream(true);

		Process p = run.start();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			for (String output = br.readLine(); output != null; output = br.readLine()) {
				System.out.println(output);
			}

		} catch (IOException e) {
			System.out.println(e);
		}
		// Process p = Runtime.getRuntime().exec(args);
		// BufferedReader input = new BufferedReader(new
		// InputStreamReader(p.getInputStream()));
		// String line;
		// while ((line = input.readLine()) != null) {
		// result += line;
		// }
		// input.close();
		//
		// System.out.println("Result: " + result);
		//
		// } catch (Exception e) {
		// e.printStackTrace(System.out);
		// System.err.println(e);
		// return null;
		// }
		//
		 return result.trim();
	}
}
