package HVMFramework;

public class Analog {
	public static short Read(short pin)
	{
		short[] data = { pin, -1 };
		read(data);
		return data[1];
	}
	
    
	public static native void Init();
	private static native void read(short[] dat);
}
