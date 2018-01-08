package ru.olegsvs.excel_reader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    final int typeFirst = 1;
    final int typeLogged = 2;

    int type;
    Button mGoBtn, mDelBtn;
    EditText mEdPass;
    String pass = "";
    String pass2 = "";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getExtras() != null) {
            type = getIntent().getExtras().getInt("type", 0);
            if(type == 1) {
                pass = "";
                setPrefs(pass);
            }
        }

        mEdPass = (EditText) findViewById(R.id.edPass);
        mGoBtn = (Button) findViewById(R.id.goBtn);
        mDelBtn = (Button) findViewById(R.id.delBtn);

        mEdPass.addTextChangedListener(mTextEditorWatcher);

        getPrefs();

        mDelBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mEdPass.setText("");
                return false;
            }
        });
    }

    private void getPrefs() {
        SharedPreferences sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        pass = sharedPref.getString("pass", "");
        if(TextUtils.isEmpty(pass)) type = typeFirst;
        if (type == typeFirst) {
            mEdPass.setHint("Установите новый пароль (от 4 до 8 символов)");
        } else {
            type = typeLogged;
            mEdPass.setHint("Введите пароль");
        }
    }

    private void setPrefs(String pass) {
        SharedPreferences sharedPref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPref.edit().putString("pass", pass).apply();
    }

    public void goBtnClick(View v) {
        if (type == typeFirst) {
            if (!TextUtils.isEmpty(mEdPass.getText().toString()) && count == 0) {
                pass = mEdPass.getText().toString();
                mEdPass.setText("");
                mEdPass.setHint("Повторите пароль");
                count = 1;
            } else if (!TextUtils.isEmpty(mEdPass.getText().toString()) && count == 1) {
                pass2 = mEdPass.getText().toString();
                if (pass.equals(pass2)) {
                    setPrefs(mEdPass.getText().toString());
                    Intent mainActivity = new Intent(this, MainActivity.class);
                    startActivity(mainActivity);
                } else {
                    mEdPass.setText("");
                    mEdPass.setHint("Установите новый пароль (от 4 до 8 символов)");
                    Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                    count = 0;
                }
            }
        }
        if (type == typeLogged) {
            pass2 = mEdPass.getText().toString();
            if (!pass.equals(pass2))
                Toast.makeText(this, "Пароль не верен!", Toast.LENGTH_SHORT).show();
            else {
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
            }

        }
    }

    public void click1(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 1);
    }

    public void click2(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 2);
    }

    public void click3(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 3);

    }

    public void click4(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 4);

    }

    public void click5(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 5);

    }

    public void click6(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 6);

    }

    public void click7(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 7);

    }

    public void click8(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 8);

    }

    public void click9(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 9);

    }

    public void click0(View v) {
        mEdPass.setText(mEdPass.getText().toString() + 0);

    }

    public void delBtnClick(View v) {
        String text = mEdPass.getText().toString();
        if (!TextUtils.isEmpty(text))
            mEdPass.setText(text.substring(0, text.length() - 1));
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() >= 4) mGoBtn.setEnabled(true);
            else mGoBtn.setEnabled(false);
            mEdPass.setSelection(mEdPass.getText().length());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
