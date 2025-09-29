package br.com.fiap.universidade_fiap.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("error", "Ocorreu um erro inesperado. Tente novamente.");
        mv.addObject("details", ex.getMessage());
        return mv;
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        ModelAndView mv = new ModelAndView("error/400");
        mv.addObject("error", "Dados inválidos: " + ex.getMessage());
        return mv;
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex) {
        ModelAndView mv = new ModelAndView("error/500");
        mv.addObject("error", "Erro interno: " + ex.getMessage());
        return mv;
    }
}
