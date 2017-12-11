package com.example.fsy.manager_date;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Welcomepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private GoalDataManager mUserDataManager;
    private List<Map<String, Object>> listItem;
    private Context mContext;
    private TextView msearch;
    private CircleImg avatarImg;// 头像图片
    private Button loginBtn;// 页面的登录按钮
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    // 上传服务器的路径【一般不硬编码到程序中】
    private String imgUrl = "";
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private String urlpath;            // 图片本地路径
    private String resultStr = "";    // 服务端返回结果集
    private static ProgressDialog pd;// 等待进度圈
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;

    private List<GoalData> parentList = new ArrayList<>();
    private List<List<GoalData>> childList = new ArrayList<>();

    private int goalType = 2;
    private int selectedGoalID = -1;
    private View selectedView = null;
    private MyExpandableListViewAdapter adapter;

    public Welcomepage() {
    }

    private void updateList() {
        parentList.clear();
        childList.clear();
        parentList = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "", "",
                "", -1, goalType, -1, -1, "", 0));
        childList.clear();
        for (int i = 0; i < parentList.size(); i++)
            childList.add(mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                    "", "", -1, goalType + 1, parentList.get(i).getID(), -1, "", 0)));
        adapter.notifyDataSetChanged();
    }

    private class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
//        private Map<Integer, Boolean> checkboxMap = new HashMap<>();

        //  获得某个父项的某个子项
        @Override
        public Object getChild(int parentPos, int childPos) {
            return childList.get(parentPos).get(childPos);
        }

        //  获得父项的数量
        @Override
        public int getGroupCount() {
            return parentList.size();
        }

        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int parentPos) {
            return childList.get(parentPos).size();
        }

        //  获得某个父项
        @Override
        public Object getGroup(int parentPos) {
            return childList.get(parentPos);
        }

        //  获得某个父项的id
        @Override
        public long getGroupId(int parentPos) {
            return parentPos;
        }

        //  获得某个父项的某个子项的id
        @Override
        public long getChildId(int parentPos, int childPos) {
            return childPos;
        }

        //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //  获得父项显示的view
        @SuppressLint("InflateParams")
        @Override
        public View getGroupView(final int parentPos, boolean b, View view, ViewGroup viewGroup) {
            class ViewHolder {
                TextView text, text2;
                CheckBox checkBox;
            }
            final ViewHolder holder;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) Welcomepage
                        .this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                view = inflater.inflate(R.layout.parent_item, null);
                holder = new ViewHolder();
                holder.text = (TextView) view.findViewById(R.id.parent_title);
                holder.text2 = (TextView) view.findViewById(R.id.parent_due_date);
                holder.checkBox = (CheckBox) view.findViewById(R.id.selected);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            view.setTag(R.layout.parent_item, parentPos);
            view.setTag(R.layout.child_item, -1);
            holder.text.setText(parentList.get(parentPos).getName());
            holder.text2.setText(parentList.get(parentPos).getEndTime());
            holder.checkBox.setTag(parentList.get(parentPos).getID());
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedView != null) {
                        selectedView.setBackgroundColor(Color.WHITE);
                        selectedGoalID = -1;
                        selectedView = null;
                    }
                    int id = (int) (view.getTag());
                    GoalData goal = mUserDataManager.fetchGoalDatasByID(id);
                    goal.setCompleted(1);
                    mUserDataManager.updateGoalData(goal);
//                    checkboxMap.put(id, true);
                    updateList();
                }
            });
