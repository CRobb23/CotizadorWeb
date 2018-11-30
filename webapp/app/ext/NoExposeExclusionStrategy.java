/**
 * 
 */
package ext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * @author rjanixz
 *
 */
public class NoExposeExclusionStrategy implements ExclusionStrategy {

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface NoExpose {
	}
	
	public NoExposeExclusionStrategy() {
		
	}
	
	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}
	
	public boolean shouldSkipField(FieldAttributes fields) {
		return fields.getAnnotation(NoExpose.class) != null ;
	}
}
