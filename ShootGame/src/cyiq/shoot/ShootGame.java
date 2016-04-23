package cyiq.shoot;

import java.awt.Color;	
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel{
	private static final long serialVersionUID = 1L;
	//background image 487*852
	public static final int WIDTH = 487;
	public static final int HEIGHT = 666;
	public static BufferedImage background;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage pause;
	public static BufferedImage start;
	public static BufferedImage gameover;
	private int flyEnteredIndex = 0; //飞行物入场倒计时
	
	private FlyingObject[] flyings = {};
	private Bullet[] bullets = {};
	private Hero hero = new Hero();
	
	private Timer timer;				//定时器
	private int intervel = 1000/100;	//时间间隔
	
	private int shootIndex = 0;
	
	private int score = 0;
	//游戏运行的状态
	private int state;
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	public ShootGame(){
//		flyings = new FlyingObject[2];
//		flyings[0] = new Airplane();
//		flyings[1] = new Bee();
//		bullets = new Bullet[1];
//		bullets[0] = new Bullet(200,350);
	}
	
	static{
		try {
			background = ImageIO.read(ShootGame.class.getResource("background.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(background,0,-2,null);
		paintHero(g);
		paintBullets(g);
		paintFlyingObjects(g);
		paintScore(g);
		paintState(g);
	}
	//绘制英雄机
	public void paintHero(Graphics g) {
		g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
	}
	//绘制子弹
	public void paintBullets(Graphics g) {
		for(int i = 0; i < bullets.length; i++){
			Bullet b = bullets[i];
			g.drawImage(b.getImage(), b.getX(), b.getY(), null);
		}
	}
	//绘制飞行物
	public void paintFlyingObjects(Graphics g) {
		for(int i = 0; i < flyings.length; i++){
			FlyingObject f = flyings[i];
			g.drawImage(f.getImage(), f.getX(), f.getY(), null);
		}
	}
	/**
	 * 随机生成飞行物
	 * 
	 * @return 飞行物对象
	 */
	public static FlyingObject nextOne(){
		Random random = new Random();
		int type = random.nextInt(10);		//[0,9)
		if(type == 0){	// 当 type==0 时 生成Bee对象
			return new Bee();
		}else{
			return new Airplane();
		}
	}
	/**
	 * 飞行物入场
	 */
	public void enterAction(){
		flyEnteredIndex++;
		if(flyEnteredIndex%40 == 0){
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings, flyings.length + 1);
			flyings[flyings.length - 1] = obj;
		}
	}
	
	/**
	 * 飞行物移动
	 */
	public void stepAction(){
		//飞行物走一步
		for(int i = 0; i < this.flyings.length ;i++){
			FlyingObject f = flyings[i];
			f.step();
		}
		//子弹走一步
		for(int i = 0; i < this.bullets.length; i++){
			Bullet b = bullets[i];
			b.step();
		}
		hero.step();
	}
	
	public void action(){
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if(state == RUNNING){	//仅当运行时鼠标操控英雄机
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(state == PAUSE){
					state = RUNNING;
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(state != GAME_OVER){
					state = PAUSE;
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:	//游戏结束，清除内容
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					hero = new Hero();
					score = 0;
					state = START;
					break;
				default:
					break;
				}
			}
		};
		this.addMouseListener(l);			//鼠标点击监听	
		this.addMouseMotionListener(l);		//鼠标滑动监听
		this.timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if(state == RUNNING){
					enterAction();	//飞行物入场
					stepAction();	//走一步
					shootAction();	//biu
					bangAction();	//检测是否击中
					outOfBoundsAction(); //删除越界的飞行物
					checkGameOverAction();
				}
				repaint();		//重绘界面	
			}
		}, intervel,intervel);
	}
	/**
	 * biu
	 */
	public void shootAction(){	
		shootIndex++;
		if(shootIndex%30 == 0){
			Bullet[] bs = this.hero.shoot();	// 英雄机发出子弹
			Audio.shoot();		//发子弹声音
			this.bullets = Arrays.copyOf(bullets, bullets.length + bs.length); 		//扩容
			System.arraycopy(bs,0, bullets, bullets.length-bs.length,bs.length);	//追加数组
		}
	}
	/**
	 * 子弹与飞行物碰撞检测
	 */
	public void bangAction(){
		for(int i = 0 ; i < bullets.length; i++){
			Bullet b = bullets[i];
			bang(b);
		}
	}
	//去除被击中的飞行物，进行加分以及奖励
	private void bang(Bullet bullet) {
		int index = -1;		//击中的飞行物索引
		for(int i = 0; i < flyings.length ; i++){
			FlyingObject obj = flyings[i];
			if(obj.shootBy(bullet)){
				index = i;
				Audio.crash(); //碰撞声音
				break;
			}
		}
		if(index != -1){
			FlyingObject one = flyings[index];
			FlyingObject temp = flyings[index];
			flyings[index] = flyings[flyings.length-1];
			flyings[flyings.length-1] = temp;
			flyings = Arrays.copyOf(flyings, flyings.length - 1);
			if(one instanceof Enemy){
				Enemy e = (Enemy) one;
				score += e.getScore();
			}
			if(one instanceof Award){
				Award a = (Award) one;
				int type = a.getType();
				switch(type){
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
		}
	}
	//分数&生命数显示
	public void paintScore(Graphics g){
		int x = 10;
		int y = 25;
		Font font = new Font(Font.SANS_SERIF,Font.BOLD,14);
		g.setColor(new Color(0x201314));
		g.setFont(font);
		g.drawString("SCORE:"+this.score, x, y);
		y+=20;
		g.drawString("LIFE:"+this.hero.getLife(), x, y);
		
	}
	
	/*
	 * 飞行物越界检测
	 */
	public void outOfBoundsAction(){
		int index = 0;
		FlyingObject [] flyingLives = new FlyingObject[flyings.length];	//存活的飞行物数组
		for(int i = 0; i < flyings.length ; i++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){
				flyingLives[index++] = f;			//不越界的飞行物添加到该数组中
			}
		}
		flyings = Arrays.copyOf(flyingLives,index);
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];	//存活的子弹数组
		for(int i = 0; i < bullets.length ; i++){
			Bullet b = bullets[i];
			if(!b.outOfBounds()){
				bulletLives[index++] = b;	//不越界的子弹添加到该数组中
			}
		}
		bullets = Arrays.copyOf(bulletLives, index);
	}
	//游戏是否结束
	public boolean isGameOver(){
		for(int i = 0 ; i < flyings.length ; i++){
			int index = -1;
			FlyingObject obj = flyings[i];
			if(hero.hit(obj)){
				index = i;
				hero.subtractLife();
				hero.setDoubleFire(0);
			}
			if(index != -1){
				FlyingObject t = flyings[index];
				flyings[index] = flyings[flyings.length - 1];
				flyings[flyings.length-1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length-1);
			}
		}
		return hero.getLife() <= 0;
	}
	
	public void checkGameOverAction(){
		if(isGameOver()){
			this.state = GAME_OVER;
		}
	}
	
	public void paintState(Graphics g){
		switch (state) {
		case START:
			g.drawImage(start,0,ShootGame.HEIGHT/4,null);
			try {
				g.drawImage(ImageIO.read(ShootGame.class.getResource("click.png")),
						200,
						333,null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case PAUSE:
			g.drawImage(pause,(ShootGame.WIDTH-pause.getWidth())/2,
						(ShootGame.HEIGHT-pause.getHeight())/2,null);
			break;
		case GAME_OVER:
			g.drawImage(gameover,41,233,null);
			break;
		default:
			break;
		}
	}
	public static void main(String[] args) {
		JFrame frame = new JFrame("飞机大战 ");
		ShootGame game = new ShootGame();
		frame.add(game);		//添加面板
		Image frame_icon=Toolkit.getDefaultToolkit().createImage(ShootGame.class.getResource("title.png"));
		frame.setIconImage(frame_icon);
		frame.setResizable(false);	//不可改变大小
		frame.setSize(ShootGame.WIDTH, ShootGame.HEIGHT); //设置大小
		frame.setAlwaysOnTop(true);		//设置总在最上
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//默认关闭操作
		frame.setLocationRelativeTo(null);	//起始位置	
		frame.setVisible(true);			//设置可见
		game.action();
	}
	
}