//            if (checkboxMap != null && checkboxMap.containsKey(parentList.get(parentPos).getID())) {
//                holder.checkBox.setChecked(true);
//            } else {
//                holder.checkBox.setChecked(false);
//            }
            holder.checkBox.setChecked(false);
            holder.text.setTag(parentList.get(parentPos).getID());
            holder.text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedView != null) {
                        selectedView.setBackgroundColor(Color.WHITE);
                        selectedGoalID = -1;
                        selectedView = null;
                    }
                    int id = (int) (view.getTag());
                    Intent intent = new Intent(Welcomepage.this, ShowDetail.class);
                    intent.putExtra("id", String.valueOf(id));
                    startActivity(intent);
                }
            });
            return view;
        }

        //  获得子项显示的view
        @SuppressLint("InflateParams")
        @Override
        public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) Welcomepage
                        .this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                view = inflater.inflate(R.layout.child_item, null);
            }
            view.setTag(R.layout.parent_item, parentPos);
            view.setTag(R.layout.child_item, childPos);
            TextView text = (TextView) view.findViewById(R.id.child_title);
            text.setText(childList.get(parentPos).get(childPos).getName());
            TextView text2 = (TextView) view.findViewById(R.id.child_due_date);
            text2.setText(childList.get(parentPos).get(childPos).getEndTime());
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.selected);
            checkBox.setTag(childList.get(parentPos).get(childPos).getID());
            checkBox.setChecked(false);
            text.setTag(childList.get(parentPos).get(childPos).getID());
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedView != null) {
                        selectedView.setBackgroundColor(Color.WHITE);
                        selectedGoalID = -1;
                        selectedView = null;
                    }
                    int id = (int) (view.getTag());
                    Intent intent = new Intent(Welcomepage.this, ShowDetail.class);
                    intent.putExtra("id", String.valueOf(id));
                    startActivity(intent);
                }
            });
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedView != null) {
                        selectedView.setBackgroundColor(Color.WHITE);
                        selectedGoalID = -1;
                        selectedView = null;
                    }
                    int id = (int) (view.getTag());
                    GoalData goal = mUserDataManager.fetchGoalDatasByID(id);
                    goal.setCompleted(1);
                    mUserDataManager.updateGoalData(goal);
                    updateList();
                }
            });
            return view;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }

    protected void onResume() {
        super.onResume();
        updateList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        View view = findViewById(R.id.layout);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedView != null) {
                    selectedView.setBackgroundColor(Color.WHITE);
                    selectedGoalID = -1;
                    selectedView = null;
                }
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        msearch = (TextView) findViewById(R.id.tv_search);
        msearch.setOnClickListener(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headview = navigationView.inflateHeaderView(R.layout.nav_header_main);
        avatarImg = (CircleImg) headview.findViewById(R.id.avatarImg);
        avatarImg.setOnClickListener(this);

        if (mUserDataManager == null) {
            mUserDataManager = new GoalDataManager(this, "test");
            mUserDataManager.openGoalDatabase();                              //建立本地数据库
        }

        mUserDataManager.deleteAllGoalDatas();
        mUserDataManager.insertGoalData(new GoalData(-1, "task", "2017-11-11", "2017-11-11",
                "2017-11-11", "this is note", 1, 2, 0, 0, "User", 0));
        mUserDataManager.insertGoalData(new GoalData(-1, "task", "2017-11-11", "2017-11-11",
                "2017-11-11", "this is note", 1, 2, 0, 0, "User", 0));
        mUserDataManager.insertGoalData(new GoalData(-1, "task", "2017-11-11", "2017-11-11",
                "2017-11-11", "this is note", 1, 2, 0, 0, "User", 0));
        mUserDataManager.insertGoalData(new GoalData(-1, "action", "2017-11-11",
                "2017-11-11", "2017-11-11", "this is note", 1, 3, 1, 0, "User", 0));
        mUserDataManager.insertGoalData(new GoalData(-1, "action", "2017-11-11",
                "2017-11-11", "2017-11-11", "this is note", 1, 3, 1, 0, "User", 0));

        parentList = mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "", "",
                "", -1, goalType, -1, -1, "", 0));
        childList.clear();
        for (int i = 0; i < parentList.size(); i++)
            childList.add(mUserDataManager.fetchAllGoalDatasBy(new GoalData(-1, "", "", "",
                    "", "", -1, goalType + 1, parentList.get(i).getID(), -1, "", 0)));

        adapter = new MyExpandableListViewAdapter();

        ExpandableListView listview = (ExpandableListView) findViewById(R.id.expandablelistview);
        listview.setAdapter(adapter);
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int parentPos, int childPos, long l) {
                if (selectedView != null) {
                    selectedView.setBackgroundColor(Color.WHITE);
                    selectedGoalID = -1;
                    selectedView = null;
                }
//                Toast.makeText(Welcomepage.this, childList.get(parentPos).get(childPos).getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String content = "";
                if ((int) view.getTag(R.layout.child_item) == -1) {
                    if (selectedView != null) {
                        selectedView.setBackgroundColor(Color.WHITE);
                        selectedGoalID = -1;
                        selectedView = null;
                    }
                    selectedView = view;
                    view.setBackgroundColor(Color.parseColor("#4FC3F7"));
                    selectedGoalID = parentList.get((int) view.getTag(R.layout.parent_item)).getID();
                    content = String.valueOf(selectedGoalID);
                }
//                Toast.makeText(Welcomepage.this, content, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText inputServer = new EditText(Welcomepage.this);
                inputServer.setLinkTextColor(Color.BLACK);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Welcomepage.this);
                dialogBuilder.setTitle("添加任务").setIcon(android.R.drawable.ic_dialog_info).
                        setView(inputServer).setNegativeButton("取消", null);
                dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        String name = inputServer.getText().toString();
                        if (selectedGoalID != -1) {
                            mUserDataManager.insertGoalData(new GoalData(-1, name,
                                    "", "", "", "",
                                    4, goalType + 1, selectedGoalID, 0, "User", 0));
                        } else {
                            mUserDataManager.insertGoalData(new GoalData(-1, name,
                                    "", "", "", "",
                                    4, goalType, 0, 0, "User", 0));
                        }
                        updateList();
