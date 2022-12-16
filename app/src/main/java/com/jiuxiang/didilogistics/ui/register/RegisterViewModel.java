package com.jiuxiang.didilogistics.ui.register;

import android.annotation.SuppressLint;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONObject;
import com.jiuxiang.didilogistics.R;
import com.jiuxiang.didilogistics.utils.HTTPResult;
import com.jiuxiang.didilogistics.utils.HTTPUtils;


public class RegisterViewModel extends ViewModel {
    @SuppressLint("StaticFieldLeak")
    private RegisterActivity registerActivity;
    public MutableLiveData<String> phone;
    public MutableLiveData<String> password;
    public MutableLiveData<String> passwordConfirm;
    public MutableLiveData<String> code;
    public MutableLiveData<String> inviteCode;
    public MutableLiveData<Boolean> codeBtnEnable;
    public MutableLiveData<Integer> roleSelect;


    public RegisterViewModel() {
        phone = new MutableLiveData<>("");
        password = new MutableLiveData<>("");
        passwordConfirm = new MutableLiveData<>("");
        code = new MutableLiveData<>("");
        inviteCode = new MutableLiveData<>("获取验证码");
        codeBtnEnable = new MutableLiveData<>(true);
        roleSelect = new MutableLiveData<>(R.id.radioButton);
//        codeBtnEnable.setValue(true);
//        inviteCode.setValue("获取验证码");
    }


    public void onClickRegister() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", getPhone().getValue());
        jsonObject.put("password", getPassword().getValue());
        jsonObject.put("type", getRoleSelect().getValue() == R.id.radioButton ? 0 : 1);
        jsonObject.put("code", getCode().getValue());
        if (jsonObject.getString("password").length() < 8 || jsonObject.getString("password").length() > 16) {
            Toast.makeText(registerActivity, "密码必须在8-16位之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!getPassword().getValue().equals(getPasswordConfirm().getValue())) {
            Toast.makeText(registerActivity, "密码不一致，请重新输入", Toast.LENGTH_LONG).show();
            return;
        }
        String data = jsonObject.toJSONString();
        HTTPUtils.post("/register", data, new HTTPResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                if (jsonObject.getInteger("code") == 200) {
                    registerActivity.runOnUiThread(() -> {
                        Toast.makeText(registerActivity, "注册成功", Toast.LENGTH_SHORT).show();
                        registerActivity.finish();
                    });
                } else {
                    registerActivity.runOnUiThread(() -> {
                        Toast.makeText(registerActivity, "注册失败，" + jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(String error) {
                registerActivity.runOnUiThread(() -> {
                    Toast.makeText(registerActivity, "注册失败，" + error, Toast.LENGTH_LONG).show();
                });
            }
        });

    }

    public void onClickCode() {
        codeBtnEnable.setValue(false);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", getPhone().getValue());
        final boolean[] running = {true};

        HTTPUtils.post("/verificationCode", jsonObject.toJSONString(), new HTTPResult() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                registerActivity.runOnUiThread(() -> {
                    Toast.makeText(registerActivity, "验证码已发送", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onFailure(String error) {
                registerActivity.runOnUiThread(() -> {
                    Toast.makeText(registerActivity, "验证码发送失败，" + error, Toast.LENGTH_LONG).show();
                });
                running[0] = false;
                codeBtnEnable.postValue(true);
                inviteCode.postValue("获取验证码");
            }
        });
        inviteCode.setValue("60s");
        new Thread(() -> {
            for (int i = 59; i >= 0 && running[0]; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final int finalI = i;
                inviteCode.postValue(finalI + "s");
                if (finalI == 0) {
                    codeBtnEnable.postValue(true);
                    inviteCode.postValue("获取验证码");
                }
            }
        }).start();
    }

    public MutableLiveData<String> getPhone() {
        return phone;
    }

    public void setPhone(MutableLiveData<String> phone) {
        this.phone = phone;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void setPassword(MutableLiveData<String> password) {
        this.password = password;
    }

    public MutableLiveData<String> getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(MutableLiveData<String> passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public MutableLiveData<String> getCode() {
        return code;
    }

    public void setCode(MutableLiveData<String> code) {
        this.code = code;
    }

    public MutableLiveData<String> getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(MutableLiveData<String> inviteCode) {
        this.inviteCode = inviteCode;
    }

    public MutableLiveData<Boolean> getCodeBtnEnable() {
        return codeBtnEnable;
    }

    public void setCodeBtnEnable(MutableLiveData<Boolean> codeBtnEnable) {
        this.codeBtnEnable = codeBtnEnable;
    }

    public MutableLiveData<Integer> getRoleSelect() {
        return roleSelect;
    }

    public void setRoleSelect(MutableLiveData<Integer> roleSelect) {
        this.roleSelect = roleSelect;
    }

    public RegisterActivity getRegisterActivity() {
        return registerActivity;
    }

    public void setRegisterActivity(RegisterActivity registerActivity) {
        this.registerActivity = registerActivity;
    }

}
