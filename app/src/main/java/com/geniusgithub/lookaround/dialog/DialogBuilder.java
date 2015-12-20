package com.geniusgithub.lookaround.dialog;

import org.apache.http.conn.params.ConnConnectionParamBean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;


public class DialogBuilder {
	
	public DialogBuilder() {


	}
	
	
	public static Dialog buildTipDialog(Context context, String title, String message, final IDialogInterface listener){
		DialogEntity entity = new DialogEntity();
		entity.context = context;
		entity.title = title;
		entity.message = message;
		entity.btnSure = "确定";
		
		return buildTipDialog(entity, listener);
	}
	
	public static Dialog buildNormalDialog(Context context, String title, String message, final IDialogInterface listener){
		DialogEntity entity = new DialogEntity();
		entity.context = context;
		entity.title = title;
		entity.message = message;
		entity.btnSure = "确定";
		entity.btnCancel = "取消";
		
		return buildNormalDialog(entity, listener);
	}
	
	
	
	
	private static Dialog buildTipDialog(DialogEntity entity, final IDialogInterface listener){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(entity.context);
		builder.setTitle(entity.title);
		builder.setMessage(entity.message);
		builder.setNegativeButton(entity.btnSure, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null){
					listener.onSure();
				}
			}
		});
		
		return builder.create();
		
	}
	
	private static Dialog buildNormalDialog(DialogEntity entity, final IDialogInterface listener){
		
		
		AlertDialog.Builder builder = new AlertDialog.Builder(entity.context);
		builder.setTitle(entity.title);
		builder.setMessage(entity.message);
		builder.setNegativeButton(entity.btnSure, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null){
					listener.onSure();
				}
			}
		});
	
		builder.setPositiveButton(entity.btnCancel, new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (listener != null){
					listener.onCancel();
				}
			}
			
		});
		
		return builder.create();
	
	}
	
	
	
}
