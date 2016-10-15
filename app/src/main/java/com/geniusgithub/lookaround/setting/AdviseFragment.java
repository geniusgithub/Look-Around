package com.geniusgithub.lookaround.setting;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.geniusgithub.lookaround.R;
import com.geniusgithub.lookaround.base.BaseFragment;
import com.geniusgithub.lookaround.util.CommonLog;
import com.geniusgithub.lookaround.util.CommonUtil;
import com.geniusgithub.lookaround.util.LogFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class AdviseFragment extends BaseFragment implements Handler.Callback, TextWatcher {
    private static final CommonLog log = LogFactory.createLog();

    private static final int MSG_TOAST = 1;
    private static final int MSG_ACTION_CCALLBACK = 2;
    private static final int MSG_CANCEL_NOTIFY = 3;

    private static final int MAX_TEXT_LENGTH = 100;

    private Toolbar toolbar;

    @BindView(R.id.et_content)
    public EditText mETContent;

    @BindView(R.id.tv_target)
    public TextView mTVTarget;

    @BindView(R.id.tv_live)
    public TextView mTVLive;

    private int notifyIcon;
    private String notifyTitle;
    private String sharePath;

    private Platform mPlatform;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advise_fragment_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onUIReady(view);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.advise_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_advise:
                share();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateTVLive();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch(msg.what) {
            case MSG_TOAST: {
                String text = String.valueOf(msg.obj);
                Toast.makeText(getParentActivity(), text, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_ACTION_CCALLBACK: {
                switch (msg.arg1) {
                    case 1: { // 成功
                        showNotification(2000, getString(R.string.share_completed));
                    }
                    break;
                    case 2: { // 失败
                        showNotification(2000, getString(R.string.share_failed));
                    }
                    break;
                    case 3: { // 取消
                        showNotification(2000, getString(R.string.share_canceled));
                    }
                    break;
                }
            }
            break;
            case MSG_CANCEL_NOTIFY: {
                NotificationManager nm = (NotificationManager) msg.obj;
                if (nm != null) {
                    nm.cancel(msg.arg1);
                }
            }
            break;
        }
        return false;
    }


    private void onUIReady(View view){
        ButterKnife.bind(this, view);
        mETContent.addTextChangedListener(this);

        initData();
    }

    private void initData(){
        setNotification(R.drawable.logo_icon,"Look Around");
        mPlatform = ShareSDK.getPlatform(getParentActivity(), SinaWeibo.NAME);
        PlatformDb db = mPlatform.getDb();
        String nickname = db.get("nickname");
        if (nickname != null){
            mTVTarget.setText(nickname);
        }
        updateTVLive();
    }


    /** 分享时Notification的图标和文字 */
    public void setNotification(int icon, String title) {
        notifyIcon = icon;
        notifyTitle = title;
    }

    /** 执行分享 */
    public void share() {
        boolean started = false;

        int relen = MAX_TEXT_LENGTH -  mETContent.length();
        if (relen == MAX_TEXT_LENGTH){
            CommonUtil.showToast(R.string.toast_no_txtcount, getParentActivity());
            return ;
        }

        if (relen < 0){
            CommonUtil.showToast(R.string.toast_too_txtcount, getParentActivity());
            return ;
        }

        Toast.makeText(getParentActivity(), "功能暂时屏蔽，敬请谅解", Toast.LENGTH_SHORT).show();

		/*	int shareType = Platform.SHARE_TEXT;
			HashMap<String, Object> reqMap = new HashMap<String, Object>();
			reqMap.put("shareType", shareType);

			String sendContent = getSendContent();
			log.e("sendContent = " + sendContent);
			reqMap.put("text", sendContent);

			if (!started) {
				started = true;
				showNotification(2000, getString(R.string.sharing));
				finish();
			}

			mPlatform.setPlatformActionListener(this);
			ShareCore shareCore = new ShareCore();
			shareCore.share(mPlatform, reqMap);*/

    }

    public String getSendContent(){
        String value = mETContent.getText().toString();
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("@android火星人 ");
        sBuffer.append("#意见与反馈#");
        sBuffer.append("\n");
        sBuffer.append(getMobileInfo());
        sBuffer.append(value);
        return sBuffer.toString();
    }

    public String getMobileInfo(){
        String value = "(版本" + CommonUtil.getSoftVersion(getParentActivity()) +
                ",厂商" + CommonUtil.getDeviceManufacturer() +
                ", 型号" + CommonUtil.getDeviceModel() +
                ", 系统" + CommonUtil.getOSVersion() + ")";
        return value;
    }



    // 在状态栏提示分享操作
    private void showNotification(long cancelTime, String text) {
        try {
            NotificationManager nm = (NotificationManager)
                    getParentActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            final int id = Integer.MAX_VALUE / 13 + 1;
            nm.cancel(id);

            long when = System.currentTimeMillis();
            Notification.Builder builder = new Notification.Builder(getParentActivity());
            builder.setSmallIcon(notifyIcon);
            builder.setContentText(text);
            builder.setWhen(when);
            builder.setContentTitle(notifyTitle);
            PendingIntent pi = PendingIntent.getActivity(getParentActivity(), 0, new Intent(), 0);
            builder.setContentIntent(pi);
            builder.setAutoCancel(true);
            nm.notify(id, builder.build());

            if (cancelTime > 0) {
                Message msg = new Message();
                msg.what = MSG_CANCEL_NOTIFY;
                msg.obj = nm;
                msg.arg1 = id;
                UIHandler.sendMessageDelayed(msg, cancelTime, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTVLive(){
        int remain = MAX_TEXT_LENGTH - mETContent.length();
        mTVLive.setText("您还可以输入" + String.valueOf(remain) + "字");
        mTVLive.setTextColor(remain > 0 ? 0xffcfcfcf : 0xffff0000);
    }
}
