package jobs;

import models.ER_Incident;
import models.ER_Inspection;
import notifiers.Mails;
import play.jobs.Job;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SendGuardJob extends Job {


	private ER_Guard guard;


	public SendGuardJob( ER_Guard guard) {

		this.guard = guard;
	}

	public void doJob() {
		Mails.generatedGuard(guard);

    }
}
