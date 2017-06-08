package com.example.lwl.download.service;

/**
 * Created by lwl on 2017/5/19.
 */
import java.io.File;
import java.text.DecimalFormat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.example.lwl.download.R;

public class AsyncTaskRunnable implements Runnable{

    public static final String TAG = "AsyncTaskRunnable";
    //主线程的activity
    private Context mContext;
    //notification的状态：更新 or 失败 or 成功
    private int mStatus;
    //notification的下载比例
    private float mSize;
    //管理下拉菜单的通知信息
    private NotificationManager mNotificationManager;
    //下拉菜单的通知信息
    private Notification mNotification;
    //下拉菜单的通知信息的view
    private RemoteViews mRemoteViews;
    //下拉菜单的通知信息的种类id
    private static final int NOTIFICATION_ID = 1;

    //设置比例和数据
    public void setDatas(int status , float size) {
        this.mStatus = status;
        this.mSize = size;
    }
    //初始化
    public AsyncTaskRunnable(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        //初始化下拉菜单的通知信息
        mNotification = new Notification();
        mNotification.icon = R.mipmap.ic_launcher;//设置下载进度的icon
        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotification.tickerText = mContext.getResources().getString(R.string.app_name); //设置下载进度的title

        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.down_notification);//对于RemoteView的使用，不懂的需要查找google
        mRemoteViews.setImageViewResource(R.id.id_download_icon, R.mipmap.ic_launcher);
    }

    @Override
    public void run() {//通过判断不同的状态：更新中/下载失败/下载成功 更新下拉菜单的通知信息
        switch (mStatus) {
            case AsyncTaskUtil.NOTIFICATION_PROGRESS_FAILED://下载失败
                mNotificationManager.cancel(NOTIFICATION_ID);
                break;

            case AsyncTaskUtil.NOTIFICATION_PROGRESS_SUCCEED://下载成功
                mRemoteViews.setTextViewText(R.id.id_download_textview, mContext.getResources().getString(R.string.download_completed));
                mRemoteViews.setProgressBar(R.id.id_download_progressbar, 100, 100, false);
                mNotification.contentView = mRemoteViews;
                mNotification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
                Toast.makeText(mContext, mContext.getString(R.string.download_completed), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = null;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(mContext,mContext.getPackageName()+".provider",
                            new File(Environment.getExternalStorageDirectory().toString()+"/downFile/","down.apk"));
                }else{
                    uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString()+"/downFile/","down.apk"));
                }
                intent.setDataAndType(uri,"application/vnd.android.package-archive");
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,0);
                mNotification.contentIntent = pendingIntent;
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                break;

            case AsyncTaskUtil.NOTIFICATION_PROGRESS_UPDATE://更新中
                DecimalFormat format = new DecimalFormat("0.00");//数字格式转换
                String progress = format.format(mSize);
                Log.d(TAG, "the progress of the download " + progress);
                mRemoteViews.setTextViewText(R.id.id_download_textview, mContext.getString(R.string.downloaded) + progress + " %");
                mRemoteViews.setProgressBar(R.id.id_download_progressbar, 100, (int)mSize, false);
                mNotification.contentView = mRemoteViews;
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                break;
        }
    }

}
