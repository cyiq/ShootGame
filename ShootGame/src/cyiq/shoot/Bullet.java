package cyiq.shoot;
/**
 * 子弹
 * @author CYIQ
 */
public class Bullet extends FlyingObject{
	private int speed = 3;
	public Bullet(int x, int y){
		this.x = x;
		this.y = y;
		this.image =ShootGame.bullet;
	}
	@Override
	public void step() {
		this.y -= speed;
	}
	@Override
	public boolean shootBy(Bullet bullet) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean outOfBounds() {
		return y< -this.height;
	}
	
}
