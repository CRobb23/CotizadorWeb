package jobs;

import models.ER_Incident;
import models.ER_Inspection;
import notifiers.Mails;
import play.jobs.Job;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SendAddressInspectionJob extends Job {


	private ER_Incident incident;

	private ER_Inspection inspection;

	public SendAddressInspectionJob( ER_Incident incident, ER_Inspection inspection) {

		this.incident = incident;
		this.inspection = inspection;
	}

	public void doJob() {
		Mails.addressInspection(incident, inspection);

    }
}
