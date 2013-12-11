package back.controller.test;

import back.controller.base.BaseAction;

public class TakeLongTime extends BaseAction {
	public String execute(){
		//等十秒再返回SUCCESS視圖
		try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
		return SUCCESS;
	}
}
