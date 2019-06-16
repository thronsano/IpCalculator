package com.ipCalculator.MVC.controllers;

import com.ipCalculator.MVC.services.CalculatorService;
import com.ipCalculator.entity.model.NetworkWorkflowResult;
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
        NetworkWorkflowResult networkWorkflowResult = null;
        boolean prevCalculationUsedMask = true;

        if (!"".equals(networkMask)) {
            networkWorkflowResult = calculatorService.createNetworkUsingMask(networkAddress, networkMask, "1");
        } else if (!"".equals(clientsAmount)) {
            prevCalculationUsedMask = false;
            networkWorkflowResult = calculatorService.createNetworkUsingClientsAmount(networkAddress, clientsAmount, "0", "1");
        }

        modelAndView.addObject("networkMask", networkMask);
        modelAndView.addObject("clientsAmount", clientsAmount);
        modelAndView.addObject("networkList", networkWorkflowResult.getNetworkList());
        modelAndView.addObject("networkCacheKey", networkWorkflowResult.getNetworkCacheKey());
        modelAndView.addObject("prevCalculationUsedMask", prevCalculationUsedMask);
        modelAndView.setViewName("publicTemplates/public");
        return modelAndView;
    }

    @RequestMapping(value = "private/calculator", method = GET)
    public ModelAndView getPrivateCalculations(@RequestParam("networkAddress") String networkAddress,
                                               @RequestParam(value = "networkMask", required = false, defaultValue = "") String networkMask,
                                               @RequestParam(value = "clientsAmount", required = false, defaultValue = "") String clientsAmount,
                                               @RequestParam(value = "subnetAmount", required = false, defaultValue = "1") String subnetAmount,
                                               @RequestParam(value = "padding", required = false, defaultValue = "0") String padding,
                                               ModelAndView modelAndView) {
        modelAndView.addObject("previousNetworks", calculatorService.getPreviousNetworks());
        NetworkWorkflowResult networkWorkflowResult = null;
        boolean prevCalculationUsedMask = true;

        if (!"".equals(networkMask)) {
            networkWorkflowResult = calculatorService.createNetworkUsingMask(networkAddress, networkMask, subnetAmount);
        } else if (!"".equals(clientsAmount)) {
            prevCalculationUsedMask = false;
            networkWorkflowResult = calculatorService.createNetworkUsingClientsAmount(networkAddress, clientsAmount, padding, subnetAmount);
        }

        modelAndView.addObject("creationLog", networkWorkflowResult.getLog());
        modelAndView.addObject("networkMask", networkMask);
        modelAndView.addObject("clientsAmount", clientsAmount);
        modelAndView.addObject("padding", padding);
        modelAndView.addObject("networkList", networkWorkflowResult.getNetworkList());
        modelAndView.addObject("networkCacheKey", networkWorkflowResult.getNetworkCacheKey());
        modelAndView.addObject("subnetAmount", subnetAmount);
        modelAndView.addObject("prevCalculationUsedMask", prevCalculationUsedMask);
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value = "private/calculator", method = POST)
    public ModelAndView saveNetwork(@RequestParam("networkCacheKey") String networkCacheKey,
                                    @RequestParam("networkName") String networkName,
                                    ModelAndView modelAndView) {
        calculatorService.saveNetworks(networkCacheKey, networkName);
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
