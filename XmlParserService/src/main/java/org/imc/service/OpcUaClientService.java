package org.imc.service;


import lombok.Data;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Data
public class OpcUaClientService {
    private static Logger logger = LoggerFactory.getLogger(OpcUaClientService.class);
    public static String endPointUrl;
  //  private final static String endPointUrl =  "opc.tcp://ubuntu:4840/";

    public static OpcUaClient opcUaClient;

    public OpcUaClientService(){
        try {
            opcUaClient = OpcUaClientService.createClient();
            //开启连接
            opcUaClient.connect().get();
        } catch (Exception e) {
            System.out.println("初始化失败");
        }
    }

    protected void finalize( )
    {
        try {
            opcUaClient.disconnect().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    private static AtomicInteger atomic = new AtomicInteger(1);
    /**
     * 创建OPC UA客户端
     * @return
     * @throws Exception
     */
    public static OpcUaClient createClient() throws Exception {
        //opc ua服务端地址
        Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
        Files.createDirectories(securityTempDir);
        if (!Files.exists(securityTempDir)) {
            throw new Exception("unable to create security dir: " + securityTempDir);
        }
        return OpcUaClient.create(endPointUrl,
                endpoints ->
                        endpoints.stream()
                                .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
                                .findFirst(),
                configBuilder ->
                        configBuilder
                                .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                                .setApplicationUri("urn:eclipse:milo:examples:client")
                                //访问方式
                                .setIdentityProvider(new AnonymousProvider())
                                .setRequestTimeout(UInteger.valueOf(5000))
                                .build()
        );
    }

    /**
     * 遍历树形节点
     *
     * @param client OPC UA客户端
     * @param uaNode 节点
     * @throws Exception
     */
    public static void browseNode(OpcUaClient client, UaNode uaNode) throws Exception {
        List<? extends UaNode> nodes;
        if (uaNode == null) {
            nodes = client.getAddressSpace().browseNodes(Identifiers.ObjectsFolder);
        } else {
            nodes = client.getAddressSpace().browseNodes(uaNode);
        }
        for (UaNode nd : nodes) {
            //排除系统行性节点，这些系统性节点名称一般都是以"_"开头
            if (Objects.requireNonNull(nd.getBrowseName().getName()).contains("_")) {
                continue;
            }
            System.out.println("Node= " + nd.getBrowseName().getName());
            browseNode(client, nd);
        }
    }

    /**
     * 读取节点数据
     *
     * @param client OPC UA客户端
     * @throws Exception
     */
    public static void readNode(OpcUaClient client,NodeId nodeId ) throws Exception {
        //读取节点数据
        DataValue value = client.readValue(0.0, TimestampsToReturn.Neither, nodeId).get();
        //标识符
        String identifier = String.valueOf(nodeId.getIdentifier());
        System.out.println(identifier + ": " + String.valueOf(value.getValue().getValue()));
    }

    /**
     * 写入节点数据
     *
     * @param client
     * @throws Exception
     */
    public static void writeNodeValue(OpcUaClient client,NodeId nodeId,double value) throws Exception {
        double i = value;
        //创建数据对象,此处的数据对象一定要定义类型，不然会出现类型错误，导致无法写入
        DataValue nowValue = new DataValue(new Variant(i), null, null);
        //写入节点数据
        StatusCode statusCode = client.writeValue(nodeId, nowValue).join();
        //System.out.println("结果：" + statusCode.isGood());
    }
    public static void writeNodeValue(OpcUaClient client,NodeId nodeId,String value) throws Exception {
        String i = value;
        //创建数据对象,此处的数据对象一定要定义类型，不然会出现类型错误，导致无法写入
        DataValue nowValue = new DataValue(new Variant(i), null, null);
        //写入节点数据
        StatusCode statusCode = client.writeValue(nodeId, nowValue).join();
        if(!statusCode.isGood()){
            System.out.println("修改節點："+nodeId+", 结果：" + statusCode.isGood());
        }
    }


    public static void main(String[] args) throws Exception {
        //创建OPC UA客户端
        OpcUaClient opcUaClient = createClient();

        //开启连接
        opcUaClient.connect().get();

        //遍历节点
    //    browseNode(opcUaClient, null);

        //读
        int namespaceIndex = 3;
        String identifier = "DoubleDataItem";
        NodeId nodeId = new NodeId(namespaceIndex, identifier);
        readNode(opcUaClient,nodeId);

        //写
        writeNodeValue(opcUaClient,nodeId,1.0);
        readNode(opcUaClient,nodeId);
        writeNodeValue(opcUaClient,nodeId,2.0);
        readNode(opcUaClient,nodeId);
        writeNodeValue(opcUaClient,nodeId,3.0);
        readNode(opcUaClient,nodeId);

        //关闭连接
        opcUaClient.disconnect().get();
    }

}
