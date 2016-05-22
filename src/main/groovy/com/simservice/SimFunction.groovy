package com.simservice

import com.simservice.TokenizerChain.TokenizerResults

class SimFunction {

    private static final int NUM_TOKENIZERS = TokenizerChain.NUM_TOKENIZERS

    private static def intersect(Map<Integer, Integer> v1, Map<Integer, Integer> v2) {
        List<Integer> v1Features = v1.keySet().asList()
        List<Integer> v2Features = v2.keySet().asList()

        List<Integer> smaller = v1Features.size() <= v2Features.size() ? v1Features : v2Features
        List<Integer> larger = v1Features.size() > v2Features.size() ? v1Features : v2Features
        Map<Integer, Integer> smallerMap = v1.size() <= v2.size() ? v1 : v2
        Map<Integer, Integer> largerMap = v1.size() > v2.size() ? v1 : v2

        int largerIndex = 0
        int smallerIndex = 0
        int numerator = 0

        while (largerIndex < larger.size() && smallerIndex < smaller.size()) {
            int feature1 = larger.get(largerIndex)
            int feature2 = smaller.get(smallerIndex)
            int diff = Integer.compare(feature1, feature2)

            if (diff == 0) {
                numerator += Math.min(largerMap.get(feature1), smallerMap.get(feature2))
                largerIndex += 1
                smallerIndex += 1
            }
            if (diff < 0) {
                smallerIndex += 1
            }
            if (diff > 0) {
                largerIndex += 1
            }
        }

        numerator
    }

    static def getSimilarity(TokenizerResults itemA, TokenizerResults itemB) {
        // Based on Jaccard Indexing
        int denom = itemA.tokenCount > itemB.tokenCount ? itemA.tokenCount : itemB.tokenCount

        // If the md5's are the same, then well, duh...
        if (itemA.md5 == itemB.md5) { return 1F }

        List<Map<Integer, Integer>> a = itemA.sigs
        List<Map<Integer, Integer>> b = itemB.sigs
        if (a.isEmpty() || b.isEmpty()) { return 0F }

        // Iterate through the signatures for both items, and find the largest numerator as determined by intersect()
        int largestSeen = Integer.MIN_VALUE
        a.eachWithIndex { s, i ->
            int num = intersect(s, b.get(i))
            largestSeen = Math.max(largestSeen, num)
        }

        // Perform the final calculation and return the result
        (largestSeen/denom)
    }

}
