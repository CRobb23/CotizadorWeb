package config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import play.modules.guice.GuiceSupport;

public class GuiceConfig extends GuiceSupport {

	@Override
	protected Injector configure() {
		Module[] modules = {new WebServiceModule()};
		
		return Guice.createInjector(modules);
	}
}