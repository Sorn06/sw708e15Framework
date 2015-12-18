package w5100;

import HVMFramework.Digital;
import HVMFramework.SPI;
public class Ethernet implements HVMFramework.Ethernet{
	/*
	 * Constant
	 */

	public static final short numSockets = 4;
	public static final short MAX_BUF = 512;
	public static final short SKT_MR_CLOSE =	0x00;    /* Unused socket */
	public static final short SKT_MR_TCP	= 0x01;    	/* TCP */
	public static final short SKT_MR_UDP	= 0x02;    	/* UDP */
	public static final short SKT_MR_IPRAW = 0x03;    /* IP LAYER RAW SOCK */
	public static final short SKT_MR_MACRAW = 0x04;   /* MAC LAYER RAW SOCK */
	public static final short SKT_MR_PPPOE = 0x05;    /* PPPoE */
	public static final short SKT_MR_ND = 0x20;       /* No Delayed Ack(TCP) flag */
	public static final short SKT_MR_MULTI = 0x80;    /* support multicasting */
	
	/*
	 * Constants used for selecting commands in the command registers for
	 * 
	 */
	public static final short SKT_CR_OPEN	 = 0x01;		/* open the socket */
	public static final short SKT_CR_LISTEN = 0x02;		/* wait for TCP connection (server mode) */
	public static final short SKT_CR_CONNECT = 0x04;		/* listen for TCP connection (client mode) */
	public static final short SKT_CR_DISCON = 0x08;		/* close TCP connection */
	public static final short SKT_CR_CLOSE = 0x10;		/* mark socket as closed (does not close TCP connection) */
	public static final short SKT_CR_SEND = 0x20;		/* transmit data in TX buffer */
	public static final short SKT_CR_SEND_MAC = 0x21;		/* SEND, but uses destination MAC address (UDP only) */
	public static final short SKT_CR_SEND_KEEP = 0x22;		/* SEND, but sends 1-byte packet for keep-alive (TCP only) */
	public static final short SKT_CR_RECV = 0x40;		/* receive data into RX buffer */
	
	/*
	 *  The following #defines are values that can be read from the Status registers for
	 *  each of the sockets.
	 */
	public static final short SKT_SR_CLOSED      = 0x00;		/* closed */
	public static final short SKT_SR_INIT        = 0x13;		/* init state */
	public static final short SKT_SR_LISTEN      = 0x14;		/* listen state */
	public static final short SKT_SR_SYNSENT     = 0x15;		/* connection state */
	public static final short SKT_SR_SYNRECV     = 0x16;		/* connection state */
	public static final short SKT_SR_ESTABLISHED = 0x17;		/* success to connect */
	public static final short SKT_SR_FIN_WAIT    = 0x18;		/* closing state */
	public static final short SKT_SR_CLOSING     = 0x1A;		/* closing state */
	public static final short SKT_SR_TIME_WAIT	  = 0x1B;		/* closing state */
	public static final short SKT_SR_CLOSE_WAIT  = 0x1C;		/* closing state */
	public static final short SKT_SR_LAST_ACK    = 0x1D;		/* closing state */
	public static final short SKT_SR_UDP         = 0x22;		/* UDP socket */
	public static final short SKT_SR_IPRAW       = 0x32;		/* IP raw mode socket */
	public static final short SKT_SR_MACRAW      = 0x42;		/* MAC raw mode socket */
	public static final short SKT_SR_PPPOE       = 0x5F;		/* PPPOE socket */
	
