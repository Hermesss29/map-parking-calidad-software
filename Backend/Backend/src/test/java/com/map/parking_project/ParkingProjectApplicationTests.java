package com.map.parking_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestMailConfig.class)
class ParkingProjectApplicationTests {

	@Test
	void contextLoads() {
		// Verifica que el contexto de Spring cargue correctamente
	}

}
