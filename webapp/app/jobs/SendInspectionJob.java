package jobs;

import models.ER_Incident;
import notifiers.Mails;
import play.jobs.Job;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SendInspectionJob extends Job {


	private ER_Incident incident;

	public SendInspectionJob( ER_Incident incident) {
				this.incident = incident;
	}

	public void doJob() {
		Mails.finishInspection(incident);

    }
}
