package jobs;

import controllers.Incidents;
import models.ER_Incident;
import play.Logger;
import play.jobs.Every;
import play.jobs.Job;
import play.jobs.On;
import play.jobs.OnApplicationStart;

import java.util.Calendar;

/**
 * Created by lester on 26/06/2017.
 */

@OnApplicationStart
@On("0 0 0 * * ?")
public class DailyCorrelativeJob extends Job{

    public void doJob() {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        Incidents.dailyCorrelativeNumber = ER_Incident.count("creationDate >= ?", now.getTime());
        Logger.info("next: "+ Incidents.dailyCorrelativeNumber );
    }


}

