import com.simservice.SimFunction
import com.simservice.TokenizerChain
import org.junit.Test

import static org.junit.Assert.assertEquals

class SimiliarityTest {

    String textA = "The quick brown fox jumped over the lazy dog yo"

    String textB = "The quick brown fox jumped over the lazy dog"

    @Test
    void testSimilarity() {
        def resultsA = new TokenizerChain().compute(textA)
        def resultsB = new TokenizerChain().compute(textB)
        assertEquals(0.9, (double)SimFunction.getSimilarity(resultsA, resultsB), 0.0)
    }

    @Test
    void testSimilarityOfSameText() {
        def resultsA = new TokenizerChain().compute(textA)
        def resultsB = new TokenizerChain().compute(textA)
        assertEquals(1.0, (double)SimFunction.getSimilarity(resultsA, resultsB), 0.0)
    }

}
