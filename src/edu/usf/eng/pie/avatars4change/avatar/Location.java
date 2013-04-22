package edu.usf.eng.pie.avatars4change.avatar;

public class Location {
	int x,y,zorder;
	int size;	//'size' is in number of 'display-independent-pixels'(dip) from center point to farthest side
	int rotation;
	
	//constructors:
	public Location(){	//set defaults values (only used for debugging)
		x        = 0;
		y        = 0;
		zorder   = 0;
		size     = 100;
		rotation = 0;
	}
	public Location(int nx, int ny, int nz, int ns, int nr){
		set(nx,ny,nz,ns,nr);
	}
	public void set(Location L){
		x        = L.x;
		y        = L.y;
		zorder   = L.zorder;
		size     = L.size;
		setRotation(L.rotation);
	}
	public void set(int nx, int ny, int nz, int ns, int nr){
		x        = nx;
		y        = ny;
		zorder   = nz;
		size     = ns;
		setRotation(nr);
	}
	public void setRotation(int newR){
		rotation = newR%360;
	}
}
