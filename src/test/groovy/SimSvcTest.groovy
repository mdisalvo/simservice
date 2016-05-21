import com.simservice.SimSvc
import org.junit.BeforeClass
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * @author Michael Di Salvo
 * michael.vincent.disalvo@gmail.com
 */
class SimSvcTest {

    static SimSvc svc

    @BeforeClass
    static void before() {
        svc = new SimSvc()
    }

    @Test
    void testSimSvc() throws Exception {
        assertEquals(svc.testMethod(1, 2), 3)
    }


}
