package com.tomgames.pit;

import java.util.ArrayList;

import com.tomgames.pit.entities.items.ClueItem;

public class SecretMessages {

	
	private static String testMap_Msg_0= "\"Look at the island with near skulls on the East.\"";
	private static String testMap_Msg_1= "\"Surrounded by totems. That will be safe.\"";
	private static String testMap_Msg_2= "\"I hope nobody will find it.\"";
	
	public static void setMessagesInBottles(String islandName, ArrayList<ClueItem> clueItems){
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
