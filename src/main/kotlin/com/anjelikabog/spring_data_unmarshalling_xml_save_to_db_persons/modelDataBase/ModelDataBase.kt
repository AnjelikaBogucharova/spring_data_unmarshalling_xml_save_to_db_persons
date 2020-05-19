package com.anjelikabog.spring_data_unmarshalling_xml_save_to_db_persons.modelDataBase


import org.springframework.stereotype.Repository
import java.io.Serializable
import java.sql.Date
import javax.persistence.*


@Entity
@Table(name = "person")
data class PersonsDB(
        @Id
        @SequenceGenerator(name = "person_id_seq", sequenceName = "persons_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_id_seq")
        @Column(name = "id", nullable = false, insertable = false)
        val idPersons: Long? = null,

        @Column(nullable = false)
        val fullname: String? = null,

        @Column(nullable = false)
        val birthday: Date? = null,


        @OneToMany(mappedBy = "idPersons", cascade = arrayOf(CascadeType.ALL))
        val hobbies: Set<HobbyDB> = HashSet()
)

@Entity
@Table(name = "hobbies")
data class HobbyDB(
        @Id
        @SequenceGenerator(name = "hob_id_seq", sequenceName = "hobbies_id_seq", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hob_id_seq")
        @Column(name = "id", nullable = false, insertable = false)
        val idHobby: Long? = null,

        @Column(nullable = false)
        val complexity: Int? = null,

        @Column(nullable = false)
        val hobby_name: String? = null,

        @ManyToOne
        @JoinColumn(name="id_person", nullable = false)
        val idPersons: PersonsDB? =null
)



