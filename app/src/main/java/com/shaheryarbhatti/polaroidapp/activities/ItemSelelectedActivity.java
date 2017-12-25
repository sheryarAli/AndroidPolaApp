package com.shaheryarbhatti.polaroidapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shaheryarbhatti.polaroidapp.R;
import com.shaheryarbhatti.polaroidapp.adapters.GenericAdapter;
import com.shaheryarbhatti.polaroidapp.dataclasses.Comment;
import com.shaheryarbhatti.polaroidapp.dataclasses.Made;
import com.shaheryarbhatti.polaroidapp.dataclasses.Post;
import com.shaheryarbhatti.polaroidapp.utilities.UtilImage;

import java.util.ArrayList;


/**
 * Created by Shahzore on 12/16/2017.
 */

public class ItemSelelectedActivity extends AppCompatActivity {
    private final String TAG = "ItemSelelectedActivity";
    private Toolbar toolbar;
    private TextView descriptionText;
    private ImageView postImageView;
    private RecyclerView commentRecyclerView, madeRecyclerView;

    private ArrayList<Comment> commentList;
    private ArrayList<Made> madeList;
    private GenericAdapter<Comment> commentGenericAdapter;
    private GenericAdapter<Made> madeGenericAdapter;
    private LinearLayoutManager commentLayoutManager;
    private GridLayoutManager madeLayoutManager;
    private View commentSectionView, madeSectionView;
    private TextView commentText, madeText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_selected);
        initViews();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        commentLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        prepareViews();
       /*
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#606060")));
        getSupportActionBar().setElevation(0);*/


    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        descriptionText = (TextView) findViewById(R.id.descriptionText);
        postImageView = (ImageView) findViewById(R.id.postImageView);
        commentSectionView = findViewById(R.id.commentSectionView);
        madeSectionView = findViewById(R.id.madeSectionView);
        commentText = (TextView) commentSectionView.findViewById(R.id.sectionText);
        madeText = (TextView) madeSectionView.findViewById(R.id.sectionText);
        commentRecyclerView = (RecyclerView) commentSectionView.findViewById(R.id.sectoinsRecyclerView);
        madeRecyclerView = (RecyclerView) madeSectionView.findViewById(R.id.sectoinsRecyclerView);

    }

    private void prepareViews() {
        commentText.setText("Comment");
        madeText.setText("Made");
        Bundle data = getIntent().getBundleExtra("post");
        Post post = (Post) data.getSerializable("post");
        descriptionText.setText(post.getTitleText());
        UtilImage.loadImageWithPicasso(this, postImageView, post.getSource());
        commentList = post.getComments();
        madeList = post.getMade();
        prepareCommentRecylerView();


    }


    private void prepareCommentRecylerView() {
        commentRecyclerView.setLayoutManager(commentLayoutManager);
        commentGenericAdapter = new GenericAdapter<Comment>(this, commentList) {
            @Override
            public RecyclerView.ViewHolder setViewHolder(ViewGroup parent) {
                final View view = LayoutInflater.from(ItemSelelectedActivity.this).inflate(R.layout.list_item_comments, parent, false);
                view.setOnClickListener(this);
                CommentViewHolder viewHolder = new CommentViewHolder(view);
                return viewHolder;
            }

            @Override
            public void onBindData(RecyclerView.ViewHolder holder, Comment val, int position) {
                CommentViewHolder viewHolder = (CommentViewHolder) holder;
                viewHolder.userImageView.setImageBitmap(UtilImage.getBitmap(context, val.getUserPicture()));
                viewHolder.userText.setText(val.getUserName());
                viewHolder.userTypeText.setText(val.getProfesson());
                viewHolder.commentText.setText(val.getCommentText());
                viewHolder.timeText.setText(val.getCommentDuration());


            }

            @Override
            public void onItemClick(View view) {

            }
        };
        commentRecyclerView.setAdapter(commentGenericAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView userImageView;
        private TextView userText, userTypeText, timeText, commentText;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userImageView = (ImageView) itemView.findViewById(R.id.userImageView);
            userText = (TextView) itemView.findViewById(R.id.userText);
            userTypeText = (TextView) itemView.findViewById(R.id.userTypeText);
            timeText = (TextView) itemView.findViewById(R.id.timeText);
            commentText = (TextView) itemView.findViewById(R.id.commentText);
        }
    }


}
