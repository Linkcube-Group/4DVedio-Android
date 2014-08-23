package me.linkcube.FourDVedio.ui.listener;

/**
 * ASmack请求的回调接口
 * 
 */
public interface ASmackRequestCallBack {

	public void responseSuccess(Object object);

	public void responseFailure(int reflag);
}
