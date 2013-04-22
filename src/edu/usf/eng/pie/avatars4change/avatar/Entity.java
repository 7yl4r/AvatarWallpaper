package edu.usf.eng.pie.avatars4change.avatar;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Canvas;
import android.util.Log;

//import edu.usf.pie.avatars4change.sprite

public class Entity {
	private final float ASSUMED_ENTITY_SIZE = 300.0f;	//because this is the size of the png
	String name = "UNNAMED";
	Location location  = new Location();

	List<Sprite> sprites = new ArrayList<Sprite>();
	List<Animation> animations = new ArrayList<Animation>();
	//sprite spriteList[];	//list of sprites in entity
	//animation animationList[];//list of animated sprites in entity
		
	public Entity(){}
	//constructor
	public Entity(String NAME, Location LOCATION){
		name = NAME;
		location = LOCATION;
	}
	private int getSpriteIndex(String spriteName){
		if(sprites.isEmpty()){
			Log.i("entity","cannot get index, no sprites in entity. -1 returned.");
			return -1;
		}
		int index = 0;
		for( Sprite s : sprites ){
			if (s.name.equals(spriteName)){
				return index;
			}
			index++;
		}
		Log.i("entity spriteIndex","cannot get index, animation "+spriteName+" not found. -1 returned.");
		return -1;
	}
	//add a new sprite, location is in percent of entity size (except rotation)
	public void addSprite(String spriteName, String imageFile, Location relativeLoc){
		Location spriteLoc = scaleLocFromPercent(
				             new Location(relativeLoc.x ,
				                          relativeLoc.y ,
				                          relativeLoc.zorder,
				                          relativeLoc.size ,
				                          relativeLoc.rotation));
		sprites.add( new Sprite(spriteName, imageFile, spriteLoc) );
	}
	
	//add a new animation, location is in percent of entity size (except rotation)
	public void addAnimation(String animName, String animFile, Location relativeLoc){
		Location animLoc = scaleLocFromPercent(
				           new Location(relativeLoc.x ,
                                        relativeLoc.y ,
                                        relativeLoc.zorder,
                                        (relativeLoc.size ),
                                        relativeLoc.rotation));
		animations.add( new Animation(animName, animFile, animLoc));
	}
	
	public void setSpriteLocation(String spriteName, Location newLoc){
		int i = getSpriteIndex(spriteName);
//		Log.v("entity","animations size = "+animations.size());
		if ( (i >= 0) && (i < sprites.size()) ){
			Sprite temp = sprites.get(i);
			temp.location = newLoc;
			sprites.set( i , temp );
		}else{
			Log.e("entity","sprite location cannot be set, invalid index: "+Integer.toString(i));
		}
	}
	
	//sets animation with specified name to given 
	public void setAnimationDir(String animName, String newDir){
		int i = getAnimationIndex(animName);
//		Log.v("entity","animations size = "+animations.size());
		if ( (i >= 0) && (i <= animations.size()) ){
			Animation temp = animations.get(i);
			temp.fileDir = newDir;
			animations.set( i , temp );
			animations.get(i).load();
		}else{
			Log.e("entity animation","animation fileDir cannot be set, invalid index: "+Integer.toString(i));
		}
	}
	
	public void setSpriteFile(String spriteName, String newFile){
		int i = getSpriteIndex(spriteName);
		if ( (i >= 0) && (i <= sprites.size()) ){
			sprites.get(i).loadImage(newFile);
//			Sprite temp = sprites.get(i);
//			temp.loadImage(newFile);
//			sprites.set( i , temp );
		}else{
			Log.e("entity sprite","sprite file cannot be set, invalid index: "+Integer.toString(i));
		}
	}
	
	public Animation getAnimation(String animName){
		int index = getAnimationIndex(animName);
		if (index >= 0){
			return animations.get(index);
		}else{
			Log.e("entity","animation " + animName + " not found");
			return null;
		}
	}
	
	private int getAnimationIndex(String animName){
		if(animations.isEmpty()){
			Log.i("entity","cannot get index, no animations in entity. -1 returned.");
			return -1;
		}
		int index = 0;
		for( Animation a : animations ){
			if (a.name.equals(animName)){
				return index;
			}
			index++;
		}
		Log.i("entity","cannot get index, animation "+animName+" not found. -1 returned.");
		return -1;
	}
	
	// draw method draws all base sprites in their relative locations
	//order drawn is animations followed by sprites in the order that they are in their respective arrays
	public void draw(Canvas c){
//		Log.v("entity draw","drawing "+Integer.toString(sprites.size())+" sprites, and "
//		      +Integer.toString(animations.size())+" animations in entity "+this.name);
		//TODO add entity location
		int objectsLeft = animations.size() + sprites.size();
		int z = 0;
		while(objectsLeft > 0){
			for (Animation a : animations){	
				if(a.location.zorder==z){
					Location temp = a.location;	//hold on to the % location
					a.location = this.scaleLocFromPercent(a.location);
					a.draw(c);
					a.location = temp;	//reset % location
					objectsLeft--;
				}
			}
			for (Sprite s : sprites){	//for each sprite 's' in spriteList
				if(s.location.zorder==z){
					Location temp = s.location;	//hold on to the % location
					s.location = this.scaleLocFromPercent(s.location);
					s.draw(c);	 
					s.location = temp;	//reset % location
					objectsLeft--;
				}
			}
			z++;
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
	
	public void resetFrameCount(){
		if(!animations.isEmpty()){	//do nothing if empty list
			for (Animation a : animations){	//for each animation
				a.resetFrameCount();
			}
		}
	}
	
	public void setSize(int newSize){
		location.size = newSize;
		
		//NOPE: THIS SIZE IS DETERMINED AT DRAW-TIME
		//Log.v("entity","size set to "+location.size);
		//scale each item in the entity
//		for (Animation a : animations){	
//			a.location.set(scaleLocFromPercent(a.location));
//		}
//		for (Sprite s : sprites){	//for each sprite 's' in spriteList
//			s.location.set(scaleLocFromPercent(s.location));
//		}
		
	}
	// scale location as percent of total width value, and return scaled absolute location
	public Location scaleLocFromPercent(Location percentLoc){
		Location absLoc = new Location();
		absLoc.x = (int) Math.round(percentLoc.x*((float)this.location.size)/ASSUMED_ENTITY_SIZE);
		absLoc.y = (int) Math.round(percentLoc.y*((float)this.location.size)/ASSUMED_ENTITY_SIZE); 
		absLoc.zorder = percentLoc.zorder;
		absLoc.size = (int) Math.round(percentLoc.size*((float)this.location.size/ASSUMED_ENTITY_SIZE)); 
		absLoc.rotation = percentLoc.rotation; 
		 return absLoc;
	}
}
