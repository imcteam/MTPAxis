package org.imc;

import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.imc.service.OpcUaClientService;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = {"org.imc.*"})
public class XmlParserWebApplication {

    public static NodeId axis1DirectionNode;
    public static NodeId axis1SpeedNode;
    public static NodeId axis2DirectionNode;
    public static NodeId axis2SpeedNode;


    public static void main(String[] args) {
        if (args.length > 0) {
            for(int i=0;i<args.length;i++){
                System.out.println("arg"+i+": "+args[i]);
            }
            //opc.tcp://LAPTOP-2U8572UR:53530/OPCUA/SimulationServer
            OpcUaClientService.endPointUrl = args[1];
            Integer nameSpace = Integer.parseInt(args[2]);
            axis1DirectionNode = new NodeId(nameSpace, Integer.parseInt(args[3]));
            axis1SpeedNode = new NodeId(nameSpace, Integer.parseInt(args[4]));
            axis2DirectionNode = new NodeId(nameSpace, Integer.parseInt(args[5]));
            axis2SpeedNode = new NodeId(nameSpace, Integer.parseInt(args[6]));
      }
        ConfigurableApplicationContext ctx = SpringApplication.run(XmlParserWebApplication.class, args);

        // todo
      //  exitApplication(ctx);
    }

    public static void exitApplication(ConfigurableApplicationContext context) {
        int exitCode = SpringApplication.exit(context, (ExitCodeGenerator) () -> 0);

        System.exit(exitCode);
    }
}
