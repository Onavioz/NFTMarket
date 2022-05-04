package service;

import java.util.Timer;
import java.util.TimerTask;

public class CronStarter {
	
	//the cronJob method get the parameters and make the cronJob actually start
	public void cronJob(CronJobTask cronJobTask, long delayFromStart,long timeToWait) {
		Timer timer =new Timer();
		timer.schedule(cronJobTask, delayFromStart , timeToWait);
	}
}
