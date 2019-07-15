var PROTO_FILE_PATH= '../proto/Student.proto';
// var PROTO_FILE_PATH = 'F:/WebStormProject/grpc_demo/proto/Student.proto';

var grpc = require('grpc');
var grpcService = grpc.load(PROTO_FILE_PATH).com.n33.proto;

var server = new grpc.Server();

server.addService(grpcService.StudentService.service, {
    getRealNameByUsername: getRealNameByUsername,
    getStudentsByAge: getStudentsByAge,
    getStudentsWrapperByAges: getStudentsWrapperByAges,
    biTalk: biTalk
});

server.bind('localhost:8899', grpc.ServerCredentials.createInsecure());
server.start();

function getRealNameByUsername(call, callback) {
    console.log("call: " + call.request.username);

    callback(null, {realname: "张三"});


}

function getStudentsByAge() {

}

function getStudentsWrapperByAges() {

}

function biTalk() {

}