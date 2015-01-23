package com.tomgames.pit;

import java.util.ArrayList;

import com.tomgames.pit.entities.items.ClueItem;

public class SecretMessages {

	//Proverbios chinos
	//http://es.wikiquote.org/wiki/Proverbios_chinos
	//
	//"El que ve el cielo en el agua ve los peces en los árboles."
	
	//Las grandes almas tienen voluntades; las débiles tan solo deseos.
	//Great souls have wills; the weak ones, only desires.
	
	private static String testMap_Msg_0= "\"Look at the island with near skulls on the East.\"";
	private static String testMap_Msg_1= "\"Surrounded by totems. That will be safe.\"";
	private static String testMap_Msg_2= "\"I hope nobody will find it.\"";
	
	public static String m3_Msg_0= " \"The strongest man is the\none who resists loneliness.\n\n\n Henrik Ibsen";
	
	public static String m5_Msg_0= "\"Here lies Sonic, the fast one.\n\nThe Lord of the Rings.\"";
	public static String m5_Msg_1= "\"Great souls have wills;\nthe weak ones, only desires.\"";
	
	public static String m6_Msg_0= "\"I went north.\nNow I face my own death on\nthe Death Islands. What a\nproper name.\n\nIf you wanna live, dont go\nNorth.\n\n\nA dead man.";
	
	public static String m7_Msg_0= "\"Enjoy life, it's later than\nyou think.\"\n\nFriend.";
	public static String m7_Msg_1= "If you find truly peace when\nsailing, then you are a real\nsailor.";
	
	public static String m8_Msg_0= " \"If the treasure is not here\nto my return, you will\npay it bastard.\"\n\nOne eye pirate.";
	public static String m8_Msg_1= "\"Here you can find more\ngold than you though.\"\n\nAnonymous.";
	public static String m8_Msg_2= "A dead body cant spend gold.\nIts yours then.\n\n\"Look between the three fishes.\"";
	
	public static String m9_Msg_0= "The story tells about\na big fortress on the\nthe west. Full of gold\nand secret places.";
	
	public static void setMessagesInBottles(String islandName, ArrayList<ClueItem> clueItems){
		//m3
		if(islandName.compareToIgnoreCase("m3")==0){
			for(int i= 0; i < clueItems.size(); i++){
				ClueItem item= clueItems.get(i);
			
				if(item.getTilePosition().x == 38 && item.getTilePosition().y == 99-75) item.setMessage(m3_Msg_0);
				
			}
		}
		
		//m5
		if(islandName.compareToIgnoreCase("m5")==0){
			for(int i= 0; i < clueItems.size(); i++){
				ClueItem item= clueItems.get(i);
			
				if(item.getTilePosition().x == 87 && item.getTilePosition().y == 99-80) item.setMessage(m5_Msg_0);
				if(item.getTilePosition().x == 42 && item.getTilePosition().y == 99-73) item.setMessage(m5_Msg_1);
			}
		}
		
		//m6
		if(islandName.compareToIgnoreCase("m6")==0){
			for(int i= 0; i < clueItems.size(); i++){
				ClueItem item= clueItems.get(i);
			
				if(item.getTilePosition().x == 88 && item.getTilePosition().y == 99-4) item.setMessage(m6_Msg_0);
			}
		}
		
		//m7
		if(islandName.compareToIgnoreCase("m7")==0){
			for(int i= 0; i < clueItems.size(); i++){
				ClueItem item= clueItems.get(i);
			
				if(item.getTilePosition().x == 57 && item.getTilePosition().y == 99-44) item.setMessage(m7_Msg_0);
				if(item.getTilePosition().x == 8 && item.getTilePosition().y == 99-34) item.setMessage(m7_Msg_1);
			}
		}
		
		//m8
		if(islandName.compareToIgnoreCase("m8")==0){
			for(int i= 0; i < clueItems.size(); i++){
				ClueItem item= clueItems.get(i);
			
				if(item.getTilePosition().x == 39 && item.getTilePosition().y == 99-21) item.setMessage(m8_Msg_0);
				if(item.getTilePosition().x == 93 && item.getTilePosition().y == 99-19) item.setMessage(m8_Msg_1);
				if(item.getTilePosition().x == 14 && item.getTilePosition().y == 99-77) item.setMessage(m8_Msg_2);
			}
		}
		
		//m9
		if(islandName.compareToIgnoreCase("m9")==0){
			for(int i= 0; i < clueItems.size(); i++){
				ClueItem item= clueItems.get(i);
			
				if(item.getTilePosition().x == 13 && item.getTilePosition().y == 99-77) item.setMessage(m9_Msg_0);
				//if(item.getTilePosition().x == 93 && item.getTilePosition().y == 99-19) item.setMessage(m9_Msg_1);
				//if(item.getTilePosition().x == 14 && item.getTilePosition().y == 99-77) item.setMessage(m9_Msg_2);
			}
		}
		
		//testmap
		if(islandName.compareToIgnoreCase("testMap")==0){
			for(int i= 0; i < clueItems.size(); i++){
				ClueItem item= clueItems.get(i);
				
				//clue 0
				if(item.getTilePosition().x == 23 && item.getTilePosition().y == 99-36) item.setMessage(testMap_Msg_0);
				//clue 1
				if(item.getTilePosition().x == 36 && item.getTilePosition().y == 99-23) item.setMessage(testMap_Msg_1);
				//clue 2
				if(item.getTilePosition().x == 61 && item.getTilePosition().y == 99-49) item.setMessage(testMap_Msg_2);
			}
		}
	}
}//end class
