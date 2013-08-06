package com.geniusgithub.lookaround.network;



public interface IRequestCallback {

	public void  onSuccess(int requestAction, ResponseDataPacket dataPacket);
	public void  onRequestFailure(int requestAction, String content);
	public void  onAnylizeFailure(int requestAction, String content);
}
