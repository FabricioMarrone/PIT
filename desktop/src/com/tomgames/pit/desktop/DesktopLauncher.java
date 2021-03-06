package com.tomgames.pit.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tomgames.pit.PIT;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width= 900;
		config.height= 700;
		config.fullscreen= false;
		new LwjglApplication(new PIT(), config);
	}
}
