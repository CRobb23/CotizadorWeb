package binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.math.BigDecimal;

import play.data.binding.TypeBinder;

public class MoneyBinder implements TypeBinder<String> {
	
	@Override
	public Object bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) throws Exception {
		if (value==null || value.isEmpty()) {
			return null;
		}
		value = value.replaceAll(",","");
		return new BigDecimal(value);
	}

}
