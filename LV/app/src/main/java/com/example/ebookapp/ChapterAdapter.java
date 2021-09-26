package com.example.ebookapp;



import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private List<ChapterModel> chapterModelList;

    public  ChapterAdapter(List<ChapterModel> chapterModelList){
        this.chapterModelList = chapterModelList;
    }


    @NonNull
    @Override
    public ChapterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chapter,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
      String chaterNumber = chapterModelList.get(position).getChapterNumber();
      String chapterName = chapterModelList.get(position).getChapterName();
      String chaptercontent = chapterModelList.get(position).getChaptercontent();

     viewHolder.setData(chaterNumber,chapterName, chaptercontent);
    }

    @Override
    public int getItemCount() {
        return chapterModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView chapterNumber;
        private TextView chapterName;
        private TextView chapterContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterNumber = itemView.findViewById(R.id.chapterNumber);
            chapterName = itemView.findViewById(R.id.chapterName);
            chapterContent = itemView.findViewById(R.id.content);
        }

        private void setData( String number , String name , String content ){
            chapterNumber.setText(number);
            chapterName.setText(name);
            chapterContent.setText(content);
        }


}
}
