package cyiq.shoot;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	protected BufferedImage[] images = {};	//两张图片交替显示
	protected int index = 0;
	
	private int doubleFire;
	private int life;
	public Hero(){
		this.life = 3;
		this.doubleFire = 0;
		this.image = ShootGame.hero0;
		this.images = new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.x = 175;
		this.y = 480;
	}
	@Override
	public void step() {
		if(images.length > 0){
			image = this.images[index++/10 % images.length];
		}
	}
	
	public Bullet[] shoot(){  
		int xStep = this.width / 5;
		int yStep = 20;
		//双倍子弹
		if(doubleFire > 0){
			Bullet[] bullets = new Bullet[2];
			bullets[0] = new Bullet(x + xStep , y - yStep);
			bullets[1] = new Bullet(x + 3*xStep , y - 3*yStep);
			doubleFire -= 2;
			return bullets;
		}else{			//单倍
			Bullet[] bullets = new Bullet[1];
			bullets[0] = new Bullet(x + 2 * xStep, y - yStep);
			return bullets;
		}
	}
	//英雄机移动
	public void moveTo(int x, int y){
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}
	//双倍火力
	public void addDoubleFire(){
		this.doubleFire += 40;
	}
	//加命
	public void addLife(){
		life++;
	}
	@Override
	public boolean shootBy(Bullet bullet) {
		return false;
	}
	
	public int getLife(){
		return this.life;
	}
	@Override
	public boolean outOfBounds() {
		return false;
	}
	//碰撞减命
	public void subtractLife(){
		this.life--;
	}
	//碰撞后去除双倍火力效果
	public void setDoubleFire(int doubleFire){
		this.doubleFire = doubleFire;
	}
	//碰撞算法
	public boolean hit(FlyingObject other){
		int x1 = other.x - this.width/2;
		int x2 = other.x + other.width + this.width/2;
		int y1 = other.y - this.height/2;
		int y2 = other.y + other.height + this.height/2;
		return this.x + this.width/2 > x1 
			&& this.x + this.width/2 < x2
			&& this.y + this.height/2 > y1
			&& this.y + this.height/2 < y2;
		
	}
	
}