//                        Toast.makeText(Welcomepage.this, name, Toast.LENGTH_SHORT).show();
                    }
                });
                dialogBuilder.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mContext = Welcomepage.this;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View v) {
        if (selectedView != null) {
            selectedView.setBackgroundColor(Color.WHITE);
            selectedGoalID = -1;
            selectedView = null;
        }
        switch (v.getId()) {
            case R.id.avatarImg:// 更换头像点击事件
                menuWindow = new SelectPicPopupWindow(mContext, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.mainLayout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_search:
                Intent intent1 = new Intent(Welcomepage.this, Search.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
            avatarImg.setImageDrawable(drawable);

            // 新线程后台上传服务端
            pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
            new Thread(uploadImageRunnable).start();
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     * <p>
     * private void setPicToView(Intent picdata) {
     * Bundle extras = picdata.getExtras();
     * if (extras != null) {
     * // 取得SDCard图片路径做显示
     * Bitmap photo = extras.getParcelable("data");
     * Drawable drawable = new BitmapDrawable(null, photo);
     * urlpath = FileUtil.saveFile(mContext, "temphead.jpg", photo);
     * avatarImg.setImageDrawable(drawable);
     * <p>
     * // 新线程后台上传服务端
     * pd = ProgressDialog.show(mContext, null, "正在上传图片，请稍候...");
     * new Thread(uploadImageRunnable).start();
     * }
     * }
     * <p>
     * <p>
     * /**
     * 使用HttpUrlConnection模拟post表单进行文件
     * 上传平时很少使用，比较麻烦
     * 原理是： 分析文件上传的数据格式，然后根据格式构造相应的发送给服务器的字符串。
     */
    Runnable uploadImageRunnable = new Runnable() {
        @Override
        public void run() {

            if (TextUtils.isEmpty(imgUrl)) {
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
                return;
            }

            Map<String, String> textParams = new HashMap<String, String>();
            Map<String, File> fileparams = new HashMap<String, File>();

            try {
                // 创建一个URL对象
                URL url = new URL(imgUrl);
                textParams = new HashMap<String, String>();
                fileparams = new HashMap<String, File>();
                // 要上传的图片文件
                File file = new File(urlpath);
                fileparams.put("image", file);
                // 利用HttpURLConnection对象从网络中获取网页数据
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 设置连接超时（记得设置连接超时,如果网络不好,Android系统在超过默认时间会收回资源中断操作）
                conn.setConnectTimeout(5000);
                // 设置允许输出（发送POST请求必须设置允许输出）
                conn.setDoOutput(true);
                // 设置使用POST的方式发送
                conn.setRequestMethod("POST");
                // 设置不使用缓存（容易出现问题）
                conn.setUseCaches(false);
                conn.setRequestProperty("Charset", "UTF-8");//设置编码
                // 在开始用HttpURLConnection对象的setRequestProperty()设置,就是生成HTML文件头
                conn.setRequestProperty("ser-Agent", "Fiddler");
                // 设置contentType
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + NetUtil.BOUNDARY);
                OutputStream os = conn.getOutputStream();
                DataOutputStream ds = new DataOutputStream(os);
                NetUtil.writeStringParams(textParams, ds);
                NetUtil.writeFileParams(fileparams, ds);
                NetUtil.paramsEnd(ds);
                // 对文件流操作完,要记得及时关闭
                os.close();
                // 服务器返回的响应吗
                int code = conn.getResponseCode(); // 从Internet获取网页,发送请求,将网页以流的形式读回来
                // 对响应码进行判断
                if (code == 200) {// 返回的响应码200,是成功
                    // 得到网络返回的输入流
                    InputStream is = conn.getInputStream();
                    resultStr = NetUtil.readString(is);
                } else {
                    Toast.makeText(mContext, "请求URL失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);// 执行耗时的方法之后发送消给handler
        }
    };

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();

                    try {
                        // 返回数据示例，根据需求和后台数据灵活处理
                        // {"status":"1","statusMessage":"上传成功","imageUrl":"http://120.24.219.49/726287_temphead.jpg"}
                        JSONObject jsonObject = new JSONObject(resultStr);

                        // 服务端以字符串“1”作为操作成功标记
                        if (jsonObject.optString("status").equals("1")) {
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图，3为三分之一
                            option.inSampleSize = 1;

                            // 服务端返回的JsonObject对象中提取到图片的网络URL路径
                            String imageUrl = jsonObject.optString("imageUrl");
                            Looper.prepare();
                            Toast.makeText(mContext, imageUrl, Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(mContext, jsonObject.optString("statusMessage"), Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                //start by lcfeng 2015-11-28
                case 1:
                    pd.dismiss();
                    Toast.makeText(mContext, "还没有设置上传服务器的路径！", Toast.LENGTH_SHORT).show();
                    break;
                //end by lcfeng 2015-11-28
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_goal) {
            goalType=0;
            updateList();
        } else if (id == R.id.nav_project) {
            goalType=1;
            updateList();
        } else if (id == R.id.nav_task) {
            goalType=2;
            updateList();
        } else if (id == R.id.nav_action) {
            goalType=3;
            updateList();
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(Welcomepage.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "注销成功", Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_exit) {
            System.exit(0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