	/*
	 *  The following offsets are added to a socket's base register address to find
	 *  a specific socket register.  For example, the address of the command register
	 *  for socket 2 would be:
	 *  (W5100_SKT_BASE(2) + W5100_CR_OFFSET
	 */
	public static final short MR_OFFSET		= 0x0000;		/* socket Mode Register offset */
	public static final short CR_OFFSET		= 0x0001;		/* socket Command Register offset */
	public static final short IR_OFFSET		= 0x0002;		/* socket Interrupt Register offset */
	public static final short SR_OFFSET		= 0x0003;		/* socket Status Register offset */
	public static final short PORT_OFFSET		= 0x0004;		/* socket Port Register offset (2 bytes) */
	public static final short DHAR_OFFSET		= 0x0006;		/* socket Destination Hardware Address Register (MAC, 6 bytes) */
	public static final short DIPR_OFFSET		= 0x000C;		/* socket Destination IP Address Register (IP, 4 bytes) */
	public static final short DPORT_OFFSET		= 0x0010;		/* socket Destination Port Register (2 bytes) */
	public static final short MSS_OFFSET		= 0x0012;		/* socket M= aximum; Segment Size (2 bytes) */
	public static final short PROTO_OFFSET		= 0x0014;		/* socket IP Protocol Register */
	public static final short TOS_OFFSET		= 0x0015;		/* socket Type Of Service Register */
	public static final short TTL_OFFSET		= 0x0016;		/* socket Time To Live Register */
	public static final short TX_FSR_OFFSET	= 0x0020;		/* socket Transmit Free Size Register (2 bytes) */
	public static final short TX_RR_OFFSET		= 0x0022;		/* socket Transmit Read Pointer Register (2 bytes) */
	public static final short TX_WR_OFFSET		= 0x0024;		/* socket Transmit Write Pointer Register (2 bytes) */
	public static final short RX_RSR_OFFSET	= 0x0026;		/* socket Receive Received Size Register (2 bytes) */
	public static final short RX_RD_OFFSET		= 0x0028;		/* socket Receive Read Pointer Register (2 bytes) */
	
	/*
	 *  Define the addresses inside the W5100 for the transmit and receive
	 *  buffers.
	 */
	private static final short TXBUFADDR = 0x4000;      /* W5100 Send Buffer Base Address */
	private static final short RXBUFADDR = 0x6000;      /* W5100 Read Buffer Base Address */
	
	
	 /*
	 *  Define masks for accessing the addresses with a TX or RX buffer.
	 *  Note that these masks assume 2K buffers!
	 */
	private static final short TX_BUF_MASK = 0x07FF;     /* Tx 2K Buffer Mask */
	private static final short RX_BUF_MASK = 0x07FF;      /* Rx 2K Buffer Mask */
	
	//Ingen grund til at gemme dem for altid, pass dem til C koden og lad dem blive freed
	//efter de er sat i shieldet.
	public char buf[] = new char[MAX_BUF];
	private short[] mac;
	private short[] source_ip;
	private short[] submask;
	private short[] gateway;
	
	private SPI spi;
	public Ethernet(short[] mac, short[] ip, short[] submask, short[] gateway){
		this.mac = mac; 
		this.source_ip = ip;
		this.submask = submask;
		this.gateway = gateway;
 		
		this.spi = new SPI(false, SPI.SPI_DATA_ORDER_MSB, true, SPI.SPI_CLOCK_DIV16, SPI.SPI_CLOCK_POLARITY_LOW, SPI.SPI_CLOCK_PHASE_LEADING, true);
		init();
	}
	
	public boolean OpenSocket(short socket, int ethProtocol, int port) {
		boolean result = false;
		int socketAddress = calculateSocketAddress(socket);
		if (socket > numSockets)  return result;	// illegal socket value is bad!


		if (read(socketAddress + SR_OFFSET) != SKT_SR_CLOSED)    // Make sure we close the socket first
		{
			CloseSocket((short) socketAddress);
		}


		write(socketAddress + MR_OFFSET, ethProtocol);
		write(socketAddress + PORT_OFFSET, ((port & 0xFF00) >> 8 ));
		write(socketAddress + PORT_OFFSET+1, (port & 0x00FF));

		write(socketAddress + CR_OFFSET, SKT_CR_OPEN);	               	// open the socket

		
		while (read(socketAddress + CR_OFFSET) != 0) {}			// loop until device reports socket is open (blocks!!)

		if (read(socketAddress + SR_OFFSET) == SKT_SR_INIT) {
			result = true;	// if success, return socket number	
		}
		else {
			CloseSocket((short) socketAddress); // if failed, close socket immediately
		}
		return  result;
	}
	
	public boolean CloseSocket(short socket) {
		boolean result = false;
		if (socket > numSockets)  return result;			// if illegal socket number, ignore request
		int socketAddress = calculateSocketAddress(socket);

		write(socketAddress + CR_OFFSET, SKT_CR_CLOSE);	// tell chip to close the socket
		
		//TODO  timeouts everywhere
		while (read(socketAddress + CR_OFFSET) != 0) { }	// loop until socket is closed (blocks!!)
		
		if (read(socketAddress + SR_OFFSET) == SKT_SR_CLOSED) {
			result = true;	// if success, return socket number	
		}	
		return result;
	}
	
