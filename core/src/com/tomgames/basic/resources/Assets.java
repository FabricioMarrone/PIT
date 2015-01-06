package com.tomgames.basic.resources;

public class Assets {

	public static TextureManager textures;
	public static AudioManager audio;
	public static FontManager fonts;
	
	/** Total time required to load all the resources into memory. */
	public static long loadTime;
	public static boolean loadDone= false;
	public static int percentLoad= 0;
	
	//Singleton class.
	private Assets(){};
	
	/**
	 * Load ALL the resources into memory.
	 */
	public static void loadAllResources(){
		//Start load...
		loadTime= System.currentTimeMillis();
		
		//Fonts...
		fonts= new FontManager();
		fonts.loadFonts();
		
		percentLoad= 15;
		
		//Textures...
		textures= new TextureManager();
		textures.loadTextures();
		
		percentLoad= 65;
		
		//Music...
		audio= new AudioManager();
		audio.loadMusic();
		
		percentLoad= 80;
		
		//Sounds...
		audio.loadSounds();
		
		percentLoad= 100;
		
		//System.out.println("RAM usage on resource manager: " + (Runtime.getRuntime().totalMemory())/1024/1024 + "mb");
		loadTime= System.currentTimeMillis() - loadTime;
		System.out.println("Assets: Load Complete (" + loadTime + "ms)");
		loadDone= true;
	}
	
	public static void disposeAllResources(){
		System.out.println("Disposing assets...");
		textures.disposeAll();
		audio.disposeAll();
		fonts.disposeAll();
	}
}//end class
