package com.example.dpit2020navem.OwnedObjectsList;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dpit2020navem.AddAnObject.Model.ObjectType;
import com.example.dpit2020navem.AddAnObject.Model.OwnedObject;
import com.example.dpit2020navem.Database.OwnedObjectsDatabase;
import com.example.dpit2020navem.R;

import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;


public class OwnedObjectsListItemAdapter extends RecyclerView.Adapter<OwnedObjectsListItemAdapter.ItemViewHolder>{

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    List<ObjectType> ownedObjectsList;
    Context context;
    OwnedObjectsListSubItemAdapter subItemAdapter;


    OwnedObjectsListItemAdapter(List<ObjectType> ownedObjectsList, Context context) {
        this.ownedObjectsList = ownedObjectsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_owned_objects_list_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        ObjectType objectType = ownedObjectsList.get(i);
        itemViewHolder.ownedObjectType.setText(objectType.getObjectTypeName());
        itemViewHolder.ownedObjecPicture.setImageResource(objectType.getObjectTypePicture());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.ownedObjects.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(objectType.getOwnedObjectList().size());

        // Create sub item view adapter
        subItemAdapter = new OwnedObjectsListSubItemAdapter(objectType.getOwnedObjectList(), context);
        subItemAdapter.setDeleteButtonListener((OwnedObjectsListSubItemAdapter.DeleteButtonListener) context);


        DividerItemDecoration divider = new DividerItemDecoration(itemViewHolder.ownedObjects.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(context, R.drawable.horizontal_line));

        itemViewHolder.ownedObjects.setLayoutManager(layoutManager);
        itemViewHolder.ownedObjects.setAdapter(subItemAdapter);
        itemViewHolder.ownedObjects.setRecycledViewPool(viewPool);
        itemViewHolder.ownedObjects.addItemDecoration(divider);
    }

    @Override
    public int getItemCount() {
        return ownedObjectsList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView ownedObjecPicture;
        private TextView ownedObjectType;
        private RecyclerView ownedObjects;

        ItemViewHolder(View itemView) {
            super(itemView);
            ownedObjecPicture = itemView.findViewById(R.id.ownedObjectPicture);
            ownedObjectType = itemView.findViewById(R.id.ownedObjectType);
            ownedObjects = itemView.findViewById(R.id.ownedObjects);
        }
    }

}