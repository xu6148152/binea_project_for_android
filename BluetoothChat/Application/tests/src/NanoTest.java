import junit.framework.TestCase;

import static com.google.protobuf.UnittestSimpleNano.SimpleMessageNano;

/**
 * Created by xubinggui on 8/27/15.
 */
public class NanoTest extends TestCase{

    @Override protected void setUp() throws Exception {
        super.setUp();
        SimpleMessageNano msg= new SimpleMessageNano();
        assertEquals(123, msg.d);
    }
}
