package config;

import com.google.inject.AbstractModule;

import service.*;
import service.implementation.*;

public class WebServiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PolicyService.class).to(PolicyServiceImpl.class);
		bind(JsonService.class).to(JsonServiceImpl.class);
		bind(CreateRequestService.class).to(CreateRequestServiceImpl.class);
		bind(InspectionService.class).to(InspectionServiceImpl.class);
		bind(PolicyFileService.class).to(PolicyFileServiceFtpImpl.class);
		bind(ClientsQueryWebService.class).to(ClientsQueryServiceBusImpl.class);
		bind(AverageValueQueryWebService.class).to(AverageValueQueryServiceBusImpl.class);
		bind(PersonQueryWebService.class).to(PersonQueryServiceBusImpl.class);
		bind(BusinessQueryWebService.class).to(BusinessQueryServiceBusImpl.class);
		bind(PolicyInputWebService.class).to(PolicyInputServiceBusImpl.class);
		bind(InsurableVehicleQueryWebService.class).to(InsurableVehicleQueryServiceBusImpl.class);
	}

}
