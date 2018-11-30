package jobs;

import play.jobs.Every;
import play.jobs.Job;
import service.PolicyFileService;

import javax.inject.Inject;

@Every("15mn")
public class PolicyFileJob extends Job {

    @Inject
    static PolicyFileService policyFileService;

	public void doJob(){
	    // Search files to Download
        policyFileService.searchDownloadablePolicies();
        // Upload files already downloaded
        policyFileService.searchUploadablePolicies();
    }
	
}
