package cyiq.shoot;
/**
 * 敌机：飞行物&敌人 
 * @author CYIQ
 */
public class Airplane extends FlyingObject implements Enemy{
	private int speed = 2;
	public Airplane(){
		this.image = ShootGame.airplane;
		this.width = this.image.getWidth();
		this.height = this.image.getHeight();
		this.x = (int)(Math.random()*(ShootGame.WIDTH - this.width));
		this.y = -this.height;
//		this.x = 100;
//		this.y = 100;
	}
	@Override
	public int getScore() {
		return 5;
	}
	@Override
	public void step() {
		this.y += this.speed;
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
