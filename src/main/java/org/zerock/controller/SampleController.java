package org.zerock.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sample/*")
@Log4j2
public class SampleController {

    @RequestMapping(value="/basic", method = {RequestMethod.GET, RequestMethod.POST})
    public void basicGet() {

        log.info("basic get...............");

    }

    @GetMapping("/basicOnlyGet")
    public void basicGet2() {

        log.info("basic get only get.................");

    }

    @RequestMapping("")
    public void basic() {

        log.info("basic...................");

    }

    @GetMapping("/doA")
    public void doA() {

        log.info("doA called.............");
        log.info("-----------------------");
    }
}
