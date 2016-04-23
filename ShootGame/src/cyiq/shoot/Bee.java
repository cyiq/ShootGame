package cyiq.shoot;

import java.util.Random;

/**
 * 飞行物&奖励
 * @author CYIQ
 */
public class Bee extends FlyingObject implements Award{
	private int xSpeed = 1;	//x坐标移动速度
	private int ySpeed = 2;	//y....
	private int awardType;
	
	public Bee(){
		this.image = ShootGame.bee;
		this.width = this.image.getWidth();
		this.height = this.image.getHeight();
		Random random = new Random();
		this.x = random.nextInt(ShootGame.WIDTH - width);
		this.y = -this.height;
//		this.x = 100;
//		this.y = 200;
		this.awardType = random.nextInt(2);
	}
	//返回奖励类型
	@Override
	public int getType() {
		return this.awardType;
	}

	@Override
	public void step() {
		this.x += xSpeed;
		this.y += ySpeed;
		if(x > ShootGame.WIDTH - this.width){
			this.xSpeed = -1;
		}
		if(x < 0){
			this.xSpeed = 1;
		}
	}
	
	public boolean shootBy(Bullet bullet){
		int x = bullet.x;
		int y = bullet.y;
		return this.x < x && x < this.x + this.width && this.y < y && y < this.y+this.height;
	}
	@Override
	public boolean outOfBounds() {
		return y>ShootGame.HEIGHT;
	}
	
}
