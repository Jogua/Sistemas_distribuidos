var serviceURL = document.URL;
var websocket = io.connect(serviceURL);
var miNombre;

//El evento "libre" solo ocurre se le hace a un cliente,
//aquel que haya enviado el evento "registrarse"
websocket.on("libre", function () {
    alert("Bienvenido al Chat");
    document.title = "Chat " + miNombre;
    document.getElementById("div_pedir_nombre").hidden = true;
    document.getElementById("div_chat").hidden = false;
    document.getElementById("mensaje_enviar").disabled = false;
    document.getElementById("mensaje_enviar").focus();
});

//El evento "ocupado" solo ocurre se le hace a un cliente,
//aquel que haya enviado el evento "registrarse"
websocket.on("ocupado", function () {
    document.getElementById("error_nombre_usuario").hidden = false;
});

function registrarse(formulario) {
    miNombre = formulario.nombre.value;
    document.getElementById("ventana_mensajes").value = ""; //borramos los posibles mensajes
    websocket.emit("registrarse", miNombre);
}

function enviarMensaje() {
    var campo_mensaje = document.getElementById("mensaje_enviar");
    if (campo_mensaje.value != "") {
        var mensaje = campo_mensaje.value;
        campo_mensaje.value = "";
        websocket.emit("enviar_mensaje", miNombre + ": " + mensaje);
    }
}
function mostrarMensaje(mensaje) {
    var ventana_mensajes = document.getElementById("ventana_mensajes");
    ventana_mensajes.value += "\n" + mensaje;
    ventana_mensajes.scrollTop = ventana_mensajes.scrollHeight; //Bajar el scroll
}

websocket.on("actualizar_conectados", function (usuarios) {
    var lista_usuarios = document.getElementById("div_lista_usuarios");
    var lista = "<ul>";
    for (var i = 0; i < usuarios.length; i++) {
        lista += "<li>" + usuarios[i] + "</li>";
    }
    lista += "</ul>";
    lista_usuarios.innerHTML = lista;
});

websocket.on("mensaje_nuevo", function (mensaje) {
    mostrarMensaje(mensaje)
});

websocket.on("disconnect", function () {
    mostrarMensaje("El Servicio ha dejado de funcionar.");
    document.getElementById("mensaje_enviar").disabled = true;
});
