package edu.usf.PIE.tylar.MirrorMe.avatar;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;

public class scene {
	String name = "UNNAMED";	//scene name
	
	//list of all objects in the scene
	List<sprite> sprites = new ArrayList<sprite>();
	List<animation> animations = new ArrayList<animation>();
	List<entity> entities = new ArrayList<entity>();
	
	public scene(){}
	public scene(String NAME){
		name = NAME;
	}
	
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
		if(animations!=null){//same for entities
			int i = 0;
			for (entity e : entities){	
				e.draw(c);	
				i++;
			}
		}
	}
	
	public void nextFrame(){
		if(entities  !=null){	//do nothing if empty list
			for (entity e : entities){ //for each enitity
				e.nextFrame();
			}
		}
		if(animations!=null){	//do nothing if nothing in list
			for (animation a : animations){	//for each animation
				a.nextFrame();
			}
		}
		//do nothing with sprites
	}
	
	public void addEntity(entity newEnt){
		entities.add(newEnt);
	}
	public void addAnimation( animation newAnim ){
		animations.add(newAnim);
	}
	public void addSprite( sprite newSp ){
		sprites.add(newSp);
	}
}
