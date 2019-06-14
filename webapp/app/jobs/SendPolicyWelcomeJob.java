package jobs;

import models.ER_Incident;
import models.ER_User;
import models.ws.PolicyResponse;
import notifiers.Mails;
import play.jobs.Job;

public class SendPolicyWelcomeJob extends Job {

    private PolicyResponse policyResponse;
    private ER_Incident incident;

    public SendPolicyWelcomeJob( PolicyResponse policyResponse,ER_Incident incident) {
        this.policyResponse = policyResponse;
        this.incident = incident;
    }
    public void doJob() {
        Mails.welcomePolicyGenerated(this.incident, this.policyResponse.getPolicy());
    }
}
