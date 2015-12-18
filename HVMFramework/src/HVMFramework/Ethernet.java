package HVMFramework;

public interface Ethernet {
	public boolean OpenSocket(short socket, int ethProtocol, int port);
	public boolean CloseSocket(short socket);
	public boolean DisconnectSocket(short socket);
	public boolean Client(short socket, short ip[], int port);
	public boolean Send(short socket, char[] data);
	public boolean Receive(short socket, int buflen);
	public int ReceivedSize(short socket);
	public boolean Listen(short socket);
	public int GetStatus(short socket);
	
}
