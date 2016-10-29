package com.android.tools.fd.runtime;

import com.android.tools.fd.common.Log;
import java.lang.reflect.Field;
import java.util.logging.Level;

public abstract class AbstractPatchesLoaderImpl implements PatchesLoader {
    public abstract String[] getPatchedClasses();

    public boolean load() {
        try {
            String[] arrayOfString = getPatchedClasses();
            int j = arrayOfString.length;
            int i = 0;
            while (i < j) {
                String str = arrayOfString[i];
                Object localObject2 = getClass().getClassLoader();
                Object localObject1 = ((ClassLoader) localObject2).loadClass(str + "$override").newInstance();
                localObject2 = ((ClassLoader) localObject2).loadClass(str).getDeclaredField("$change");
                ((Field) localObject2).setAccessible(true);
                Object localObject3 = ((Field) localObject2).get(null);
                if (localObject3 != null) {
                    localObject3 = localObject3.getClass().getDeclaredField("$obsolete");
                    if (localObject3 != null) {
                        ((Field) localObject3).set(null, Boolean.valueOf(true));
                    }
                }
                ((Field) localObject2).set(null, localObject1);
                if ((Log.logging != null) && (Log.logging.isLoggable(Level.FINE))) {
                    Log.logging.log(Level.FINE, String.format("patched %s", new Object[] { str }));
                }
                i += 1;
            }
            return true;
        } catch (Exception localException) {
            if (Log.logging != null) {
                Log.logging.log(Level.SEVERE, String.format("Exception while patching %s", new Object[] { "foo.bar" }), localException);
            }
            return false;
        }
    }
}
