package jobs;

import models.ER_User;
import notifiers.Mails;
import play.jobs.Job;

public class UserRegistrationJob extends Job {

    private ER_User user;

    public UserRegistrationJob( ER_User user) {

        this.user = user;
    }
    public void doJob() {
        Mails.welcomeUserFinal(this.user);
    }


}