	public boolean DisconnectSocket(short socket) {
		boolean result = false;
		if (socket > numSockets)  return result;			// if illegal socket number, ignore request
		int socketAddress = calculateSocketAddress(socket);

		write(socketAddress + CR_OFFSET, SKT_CR_DISCON);		// disconnect the socket
		while (read(socketAddress + CR_OFFSET) != 0)  { }	// loop until socket is closed (blocks!!)
		
		result = true;
		
		return result;
	}
	
	public boolean Client(short socket, short ip[], int port) {
	    boolean result = false;
	    short timeout = 0;

	    if (socket > numSockets) return  result;	// if illegal socket number, ignore request

	    int socketAddress = calculateSocketAddress(socket);
	    //Set destination IP 
	    SetDIPR(socket, ip);

	    //Set destination port
	    SetDPORT(socket, port);

	    write(socketAddress + CR_OFFSET, SKT_CR_CONNECT); // Enter client mode

	    while (read(socketAddress + CR_OFFSET) != 0) { } // Block till we are in client mode
	    while(read(socketAddress + SR_OFFSET) != SKT_SR_ESTABLISHED) { 

	    	if(timeout > 1000)
	    	{
	    		CloseSocket(socket);
	    		return result;
	    	}
	    	timeout++;
	    }
	    result = true;
	    return  result;
	}
	
	private void SetDIPR(short socket, short ip[])	{
	    for (int i = 0; i < 4; ++i)
	    {
	        write(calculateSocketAddress(socket) + DIPR_OFFSET + i, ip[i]);
	    }
	}

	private void SetDPORT(short socket, int port)	{
		int socketAddress = calculateSocketAddress(socket);
	    write(socketAddress + DPORT_OFFSET, ((port & 0xff00) >> 8 ));
	    write(socketAddress + DPORT_OFFSET + 1, (port & 0x00ff));
	}
	 
	
	private int getWritePointer(int socketAddress) {
		int ptr = read(socketAddress + TX_WR_OFFSET);
		return (((ptr & 0x00FF) << 8 ) + read(socketAddress + TX_WR_OFFSET + 1));
	}
	
	public boolean Send(short socket, char[] data) {
		boolean result = false;
	    int ptr = 0;
	    int offaddr = 0;
	    int realaddr = 0;
	    int txsize = 0;
	    int timeout = 0;
	    
		//Reset the buffer for old values
		resetBuffer();
		//Copy data to buffer
		for(int i = 0; i < data.length; i++) {
			buf[i] = data[i];
		}
		int buflen = data.length;
		
	    if (buflen == 0 || socket > numSockets)  return  result;      // ignore illegal requests
	    int socketAddress = calculateSocketAddress(socket);
	    // Make sure the TX Free Size Register is available
	    txsize = read(socketAddress + TX_FSR_OFFSET);
	    txsize = (((txsize & 0x00FF) << 8 ) + read(socketAddress + TX_FSR_OFFSET + 1));

	    timeout = 0;
	    while (txsize < buflen)
	    {
	        //_delay_ms(1);

	        txsize = read(socketAddress + TX_FSR_OFFSET);        // make sure the TX free-size reg is available
	        txsize = (((txsize & 0x00FF) << 8 ) + read(socketAddress + TX_FSR_OFFSET + 1));

	        if (timeout > 1000)                       // if max delay has passed...
	        {
	            DisconnectSocket(socket);                 // can't connect, close it down
	            return  false;                     // show failure
	        }
	        timeout++;
	    }   

	    // Read the Tx Write Pointer
	    ptr = read(socketAddress + TX_WR_OFFSET);
	    offaddr = (((ptr & 0x00FF) << 8 ) + read(socketAddress + TX_WR_OFFSET + 1));
	    
	    int bufIndex = 0;
	    while (buflen != 0)
	    {
	        buflen--;
	        realaddr = (TXBUFADDR + (0x0800 * socket)) + (offaddr & TX_BUF_MASK); // calc W5100 physical buffer addr for this socket
	        //TODO - parse af char til int, ok?
	        write(realaddr, buf[bufIndex]);                  // send a byte of application data to TX buffer
	        offaddr++;                                  // next TX buffer addr
	        bufIndex++;                                      // next input buffer addr
	    }

	    write(socketAddress + TX_WR_OFFSET, (offaddr & 0xFF00) >> 8);    // send MSB of new write-pointer addr
	    write(socketAddress + TX_WR_OFFSET + 1, (offaddr & 0x00FF));     // send LSB 

	    write(socketAddress + CR_OFFSET, SKT_CR_SEND); // start the send on its way
	    while (read(socketAddress + CR_OFFSET) != 0)  ;   // loop until socket starts the send (blocks!!)
    
    	result = true;
	    return  result;
	}
	
