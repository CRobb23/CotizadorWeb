package jobs;

import models.ER_Incident;
import models.ER_Inspection;
import notifiers.Mails;
import play.jobs.Job;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SencCenterListJob extends Job {


	private ER_Incident incident;


	public SencCenterListJob( ER_Incident incident) {

		this.incident = incident;
	}

	public void doJob() {
		Mails.centersList(incident);

    }
}
