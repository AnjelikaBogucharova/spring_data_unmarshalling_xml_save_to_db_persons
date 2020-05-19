package com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons

import com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons.modelDataBase.*
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons.repo.*
import com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons.xml.Persons
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.MessageChannels
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.file.dsl.Files
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.support.ErrorMessage
import java.io.File
import java.sql.Date


@Configuration
class ChanelConfiguration {
    @Bean
    fun xml() = MessageChannels.direct().get()

    @Bean
    fun errors() = MessageChannels.direct().get()
}

@Configuration
class FileConfiguration(
        private val channels: ChanelConfiguration,
        private val xmlMapper: XmlMapper,
        var personsRepository: PersonsRepository,
        var hobbyRepository: HobbyRepository
) {
    private val input = File("src/main/resources/input")
    private val errors = File("src/main/resources/errors")
    private val archive = File("src/main/resources/archive")


    @Bean
    fun filesFlow() = integrationFlow(
            Files.inboundAdapter(this.input)
                    .autoCreateDirectory(true),
            {
                poller { it.fixedDelay(500).maxMessagesPerPoll(1) }
            }
    ) {
        filter<File> { it.isFile }
        enrichHeaders(mapOf(
                "errorChannel" to "errors"
        ))

        handle { file: File, _: MessageHeaders ->
            val person = xmlMapper.readValue(file, Persons::class.java)

            for (per in person.person!!) {
                val pers  = PersonsDB(
                        fullname = per.name!!,
                        birthday = Date(per.birthday!!.time))
                personsRepository.save(pers)

                for (hobbs in per.hobbies?.hobby!!) {
                    val hobby = HobbyDB(
                            hobby_name = hobbs.hobby_name!!,
                            complexity = hobbs.complexity!!,
                            idPersons = personsRepository.findById(pers.idPersons!!).get())
                    hobbyRepository.save(hobby)

                }
            }

            file
        }
        channel("xml")
    }

    @Bean
    fun archiveFlow() = integrationFlow(channels.xml()) {
        handle(Files.outboundAdapter(archive)
                .deleteSourceFiles(true)
                .autoCreateDirectory(true)
        )
    }

    @Bean
    fun errorsFlow() = integrationFlow(channels.errors()) {
        transform<ErrorMessage> {
            it.originalMessage?.payload as File
        }
        handle(Files.outboundAdapter(errors)
                .deleteSourceFiles(true)
                .autoCreateDirectory(true)
        )
    }
}

