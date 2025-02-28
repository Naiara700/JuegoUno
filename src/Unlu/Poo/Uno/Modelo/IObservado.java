package Unlu.Poo.Uno.Modelo;

import java.rmi.RemoteException;

public interface IObservado {
    void notificar (EventosUno evento) throws RemoteException;
    void registrar (IObserver observador);
    void desregistrar (IObserver observador);
}
