package com.simservice

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.simservice.TokenizerChain.TokenizerResults
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement

import static com.google.common.base.Preconditions.checkNotNull
import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * <pre>
 * The class responsible for coordinating the data from the REST request into the {@link SimFunction}, and returning
 * the result.  It also contains the class definitions for the request and response entities.
 * </pre>
 *
 * <pre>
 *  SimRequestEntity:
 *      String textItemA
 *      String textItemB
 * </pre>
 *
 * <pre>
 *  SimResponseEntity:
 *      String message
 *      double result
 *      String md5TextItemA
 *      int tokenCountTextItemA
 *      String md5TextItemB
 *      int tokenCountTextItemB
 * </pre>
 *
 * <pre>
 *  Sample Request:
 *      POST /docsim ->
 *      {
 *          "textItemA": "The quick brown fox jumped over the lazy dog",
 *          "textItemB": "The quick brown fox jumped over the lazy dog yo"
 *      }
 *
 *  Sample Response:
 *      {
 *          "message": Item with md5 08a008a01d498c404.... is 0.9% similar to item with md5 1086e0074b3fa4f14....",
 *          "result": 0.9
 *          "md5TextItemA": "08a008a01d498c404b0c30852b39d3b8",
 *          "tokenCountTextItemA": 9,
 *          "md5TextItemB": "1086e0074b3fa4f14e214cd99b5b4ca9",
 *          "tokenCountTextItemB": 10
 *      }
 * </pre>
 */
@RestController
@RequestMapping("/docsim")
class SimSvc {

    @ApiOperation(value = "calculateSimilarity", nickname = "calculateSimilarity")
    @RequestMapping(value = "", method = POST, consumes = "application/json", produces = "application/json")
    @ApiImplicitParam(name = "simRequestEntity", value = "Text Items", required = true, dataType = "SimRequestEntity")
    @ApiResponses(value = [
        @ApiResponse(code = 200, message = "Success", response = SimResponseEntity.class),
        @ApiResponse(code = 400, message = "Bad Request"),
        @ApiResponse(code = 500, message = "Internal Server Error")
    ])
    ResponseEntity calculateSimilarity(@RequestBody SimRequestEntity simRequestEntity) {
        checkNotNull(simRequestEntity)
        TokenizerResults itemA = new TokenizerChain().compute(simRequestEntity.textItemA)
        TokenizerResults itemB = new TokenizerChain().compute(simRequestEntity.textItemB)
        double sim = SimFunction.getSimilarity(itemA, itemB)
        String message = generateResponseMessage(itemA, itemB, sim)
        ResponseEntity.ok(
                new SimResponseEntity(
                    message, sim,
                    itemA.md5, itemA.tokenCount,
                    itemB.md5, itemB.tokenCount
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
        @JsonProperty(required = true)
        @ApiModelProperty(notes = "The text for item A", required = true)
        String getTextItemA() { return textItemA }
        @JsonProperty(required = true)
        @ApiModelProperty(notes = "The text for item B", required = true)
        String getTextItemB() { return textItemB }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "SimResponseEntity")
    static class SimResponseEntity {
        private String message
        private double result
        private String md5TextItemA
        private int tokenCountTextItemA
        private String md5TextItemB
        private int tokenCountTextItemB
        SimResponseEntity(String message,
                          double result,
                          String md5TextItemA,
                          int tokenCountTextItemA,
                          String md5TextItemB,
                          int tokenCountTextItemB) {
            this.message = message
            this.result = result
            this.md5TextItemA = md5TextItemA
            this.tokenCountTextItemA = tokenCountTextItemA
            this.md5TextItemB = md5TextItemB
            this.tokenCountTextItemB = tokenCountTextItemB
        }
        String getMessage() { return message }
        double getResult() { return result }
        String getMd5TextItemA() { return md5TextItemA }
        int getTokenCountTextItemA() { return tokenCountTextItemA }
        String getMd5TextItemB() { return md5TextItemB }
        int getTokenCountTextItemB() { return tokenCountTextItemB }
    }
}
