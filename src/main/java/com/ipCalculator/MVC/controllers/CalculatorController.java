package com.ipCalculator.MVC.controllers;

import com.ipCalculator.MVC.services.CalculatorService;
import com.ipCalculator.entity.model.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/public")
public class CalculatorController {
    @Autowired
    CalculatorService calculatorService;

    @RequestMapping(value = "/calculator", method = GET)
    public ModelAndView getEditUserPage(@RequestParam("networkIp") String networkIp,
                                        @RequestParam("networkMask") String networkMask,
                                        ModelAndView modelAndView) {
        Network network = calculatorService.getNetwork(networkIp, networkMask);
        modelAndView.addObject("network", network);
        modelAndView.setViewName("publicTemplates/public");
        return modelAndView;
    }
}
