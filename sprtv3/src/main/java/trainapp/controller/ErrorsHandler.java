package trainapp.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(Exception.class)
    public @ResponseBody String handleAllException(Exception ex) {
        return ex.getMessage();
    }
}

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class BadIdHandler {
    @ExceptionHandler(NumberFormatException.class)
    public @ResponseBody String handleAllException(Exception ex) {
        return "Use correct int ID";
    }

}