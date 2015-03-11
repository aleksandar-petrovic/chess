//Aleksandar Petrovic

package registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class RegistryManager
{
	private static final Logger logger = Logger.getLogger(RegistryManager.class.getName());

	public static Registry get() throws RemoteException
	{
		String host = System.getProperty("java.rmi.server.hostname");
		if (host == null)
		{
			logger.info("VM argument not defined, RMI registry will be available on localhost only");
			host = "localhost";
		}
		return get(host);
	}

	public static Registry get(String host) throws RemoteException
	{
		try
		{
			// pokusaj da kreiras registar. ne mozemo zadati host, vec metod koristi vrednost iz VM
			// argumenta
			return LocateRegistry.createRegistry(1099);
		} catch (RemoteException ex)
		{
			// registar vec postoji, povezi se
			return LocateRegistry.getRegistry(host, 1099);
		}
	}
}
