package edu.usf.eng.pie.avatars4change.avatar;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;

public class Scene {
	String name = "UNNAMED";	//scene name
	
	//list of all objects in the scene
	List<Sprite> sprites = new ArrayList<Sprite>();
	List<Animation> animations = new ArrayList<Animation>();
	List<Entity> entities = new ArrayList<Entity>();
	
	public Scene(){}
	public Scene(String NAME){
		name = NAME;
	}
	
	public void draw(Canvas c){
//		Log.v("entity draw","drawing "+Integer.toString(sprites.size())+" sprites, "
//			      +Integer.toString(animations.size())+" animations, "
//				  +Integer.toString(entities.size())+" entities in scene "+this.name);
		//TODO: add scene location
		int objectsLeft = animations.size() + sprites.size() + entities.size();
		int z = 0;
		while(objectsLeft > 0){
			for (Sprite s : sprites){	//for each sprite 's' in spriteList
				if(s.location.zorder==z){
					s.draw(c); 
					objectsLeft--;
				}
			}
			for (Animation a : animations){	
				if(a.location.zorder==z){
					a.draw(c); 
					objectsLeft--;
				}			}
			for (Entity e : entities){	
				if(e.location.zorder==z){
					e.draw(c);
					objectsLeft--;
				}			}
		}
	}
	
	public void nextFrame(){
		if(!entities.isEmpty()){	//do nothing if empty list
			for (Entity e : entities){ //for each entity
				e.nextFrame();
			}
		}
		if(!animations.isEmpty()){	//do nothing if nothing in list
			for (Animation a : animations){	//for each animation
				a.nextFrame();
			}
		}
		//do nothing with sprites
	}
	
	public void addEntity(Entity newEnt){
		entities.add(newEnt);
	}
	public void addAnimation( Animation newAnim ){
		animations.add(newAnim);
	}
	public void addSprite( Sprite newSp ){
		sprites.add(newSp);
	}
}
