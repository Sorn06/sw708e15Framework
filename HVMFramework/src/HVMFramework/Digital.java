package HVMFramework;

public class Digital {
	public static final short ModePullUp = 2;
	public static final short ModeOutput = 1;
	public static final short ModeInput = 0;
	
	public static final short Low = 0;
	public static final short High = 1;
	
	public static final short PullUpLow = 1;
	public static final short PullUpHigh = 0;
	
	public static short Read(short pin) 
	{
		short[] data = { pin, -1 };
		read(data);
		if(data[1] != 0) return 1;
		return 0;
	}
	
	public static native void Write(short pin, short out);
	public static native void PinMode(short pin, short out);
	private static native void read(short[] dat);
}
