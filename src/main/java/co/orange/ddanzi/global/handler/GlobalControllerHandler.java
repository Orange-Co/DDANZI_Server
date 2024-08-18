package co.orange.ddanzi.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerHandler {
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    @ModelAttribute
    public void setRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }
}
