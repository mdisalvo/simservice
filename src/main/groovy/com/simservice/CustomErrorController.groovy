package com.simservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.web.ErrorAttributes
import org.springframework.boot.autoconfigure.web.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.ServletRequestAttributes

import javax.servlet.http.HttpServletRequest

@RestController
class CustomErrorController implements ErrorController {

    String MESSAGE = "message"

    @Autowired ErrorAttributes errorAttributes

    CustomErrorController() { }

    @RequestMapping(value = "/error", produces = "application/json")
    @ResponseBody
    ResponseEntity error(HttpServletRequest request) {
        def body = getErrorAttributes(request, getTraceParameter(request))
        def status = getStatus(request)
        new ResponseEntity(body, status)
    }

    @Override String getErrorPath() { "/error" }

    static def getTraceParameter(HttpServletRequest request) {
        request.getParameter("trace") != null && !"false".equals(request.getParameter("trace").toLowerCase())
    }

    static def getStatus(HttpServletRequest request) {
        def statusCode = request.getAttribute("javax.servlet.error.status_code")
        if (statusCode != null) {
            return HttpStatus.valueOf(statusCode)
        }
        HttpStatus.INTERNAL_SERVER_ERROR
    }

    def getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        def requestAttributes = new ServletRequestAttributes(request)
        def errorAttributesMap = errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace)
        errorAttributesMap <<
                [
                        "$MESSAGE":
                                errorAttributes.getError(requestAttributes) != null ?
                                        errorAttributes.getError(requestAttributes).getLocalizedMessage() :
                                        null
                ]
        errorAttributesMap
    }
}
