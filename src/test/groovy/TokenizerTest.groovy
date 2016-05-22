import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import com.simservice.TokenizerChain
import org.apache.log4j.Logger
import org.junit.BeforeClass
import org.junit.Test

import static org.junit.Assert.assertEquals

class TokenizerTest {

    private static final Logger LOG = Logger.getLogger(this)

    static TokenizerChain tc

    String text = "The quick brown fox jumped over the lazy dog"

    String truth =
            "[" +
                "[29:2, 26:1, 11:1, 15:1, 19:1, 24:1, 21:1, 13:1], " +
                "[28:1, 55:1, 37:1, 26:1, 34:2, 43:1, 53:1, 50:1], " +
                "[1831650447:2, 1786049309:1, -92605329:1, -254700245:1, 1889725096:1, -1667599045:1, -1271595505:1, -1341204474:1], " +
                "[3:4, 5:2, 6:1, 4:2]" +
            "]"


    @BeforeClass
    static void before() {
        LOG.info("Initializing TokenizerChain...")
        tc = new TokenizerChain()
    }

    @Test
    void testTokenizer() {
        LOG.info("Truth data: $truth")
        def results = tc.compute(text)
        assertEquals(truth, results.sigs.toString())
        assertEquals(9, results.tokenCount)
        assertEquals(Hashing.md5().newHasher().putString(text, Charsets.UTF_8).hash().toString(), results.md5)
    }

}
