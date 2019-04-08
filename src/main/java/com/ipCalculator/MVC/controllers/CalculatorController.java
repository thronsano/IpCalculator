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
                                        @RequestParam(value = "networkMask", required = false, defaultValue = "") String networkMask,
                                        @RequestParam(value = "clientsAmount", required = false, defaultValue = "") String clientsAmount,
                                        ModelAndView modelAndView) {
        Network network = null;
        boolean prevCalculationUsedMask = true;

        if (!"".equals(networkMask)) {
            network = calculatorService.getNetworkByMask(networkIp, networkMask);
        } else if (!"".equals(clientsAmount)) {
            prevCalculationUsedMask = false;
            network = calculatorService.getNetworkByClientsAmount(networkIp, clientsAmount);
        }

        modelAndView.addObject("network", network);
        modelAndView.addObject("prevCalculationUsedMask", prevCalculationUsedMask);
        modelAndView.setViewName("publicTemplates/public");

        return modelAndView;
    }
}
