package com.salpreh.products;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class ApplicationTests {

  private ApplicationModules modules = ApplicationModules.of(Application.class, JavaClass.Predicates.resideInAPackage("com.salpreh.products.restapi.."));

	@Test
	void contextLoads() {
	}

  @Test
  void validModuleStructure() {
    modules.verify();
  }

  @Test
  void validDocumentation() {
    assertDoesNotThrow(() -> {
      new Documenter(modules)
        .writeModulesAsPlantUml()
        .writeIndividualModulesAsPlantUml();
    });
  }

}
