package com.popov.fintrack;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = {"classpath:db-init/liquibase/changesets/V1__init.sql", "classpath:data.sql"})
@ActiveProfiles("test")
abstract class BaseTests {
}
