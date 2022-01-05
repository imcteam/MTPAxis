1.关于启动脚本
启动脚本内部命令是：
java -jar .\out\artifacts\xmlparserweb_jar\xmlparserweb.jar --server.port=8082 opc.tcp://LAPTOP-2U8572UR:53530/OPCUA/SimulationServer 3 1 2 3 4
运行请修改：
其中：
arg[0]: --server.port=8082 //本程序启动端口
arg[1]: opc.tcp://LAPTOP-2U8572UR:53530/OPCUA/SimulationServer //opcua服务器地址
arg[2]: 3 //信息模型节点命名空间
arg[3]: 1 //轴1方向identifier
arg[4]: 2 //轴1速度identifier
arg[5]: 3 //轴2方向identifier
arg[6]: 4 //轴2速度identifier

2.关于程序
程序内部集成了OPCUA 客户端Java实现和前端界面。
并且在程序中自动同步两个轴的运动。

3.启动程序
windows：双击启动.bat
ubuntu下写一个同等脚本

4.查看前端：
浏览器打开：
http://localhost:8082/test

