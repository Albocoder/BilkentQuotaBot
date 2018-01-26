import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class QuotaSpy {
	
	public static void main(String[] args) {
		
		//************** This is my code "Spy and Notify" *****************
		while(true){
			// TODO: show demo for this!
			DataSearcher x = new DataSearcher("cs","mlgknabfaahgksd338bree8823",101,3);
			int quota = x.singleSearchAvailQuota();
			if(quota>0 ){
				System.out.println("FREE!!!!!");
				try {
					alert();
				} catch (LineUnavailableException e) { System.out.println("Error: "+e); }
			}
			else{
				System.out.println("full");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) { e.printStackTrace(); }
			}
		}
	}
	public static void alert() throws LineUnavailableException{
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
}