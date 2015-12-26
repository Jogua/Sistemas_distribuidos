
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server_I extends Remote {

    public boolean estaLibre(String nombre) throws RemoteException;
    
    public void registrar(String nombre, Client_I stubCliente) throws RemoteException;

    public void desconectar(String nombre) throws RemoteException;
}
