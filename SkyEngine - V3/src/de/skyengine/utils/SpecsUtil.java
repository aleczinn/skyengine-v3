package de.skyengine.utils;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL11;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.ProcessorIdentifier;
import oshi.hardware.GlobalMemory;

public class SpecsUtil {

	private static SystemInfo info;
	private static CentralProcessor processor;
	private static GlobalMemory memory;
	private static ProcessorIdentifier identifier;
	
	private static String CPU_NAME;
	
	private static String GL_RENDERER;
	private static String GL_VERSION;
	private static String GL_VENDOR;
	
	private static String OS;
	
	private static String JAVA;
	private static String JAVA_VERSION;
	private static String VIRTUAL_MACHINE;
	
	static {
		info = new SystemInfo();
		
		processor = info.getHardware().getProcessor();
		memory = info.getHardware().getMemory();
		identifier = processor.getProcessorIdentifier();		
	}
	
	public static String getOS() {
		if(SpecsUtil.OS == null) {
			SpecsUtil.OS = System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") Version " + System.getProperty("os.version");
		}
		return SpecsUtil.OS;
	}
	
	public static String getJava() {
		if(SpecsUtil.JAVA == null) {
			SpecsUtil.JAVA = System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
		}
		return SpecsUtil.JAVA;
	}
	
	public static String getJavaVersion() {
		if(SpecsUtil.JAVA_VERSION == null) {
			SpecsUtil.JAVA_VERSION = System.getProperty("java.version");
		}
		return SpecsUtil.JAVA_VERSION;
	}
	
	public static String getVirtualMachine() {
		if(SpecsUtil.VIRTUAL_MACHINE == null) {
			SpecsUtil.VIRTUAL_MACHINE = System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
		}
		return SpecsUtil.VIRTUAL_MACHINE;
	}
	
	public static String getLWJGLVersion() {
		return Version.getVersion();
	}
	
	public static String getCPU() {
		if(SpecsUtil.CPU_NAME == null) {
			SpecsUtil.CPU_NAME = SpecsUtil.identifier.getName();
		}
		return SpecsUtil.CPU_NAME;
	}
	
	public static String getRendererVendor() {
		if(SpecsUtil.GL_VENDOR == null) {
			SpecsUtil.GL_VENDOR = GL11.glGetString(GL11.GL_VENDOR);
		}
		return SpecsUtil.GL_VENDOR;
	}
	
	public static String getRenderer() {
		if(SpecsUtil.GL_RENDERER == null) {
			SpecsUtil.GL_RENDERER = GL11.glGetString(GL11.GL_RENDERER);
		}
		return SpecsUtil.GL_RENDERER;
	}
	
	public static String getDriverVersion() {
		if(SpecsUtil.GL_VERSION == null) {
			SpecsUtil.GL_VERSION = GL11.glGetString(GL11.GL_VERSION);
		}
		return SpecsUtil.GL_VERSION;
	}
	
	public static String getSystemRam() {
		return String.valueOf(bytesToMb(SpecsUtil.memory.getTotal()));
	}
	
	public static boolean isJvm64bit() {
		String[] astring = new String[] { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };

		for (String s : astring) {
			String s1 = System.getProperty(s);

			if (s1 != null && s1.contains("64")) {
				return true;
			}
		}
		return false;
	}
	
	public static long getMaxRam() {
		return bytesToMb(Runtime.getRuntime().maxMemory());
	}
	
	public static long getTotalRam() {
		return bytesToMb(Runtime.getRuntime().totalMemory());
	}
	
	public static long getFreeRam() {
		return bytesToMb(Runtime.getRuntime().freeMemory());
	}
	
	public static String getRamUsageInPercent() {
		long i = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		return String.valueOf(i * 100 / Runtime.getRuntime().maxMemory());
	}
	
	public static String getRamUsage() {
		return String.valueOf(bytesToMb(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()));
	}
	
	public static long bytesToMb(long bytes) {
		return bytes / 1024L / 1024L;
	}
}
