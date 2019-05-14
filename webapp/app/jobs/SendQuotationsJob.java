package jobs;

import models.ER_Incident;
import notifiers.Mails;
import play.jobs.Job;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SendQuotationsJob extends Job {

	private List<ByteArrayOutputStream> streamArray;
	private ER_Incident incident;

	public SendQuotationsJob(List<ByteArrayOutputStream> streamArray, ER_Incident incident) {
		this.streamArray = streamArray;
		this.incident = incident;
	}

	public void doJob() {
		Mails.quotations(incident, streamArray, true);
    }
}
