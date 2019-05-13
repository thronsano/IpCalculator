package com.ipCalculator.MVC.controllers;

import com.ipCalculator.MVC.services.CalculatorService;
import com.ipCalculator.entity.model.NetworkTable;
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
    public ModelAndView getPublicCalculations(@RequestParam("networkIp") String networkIp,
                                              @RequestParam(value = "networkMask", required = false, defaultValue = "") String networkMask,
                                              @RequestParam(value = "clientsAmount", required = false, defaultValue = "") String clientsAmount,
                                              ModelAndView modelAndView) {
        NetworkTable networkTable = null;
        boolean prevCalculationUsedMask = true;

        if (!"".equals(networkMask)) {
            networkTable = calculatorService.getNetworkByMask(networkIp, networkMask);
        } else if (!"".equals(clientsAmount)) {
            prevCalculationUsedMask = false;
            networkTable = calculatorService.getNetworkByClientsAmount(networkIp, clientsAmount, "0");
        }

        modelAndView.addObject("networkTable", networkTable);
        modelAndView.addObject("prevCalculationUsedMask", prevCalculationUsedMask);
        modelAndView.setViewName("publicTemplates/public");
        return modelAndView;
    }

    @RequestMapping(value = "private/calculator", method = GET)
    public ModelAndView getPrivateCalculations(@RequestParam("networkIp") String networkIp,
                                               @RequestParam(value = "networkMask", required = false, defaultValue = "") String networkMask,
                                               @RequestParam(value = "clientsAmount", required = false, defaultValue = "") String clientsAmount,
                                               @RequestParam(value = "padding", required = false, defaultValue = "0") String padding,
                                               ModelAndView modelAndView) {
        modelAndView.addObject("previousNetworks", calculatorService.getPreviousNetworks());
        NetworkTable networkTable = null;
        boolean prevCalculationUsedMask = true;

        if (!"".equals(networkMask)) {
            networkTable = calculatorService.getNetworkByMask(networkIp, networkMask);
        } else if (!"".equals(clientsAmount)) {
            prevCalculationUsedMask = false;
            networkTable = calculatorService.getNetworkByClientsAmount(networkIp, clientsAmount, padding);
        }

        modelAndView.addObject("networkTable", networkTable);
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

    @RequestMapping(value = "home", method = GET)
    public ModelAndView getPreviousNetworks(ModelAndView modelAndView) {
        modelAndView.addObject("previousNetworks", calculatorService.getPreviousNetworks());
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
