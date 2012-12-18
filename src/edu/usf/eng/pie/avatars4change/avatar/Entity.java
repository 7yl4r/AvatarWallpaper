package edu.usf.eng.pie.avatars4change.avatar;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Canvas;
import android.util.Log;

//import edu.usf.pie.avatars4change.sprite

public class Entity {
	String name = "UNNAMED";
	Location entityLoc  = new Location();

	List<Sprite> sprites = new ArrayList<Sprite>();
	List<Animation> animations = new ArrayList<Animation>();
	//sprite spriteList[];	//list of sprites in entity
	//animation animationList[];//list of animated sprites in entity
		
	public Entity(){}
	//constructor
	public Entity(String NAME, Location LOCATION){
		name = NAME;
		entityLoc = LOCATION;
	}
	
	public void addSprite(Sprite newSprite){
		sprites.add( newSprite );
	}
	
	public void addAnimation(Animation newAnimation){
		animations.add( newAnimation );
	}
	
	// draw method draws all base sprites in their relative locations
	public void draw(Canvas c){
//		Log.v("entity draw","drawing "+Integer.toString(sprites.size())+" sprites, and "
//		      +Integer.toString(animations.size())+" animations in entity "+this.name);
		if(!sprites.isEmpty()){	//do nothing if no sprites in list
			for (Sprite s : sprites){	//for each sprite 's' in spriteList
				s.draw(c);	//TODO add entity location 
			}
		}
		if(!animations.isEmpty()){//same for animations
			for (Animation a : animations){	
				a.draw(c);	
			}
		}
	}
	
	//moves animation to the next frame by incrementing currentFrame
	public void nextFrame(){
		if(!animations.isEmpty()){	//do nothing if empty list
			for (Animation a : animations){	//for each animation
				a.nextFrame();
			}
		}
		//do nothing with sprites
	}
}
