import com.simservice.TokenizerChain
import org.apache.log4j.Logger
import org.junit.BeforeClass
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * @author Michael Di Salvo
 * mdisalvo@kcura.com
 */
class TokenizerTest {

    private static final Logger LOG = Logger.getLogger(this)

    static TokenizerChain tc

    String text = "The quick brown fox jumped over the lazy dog"

    String truth =
            "[" +
                "[29:2, 26:1, 11:1, 15:1, 19:1, 24:1, 21:1, 13:1], " +
                "[-1:1, 29:2, 26:1, 11:1, 15:1, 19:1, 24:1, 21:1], " +
                "[1831650447:2, 1786049309:1, -92605329:1, -254700245:1, 1889725096:1, -1667599045:1, -1271595505:1, -1341204474:1], " +
                "[3:4, 5:2, 6:1, 4:2], " +
                "[md5:08a008a01d498c404b0c30852b39d3b8]" +
            "]"

    @BeforeClass
    static void before() {
        LOG.info("Initializing TokenizerChain...")
        tc = new TokenizerChain()
    }

    @Test
    void testTokenizer() {
        LOG.info("Truth data: $truth")
        assertEquals(tc.compute(text).toString(), truth)
    }

}
