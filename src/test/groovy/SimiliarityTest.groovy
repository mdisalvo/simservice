import com.simservice.SimFunction
import com.simservice.TokenizerChain
import com.simservice.TokenizerChain.TokenizerResults
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 * @author Michael Di Salvo
 * mdisalvo@kcura.com
 */
class SimiliarityTest {

    String textA = "The quick brown fox jumped over the lazy dog yo"

    String textB = "The quick brown fox jumped over the lazy dog"

    @Test
    void testSimilarity() {
        def resultsA = new TokenizerChain().compute(textA)
        def resultsB = new TokenizerChain().compute(textB)
        int denom = getDenom(resultsA, resultsB)

        assertEquals(0.9, (double)SimFunction.getSimilarity(resultsA, resultsB, denom), 0.0)
    }

    @Test
    void testSimilarityOfSameText() {
        def resultsA = new TokenizerChain().compute(textA)
        def resultsB = new TokenizerChain().compute(textA)
        int denom = getDenom(resultsA, resultsB)

        assertEquals(1.0, (double)SimFunction.getSimilarity(resultsA, resultsB, denom), 0.0)
    }

    static def getDenom(TokenizerResults itemA, TokenizerResults itemB) {
        itemA.tokenCount > itemB.tokenCount ? itemA.tokenCount : itemB.tokenCount
    }

}
