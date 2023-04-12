import java.io.*;

public class CmdTest {
	public static void main(String[] args) throws Exception {

		try {
			Process p = Runtime.getRuntime()
					.exec("C:/Users/bommu/Desktop/IPL21Dream11/TEst/trigger.bat");
			p.waitFor();

		} catch (IOException ex) {
			// Validate the case the file can't be accesed (not enought permissions)

		} catch (InterruptedException ex) {
			// Validate the case the process is being stopped by some external situation

		}
	}
}