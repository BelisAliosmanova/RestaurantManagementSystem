package com.example.waiter.Exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", "YOU CANNOT DELETE THAT DISH/DRINK NOW BECAUSE IT IS ORDERED BY SOMEONE! PLEASE TRY AGAIN LATER! :) ");
        return mav;
    }

}
