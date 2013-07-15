package edu.usf.eng.pie.avatars4change.dataInterface;

public class userData {
	private static final String TAG = "userData";
	
	//user info:
    public static String USERID = "defaultUID";
    
    //user context:
    private static String currentActivityName = "???";
    public static float    currentActivityLevel = 0;
    public static double[] FFT = new double[65];
    
	public static int[] recentLevels = new int[20];
	public static int recentSum = 0;
	public static float recentAvg = 0;

	
	public static void appendValueAndRecalc(int newV){
		recentSum = 0;
		//push buffer to make room for new value
		for (int i = 0 ; i < recentLevels.length-1 ; i++){
			recentLevels[i] = recentLevels[i+1];
			recentSum += recentLevels[i];
		}
		recentLevels[recentLevels.length-1] = newV ;
		// add final value to new sum
		recentSum += recentLevels[recentLevels.length-1];
		// compute new recent average
		recentAvg = ((float)recentSum) / ((float)recentLevels.length);
		currentActivityLevel = recentAvg;
	}
	
	public static void setCurrentActivityName(String newName){
    	//Toast.makeText(context, url, Toast.LENGTH_SHORT).show();
	}
	
	public static String getCurrentActivityName(){
		return currentActivityName;
	}	
}
