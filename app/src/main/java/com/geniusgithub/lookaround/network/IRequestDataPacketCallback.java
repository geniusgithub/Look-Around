package com.geniusgithub.lookaround.network;



public interface IRequestDataPacketCallback {

	public void  onSuccess(int requestAction, ResponseDataPacket dataPacket, Object extra);
	public void  onRequestFailure(int requestAction, String content,  Object extra);
	public void  onAnylizeFailure(int requestAction, String content,  Object extra);
}
