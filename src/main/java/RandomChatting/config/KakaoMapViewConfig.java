package RandomChatting.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/** Provides a Kakao browser SDK key from runtime configuration to Thymeleaf views. */
@ControllerAdvice
public class KakaoMapViewConfig {
    @Value("${kakao.javascript.key:}")
    private String javascriptKey;

    @ModelAttribute("kakaoJavascriptKey")
    public String kakaoJavascriptKey() {
        return javascriptKey;
    }
}
