package com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class ConvertXmlConfig {

    @Bean
    fun xmlMapper() = XmlMapper()
}