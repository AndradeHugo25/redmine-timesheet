package redmine.runner;

public class ThreadB extends Thread{

    @Override
    public void run() {
        try {
            synchronized(this){
                notify();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
