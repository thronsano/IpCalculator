package com.ipCalculator.MVC.controllers;

import com.ipCalculator.MVC.services.CalculatorService;
import com.ipCalculator.entity.db.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class CalculatorController {
    @Autowired
    CalculatorService calculatorService;

    @RequestMapping(value = "public/calculator", method = GET)
    public ModelAndView getPublicCalculations(@RequestParam("networkAddress") String networkAddress,
                                              @RequestParam(value = "networkMask", required = false, defaultValue = "") String networkMask,
                                              @RequestParam(value = "clientsAmount", required = false, defaultValue = "") String clientsAmount,
                                              ModelAndView modelAndView) {
        Network network = null;
        boolean prevCalculationUsedMask = true;

        if (!"".equals(networkMask)) {
            network = calculatorService.getNetworkByMask(networkAddress, networkMask);
        } else if (!"".equals(clientsAmount)) {
            prevCalculationUsedMask = false;
            network = calculatorService.getNetworkByClientsAmount(networkAddress, clientsAmount, "0");
        }

        modelAndView.addObject("network", network);
        modelAndView.addObject("prevCalculationUsedMask", prevCalculationUsedMask);
        modelAndView.setViewName("publicTemplates/public");
        return modelAndView;
    }

    @RequestMapping(value = "private/calculator", method = GET)
    public ModelAndView getPrivateCalculations(@RequestParam("networkAddress") String networkAddress,
                                               @RequestParam(value = "networkMask", required = false, defaultValue = "") String networkMask,
                                               @RequestParam(value = "clientsAmount", required = false, defaultValue = "") String clientsAmount,
                                               @RequestParam(value = "padding", required = false, defaultValue = "0") String padding,
                                               ModelAndView modelAndView) {
        modelAndView.addObject("previousNetworks", calculatorService.getPreviousNetworks());
        Network network = null;
        boolean prevCalculationUsedMask = true;

        if (!"".equals(networkMask)) {
            network = calculatorService.getNetworkByMask(networkAddress, networkMask);
        } else if (!"".equals(clientsAmount)) {
            prevCalculationUsedMask = false;
            network = calculatorService.getNetworkByClientsAmount(networkAddress, clientsAmount, padding);
        }

        modelAndView.addObject("network", network);
        modelAndView.addObject("prevCalculationUsedMask", prevCalculationUsedMask);
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = "private/calculator", method = POST)
    public ModelAndView saveNetwork(@RequestParam("networkCacheKey") String networkCacheKey,
                                    @RequestParam("networkName") String networkName,
                                    ModelAndView modelAndView) {
        calculatorService.saveNetwork(networkCacheKey, networkName);
        modelAndView.addObject("previousNetworks", calculatorService.getPreviousNetworks());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = "private/deleteNetwork", method = POST)
    public ModelAndView saveNetwork(@RequestParam("networkId") String networkId,
                                    ModelAndView modelAndView) {
        calculatorService.deleteNetwork(networkId);
        modelAndView.addObject("previousNetworks", calculatorService.getPreviousNetworks());
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = "home", method = GET)
    public ModelAndView getPreviousNetworks(ModelAndView modelAndView) {
        modelAndView.addObject("previousNetworks", calculatorService.getPreviousNetworks());
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
