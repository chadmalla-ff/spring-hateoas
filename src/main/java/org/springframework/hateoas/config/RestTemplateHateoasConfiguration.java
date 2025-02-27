/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.hateoas.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.WebMvcHateoasConfiguration.HypermediaWebMvcConfigurer;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestTemplate;

/**
 * Spring MVC HATEOAS Configuration
 *
 * @author Greg Turnquist
 * @author Oliver Drotbohm
 */
@Configuration
class RestTemplateHateoasConfiguration {

	@Bean
	static HypermediaRestTemplateBeanPostProcessor hypermediaRestTemplateBeanPostProcessor(
			ObjectProvider<HypermediaWebMvcConfigurer> configurer) {
		return new HypermediaRestTemplateBeanPostProcessor(configurer);
	}
	
	/**
	 * {@link BeanPostProcessor} to register hypermedia support with {@link RestTemplate} instances found in the
	 * application context.
	 *
	 * @author Oliver Gierke
	 * @author Greg Turnquist
	 */
	@RequiredArgsConstructor
	static class HypermediaRestTemplateBeanPostProcessor implements BeanPostProcessor {

		private final ObjectProvider<HypermediaWebMvcConfigurer> configurer;

		/*
		 * (non-Javadoc)
		 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object, java.lang.String)
		 */
		@NonNull
		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

			if (!RestTemplate.class.isInstance(bean)) {
				return bean;
			}

			configurer.getObject().extendMessageConverters(((RestTemplate) bean).getMessageConverters());

			return bean;
		}
	}
}
