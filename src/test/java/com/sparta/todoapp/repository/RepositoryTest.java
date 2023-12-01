package com.sparta.todoapp.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test") // application-test.properties에 저장되어있는 db와 연동
public class RepositoryTest {
}
