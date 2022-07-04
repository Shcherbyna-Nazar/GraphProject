package pw.edu.pl.jimppro.mainpack;

public class Launcher {

    static boolean flag = false;
    public static Thread thread;

    public static void start() {
        thread = new Thread() {
            @Override
            public synchronized void run() {
                if(!flag){
                    synchronized (this) {
                        flag = true;
                        Main.main();
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
