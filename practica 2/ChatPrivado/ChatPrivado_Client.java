
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatPrivado_Client implements Client_I {

    private static ClienteView clienteView;

    @Override
    public void mostrarMensaje(String nombreEmisor, String mensaje) throws RemoteException {
	clienteView.mostrarMensaje(nombreEmisor, mensaje);
    }
    
    public void addUsuarios(TreeMap<String, Client_I> usuarios) throws RemoteException{
	clienteView.addUsuarios(usuarios);
    }

    @Override
    public void addUsuario(String nombre, Client_I otroUsuario) throws RemoteException {
	clienteView.addUsuario(nombre, otroUsuario);
    }

    @Override
    public void removeUsuario(String nombre) throws RemoteException {
	clienteView.removeUsuario(nombre);
    }

    public static void main(String args[]) {
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	try {
	    String nombre_objeto_remoto = "JoGua_ChatPrivado_Server";
	    System.out.println("Buscando el objeto remoto");

	    Registry registry = LocateRegistry.getRegistry(args[0]);
	    Server_I stubServidor = (Server_I) registry.lookup(nombre_objeto_remoto);
	    System.out.println("Invocando el objeto remoto");
	    
	    RequestNameView requestNameView = new RequestNameView(null, true, null);
	    String miNombre = requestNameView.getNombreUsuario();
	    
	    while (!stubServidor.estaLibre(miNombre)) {		
		requestNameView = new RequestNameView(null, true, "El nombre de usuario elegido ya está ocupado");
		miNombre = requestNameView.getNombreUsuario();
	    }
	    
	    Client_I client = new ChatPrivado_Client();
	    Client_I stubClient = (Client_I) UnicastRemoteObject.exportObject(client, 0);

	    Registry registryLocal = LocateRegistry.getRegistry();
	    registryLocal.rebind(miNombre, stubClient);
	    
	    clienteView = new ClienteView(stubServidor, miNombre);
	    stubServidor.registrar(miNombre, stubClient);
	    clienteView.setVisible(true);

	} catch (RemoteException | NotBoundException e) {
	    System.err.println("El servicio no está activo");
	    try {
		Thread.sleep(2000);
	    } catch (InterruptedException ex) {
	    }
	    System.exit(-1);
	}
    }
}
