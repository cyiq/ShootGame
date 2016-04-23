package cyiq.shoot;

import java.io.IOException;


import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Audio {
	private static AudioStream as;
	public static void shoot(){
		play("shoot.wav");
		
	}
	public static void crash(){
		play("crash.wav");
	}
	private static void play(String name){
		try {
			as= new AudioStream(Audio.class.getResource(name).openStream());
			AudioPlayer.player.start(as);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
