
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author jose
 */
public class ChatPrivado_Server implements Server_I {

    private TreeMap<String, Client_I> clientes;

    public ChatPrivado_Server() {
	System.out.println("Funcion: Constructor");
	clientes = new TreeMap<>();
    }

    @Override
    public boolean estaLibre(String nombre) {
	boolean existe = clientes.containsKey(nombre);
	return !existe;
    }

    @Override
    public void registrar(String nombre, Client_I stubCliente) throws RemoteException {
	System.out.println("Funcion: registrar");
	for (Client_I itCliente_stub : clientes.values()) {
	    
	    itCliente_stub.addUsuario(nombre, stubCliente);

	}
	stubCliente.addUsuarios(clientes);
	clientes.put(nombre, stubCliente);
    }

    @Override
    public void desconectar(String nombre) throws RemoteException {
	System.out.println("Funcion: desconectar");
	clientes.remove(nombre);
	for (Client_I cliente : clientes.values()) {
	    cliente.removeUsuario(nombre);
	}
    }

    public static void main(String[] args) {
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	try {
	    String nombre_objeto_remoto = "JoGua_ChatPrivado_Server";

	    Server_I server = new ChatPrivado_Server();
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
