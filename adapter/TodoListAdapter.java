package com.codepolitan.grammar.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepolitan.grammar.InsertDataActivity;
import com.codepolitan.grammar.data.MyDatabase;
import com.codepolitan.grammar.entity.Item;
import com.codepolitan.grammar.R;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoListHolder> {

    List<Item> itemList;
    Context context;

    public TodoListAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent, false);
        return new TodoListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListHolder holder, int position) {
        final Item item = itemList.get(position);
        holder.todoText.setText(item.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (context, InsertDataActivity.class);
                intent.putExtra("ITEM",item);
                context.startActivity(intent);
            }
        });
        holder.doneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setup the alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Konfirmasi");
                builder.setMessage("apakah anda ingin menghapus materi tersebut?");

                //add Button
                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyDatabase myDatabase = new MyDatabase(context);
                        myDatabase.deleteItem(item);

                        itemList.clear();
                        itemList.addAll(myDatabase.getAll());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("BELUM", null);

                //create and show alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class TodoListHolder extends RecyclerView.ViewHolder {
        TextView todoText;
        ImageView editImageView;
        ImageView doneImageView;


        public TodoListHolder(View itemView) {
            super(itemView);
            todoText = itemView.findViewById(R.id.todo_name);
            editImageView = itemView.findViewById(R.id.edit_todo);
            doneImageView = itemView.findViewById(R.id.done_todo);
        }
    }
}
