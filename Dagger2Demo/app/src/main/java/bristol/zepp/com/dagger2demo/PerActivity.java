package bristol.zepp.com.dagger2demo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;

/**
 * Created by xubinggui on 2/5/16.
 */
@Scope @Retention(RetentionPolicy.RUNTIME) public @interface PerActivity {
    /**
     * A scoping annotation to permit objects whose lifetime should
     * conform to the life of the activity to be memoized in the
     * correct component.
     */

}
