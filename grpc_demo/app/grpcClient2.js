// cd ../../protos
// npm install -g grpc-tools
// grpc_tools_node_protoc --js_out=import_style=commonjs,binary:../node/static_codegen/ --grpc_out=../node/static_codegen --plugin=protoc-gen-grpc=`which grpc_tools_node_protoc_plugin` helloworld.proto
// grpc_tools_node_protoc --js_out=import_style=commonjs,binary:../node/static_codegen/route_guide/ --grpc_out=../node/static_codegen/route_guide/ --plugin=protoc-gen-grpc=`which grpc_tools_node_protoc_plugin` route_guide.proto



//注意是grpc_tools_node_protoc_plugin.cmd不是grpc_tools_node_protoc
// grpc_tools_node_protoc --js_out=import_style=commonjs,binary:./static_codegen/ --grpc_out=./static_codegen --plugin=protoc-gen-grpc=C:\Users\N33\AppData\Roaming\npm\grpc_tools_node_protoc_plugin.cmd proto/Student.proto
// grpc_tools_node_protoc --js_out=import_style=commonjs,binary:../node/static_codegen/route_guide/ --grpc_out=../node/static_codegen/route_guide/ --plugin=protoc-gen-grpc=`which grpc_tools_node_protoc_plugin` route_guide.proto


//静态，先生成静态文件
//路径写对了没有代码提示，什么玩意啊

var service = require('../static_codegen/proto/Student_grpc_pb');
var messages = require('../static_codegen/proto/Student_pb');

var grpc = require('grpc');

var client = new service.StudentServiceClient('localhost:8899', grpc.credentials.createInsecure());

var request = new messages.MyRequest();
request.setUsername('wangwu');

client.getRealNameByUsername(request, function (error, respData) {
    console.log(respData.getRealname());

});