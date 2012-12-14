package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.graphics.Canvas;

//import edu.usf.pie.avatars4change.sprite

public class entity {
	//list of sprites in the entity
	sprite spriteList[] = null;
	location entityLoc  = new location();
	String name = "UNNAMED";
	
	
	//constructor
	public entity(){
		
	}
	
	public void addSprite(sprite newSprite){
		
	}
	
	// draw method draws all base sprites in their relative locations
	public void draw(Canvas c){
		if(spriteList==null) return;	//do nothing if no sprites in list
		int i = 0;	//index for spriteLocations (same as implicit index of spriteList, i.e. s=spriteList[i])
		for (sprite s : spriteList){	//for each sprite 's' in spriteList
			s.draw(c);	//TODO add entity location 
			i++;
		}
	}
	
	//moves animation to the next frame by incrementing currentFrame
	public void nextFrame(){
		if(spriteList==null) return;	//do nothing if no sprites in list
		for (sprite s : spriteList){	//for each sprite 's' in spriteList
			s.nextFrame();
		}
	}
}
