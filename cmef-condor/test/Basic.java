import java.io.FileWriter;
import java.util.Date;

public class Basic {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Starting Basic...");
		System.err.println("Starting Basic...");
		FileWriter fw = new FileWriter("Basic.txt");

		fw.write("Basic ");
		for (int i = 0; i < args.length; i++)
			fw.write(args[i] + " ");
		fw.write("\r\n");

		int count = Integer.parseInt(args[0]);
		System.out.println("count = " + count);
		for (int i = 0; i < count; i++) {
			fw.write(i + ".\r\n");
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
			}
		}
		fw.write(new Date().toString());
		fw.close();
		System.out.println("End Basic." + new Date());
		System.err.println("End Basic." + new Date());
	}

}
