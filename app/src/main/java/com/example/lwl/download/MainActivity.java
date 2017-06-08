package com.example.lwl.download;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.lwl.download.service.DownloadService;

import java.util.List;

public class MainActivity extends BaseActivity {
    public String url ="http://www.wandoujia.com/apps/com.kandian.vodapp/binding?source=web_inner_referral_binded";
    private Button down_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        down_btn = (Button)findViewById(R.id.down_btn);
//        PermissionUtils.requestWriteStoragePermission(this);
//        PermissionUtils.requestReadPermission(this);
        BaseActivity.requestRunTimePermission(new String[]{Manifest.permission.MOUNT_FORMAT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, new IPermission() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(List<String> deniedPermissions) {
                for (String deniedPermission : deniedPermissions) {

                    Toast.makeText(MainActivity.this, "被拒绝的权限是"+deniedPermission, Toast.LENGTH_SHORT).show();
                }
            }
        });

        down_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.putExtra("url",url);
                startService(intent);
            }
        });
    }


}
