package edu.usf.eng.pie.avatars4change.avatar;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Canvas;

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
		if(sprites!=null){	//do nothing if no sprites in list
			for (Sprite s : sprites){	//for each sprite 's' in spriteList
				s.draw(c);	//TODO add entity location 
			}
		}
		if(animations!=null){//same for animations
			for (Animation a : animations){	
				a.draw(c);	
			}
		}
	}
	
	//moves animation to the next frame by incrementing currentFrame
	public void nextFrame(){
		if(animations!=null){	//do nothing if no sprites in list
			for (Animation a : animations){	//for each sprite 's' in spriteList
				a.nextFrame();
			}
		}
		//do nothing with sprites
	}
}
