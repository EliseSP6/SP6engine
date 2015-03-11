package Bleach;

public class Weapon {
	
	private long timeLastFire;
	protected int clipsize;
	protected int ammo;
	protected int firerate;
	
	protected Weapon(int clipsize, int ammo, int firerate){
		this.clipsize = clipsize;
		this.ammo = ammo;
		this.firerate = firerate;
		timeLastFire = System.currentTimeMillis();
	}
	
	public boolean tryFire(){
		if(System.currentTimeMillis() - timeLastFire >= firerate){
			
			// fire
			
			timeLastFire = System.currentTimeMillis();
			return true;
		}else{
			return false;
		}
	}
	
	private void fire(){}
}
