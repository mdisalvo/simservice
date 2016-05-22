package com.simservice

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

/**
 * @author Michael Di Salvo
 * mdisalvo@kcura.com
 */
@RestController
@RequestMapping("/docsim")
class SimSvc {

    @RequestMapping(value = "", method = GET, produces = "application/json")
    ResponseEntity testSvc() {
        ResponseEntity.ok("Testing the service.")
    }

}
