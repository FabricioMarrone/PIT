package com.tomgames.pit.entities.shoots;

public interface Pool<Type> {
	   public void recycle(final Type data);
	   public Type get();   
	   public void reset();
	   public String debug();
	}
