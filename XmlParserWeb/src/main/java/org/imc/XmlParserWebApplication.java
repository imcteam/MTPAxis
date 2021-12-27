package org.imc;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"org.imc.*"})
public class XmlParserWebApplication {
    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(XmlParserWebApplication.class, args);
        // todo
      //  exitApplication(ctx);
    }

    public static void exitApplication(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);

        System.exit(exitCode);
    }
}
