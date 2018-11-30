/**
 * 
 */
package ext;

import play.mvc.Controller;
import play.mvc.Util;
import play.mvc.results.RenderJson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author rjanixz
 *
 */
public abstract class NoExposeStrategyController extends Controller {

	private static final Gson gson;
	
	static {
		gson = new GsonBuilder().addSerializationExclusionStrategy(new NoExposeExclusionStrategy()).create();
	}
	
	@Util
	public static void renderJSON(Object object) {
		throw new RenderJson(gson.toJson(object));
	}
}
