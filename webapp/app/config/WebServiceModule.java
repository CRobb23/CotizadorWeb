package config;

import com.google.inject.AbstractModule;

import service.CreateRequestService;
import service.InspectionService;
import service.PolicyFileService;
import service.PolicyService;
import service.implementation.CreateRequestServiceImpl;
import service.implementation.InspectionServiceImpl;
import service.implementation.PolicyFileServiceFtpImpl;
import service.implementation.PolicyServiceImpl;

public class WebServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PolicyService.class).to(PolicyServiceImpl.class);
		bind(CreateRequestService.class).to(CreateRequestServiceImpl.class);
		bind(InspectionService.class).to(InspectionServiceImpl.class);
		bind(PolicyFileService.class).to(PolicyFileServiceFtpImpl.class);
	}

}
