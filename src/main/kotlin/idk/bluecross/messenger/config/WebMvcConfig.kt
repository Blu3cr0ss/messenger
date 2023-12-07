package idk.bluecross.messenger.config

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller

import org.springframework.web.method.HandlerTypePredicate

import org.springframework.web.servlet.config.annotation.PathMatchConfigurer

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        configurer.addPathPrefix(
            "/api",
            HandlerTypePredicate.forAnnotation(Controller::class.java)
        )
    }
}