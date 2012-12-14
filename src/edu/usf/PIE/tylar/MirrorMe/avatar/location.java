package edu.usf.PIE.tylar.MirrorMe.avatar;

public class location {
	int x,y;
	int scale;	//'scale' is in number of 'display-independent-pixels' from center point to farthest side
	int rotation;
	
	public location(){	//set defaults values (only used for debugging)
		x = 0;
		y = 0;
		scale = 1;
		rotation = 0;
	}
	public location(int nx, int ny, int ns, int nr){
		x = nx;
		y = ny;
		scale=ns;
		rotation=nr;
	}
}
