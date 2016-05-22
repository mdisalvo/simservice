package com.simservice

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.simservice.TokenizerChain.TokenizerResults
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

import static com.google.common.base.Preconditions.checkNotNull
import static org.springframework.web.bind.annotation.RequestMethod.POST

@RestController
@RequestMapping("/docsim")
class SimSvc {

    @RequestMapping(value = "", method = POST, consumes = "application/json", produces = "application/json")
    ResponseEntity testSvc(@RequestBody SimRequestEntity simRequestEntity) {
        checkNotNull(simRequestEntity)
        TokenizerResults itemA = new TokenizerChain().compute(simRequestEntity.textItemA)
        TokenizerResults itemB = new TokenizerChain().compute(simRequestEntity.textItemB)
        double sim = SimFunction.getSimilarity(itemA, itemB)
        String message = generateResponseMessage(itemA, itemB, sim)
        ResponseEntity.ok(
                new SimResponseEntity(
                    message, sim,
                    itemA.md5, itemA.tokenCount, itemA.sigs,
                    itemB.md5, itemB.tokenCount, itemB.sigs
                )
        )
    }

    private static def generateResponseMessage(TokenizerResults itemA, TokenizerResults itemB, double sim) {
        TokenizerResults larger = itemA.tokenCount > itemB.tokenCount ? itemA : itemB
        TokenizerResults smaller = itemA.tokenCount <= itemB.tokenCount ? itemA : itemB
        "Item with md5 $smaller.md5 is $sim% similar to item with md5 $larger.md5."
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "SimRequestEntity")
    static class SimRequestEntity {
        private String textItemA
        private String textItemB
        @JsonCreator
        SimRequestEntity(@JsonProperty("textItemA") String textItemA, @JsonProperty("textItemB") String textItemB) {
            this.textItemA = textItemA
            this.textItemB = textItemB
        }
        String getTextItemA() { return textItemA }
        String getTextItemB() { return textItemB }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "SimResponseEntity")
    static class SimResponseEntity {
        private String message
        private double result
        private String md5TextItemA
        private int tokenCountTextItemA
        private List<Map<Integer, Integer>> sigsItemA
        private String md5TextItemB
        private int tokenCountTextItemB
        private List<Map<Integer, Integer>> sigsItemB
        SimResponseEntity(String message,
                          double result,
                          String md5TextItemA,
                          int tokenCountTextItemA,
                          List<Map<Integer, Integer>> sigsItemA,
                          String md5TextItemB,
                          int tokenCountTextItemB,
                          List<Map<Integer, Integer>> sigsItemB) {
            this.message = message
            this.result = result
            this.md5TextItemA = md5TextItemA
            this.tokenCountTextItemA = tokenCountTextItemA
            this.sigsItemA = sigsItemA
            this.md5TextItemB = md5TextItemB
            this.tokenCountTextItemB = tokenCountTextItemB
            this.sigsItemB = sigsItemB
        }
        String getMessage() { return message }
        double getResult() { return result }
        String getMd5TextItemA() { return md5TextItemA }
        int getTokenCountTextItemA() { return tokenCountTextItemA }
        List<Map<Integer, Integer>> getSigsItemA() { return sigsItemA }
        String getMd5TextItemB() { return md5TextItemB }
        int getTokenCountTextItemB() { return tokenCountTextItemB }
        List<Map<Integer, Integer>> getSigsItemB() { return sigsItemB }
    }
}
