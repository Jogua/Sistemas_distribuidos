
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jose
 */
public interface Client_I extends Remote {

    public void mostrarMensaje(String nombreEmisor, String mensaje) throws RemoteException;
    
    public void addUsuario(String nombre, Client_I otroUsuario) throws RemoteException;
    
    public void addUsuarios(TreeMap<String, Client_I> usuarios) throws RemoteException;
    
    public void removeUsuario(String nombre) throws RemoteException;
    
}
