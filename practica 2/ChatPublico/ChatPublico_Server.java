
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;

/**
 *
 * @author jose
 */
public class ChatPublico_Server implements Server_I {

    private TreeMap<String, Client_I> clientes_;

    public ChatPublico_Server() {
	System.out.println("Funcion: Constructor");
	clientes_ = new TreeMap<>();
    }

    @Override
    public boolean estaLibre(String nombre) throws RemoteException {
	System.out.println("Funcion: estaLibre ");
	boolean existe = clientes_.containsKey(nombre);
	return !existe;

    }

    @Override
    public void registrar(String nombre, Client_I stubCliente) throws RemoteException {
	System.out.println("Funcion: registrar");
	clientes_.put(nombre, stubCliente);
	difundirMensaje("Chat", nombre + " ha entrado en el chat");
    }

    @Override
    public void difundirMensaje(String nombre, String mensaje) throws RemoteException {
	System.out.println("Funcion: difundirMensaje ");
	for (Client_I cliente : clientes_.values()) {
	    cliente.mostrarMensaje(nombre + ": " + mensaje);
	}
    }

    @Override
    public void desconectar(String nombre) throws RemoteException {
	System.out.println("Funcion: desconectar");
	clientes_.remove(nombre);
	difundirMensaje("Chat", nombre + " se ha desconectado");
    }

    public static void main(String[] args) {
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	try {
	    String nombre_objeto_remoto = "JoGua_ChatPublico_Server";

	    Server_I server = new ChatPublico_Server();
	    Server_I stub = (Server_I) UnicastRemoteObject.exportObject(server, 0);

	    Registry registry = LocateRegistry.getRegistry();
	    registry.rebind(nombre_objeto_remoto, stub);

	    System.out.println("Servidor Cargado. Esperando Clientes...");
	} catch (Exception e) {
	    System.err.println("Ejemplo exception: ");
	    e.printStackTrace();
	    System.exit(20);
	}
    }
}
