package com.jnu.pureaccount.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.jnu.pureaccount.R;

public class KeyBoardUtils {
    public final static int KEYCODE_ADD = -99;
    public final static int KEYCODE_MINUS = -100;
    private final Keyboard keyboard;
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener{
        public void onEnsure();
    }

    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);//取消弹出系统键盘
        keyboard = new Keyboard(this.editText.getContext(), R.xml.keyboard);

        this.keyboardView.setKeyboard(keyboard);
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener); //按钮点击事件监听
    }
    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener(){

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        //TODO：键盘按键未全部完成
        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch(primaryCode){
                case Keyboard.KEYCODE_DELETE:
                    if (editable!=null && editable.length()>0){
                        if(start>0){
                            editable.delete(start-1,start);
                        }
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:
                    break;
                case Keyboard.KEYCODE_DONE:
                    onEnsureListener.onEnsure(); //接口回调
                    break;
                case KEYCODE_ADD:
                    break;
                case KEYCODE_MINUS:
                    break;
                default://其他数字
                    editable.insert(start,Character.toString((char)primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void swipeUp() {
        }
    };
    public void showKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility == View.INVISIBLE||visibility==View.GONE){
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    public void hideKeyboard(){
        int visibility = keyboardView.getVisibility();
        if(visibility==View.VISIBLE||visibility==View.INVISIBLE){
            keyboardView.setVisibility(View.GONE);
        }
    }
}
