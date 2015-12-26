var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");
var mimeTypes = {"html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

var httpServer = http.createServer(
        function (request, response) {
            var uri = url.parse(request.url).pathname;
            if (uri == "/")
                uri = "/index.html";
            var fname = path.join(process.cwd(), uri);
            fs.exists(fname, function (exists) {
                if (exists) {
                    fs.readFile(fname, function (err, data) {
                        if (!err) {
                            var extension = path.extname(fname).split(".")[1];
                            var mimeType = mimeTypes[extension];
                            response.writeHead(200, mimeType);
                            response.write(data);
                            response.end();
                        }
                        else {
                            response.writeHead(200, {"Content-Type": "text/plain"});
                            response.write('Error de lectura en el fichero: ' + uri);
                            response.end();
                        }
                    });
                }
                else {
                    console.log("Peticion invalida: " + uri);
                    response.writeHead(200, {"Content-Type": "text/plain"});
                    response.write('404 Not Found\n');
                    response.end();
                }
            });
        }
);

httpServer.listen(8080);
var io = socketio.listen(httpServer);

var clientes = new Array();

io.sockets.on('connection', function (cliente) { //cuando un cliente se conecte
    
    var nombreUsuario;
    
    cliente.on("registrarse", function (nombre) {
        if (clientes.indexOf(nombre) == -1) {
            nombreUsuario = nombre;
            clientes.push(nombreUsuario);
            clientes.sort();
            cliente.emit("libre");
            io.sockets.emit("actualizar_conectados", clientes);
            io.sockets.emit("mensaje_nuevo", "Chat: " + nombreUsuario + " se ha conectado.");
        } else {
            cliente.emit("ocupado");
        }
    });

    cliente.on("enviar_mensaje", function (mensaje) {
        io.sockets.emit("mensaje_nuevo", mensaje);
    });

    cliente.on('disconnect', function () { //si el cliente se desconecta
        var index = clientes.indexOf(nombreUsuario);
        if (index != -1) {
            clientes.splice(index, 1);
            io.sockets.emit("mensaje_nuevo", "Chat: " + nombreUsuario + " se ha desconectado.");
            io.sockets.emit('actualizar_conectados', clientes);
        }
    });
});

console.log("Servicio chat instantáneo se ha iniciado");
