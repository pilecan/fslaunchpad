package com.cfg.dialog;

import java.io.IOException;

public class LaunchFSL {

	public static void main(String[] args) {
		Runtime rt = Runtime.getRuntime();
		try {
			Process ps = rt.exec("FSLaunchPad.exe");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
