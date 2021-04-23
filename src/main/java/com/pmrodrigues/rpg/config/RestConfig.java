package com.pmrodrigues.rpg.config;

import java.lang.reflect.ParameterizedType;

import javax.persistence.Entity;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

@Configuration
@Slf4j
public class RestConfig implements RepositoryRestConfigurer {

	@Autowired
	private ApplicationContext bean;

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

		var reflections = new Reflections("com.pmrodrigues.rpg.entities");
		var entities = reflections.getTypesAnnotatedWith(Entity.class);

		entities.stream().forEach(entity -> {
			log.debug("expose id from class {}", entity);
			config.exposeIdsFor(entity);
		});

		
	}
	
	@Override
	public void configureConversionService(ConfigurableConversionService conversionService) {
		
		var reflections = new Reflections("com.pmrodrigues.rpg.converters");
		var conveters = reflections.getSubTypesOf(Converter.class);

		conveters.stream().forEach(converter -> {

			var type = (ParameterizedType)converter.getGenericInterfaces()[0];
			var source = type.getActualTypeArguments()[0];
			var target = type.getActualTypeArguments()[1];
					
			log.debug("add a converter {} for {} to {} ", converter, source, target);

			conversionService.addConverter(bean.getBean(converter));
			
			
		});
	}


}