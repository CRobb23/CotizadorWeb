package jobs;

import models.ER_User;
import notifiers.Mails;
import play.jobs.Job;

public class ResetPasswordJob extends Job {
  private ER_User user;
  private String code;

    public ResetPasswordJob( ER_User user , String code) {

        this.user = user;
        this.code = code;
    }
    public void doJob() {
        Mails.passwordReset(this.user, this.code);
    }
}
