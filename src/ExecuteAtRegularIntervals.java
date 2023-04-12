
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecuteAtRegularIntervals {

	static long mins = 1;
	static String currResult = "";
	static String currResult_Nava = "";

	public static void main(String args[]) {

		Date dateobj = new Date();
		final DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		System.out.println("ScheduledExecutorService started : " + formatter.format(dateobj));

		ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
		ses.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					String prevResult = currResult;
					String prevResult_Nava = currResult_Nava;
					currResult = DreamXi.executeDream11();
					currResult_Nava = DreamXi_Nava.executeDream11();
					if (!prevResult.equals(currResult)) {
						System.out.println("\tDream11 Points Updated @ " + formatter.format(new Date()));
						Process p = Runtime.getRuntime().exec("C:/Users/s0a0838/Desktop/TestMukil/trigger.bat");
						p.waitFor();
					}
					
					if (!prevResult_Nava.equals(currResult_Nava)) {
						System.out.println("\tDream11_Nava Points Updated @ " + formatter.format(new Date()));
						Process p = Runtime.getRuntime().exec("C:/Users/s0a0838/Desktop/TestMukil/trigger2.bat");
						p.waitFor();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, mins, TimeUnit.MINUTES);
	}
}