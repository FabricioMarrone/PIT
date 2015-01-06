package com.tomgames.pit.entities.shoots;

import java.util.Stack;

public class ShootPool implements Pool<Shoot>{

	private final Stack<Shoot> freeObjects;
	
	public ShootPool(){
		freeObjects = new Stack<Shoot>();
	}
	
	@Override
	public void recycle(Shoot data) {
		freeObjects.push(data);
	}

	@Override
	public Shoot get() {
		 if (freeObjects.isEmpty()) {
	         return new Shoot();
	      }
	      
	      return freeObjects.pop();
	}

	@Override
	public void reset() {
		freeObjects.clear();
	}

	@Override
	public String debug() {
		 return "Current Pool Size: " + freeObjects.size();
	}

}//end class
