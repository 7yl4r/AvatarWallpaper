package edu.usf.eng.pie.avatars4change.avatar;

import java.util.ArrayList;
import java.util.List;


import android.graphics.Canvas;
import android.util.Log;

//import edu.usf.pie.avatars4change.sprite

public class Entity {
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
	private int spriteIndex(String spriteName){
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
		Location spriteLoc = new Location(scaleValueFromPercent(relativeLoc.x),
				                          scaleValueFromPercent(relativeLoc.y),
				                          relativeLoc.zorder,
				                          scaleValueFromPercent(relativeLoc.size),
				                          relativeLoc.rotation);
		sprites.add( new Sprite(spriteName, imageFile, spriteLoc) );
	}
	
	//add a new sprite, location is in percent of entity size (except rotation)
	public void addAnimation(String animName, String animFile, Location relativeLoc){
		Location animLoc = new Location(scaleValueFromPercent(relativeLoc.x),
                                        scaleValueFromPercent(relativeLoc.y),
                                        relativeLoc.zorder,
                                        scaleValueFromPercent(relativeLoc.size),
                                        relativeLoc.rotation);
		animations.add( new Animation(animName, animFile, animLoc));
	}
	
	public void setSpriteLocation(String spriteName, Location newLoc){
		int i = spriteIndex(spriteName);
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
		int i = animationIndex(animName);
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
		int i = spriteIndex(spriteName);
		if ( (i >= 0) && (i <= sprites.size()) ){
			sprites.get(i).loadImage(newFile);
//			Sprite temp = sprites.get(i);
//			temp.loadImage(newFile);
//			sprites.set( i , temp );
		}else{
			Log.e("entity sprite","sprite file cannot be set, invalid index: "+Integer.toString(i));
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
		Log.i("entity","cannot get index, animation "+animName+" not found. -1 returned.");
		return -1;
	}
	
	// scale given percent of total width value, and return scaled absolute value
	public int scaleValueFromPercent(int percent){
		return (int) Math.round(percent*location.size/100.0f);
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
					a.draw(c);
					objectsLeft--;
				}
			}
			for (Sprite s : sprites){	//for each sprite 's' in spriteList
				if(s.location.zorder==z){
					s.draw(c);	 
					objectsLeft--;
				}
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
		location.size = newSize;
		Log.v("entity","size set to "+location.size);
	}
}
