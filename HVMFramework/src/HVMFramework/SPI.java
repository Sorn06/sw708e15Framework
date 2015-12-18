package HVMFramework;

import vm.Memory;

public class SPI {
	
	public static final byte SPI_CLOCK_DIV4 = 0x00;
	public static final byte SPI_CLOCK_DIV16 = 0x01;
	public static final byte SPI_CLOCK_DIV64 = 0x02;
	public static final byte SPI_CLOCK_DIV128 = 0x03;
	
	public static final byte SPI_DATA_ORDER_MSB = 0x00; //Most significant bit first
	public static final byte SPI_DATA_ORDER_LSB = 0x01; //Least significant bit first
	
	public static final byte SPI_CLOCK_POLARITY_LOW = 0x00;
	public static final byte SPI_CLOCK_POLARITY_HIGH = 0x01;
	
	public static final byte SPI_CLOCK_PHASE_LEADING = 0x00;
	public static final byte SPI_CLOCK_PHASE_TRAILING = 0x01;
	
	//Native methods
	private static native void nInit(boolean interrupt, byte dataOrder, 
			boolean master, byte clockDivider, byte clockPolarity, byte clockPhase, boolean doubleClockRate);
	
	private static native void nTransfer(int [] params);
	
	
	public SPI(boolean interrupt, byte dataOrder, 
			boolean master, byte clockDivider, byte clockPolarity, byte clockPhase, boolean doubleClockRate)
	{
		nInit(interrupt, dataOrder, master, clockDivider, clockPolarity, clockPhase, doubleClockRate);
	}
	
	

	public short Transfer(short val) {
		int[] output = new int[2];
		output[0] = val;
		output[1] = 99;
		nTransfer(output);
		return (short) output[1];
	}	


	
}