	private void resetBuffer() {
		for(int i = 0; i < 500; i++) {
			buf[i] = '\0';
		}
	}
	public static native void nprint_char(char c);
	public boolean Receive(short socket, int buflen) {
		boolean result = false;
		if (buflen == 0 || socket >= numSockets)  return  result;		// ignore illegal conditions

		int ptr;
		int offaddr;
		int realaddr;   	

		if (buflen > (MAX_BUF-2)) {
			buflen = MAX_BUF - 2;		// requests that exceed the max are truncated
		}  
		
		//Reset the buffer for old values
		resetBuffer();
		
		int socketAddress = calculateSocketAddress(socket);  // calc base addr for this socket
		
		ptr = read(socketAddress + RX_RD_OFFSET);			// get the RX read pointer (MSB)
		offaddr = (((ptr & 0x00FF) << 8 ) + read(socketAddress + RX_RD_OFFSET + 1));		//get LSB and calc offset addr
		
		int bufIndex = 0;
		while (buflen != 0)
		{
			buflen--;
			realaddr = (RXBUFADDR + (0x0800 * socket)) + (offaddr & RX_BUF_MASK);
			char c = (char) read(realaddr);
			buf[bufIndex] = c;
			offaddr++;
			bufIndex++;
		}
		buf[bufIndex] ='\0'; // buffer read is complete, terminate the string

		// Increase the S0_RX_RD value, so it point to the next receive
		write(socketAddress + RX_RD_OFFSET, (offaddr & 0xFF00) >> 8);	// update RX read offset (MSB)
		write(socketAddress + RX_RD_OFFSET + 1,(offaddr & 0x00FF));		// update LSB

		// Now Send the RECV command
		write(socketAddress + CR_OFFSET, SKT_CR_RECV);			// issue the receive command
		while (read(socketAddress + CR_OFFSET) != 0)  ;	// wait for command to get executed										// wait for receive to start
		
		result = true;
		return  result;
	}
	

	
	public int ReceivedSize(short socket) {
		if (socket > numSockets) return  0;
		int	val;
		int socketAddress = calculateSocketAddress(socket);

		val = read(socketAddress + RX_RSR_OFFSET) & 0xff;
		val = (val << 8) + read(socketAddress + RX_RSR_OFFSET + 1);
		return  val;
	}
	
	public boolean Listen(short socket) {
		boolean result = false;
		if (socket > numSockets)  return  result;	// if illegal socket number, ignore request

		int socketAddress = calculateSocketAddress(socket);
		
		write(socketAddress + CR_OFFSET, SKT_CR_LISTEN);		// put socket in listen state
		while (read(socketAddress + CR_OFFSET) != 0) { }	// block until command is accepted

		if (read(socketAddress + SR_OFFSET) == SKT_SR_LISTEN) {
			result = true;	// if socket state changed, show success
		} else {
			CloseSocket((short) socketAddress);					// not in listen mode, close and show an error occurred
		}
		return  result;
	}
	
	
	
	public int GetStatus(short socket) {
		return read(calculateSocketAddress(socket) + SR_OFFSET);
		
		
	}
	
	private static final short SS_PIN = 10;
	
	
	private static final short WRITE_OPCODE = 0xF0;
	private static final short READ_OPCODE = 0x0F;
	
	private void init() {
		//Todo implement hardware reset
		/*
		 * resetpin high
		 * pinmode resetpin to output
		 * resetpin low
		 * 5ms delay
		 * resetpin high
		 * 10ms delay, wait for device
		 */
		
		//Soft reset the W5100 chip
		Digital.PinMode((short) 10, Digital.ModeOutput);
		write(MR, (1<<7));
		configure();
	}
	
