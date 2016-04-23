package cyiq.shoot;
/**
 * 奖励
 * @author CYIQ
 */
public interface Award {
	int DOUBLE_FIRE = 0; //双倍火力
	int LIFE = 1;		//一条命
	int getType(); 		//获取奖励类型
}
