package com.simservice

import com.google.common.base.Charsets
import com.google.common.hash.Hasher
import com.google.common.hash.Hashing

/**
 * @author Michael Di Salvo
 * mdisalvo@kcura.com
 */
class TokenizerChain {

    private abstract class Tokenizer {
        def vals = [:]
        abstract def handle(f)
        def results() { vals }
        void process(f) { vals.compute(f) { k,v -> v == null ? 1 : v + 1 } }
    }

    private class Tokenizer1 extends Tokenizer {
        @Override def handle(f) { process(Character.getNumericValue(f.charAt(0))) }
    }

    private class Tokenizer2 extends Tokenizer {
        private Character last = ' '
        @Override def handle(f) {
            char n = f.charAt(0)
            if (last.charValue() != null) { process(Character.getNumericValue(last.charValue())) }
            last = n
        }
    }

    private class Tokenizer3 extends Tokenizer {
        @Override def handle(f) { process(Hashing.md5().hashString(f, Charsets.UTF_8).asInt()) }
    }

    private class Tokenizer4 extends Tokenizer {
        @Override def handle(f) { process(f.length()) }
    }

    private tokenizers = []

    private Hasher md5

    TokenizerChain() {
        tokenizers << new Tokenizer1()
        tokenizers << new Tokenizer2()
        tokenizers << new Tokenizer3()
        tokenizers << new Tokenizer4()
        md5 = Hashing.md5().newHasher()
    }

    def compute(String text) {
        def results = []
        text.toLowerCase().tokenize().each { String token ->
            tokenizers.each { Tokenizer tokenizer ->
                tokenizer.handle(token)
            }
        }
        tokenizers.each { Tokenizer tokenizer ->
            results << tokenizer.results()
        }
        results << [md5: md5.putString(text, Charsets.UTF_8).hash()]
    }

}
