package com.example.myfirstapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myfirstapp.model.User;

import java.util.ArrayList;
import java.util.List;


public class UserScoreAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> userList = new ArrayList<>();

    public UserScoreAdapter(@NonNull Context context, ArrayList<User> list) {
        super(context, 0, list);
        mContext = context;
        userList = list;
    }

    @NonNull
    @Override
    public View getView(int pos, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            item = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent,
                    false);
        }

        User currentUser = userList.get(pos);

        TextView email = (TextView) item.findViewById(R.id.listItemEmail);
        email.setText(currentUser.getmEmail());

        TextView points = (TextView) item.findViewById(R.id.listItemPoints);
        points.setText(currentUser.getmPoints().toString());

        return item;
    }
}
