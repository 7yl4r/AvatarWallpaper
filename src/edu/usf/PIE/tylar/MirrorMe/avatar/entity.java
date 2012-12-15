package edu.usf.PIE.tylar.MirrorMe.avatar;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

//import edu.usf.pie.avatars4change.sprite

public class entity {
	String name = "UNNAMED";
	location entityLoc  = new location();

	List<sprite> sprites = new ArrayList<sprite>();
	List<animation> animations = new ArrayList<animation>();
	//sprite spriteList[];	//list of sprites in entity
	//animation animationList[];//list of animated sprites in entity
		
	public entity(){}
	//constructor
	public entity(String NAME, location LOCATION){
		name = NAME;
		entityLoc = LOCATION;
	}
	
	public void addSprite(sprite newSprite){
		sprites.add( newSprite );
	}
	
	public void addAnimation(animation newAnimation){
		animations.add( newAnimation );
	}
	
	// draw method draws all base sprites in their relative locations
	public void draw(Canvas c){
		if(sprites!=null){	//do nothing if no sprites in list
			int i = 0;	//index for spriteLocations (same as implicit index of spriteList, i.e. s=spriteList[i])
			for (sprite s : sprites){	//for each sprite 's' in spriteList
				s.draw(c);	//TODO add entity location 
				i++;
			}
		}
		if(animations!=null){//same for animations
			int i = 0;
			for (animation a : animations){	
				a.draw(c);	
				i++;
			}
		}
	}
	
	//moves animation to the next frame by incrementing currentFrame
	public void nextFrame(){
		if(animations!=null){	//do nothing if no sprites in list
			for (animation a : animations){	//for each sprite 's' in spriteList
				a.nextFrame();
			}
		}
		//do nothing with sprites
	}
}
