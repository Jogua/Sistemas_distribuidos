
import java.awt.Dialog;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JDialog;

public class ChatPublico_Client implements Client_I {

    private static ClienteView clienteView;

    @Override
    public void mostrarMensaje(String mensaje) throws RemoteException {
	clienteView.mostrarMensaje(mensaje);
    }

    public static void main(String args[]) {
	if (System.getSecurityManager() == null) {
	    System.setSecurityManager(new SecurityManager());
	}
	try {
	    String nombre_objeto_remoto = "JoGua_ChatPublico_Server";
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
	    
	    Client_I client = new ChatPublico_Client();
	    Client_I stubClient = (Client_I) UnicastRemoteObject.exportObject(client, 0);

	    Registry registryLocal = LocateRegistry.getRegistry();
	    registryLocal.rebind(miNombre, stubClient);
	    
	    clienteView = new ClienteView(stubServidor, miNombre);
	    stubServidor.registrar(miNombre, stubClient);
	    clienteView.setVisible(true);

	} catch (Exception e) {
	    System.err.println("Ejemplo_I exception:");
	    e.printStackTrace();
	}
    }
}
