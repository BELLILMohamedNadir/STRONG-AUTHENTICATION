package com.example.tp.util;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.tp.object.URL;
import com.example.tp.ui.fragment.tp3.dialog.RetryFragment;

public class STATUS {
    public static void status(int it, Context context, FragmentManager fragmentManager){
        if (it == URL.CODE_422)
            toast("UNPROCESSABLE_CONTENT_STATUS",context);
        else{
            if (it == URL.CODE_401)
                toast("UNAUTHORIZED",context);
            else{
                if (it == URL.CODE_408)
                    fragment("TIMEOUT",fragmentManager);
                else{
                    if (it ==URL.CODE_423)
                        fragment("LOCKED STATUS",fragmentManager);
                    else{
                        if (it == URL.CODE_404){
                            toast("NOT FOUND",context);
                        }else
                            toast("Server error",context);
                    }

                }
            }
        }
    }
    public static void fragment(String data, FragmentManager fragmentManager){
        RetryFragment fragment = RetryFragment.Companion.newInstance(data);
        fragment.show(fragmentManager, "fragment_retry");
    }

    public static void toast(String data, Context context){
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
    }
}
