package edu.usf.PIE.tylar.MirrorMe.avatar;

import android.graphics.Canvas;

//import edu.usf.pie.avatars4change.sprite

public class entity {
	//list of sprites in the entity
	sprite spriteList[];
	//list of sprite locations (relative to entity center? scaled?)
	location spriteLocations[];
	
	//constructor
	public entity(){
		
	}
	
	// draw method draws all base sprites in their relative locations
	public void draw(Canvas c){
		int i = 0;	//index for spriteLocations (same as implicit index of spriteList, i.e. s=spriteList[i])
		for (sprite s : spriteList){	//for each sprite 's' in spriteList
			s.draw(c, spriteLocations[i]);
			i++;
		}
	}
	
	public void nextFrame(){
		for (sprite s : spriteList){	//for each sprite 's' in spriteList
			s.nextFrame();
		}
	}
}
