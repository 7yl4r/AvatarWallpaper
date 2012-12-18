package edu.usf.eng.pie.avatars4change.avatar;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Canvas;
import android.util.Log;

//import edu.usf.pie.avatars4change.sprite

public class Entity {
	String name = "UNNAMED";
	Location Loc  = new Location();

	List<Sprite> sprites = new ArrayList<Sprite>();
	List<Animation> animations = new ArrayList<Animation>();
	//sprite spriteList[];	//list of sprites in entity
	//animation animationList[];//list of animated sprites in entity
		
	public Entity(){}
	//constructor
	public Entity(String NAME, Location LOCATION){
		name = NAME;
		Loc = LOCATION;
	}
	
	//add a new sprite, location is in percent of entity size (except rotation)
	public void addSprite(String spriteName, String imageFile, Location relativeLoc){
		Location spriteLoc = new Location(scaleValueFromPercent(relativeLoc.x),
				                          scaleValueFromPercent(relativeLoc.y),
				                          scaleValueFromPercent(relativeLoc.size),
				                          relativeLoc.rotation);
		sprites.add( new Sprite(spriteName, imageFile, spriteLoc) );
	}
	
	//add a new sprite, location is in percent of entity size (except rotation)
	public void addAnimation(String animName, String animFile, Location relativeLoc){
		Location animLoc = new Location(scaleValueFromPercent(relativeLoc.x),
                                        scaleValueFromPercent(relativeLoc.y),
                                        scaleValueFromPercent(relativeLoc.size),
                                        relativeLoc.rotation);
		animations.add( new Animation(animName, animFile, animLoc));
	}
	
	//sets animation with specified name to given 
	public void setAnimationDir(String animName, String newDir){
		int i = animationIndex(animName);
//		Log.v("entity","animations size = "+animations.size());
		if ( (i >= 0) && (i < animations.size()) ){
			Animation temp = animations.get(i);
			temp.fileDir = newDir;
			animations.set( i , temp );
			animations.get(i).load();
		}else{
			Log.e("entity","animation cannot be set, invalid index: "+Integer.toString(i));
		}
	}
	
	private int animationIndex(String animName){
		int index = 0;
		for( Animation a : animations ){
			if (a.name.equals(animName)){
				return index;
			}
			index++;
		}
		Log.i("entity","cannot get index, animation "+animName+" not found.");
		return -1;
	}
	
	// scale given percent of total width value, and return scaled absolute value
	public int scaleValueFromPercent(int percent){
		return (int) Math.round(percent*Loc.size/100.0f);
	}
	
	// draw method draws all base sprites in their relative locations
	//order drawn is animations followed by sprites in the order that they are in their respective arrays
	public void draw(Canvas c){
//		Log.v("entity draw","drawing "+Integer.toString(sprites.size())+" sprites, and "
//		      +Integer.toString(animations.size())+" animations in entity "+this.name);
		if(!animations.isEmpty()){//same for animations
			for (Animation a : animations){	
				a.draw(c);	
			}
		}
		if(!sprites.isEmpty()){	//do nothing if no sprites in list
			for (Sprite s : sprites){	//for each sprite 's' in spriteList
				s.draw(c);	//TODO add entity location 
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
	
	public void setSize(int newSize){
		Loc.size = newSize;
		Log.v("entity","size set to "+Loc.size);
	}
}
