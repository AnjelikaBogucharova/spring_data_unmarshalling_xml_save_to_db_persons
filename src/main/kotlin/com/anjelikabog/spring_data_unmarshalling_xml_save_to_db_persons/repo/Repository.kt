package com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons.repo

import com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons.modelDataBase.*
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository


@Repository
interface PersonsRepository : JpaRepository<PersonsDB, Long>

@Repository
interface HobbyRepository : JpaRepository<HobbyDB, Long>
