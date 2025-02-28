package Unlu.Poo.Uno.Modelo;

import java.rmi.RemoteException;

public interface IObserver {
    void actualizar(EventosUno event) throws RemoteException;
}
