package shirley.com.sudoku.uiBase;
import java.util.Stack;

import android.app.Activity;

public class ApplicationManager { 
    private static Stack<Activity> activityStack; 
    private static ApplicationManager instance; 
    private ApplicationManager() { 
    } 
    public static ApplicationManager getScreenManager() { 
        if (instance == null) { 
            instance = new ApplicationManager(); 
        } 
        return instance; 
    } 
    //退出Activity 
    public void popActivity(Activity activity) { 
        if (activity != null) { 
           //在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作 
            activity.finish(); 
            activityStack.remove(activity); 
            activity = null; 
        } 
    } 
    
    public void removeActivity(Activity activity){
    	if(activityStack != null && !activityStack.isEmpty()){
    		if(activity != null){
    			activityStack.remove(activity);
    			activity = null;
    		}
    	}
    }
    
    //获得当前栈顶Activity 
    public Activity currentActivity() { 
        Activity activity = null; 
       if(!activityStack.empty()) 
         activity= activityStack.lastElement(); 
        return activity; 
    } 
    //将当前Activity推入栈中 
    public void pushActivity(Activity activity) { 
        if (activityStack == null) { 
            activityStack = new Stack<Activity>(); 
        } 
        activityStack.add(activity); 
    } 
    
    //退出栈中所有Activity 
    public void popAllActivity() { 
        while (true) { 
            Activity activity = currentActivity(); 
            if (activity == null) { 
                break; 
            } 
            popActivity(activity); 
        } 
    } 
} 
