package com.example.administrator.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SHOW_RESPONSE = 1;
    private static final int GET_RESPONSE = 2;
    @InjectView(R.id.textMes)
    TextView mTextMes;
    @InjectView(R.id.et_sendMessage)
    EditText mEtSendMessage;
    @InjectView(R.id.sendMessage)
    Button mSendMessage;


    private Socket socket;
    private String s1;
    private String sdMes;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mSendMessage.setOnClickListener(this);


        handler = new Handler() {

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_RESPONSE:
                        String response1 = (String) msg.obj;
                        mTextMes.setText(response1);
                    case GET_RESPONSE:
                        String response2 = (String) msg.obj;

                }
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {

                try {
                    socket = new Socket("192.168.1.111", 6666);
                    Log.i("test", "thread start");
                    //得到输入流
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    //得到输出流
                    OutputStream outputStream = socket.getOutputStream();
                    //                    PrintWriter os = new PrintWriter(socket.getOutputStream());

                    //                    int temp = 0;
                    String s = "test";
                    //                    byte[] byteBuffer = new byte[1024];
                    Log.i("test", inputStream.toString());
                    while (!(s = br.readLine()).isEmpty()) {
                        Log.i("test2", s);
                        s1 += s;
                        Log.i("test3", s1);
                    }
                    Log.i("test4", s1);
                    outputStream.flush();
                    //                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    //                    String s2 = br.readLine();


                    Message message = new Message();
                    message.what = GET_RESPONSE;
                    message.obj = s1;
                    handler.sendMessage(message);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void onClick(View v) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                try {
                    Socket socket = new Socket("192.168.1.111", 6666);

                    //获得输出流
                    PrintWriter os = new PrintWriter(socket.getOutputStream());
                    os.println("sfsfs");
                    //   os.println(et_sendMessage.getText().toString());

                    os.flush();
                    socket.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }
}

