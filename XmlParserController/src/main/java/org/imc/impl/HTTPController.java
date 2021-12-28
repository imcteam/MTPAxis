package org.imc.impl;

import com.alibaba.fastjson.JSONObject;
import netscape.javascript.JSObject;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.imc.service.Axis;
import org.imc.service.OpcUaClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.spi.http.HttpContext;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HTTPController {


    @GetMapping( "/test")
    public String test() {
        // 指定返回模板名称
        return "test";
    }

    @PostMapping( "/write")
    public void read(@RequestBody Axis axis,
                       HttpServletResponse response) throws IOException {

        Double speed = Double.parseDouble(axis.getSpeed());
        String direction = axis.getDirection();
        OpcUaClient opcUaClient = OpcUaClientService.opcUaClient;
//        NodeId axis1DirectionNode = new NodeId(3, 51);
//        NodeId axis1SpeedNode = new NodeId(3, 50);
//        NodeId axis2DirectionNode = new NodeId(3, 56);
//        NodeId axis2SpeedNode = new NodeId(3, 55);

        NodeId axis1DirectionNode = new NodeId(3, "StringDataItem");
        NodeId axis1SpeedNode = new NodeId(3, "DoubleDataItem");
        NodeId axis2DirectionNode = new NodeId(3, "StringDataItem");
        NodeId axis2SpeedNode = new NodeId(3, "DoubleDataItem");
        //读写
        Axis res = new Axis();
        try {

            OpcUaClientService.writeNodeValue(opcUaClient,axis1DirectionNode,direction);
            OpcUaClientService.writeNodeValue(opcUaClient,axis1SpeedNode,speed);
        //    System.out.println("軸1方向:"+direction);
            OpcUaClientService.writeNodeValue(opcUaClient,axis2DirectionNode,direction);
            OpcUaClientService.writeNodeValue(opcUaClient,axis2SpeedNode,speed);
            DataValue direction2 = opcUaClient.readValue(0.0, TimestampsToReturn.Neither, axis2DirectionNode).get();
            DataValue speed2 = opcUaClient.readValue(0.0, TimestampsToReturn.Neither, axis2SpeedNode).get();
            res.setDirection(String.valueOf(direction2.getValue().getValue()));
            res.setSpeed(String.valueOf(speed2.getValue().getValue()));
       //     System.out.println("軸2方向:"+res.getDirection());
        } catch (Exception e) {

        }
        //响应
        JSONObject jsonObject= (JSONObject) JSONObject.toJSON(res);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(jsonObject.toJSONString());
    }
}
