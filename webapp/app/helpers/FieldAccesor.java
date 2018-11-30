package helpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

public class FieldAccesor {

	public static final boolean isNull(Object bean, String associationPath) {
		return !isNotNull(bean, associationPath);
	}

	public static final boolean isNotNull(Object bean, String associationPath) {
		boolean result = false;
		if (bean != null && associationPath != null
				&& associationPath.trim().length() > 0) {
			try {
				result = PropertyUtils.getNestedProperty(bean, associationPath) != null;
			} catch (NestedNullException e) {
				result = false;
			} catch (IllegalAccessException e) {
				result = false;
			} catch (InvocationTargetException e) {
				result = false;
			} catch (NoSuchMethodException e) {
				result = false;
			}
		}
		return result;

	}

	public static final boolean isNull(Object bean, String[] associationPaths) {
		return !isNotNull(bean, associationPaths);
	}

	public static final boolean isNotNull(Object bean, String[] associationPaths) {
		boolean result = false;
		if (bean != null && associationPaths != null
				&& associationPaths.length > 0) {
			try {
			  for(Object associationPath: associationPaths){
				result = PropertyUtils.getNestedProperty(bean, associationPath.toString()) != null;
				if(!result){
					break;
				}
			  }
			} catch (NestedNullException e) {
				result = false;
			} catch (IllegalAccessException e) {
				result = false;
			} catch (InvocationTargetException e) {
				result = false;
			} catch (NoSuchMethodException e) {
				result = false;
			}
		}
		return result;
	}
	
	public static final boolean isEmptyOrNull(Object bean, String field) {
		boolean result = false;
		if(bean != null && field != null){
			try{
				Object value = PropertyUtils.getNestedProperty(bean, field);
				if(isEmptyOrNull(value)){
					return true;
				}
			}catch(NestedNullException e){
				result = true;
			}catch(IllegalAccessException e){
				result = true;
			}catch(InvocationTargetException e){
				result = true;
			}catch(NoSuchMethodException e){
				result = true;
			}
		}
		return result;
	}

	public static final Boolean isEmptyOrNull(String ...strings) {
		return strings == null || strings.length < 1;
	}

	public static final Boolean isEmptyOrNull(Object string) {
		return string == null || string.toString().trim().isEmpty();
	}
	
	public static final Boolean isEmptyOrNull(String string) {
		return string == null || string.trim().isEmpty();
	}

	public static final Boolean isEmptyOrNull(Object[] array) {
		return array == null || array.length < 1;
	}

	public static final Boolean isEmptyOrNull(byte[] array) {
		return array == null || array.length < 1;
	}

	public static final Boolean isEmptyOrNull(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static final Boolean isEmptyOrNull(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static final Boolean allAreEmptyOrNull(String ...strings) {
		if (strings != null) {
			for (String string : strings) {
				if (!isEmptyOrNull(string)) {
					return Boolean.FALSE;
				}
			}
		} else {
			return null;
		}
		return Boolean.TRUE;
	}

	public static final Boolean oneIsEmptyOrNull(String ...strings) {
		if (strings != null) {
			for (String string : strings) {
				if (isEmptyOrNull(string)) {
					return Boolean.TRUE;
				}
			}
		} else {
			return null;
		}
		return Boolean.FALSE;
	}

	@SuppressWarnings("unchecked")
	public static final <T> T  getPropertyValue(Object bean, String associationPath){
		T result = null;
		if (bean != null && associationPath != null
				&& associationPath.trim().length() > 0) {
			try {
				result = (T) PropertyUtils.getNestedProperty(bean, associationPath);
			} catch (NestedNullException e) {
				result = null;
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				result = null;
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				result = null;
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				result = null;
				e.printStackTrace();
			}
		}
		return result;
	}

	public static final void setPropertyValue(Object bean, String associationPath, Object value){
		try {
			PropertyUtils.setNestedProperty(bean, associationPath, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static final Map<String, Object> describe(Object bean) {
		Map<String, Object> params = new HashMap<String, Object>();
		if(bean != null){
			try{
				params = PropertyUtils.describe(bean);
			}catch(NestedNullException e){
			}catch(IllegalAccessException e){
			}catch(InvocationTargetException e){
			}catch(NoSuchMethodException e){
			}
		}
		return params;
	}
	
	public static final void setNullToProperties(Object bean, Set<String> excludeProperties){
		for(Field field: bean.getClass().getDeclaredFields()){
			if(!excludeProperties.contains(field.getName())){
				setPropertyValue(bean, field.getName(), null);
			}
		}
	}

}