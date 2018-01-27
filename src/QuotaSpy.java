import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.github.albocoder.jnotify.Notification;
import com.github.albocoder.jnotify.NotificationManager;

public class QuotaSpy {
	
	public static void main(String[] args) {
		// Setup the stuff with config.xml
		NotificationManager nm = new NotificationManager();
		nm.notify(Notification.TYPE_SUCCESS,"QuotaBot","Author: Erin \"Albocoder\" Avllazagaj");
		Config c = null;
		try {
			if(args.length>0)
				c = new Config(args[0]);
			else
				c = new Config();
		} catch (IOException e) {
			nm.notify(Notification.TYPE_DANGER,"Error", "config.xml file missing!");
			System.err.println("config.xml file missing!");
			System.exit(1);
		}
		
		
		//************** This is "Spy and Notify" part of the code *****************
		while(true){
			for (DataSearcher ds: c.getTargets()) {
				int quota = ds.singleSearchAvailQuota();
				if(quota>0 ){
//					new Thread(new SoundAlert()).start();
					//nm.notify(Notification.TYPE_SUCCESS,"Free Spot!", ds.getID()+" "+" is now free !");
					System.out.println(ds.getID()+" "+" is now free !");
				}
			}
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {  }
		}
	}
	
	private static class SoundAlert implements Runnable{
		public void alert() throws LineUnavailableException {
			byte[] buf = new byte[1];
			AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
			SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
			sdl.open();
			sdl.start();
			for( int i = 0; i < 1000 * (float )44100 / 1000; i++ ) {
				double angle = i / ( (float )44100 / 440 ) * 2.0 * Math.PI;
				buf[ 0 ] = (byte )( Math.sin( angle ) * 100 );
				sdl.write( buf, 0, 1 );
			}
			sdl.drain();
			sdl.stop();
		}
		@Override
		public void run() {
			try {
				alert();
			} catch (LineUnavailableException e) {}
		}
	}
}