	/*
	 *  Wiznet W5100 register addresses
	 */
	private static final short MR   		=	0x0000; /* Mode Register */   	
	private static final short GAR  		=	0x0001; /* Gateway Address: 0x0001 to 0x0004 */   	
	private static final short SUBR 		=	0x0005; /* Subnet mask Address: 0x0005 to 0x0008 */   	
	private static final short SHAR  		=	0x0009; /* Source Hardware Address (MAC): 0x0009 to 0x000E */   	
	private static final short SIPR 		=	0x000F; /* Source IP Address: 0x000F to 0x0012 */   	
	private static final short IR			=	0x0015; /* Interrupt Register */		
	private static final short IMR			=	0x0016; /* Interrupt Mask Register */		
	private static final short RTR			=	0x0017; /* Retry Timeout Register: 0x0017 to 0x0018 */		
	private static final short RCR			=	0x0019; /* Retry Count Register */		
	private static final short RMSR 		=	0x001A; /* RX Memory Size Register */   	
	private static final short TMSR 		=	0x001B; /* TX Memory Size Register */   	
	private static final short PATR			=	0x001C; /* PPPoE Authentication-Type Register: 0x001C to 0x001D */		
	private static final short PTIMER		=	0x0028; /* PPP Link Control Protocol Request Timer Register */		
	private static final short PMAGIC		=	0x0029; /* PPP Link Control Protocol Magic number Register */		
	private static final short UIPR			=	0x002A; /* Unreachable IP Address Register: 0x002A to 0x002D */		
	private static final short UPORT		=	0x002E; /* Unreachable Port Register: 0x002E to 0x002F */		
	

	private void configure() {
		//Setup gateway
		for(int i = 0; i < 4; i++) {
			write((GAR + i),  gateway[i]);
		}
		
		//Setup MAC address
		for(int i = 0; i < 6; i++) {
			write((SHAR + i), mac[i]);
		}
		
		//Setup Subnet mask
		for(int i = 0; i < 4; i++) {
			write((SUBR + i), submask[i]);
		}
		
		//Setup source ip address
		for(int i = 0; i < 4; i++) {
			write((SIPR + i), source_ip[i]);
		}
		
		//Todo Destination ip? Nah
		//Todo insert delay?
		
		
		//Use default buffer sizes, 2K bytes RX and TX for each socket
		write(RMSR, 0x55);
		write(TMSR, 0x55);
		
	}
	
	
	
	private void write(int address, int data) {
		//Enable the W5100 chip
		Digital.Write(SS_PIN, Digital.Low); 
		
		//Send write code to the chip
		spi.Transfer(WRITE_OPCODE);
		
		//Send MSB of address
		spi.Transfer((short) ((address & 0xFF00) >> 8));
		
		//Send LSB of address
		spi.Transfer((short) (address & 0xFF));
		
		//Send data
		spi.Transfer((short) data);
		
		//disable the W5100 chip
		Digital.Write(SS_PIN, Digital.High);
	}
	
	
	private short read(int address){

		short val = 0;
		//Enable the W5100 chip
		Digital.Write(SS_PIN, Digital.Low); 
		
		//Send write code to the chip
		spi.Transfer(READ_OPCODE);
		
		//Send MSB of address
		spi.Transfer((short) ((address & 0xFF00) >> 8));
		
		//Send LSB of address
		spi.Transfer((short) (address & 0xFF));
		
		//Get data, send dummy data to get it
		val = spi.Transfer((short) 0x00);
		
		//disable the W5100 chip
		Digital.Write(SS_PIN, Digital.High); 
		
		
		return val;
	}
	
	/*
	*  Use W5100_SKT_REG_BASE and W5100_SKT_OFFSET to calc the address of each set of socket registers.
	*  For example, the base address of the socket registers for socket 2 would be:
	*  (W5100_SKT_REG_BASE + (2 * W5100_SKT_OFFSET))
	*/
	private static final short SKT_REG_BASE =	0x0400;		/* start of socket registers */
	private static final short SKT_OFFSET	 =	0x0100;		/* offset to each socket regester set */

	//Todo skal maaske vaere long pga signed ints
	private int calculateSocketAddress(short socket) {
		return SKT_REG_BASE + ((int)socket * SKT_OFFSET);
	}
	
	public static final class Protocol{
		public static final short TCP = 0x01;
		public static final short UDP = 0x02;
		public static final short IPRaw = 0x03;
		public static final short MACRaw = 0x04;
		public static final short PPPOE = 0x05;
		public static final short NoDelay = 0x20;
		public static final short Multicasting = 0x80;
	}
}
