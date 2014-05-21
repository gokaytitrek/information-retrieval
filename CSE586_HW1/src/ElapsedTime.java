import java.util.concurrent.TimeUnit;


public class ElapsedTime {

	private long startTime;
    private long endTime;
    
	public void start() {
		startTime = System.currentTimeMillis();
    }
	
	public void stop() {
        endTime = System.currentTimeMillis();
    }
	
	public void getElapsedTime() {
        long elapsedTime = endTime - startTime;
	    System.out.println(String.format("%d min, %d sec", 
	    	    TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
	    	    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
	    	    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
	    	));
    }

}
