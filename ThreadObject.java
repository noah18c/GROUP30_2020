package multiThread;

public class ThreadObject extends Thread
{
	private int counter = 0;
	private int delay;
	
	public ThreadObject(int delay)
	{
		this.delay = delay; 
	}
	
	public void run()
	{
		try
		{
			System.out.println("Thread " + Thread.currentThread().getId() + " is running");
			for(; counter < 10; counter ++)
			{
				try
				{
					System.out.println(Thread.currentThread().getId() + ": " + counter);
					Thread.currentThread().sleep(delay);
				}
				catch(InterruptedException e)
				{
					System.out.println(e);
				}
			}
			System.out.println("Thread closed");
		}
		catch(Exception e)
		{
			System.out.println("Exception caught successfully");
		}
	}
}
