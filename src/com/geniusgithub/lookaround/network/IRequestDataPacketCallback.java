package com.geniusgithub.lookaround.network;



public interface IRequestDataPacketCallback {

	public void  onSuccess(int requestAction, ResponseDataPacket dataPacket);
	public void  onRequestFailure(int requestAction, String content);
	public void  onAnylizeFailure(int requestAction, String content);
}
