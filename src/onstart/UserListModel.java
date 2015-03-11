//Aleksandar Petrovic

package onstart;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import registry.RegistryManager;
import server.UserDbI;
import user.UserI;
import user.UserInfo;


@SuppressWarnings("serial")
public class UserListModel extends AbstractTableModel
{
	private UserDbI userDb; 
	private List<UserInfo> users;
	private UserInfo me;

	// konstruktor prihvata host baze korisnika
	public UserListModel(String dbHost, UserInfo me) throws RemoteException, NotBoundException
	{
		this.me=me;
		users = new ArrayList<>();
		userDb = (UserDbI) RegistryManager.get(dbHost).lookup(UserDbI.RMI_NAME);
		reload();
	}

	public void add(UserInfo info) throws RemoteException, AlreadyBoundException
	{
		try
		{
			userDb.add(info);
		} finally
		{
			reload();
		}
	}

	public void remove(UserInfo info) throws RemoteException
	{
		try
		{
			userDb.remove(info);
		} finally
		{
			reload();
		}
	}

	public void reload() throws RemoteException
	{
		try
		{
			users.clear();
			users.addAll(userDb.get());
			users.remove(me);
		} catch (RemoteException ex)
		{
			throw ex;
		} finally
		{
			fireTableDataChanged();
		}
	}

	public UserInfo getInfo(int row)
	{
		return users.get(row);
	}

	public UserI getRemoteUser(int row) throws RemoteException, NotBoundException
	{
		UserInfo info = getInfo(row);
		Registry reg = RegistryManager.get(info.getHost());
		return (UserI) reg.lookup(info.getName());
	}

	@Override
	public int getRowCount()
	{
		return users.size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public String getColumnName(int col)
	{
		return col == 0 ? "Avatar" : "Name";
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return col == 0 ? ImageIcon.class : String.class;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		UserInfo user = getInfo(rowIndex);
		return columnIndex == 0 ? new ImageIcon(getClass().getResource("res/"+user.getAvatar())) : user.getName();
		
	}
}
